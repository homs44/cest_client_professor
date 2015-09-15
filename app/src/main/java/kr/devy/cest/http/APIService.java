package kr.devy.cest.http;

import com.google.gson.JsonElement;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by pc on 2015-07-31.
 */
public interface APIService {

    //로그인
    @FormUrlEncoded
    @POST("/login")
    void login(@Field("user_id") String user_id, @Field("password") String password, @Field("gcm_id") String gcm_id, @Field("type") int type, Callback<JsonElement> callback);

    //수업정보
    @GET("/class/professor/{c_user_id}/{semester}")
    void getMyClass(@Path("c_user_id") int c_user_id, @Path("semester") String semester, Callback<JsonElement> callback);


    //출석시작
    @FormUrlEncoded
    @POST("/attendance")
    void startAttendance(@Field("c_class_id") int c_class_id, @Field("name")
    String name, Callback<JsonElement> callback);

    //출석정보
    @GET("/attendance/professor/{c_class_id}")
    void getAttendance(@Path("c_class_id") int c_class_id, Callback<JsonElement> callback);

    //출석결과
    @GET("/attendance/professor/result/{c_class_id}/{uuid}")
    void getAttendanceResult(@Path("c_class_id") int c_class_id,@Path("uuid") String uuid, Callback<JsonElement> callback);

    //출석수정
    @FormUrlEncoded
    @POST("/attendance/professor/result")
    void updateAttendanceResult(@Field("c_user_id") int c_user_id, @Field("uuid")
    String uuid,@Field("result")
    int result, Callback<JsonElement> callback);


}
