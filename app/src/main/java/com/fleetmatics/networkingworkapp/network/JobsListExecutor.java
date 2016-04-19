package com.fleetmatics.networkingworkapp.network;

import android.support.annotation.NonNull;
import android.util.Log;

import com.fleetmatics.networkingworkapp.model.JobList;
import com.fleetmatics.networkmanager.model.NetworkRequest;
import com.fleetmatics.networkmanager.model.NetworkRequestExecutor;
import com.fleetmatics.networkmanager.model.NetworkRequestStatus;

import retrofit2.Response;
import rx.Observable;

/**
 * Created by luigi.papino on 08/04/16.
 */
public class JobsListExecutor extends NetworkRequestExecutor<JobList> {


    private static final String TAG = JobsListExecutor.class.getSimpleName();
    private static JobsListExecutor instance;
    private final NetworkApi<APIService> networkApi;

    public JobsListExecutor(NetworkApi<APIService> networkApi) {
        this.networkApi = networkApi;
        instance = this;
    }

    public static NetworkRequest createRequest(int customerId) {
        return new NetworkRequest.Builder(getInstance(), false)
                .putParam("customerId", String.valueOf(customerId))
                .build();
    }

    public static NetworkRequestExecutor getInstance() {
        return instance;
    }
    @Override
    public Observable<Response<JobList>> createNetworkRequestObservable(@NonNull NetworkRequest request) {
        return networkApi.getApiService().searchByCustomer(request.getParams().get("customerId"));
        /*ArrayList<Job> jobs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Job j = new Job();
            j.setTitle("Title " + i);
            j.setStatus(1);
            j.setScheduleTime(new Date());
            j.setEstimateTime(new Date());
            j.setLatitude(i);
            j.setLongitude(i);
            jobs.add(j);
        }
        JobList list = new JobList();
        list.setJobs(jobs);
        Response<JobList> response = Response.success(list);
        BehaviorSubject<Response<JobList>> subject = BehaviorSubject.create();
        subject.onNext(response);
        //subject.onCompleted();
        return subject.asObservable();*/

    }

    @Override
    public String getExecutableType() {
        return "GET_JOBS";
    }

    @Override
    public void updatedStatus(NetworkRequest request, NetworkRequestStatus status, boolean hasObservers) {
        Log.d(TAG, "updatedStatus() called with: " + "request = [" + request + "], status = [" + status + "], hasObservers = [" + hasObservers + "]");
    }
}
