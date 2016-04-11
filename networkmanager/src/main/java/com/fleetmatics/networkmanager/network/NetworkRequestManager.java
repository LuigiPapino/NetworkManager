package com.fleetmatics.networkmanager.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.fleetmatics.networkmanager.commons.Utility;
import com.fleetmatics.networkmanager.data.NetworkRequestsStore;
import com.fleetmatics.networkmanager.data.NetworkRequestsStoreFromProvider;
import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.subjects.BehaviorSubject;

/**
 * Created by luigi.papino on 06/04/16.
 */
public class NetworkRequestManager {

    private static final String TAG = NetworkRequestManager.class.getSimpleName();
    private static NetworkRequestManager instance;
    private final NetworkRequestsStore requestsStore;
    private Context context;
    private HashMap<Integer, BehaviorSubject<NetworkRequestStatus>> requestsMap = new HashMap<>();
    private Map<String, NetworkRequestExecutor> executorMap = new HashMap<>();

    private NetworkRequestManager(@NonNull Context context, @NonNull NetworkRequestsStore requestsStore) {
        this.context = context;
        this.requestsStore = requestsStore;
    }

    @NonNull
    public static NetworkRequestManager getInstance() {
        if (instance == null)
            throw new IllegalStateException("You need to init the manager before to access it");

        return instance;
    }

    @NonNull
    public static NetworkRequestManager init(@NonNull Context context, @NonNull String basePath, @NonNull Class apiClass) {
        if (context == null)
            throw new IllegalArgumentException("application can't be null");

        if (instance == null) {
            NetworkRequestsStoreFromProvider networkRequestsStore = new NetworkRequestsStoreFromProvider(context.getApplicationContext());
            instance = new NetworkRequestManager(context.getApplicationContext(), networkRequestsStore);
            NetworkApi.init(context.getApplicationContext(), basePath, apiClass);
        }
        return instance;

    }

    public NetworkRequestsStore getRequestsStore() {
        return requestsStore;
    }

    public boolean contains(int hashCode) {
        return requestsMap.containsKey(hashCode);
    }

    public boolean isWaitingConnection(int hashCode) {

        if (requestsMap.containsKey(hashCode) && requestsMap.get(hashCode).hasValue()) {
            return requestsMap.get(hashCode).getValue().isWaitingConnection();
        } else
            return false;
    }

    public <T> Observable<NetworkRequestStatus<T>> executeRequest(@NonNull NetworkRequest request, Class<T> _class) {
        if (!requestsMap.containsKey(request.hashCode()))
            requestsMap.put(request.hashCode(), BehaviorSubject.create());

        if (request.getRetry() > 1 && requestsStore.get(request.hashCode()) == null)
            requestsStore.put(request);

        NetworkRequestService_.intent(context)
                .executeRequest(request)
                .start();

        BehaviorSubject<NetworkRequestStatus<T>> subject = (BehaviorSubject<NetworkRequestStatus<T>>) (Object) requestsMap.get(request.hashCode());
        return subject.asObservable();
    }

    public <T> void notifyRequestStatusSuccess(int hashcode, T data) {
        notifyRequestStatus(hashcode, NetworkRequestStatus.completed(data));
    }

    public void notifyRequestStatusError(int hashcode, @NonNull String message, boolean isErrorOther) {
        notifyRequestStatus(hashcode, NetworkRequestStatus.error(message, isErrorOther));
    }

    public void notifyRequestStatusError(int hashcode, ResponseBody body, boolean isErrorOther) {
        String message = "";
        try {
            message = body.string();
            JSONObject jsonObject = new JSONObject(message);
            message = jsonObject.optString("message");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {

            notifyRequestStatus(hashcode, NetworkRequestStatus.error(message, isErrorOther));
        }
        //ResponseGeneric from server, don't need to retry
        //if (!isErrorOther)
        //requestsStore.delete(hashcode);
    }

    public void notifyRequestStatusOnGoing(int hashcode) {
        notifyRequestStatus(hashcode, NetworkRequestStatus.ongoing());
    }

    public void notifyRequestStatusWaitingConnection(int hashcode) {
        notifyRequestStatus(hashcode, NetworkRequestStatus.waitingConnection());
    }

    public void notifyRequestStatus(int hashcode, NetworkRequestStatus status) {
        Utility.logD(TAG, "notifyRequestStatus() called with: " + "hashcode = [" + hashcode + "], status = [" + status + "]");
        if (requestsMap.containsKey(hashcode)) {
            BehaviorSubject<NetworkRequestStatus> subject = requestsMap.get(hashcode);
            subject.onNext(status);
            NetworkRequest request = requestsStore.get(hashcode);

            if (!subject.hasObservers()) {
                //TODO maybe a notification?
                Utility.logD(TAG, String.format("NetworkRequest %s has not observer", request != null ? request.getUri() : hashcode));
            }

            if (status.isCompleted()) {
                requestsMap.remove(hashcode).onCompleted();
            }
        }
    }

    public void addNetworkRequestExecutor(@NonNull NetworkRequestExecutor executor) {
        if (executor == null)
            throw new NullPointerException("executor can't be null");
        if (executorMap.containsKey(executor.getExecutableType()))
            throw new IllegalArgumentException("executor already added");

        executorMap.put(executor.getExecutableType(), executor);

    }

    @NonNull
    public NetworkRequestExecutor getExecutor(@NonNull NetworkRequest request) {
        NetworkRequestExecutor executor = executorMap.get(request.getType());
        if (executor == null)
            throw new UnsupportedOperationException(String.format("NetworkRequest with type %s doesn't have an executor. Did you forgot to add it?", request.getType()));

        return executor;
    }
}