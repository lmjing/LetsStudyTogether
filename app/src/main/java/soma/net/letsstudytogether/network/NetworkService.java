package soma.net.letsstudytogether.network;


import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import soma.net.letsstudytogether.model.Comment;
import soma.net.letsstudytogether.model.Participant;
import soma.net.letsstudytogether.model.Region;
import soma.net.letsstudytogether.model.Study;
import soma.net.letsstudytogether.model.User;
import soma.net.letsstudytogether.model.UserTime;

public interface NetworkService {

    // TODO: 1. 서버와 네트워킹을 하기 위한 서비스(인터페이스로 구현)

    @Headers("Content-Type: application/json")

    //회원가입
    @POST("/user")
    Call<User> newUser(@Body User user);
    //회원 정보 가져오기
    @GET("/user/{id}")
    Call<User> getUser(@Path("id") String id);
    // 회원탈퇴
    @DELETE("/user/{id}")
    Call<String> getDelete(@Path("id") String id);
    // 정보수정
    @PUT("/user")
    Call<String> editUser(@Body User user);
    // 시간 저장
    @PUT("/user/time")
    Call<String> editUserTime(@Body UserTime user);

    // 모집중인 스터디목록
    @GET("/study")
    Call<List<Study>> getStudyList();
    // 스터디등록
    @POST("/study/{name}")
    Call<String> newStudy(@Body Study study, @Path("name") String name);
    // 특정 스터디
    @GET("/study/{id}")
    Call<Study> toMoveStudy(@Path("id") int id);
    // 스터디 삭제
    @DELETE("/study/{id}")
    Call<String> deleteStudy(@Path("id") int id);
    // 스터디 마감
    @PUT("/study")
    Call<String> endStudy(@Body Study study);

    //댓글등록
    @POST("/comment")
    Call<String> newComment(@Body Comment comment);
    //댓글 목록 가져오기
    @GET("/comment/{studyid}")
    Call<List<Comment>> getCommentList(@Path("studyid") int studyid);
    // 댓글 삭제
    @DELETE("/comment/{studyid}/{writetime}")
    Call<String> deleteComment(@Path("studyid") int studyid, @Path("writetime") String writetime);
    // 댓글 수정
    @PUT("/comment")
    Call<String> editComment(@Body Comment comment);

    // 불참
    @DELETE("/participate/{id}/{userid}")
    Call<String> comeoutStudy(@Path("id") int id, @Path("userid") String userid);
    // 참여
    @POST("/participate/{su1}/{su2}")
    Call<String> participateStudy(@Body Participant participant, @Path("su1") int su1, @Path("su2") int su2);
    //참여자 목록 가져오기
    @GET("/participate/{id}")
    Call<List<Participant>> getParticipant(@Path("id") int studyid);
    //특정 참여자 찾기
    @GET("/participate/{userid}/{id}")
    Call<Participant> findParticipant(@Path("userid") String userid, @Path("id") int id);

    //push보내기
    @GET("/Push/{registerID}")
    Call<String> pushGCM(@Path("registerID") String id);

    //시 목록 가져오기
    @GET("/region")
    Call<List<Region>> getCity();
    //군구 목록 가져오기
    @GET("/region/{city}")
    Call<List<Region>> getGu(@Path("city") String city);
}
