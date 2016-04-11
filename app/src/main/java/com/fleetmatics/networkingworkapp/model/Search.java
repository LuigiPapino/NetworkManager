package com.fleetmatics.networkingworkapp.model;

/**
 * Created by luigi.papino on 08/04/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;


@JsonObject
public class Search implements Parcelable{

    private static final String FIELD_YEAR = "Year";
    private static final String FIELD_POSTER = "Poster";
    private static final String FIELD_TITLE = "Title";
    private static final String FIELD_IMDB_ID = "imdbID";
    private static final String FIELD_TYPE = "Type";


    @SerializedName(FIELD_YEAR)
    @JsonField(name = FIELD_YEAR)
    private int mYear;
    @SerializedName(FIELD_POSTER)
    @JsonField(name = FIELD_POSTER)
    private String mPoster;
    @SerializedName(FIELD_TITLE)
    @JsonField(name = FIELD_TITLE)
    private String mTitle;
    @SerializedName(FIELD_IMDB_ID)
    @JsonField(name = FIELD_IMDB_ID)
    private String mImdbID;
    @SerializedName(FIELD_TYPE)
    @JsonField(name = FIELD_TYPE)
    private String mType;


    public Search(){

    }

    public void setYear(int year) {
        mYear = year;
    }

    public int getYear() {
        return mYear;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setImdbID(String imdbID) {
        mImdbID = imdbID;
    }

    public String getImdbID() {
        return mImdbID;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getType() {
        return mType;
    }

    public Search(Parcel in) {
        mYear = in.readInt();
        mPoster = in.readString();
        mTitle = in.readString();
        mImdbID = in.readString();
        mType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        public Search[] newArray(int size) {
            return new Search[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mYear);
        dest.writeString(mPoster);
        dest.writeString(mTitle);
        dest.writeString(mImdbID);
        dest.writeString(mType);
    }

    @Override
    public String toString(){
        return "year = " + mYear + ", poster = " + mPoster + ", title = " + mTitle + ", imdbID = " + mImdbID + ", type = " + mType;
    }


}