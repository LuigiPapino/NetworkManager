package com.fleetmatics.networkmanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luigi.papino on 06/04/16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS_AND_ACCESSORS)
public class NetworkRequest implements Parcelable {

    public static final Creator<NetworkRequest> CREATOR = new Creator<NetworkRequest>() {
        public NetworkRequest createFromParcel(Parcel source) {
            return new NetworkRequest(source);
        }

        public NetworkRequest[] newArray(int size) {
            return new NetworkRequest[size];
        }
    };
    private String type;
    private Map<String, String> params = new HashMap<>();
    private String uri;
    private int retry;

    public NetworkRequest() {
    }

    protected NetworkRequest(Parcel in) {

        this.type = in.readString();
        this.uri = in.readString();
        this.retry = in.readInt();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            params.put(in.readString(), in.readString());
        }
    }

    public int getRetry() {
        return retry;
    }

    public void setRetry(int retry) {
        this.retry = retry;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NetworkRequest that = (NetworkRequest) o;

        if (type != that.type) return false;
        if (params != null ? !params.equals(that.params) : that.params != null) return false;
        return uri != null ? uri.equals(that.uri) : that.uri == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (params != null ? params.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return Math.abs(result);
    }

    @Override
    public String toString() {
        return "NetworkRequest{" +
                "type=" + type +
                ", params=" + params +
                ", uri='" + uri + '\'' +
                ", retry='" + retry + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(this.uri);
        dest.writeInt(this.retry);
        dest.writeInt(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }

    }

    /**
     * Generate and set the uri for this network request
     *
     * @param withTimestamp include or not the timestamp at the end
     * @return String
     */
    public String generateUri(boolean withTimestamp) {
        StringBuilder uri = new StringBuilder(type.toString());
        for (Map.Entry<String, String> entry :
                params.entrySet()) {
            uri.append("_")
                    .append(entry.getKey())
                    .append("_")
                    .append(entry.getValue());
        }
        if (withTimestamp)
            uri.append("_")
                    .append(new Date().getTime());

        this.uri = uri.toString();
        return this.uri;


    }

    public static class Builder {

        private String type;
        private Map<String, String> params = new HashMap<>();
        private boolean isWithTimestamp;
        private int retry = 0;

        public Builder(@NonNull NetworkRequestExecutor executor, boolean isWithTimestamp) {
            type = executor.getExecutableType();
            this.isWithTimestamp = isWithTimestamp;
        }

        public Builder putParam(@NonNull String key, @NonNull String value) {
            params.put(key, value);
            return this;
        }

        public Builder retry(int count) {
            retry = count;
            return this;
        }

        public NetworkRequest build() {
            NetworkRequest request = new NetworkRequest();
            request.setType(Builder.this.type);
            request.setParams(Builder.this.params);
            request.setRetry(Builder.this.retry);
            request.generateUri(isWithTimestamp);
            return request;
        }
    }

}
