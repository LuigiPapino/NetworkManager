package com.fleetmatics.networkingworkapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Date;

/**
 * Created by luigi.papino on 19/04/16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class Job implements Parcelable {

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel source) {
            return new Job(source);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };
    int id;
    int customerId;
    String customerName;
    int workerId;
    String workerName;
    String title;
    String description;
    Date scheduleTime;
    int status;
    String workerComments;
    double latitude;
    double longitude;
    String addres;
    Date estimateTime;

    public Job() {
    }


    protected Job(Parcel in) {
        this.id = in.readInt();
        this.customerId = in.readInt();
        this.customerName = in.readString();
        this.workerId = in.readInt();
        this.workerName = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        long tmpScheduleTime = in.readLong();
        this.scheduleTime = tmpScheduleTime == -1 ? null : new Date(tmpScheduleTime);
        this.status = in.readInt();
        this.workerComments = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.addres = in.readString();
        long tmpEstimateTime = in.readLong();
        this.estimateTime = tmpEstimateTime == -1 ? null : new Date(tmpEstimateTime);
    }

    @Override
    public String toString() {
        return "Job{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", workerId=" + workerId +
                ", workerName='" + workerName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", scheduleTime=" + scheduleTime +
                ", status=" + status +
                ", workerComments='" + workerComments + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", addres='" + addres + '\'' +
                ", estimateTime=" + estimateTime +
                '}';
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerComments() {
        return workerComments;
    }

    public void setWorkerComments(String workerComments) {
        this.workerComments = workerComments;
    }

    public String getAddres() {
        return addres;
    }

    public void setAddres(String addres) {
        this.addres = addres;
    }

    public Date getEstimateTime() {
        return estimateTime;
    }

    public void setEstimateTime(Date estimateTime) {
        this.estimateTime = estimateTime;
    }

    public Date getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(Date scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getWorkerId() {
        return workerId;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.customerId);
        dest.writeString(this.customerName);
        dest.writeInt(this.workerId);
        dest.writeString(this.workerName);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeLong(scheduleTime != null ? scheduleTime.getTime() : -1);
        dest.writeInt(this.status);
        dest.writeString(this.workerComments);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeString(this.addres);
        dest.writeLong(estimateTime != null ? estimateTime.getTime() : -1);
    }
}
