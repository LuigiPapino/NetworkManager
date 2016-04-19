package com.fleetmatics.networkingworkapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by luigi.papino on 19/04/16.
 */
@JsonObject(fieldDetectionPolicy = JsonObject.FieldDetectionPolicy.NONPRIVATE_FIELDS)
public class JobList implements Parcelable {
    public static final Creator<JobList> CREATOR = new Creator<JobList>() {
        @Override
        public JobList createFromParcel(Parcel in) {
            return new JobList(in);
        }

        @Override
        public JobList[] newArray(int size) {
            return new JobList[size];
        }
    };
    List<Job> jobs;


    protected JobList(Parcel in) {
        jobs = in.createTypedArrayList(Job.CREATOR);
    }

    public JobList() {

    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(jobs);
    }
}
