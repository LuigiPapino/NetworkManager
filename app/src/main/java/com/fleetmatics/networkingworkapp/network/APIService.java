package com.fleetmatics.networkingworkapp.network;


import com.fleetmatics.networkingworkapp.model.ResponseSearch;
import com.google.gson.JsonObject;


import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by nietzsche on 12/02/16.
 */
public interface APIService {

    @GET("?r=json&type=movie")
    Observable<Response<ResponseSearch>> search(@Query("s") String search, @Query("page") String page);

  /*  @GET("users/me")
    Observable<Response<User>> getMe();

    @GET("bitos/{bito_url_id}")
    Observable<Response<Bito>> getBito(@Path("bito_url_id") String bitoUrlId);

    @PUT("users/me/bitoships/{bito_url_id}/{bito_followed_url_id}")
    Observable<Response<User>> putBitoship(@Path("bito_url_id") String bitoUrlId, @Path("bito_followed_url_id") String bitoUrlFollowedId);

    @POST("users")
    Observable<Response<User>> postUser(@Body JsonObject body);

    @POST("users")
    Observable<Response<User>> postUserSignin(@Query("username") String username, @Query("plainPassword") String plainPassword);

    @POST("users/{username}/reset_password")
    Observable<Response<ResponseGeneric>> postUserResetPassword(@Path("username") String username);

    @PATCH("users/me")
    Observable<Response<User>> patchUserMe(@Body JsonObject body);

    @PUT("users/me/bitos/{bito_id}/has_socials")
    Observable<Response<User>> putBitoHasSocial(@Path("bito_id") String bitoId, @Query("social_id") String socialId, @Query("url") String value);

    @PATCH("users/me/bitos/{bito_id}/has_socials/{has_id}")
    Observable<Response<User>> patchBitoHasSocial(@Path("bito_id") String bitoId, @Path("has_id") String hasSocialId, @Query("url") String value);

    @DELETE("users/me/bitos/{bito_id}/has_socials/{has_id}")
    Observable<Response<User>> deleteBitoHasSocial(@Path("bito_id") String bitoId, @Path("has_id") String hasSocialId);

    @PUT("users/me/bitoships/{id}/note/{text}")
    Observable<Response<User>> putBitoshipNote(@Path("id") String id, @Path("text") String text);

    *//*@Multipart
    @POST("users/me/bitos")
    Observable<ResponseGeneric<User>> postBito(@Part("bito[picture_avatar]\"; filename=\"picture_avatar.jpg\" ") RequestBody avatar,
                                        @Part("bito[picture_background]\"; filename=\"picture_background.jpg\" ") RequestBody background,
                                        @Part("bito[name_first]") String nameFirst,
                                        @Part("bito[name_last]") String nameLast,
                                        @Part("bito[work]") String work,
                                        @Part("bito[company]") String company,
                                        @Part("bito[background]") String backgroundColor
                                        );*//*

    @POST("users/me/bitos")
    Observable<Response<User>> postBito(@Body RequestBody body);

    @POST("users/me/bitos/{id}/pictures/{type}")
    Observable<Response<User>> postBitoPicture(@Path("id") String bitoId, @Path("type") String pictureType, @Body RequestBody body);

    @PUT("rooms/{pin}/{bito_url_id}")
    Observable<Response<Room>> putRoomJoin(@Path("pin") String pin, @Path("bito_url_id") String bitoUrlId);

    @POST("rooms/{bito_url_id}")
    Observable<Response<Room>> postRoom(@Path("bito_url_id") String bitoUrlId, @Query("duration") String duration, @Query("name") String roomName);*/

}
