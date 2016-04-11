package com.fleetmatics.networkmanager.model;

import android.support.annotation.NonNull;

/**
 * Created by luigi.papino on 06/04/16.
 */
public class NetworkRequestStatus<T> {

    private String message = "";
    private Status status = Status.IDLE;
    private T data;

    public NetworkRequestStatus(String message, Status status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public NetworkRequestStatus(Status status) {
        this("", status, null);
    }

    public NetworkRequestStatus(String message, Status status) {
        this(message, status, null);
    }

    public NetworkRequestStatus(Status status, T data) {
        this("", status, data);
    }

    public static <T> NetworkRequestStatus<T> completed(T data) {
        return new NetworkRequestStatus<T>(Status.SUCCESS, data);
    }

    public static NetworkRequestStatus ongoing() {
        return new NetworkRequestStatus(Status.ONGOING);
    }


    public static NetworkRequestStatus waitingConnection() {
        return new NetworkRequestStatus(Status.WAITING_CONNECTION);
    }

    public static NetworkRequestStatus error(@NonNull String message, boolean isErrorOther) {
        return new NetworkRequestStatus(message, isErrorOther ? Status.ERROR_OTHER : Status.ERROR_SERVER);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isCompleted() {
        return status == Status.SUCCESS || status == Status.ERROR_OTHER || status == Status.ERROR_SERVER;
    }

    public boolean isWaitingConnection() {
        return status == Status.WAITING_CONNECTION;
    }
    public boolean isError() {
        return status == Status.ERROR_OTHER || status == Status.ERROR_SERVER;
    }

    public boolean isErrorOther() {
        return status == Status.ERROR_OTHER;
    }

    public boolean isOnGoing() {
        return status == Status.ONGOING;
    }

    public boolean isNotError() {
        return status != Status.ERROR_OTHER && status != Status.ERROR_SERVER;
    }

    @Override
    public String toString() {
        return "NetworkRequestStatus{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", data=" + data +
                '}';
    }

    public enum Status {IDLE, ONGOING, WAITING_CONNECTION, SUCCESS, ERROR_SERVER, ERROR_OTHER}
}
