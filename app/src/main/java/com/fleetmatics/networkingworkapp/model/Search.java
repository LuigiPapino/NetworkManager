package com.fleetmatics.networkingworkapp.model;

/**
 * Created by luigi.papino on 08/04/16.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Search implements Parcelable {

    public static final Creator<Search> CREATOR = new Creator<Search>() {
        public Search createFromParcel(Parcel in) {
            return new Search(in);
        }

        public Search[] newArray(int size) {
            return new Search[size];
        }
    };
    private static final String FIELD_YEAR = "Year";
    private static final String FIELD_POSTER = "Poster";
    private static final String FIELD_TITLE = "Title";
    private static final String FIELD_IMDB_ID = "imdbID";
    private static final String FIELD_TYPE = "Type";
    @JsonField(name = FIELD_YEAR)
    private int mYear;
    @JsonField(name = FIELD_POSTER)
    private String mPoster;
    @JsonField(name = FIELD_TITLE)
    private String mTitle;
    @JsonField(name = FIELD_IMDB_ID)
    private String mImdbID;
    @JsonField(name = FIELD_TYPE)
    private String mType;

    public Search() {

    }

    public Search(Parcel in) {
        mYear = in.readInt();
        mPoster = in.readString();
        mTitle = in.readString();
        mImdbID = in.readString();
        mType = in.readString();
    }

    public int getYear() {
        return mYear;
    }

    public void setYear(int year) {
        mYear = year;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImdbID() {
        return mImdbID;
    }

    public void setImdbID(String imdbID) {
        mImdbID = imdbID;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mYear);
        dest.writeString(mPoster);
        dest.writeString(mTitle);
        dest.writeString(mImdbID);
        dest.writeString(mType);
    }

    @Override
    public String toString() {
        return "year = " + mYear + ", poster = " + mPoster + ", title = " + mTitle + ", imdbID = " + mImdbID + ", type = " + mType;
    }


}