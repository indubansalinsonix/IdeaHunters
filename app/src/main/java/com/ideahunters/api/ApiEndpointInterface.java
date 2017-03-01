package com.ideahunters.api;



import com.ideahunters.model.CategoryListPojo;
import com.ideahunters.model.CommentListPojo;
import com.ideahunters.model.CompanyListPojo;
import com.ideahunters.model.IdeasListModel;
import com.ideahunters.model.Likes;
import com.ideahunters.model.UserLoginModel;
import com.ideahunters.model.UserRegister;

import java.net.URLEncoder;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Created by android3 on 26/8/16.
 */
public interface ApiEndpointInterface {


    @GET("get_company.php")
    Call<CompanyListPojo> CompanyListService();

    @GET("register.php")
    Call<UserRegister> registerUser(@Query("name") String username, @Query("email") String email1, @Query("password") String password, @Query("phone_number") String phone, @Query("company") String company, @Query("unique_id") String android_id, @Query("device_id") String token, @Query("status") String status, @Query("device_type") String device_type,@Query("employee_code") String emp_code);

    @GET("login.php")
    Call<UserLoginModel> UserLoginService(@Query("email") String email, @Query("password") String password);

    @GET
    Call<IdeasListModel> IdeasListService(@Url String url);
    @GET("get_categories.php")
    Call<CategoryListPojo> getIdeaCategoryList(@Query("admin_id")String admin_id);

    @FormUrlEncoded
    @POST("post_idea.php")
    Call<UserRegister> submitIdea1(@Field("user_id")String user_id,@Field("idea_title") String idea_title,@Field("cat_id") String category_name,@Field("subcat_id") String subcategory_name, @Field("explain_idea")String idea_submit,@Field("key_result_expected") String key_result,@Field("company_id") String company_id,@Field("imagename") String image_name,@Field("file") String file);


    @GET("get_comment.php")
    Call<CommentListPojo> CommentListService(@Query("suggestion_id") String sug_id,@Query("user_id") String value);
    @GET("comment.php")
    Call<UserRegister> postComment(@Query("user_id")String value,@Query("sug_id") String sug_id,@Query("comment") String message);
    @GET("like.php")
    Call<Likes> postLikes(@Query("user_id") String value, @Query("sug_id") String sug_id);

    @GET("idea_description.php")
    Call<IdeasListModel> IdeasDetailsService(@Query("sug_id")String sug_id);
    @GET("delete_idea.php")
    Call<UserRegister> deleteIdea(@Query("user_id")String user_id,@Query("sug_id") String sug_id);

    @FormUrlEncoded
    @POST("edit_idea.php")
    Call<UserRegister> updateIdea(@Field("user_id")String user_id,@Field("title") String idea_title,@Field("cat_id") String category_name,@Field("subcat_id") String subcategory_name, @Field("explain_idea")String idea_submit,@Field("key_result") String key_result,@Field("company_id") String company_id,@Field("imagename") String image_name,@Field("file") String file,@Field("sug_id") String sug_id);
    @GET("report_comment.php")
    Call<UserRegister> reportComment(@Query("user_id") String value, @Query("sug_id") String sug_id,@Query("comment_id") String comment_id);

    @GET("report_suggestion.php")
    Call<UserRegister> reportIdea(@Query("user_id") String value, @Query("sug_id") String sug_id);
}
