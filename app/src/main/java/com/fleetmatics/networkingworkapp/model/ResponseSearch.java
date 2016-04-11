package com.fleetmatics.networkingworkapp.model;

/**
 * Created by luigi.papino on 08/04/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


@JsonObject
public class ResponseSearch implements Parcelable{

    public static final Creator<ResponseSearch> CREATOR = new Creator<ResponseSearch>() {
        public ResponseSearch createFromParcel(Parcel in) {
            return new ResponseSearch(in);
        }

        public ResponseSearch[] newArray(int size) {
            return new ResponseSearch[size];
        }
    };
    private static final String FIELD_SEARCH = "Search";
    private static final String FIELD_ERROR = "Error";
    private static final String FIELD_RESPONSE = "Response";
    private static final String FIELD_TOTAL_RESULTS = "totalResults";
    @SerializedName(FIELD_SEARCH)
    @JsonField(name = FIELD_SEARCH)
    private List<Search> mSearches;
    @SerializedName(FIELD_ERROR)
    @JsonField(name = FIELD_ERROR)
    private String mError;
    @SerializedName(FIELD_RESPONSE)
    @JsonField(name = FIELD_RESPONSE)
    private String mResponse;
    @SerializedName(FIELD_TOTAL_RESULTS)
    @JsonField(name = FIELD_TOTAL_RESULTS)
    private int mTotalResult;
    private String keyword;

    public ResponseSearch(){

    }

    public ResponseSearch(Parcel in) {
        mSearches = new ArrayList<Search>();
        in.readTypedList(mSearches, Search.CREATOR);
        mError = in.readString();
        mResponse = in.readString();
        mTotalResult = in.readInt();
        keyword = in.readString();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Search> getSearches() {
        return mSearches;
    }

    public void setSearches(List<Search> searches) {
        mSearches = searches;
    }

    public String getError() {
        return mError;
    }

    public void setError(String error) {
        mError = error;
    }

    public String getResponse() {
        return mResponse;
    }

    public void setResponse(String response) {
        mResponse = response;
    }

    public int getTotalResult() {
        return mTotalResult;
    }

    public void setTotalResult(int totalResult) {
        mTotalResult = totalResult;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mSearches);
        dest.writeString(mError);
        dest.writeString(mResponse);
        dest.writeInt(mTotalResult);
        dest.writeString(keyword);
    }

    @Override
    public String toString(){
        return "searches = " + mSearches + ", error = " + mError + ", response = " + mResponse + ", totalResult = " + mTotalResult;
    }


}