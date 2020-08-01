package com.example.beckart.net;

import com.example.beckart.model.Cart;
import com.example.beckart.model.CartApiResponse;
import com.example.beckart.model.Favorite;
import com.example.beckart.model.FavoriteApiResponse;
import com.example.beckart.model.Image;
import com.example.beckart.model.LoginApiResponse;
import com.example.beckart.model.NewsFeedResponse;
import com.example.beckart.model.OrderApiResponse;
import com.example.beckart.model.Ordering;
import com.example.beckart.model.Otp;
import com.example.beckart.model.ProductApiResponse;
import com.example.beckart.model.RegisterApiResponse;
import com.example.beckart.model.Review;
import com.example.beckart.model.ReviewApiResponse;
import com.example.beckart.model.Shipping;
import com.example.beckart.model.User;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {


    @Headers("Content-Type: application/json")
    @POST("android/api/createUser.php")
    Call<RegisterApiResponse> createUser(@Body User user);

    @GET("android/api/logInUser.php")
    Call<LoginApiResponse> logInUser(@Query("email") String email, @Query("password") String password);

    @DELETE("users/{userId}")
    Call<ResponseBody> deleteAccount(@Path("userId") int userId);

    @Multipart
    @PUT("users/upload")
    Call<ResponseBody> uploadPhoto(@Part MultipartBody.Part userPhoto, @Part("id") RequestBody userId);

    @PUT("users/info")
    Call<ResponseBody> updatePassword(@Query("password") String password, @Query("id") int userId);

    @Multipart
    @POST("android/api/insertProduct.php")
    Call<ResponseBody> insertProduct(@PartMap Map<String, RequestBody> productInfo, @Part MultipartBody.Part image);

    @GET("users/getImage")
    Call<Image> getUserImage(@Query("id") int userId);

    @GET("android/api/getOtp.php")
    Call<Otp> getOtp(@Query("email") String email);

//    @GET("products")
//    Call<ProductApiResponse> getProducts(@Query("page") int page);

    @GET("android/api/getProductsByCategory.php")
    Call<ProductApiResponse> getProductsByCategory(@Query("category") String category, @Query("userId") int userId,@Query("page") int page);

    @GET("products/search")
    Call<ProductApiResponse> searchForProduct(@Query("q") String keyword, @Query("userId") int userId);

    @Headers("Content-Type: application/json")
    @POST("android/api/addFavorite.php")
    Call<ResponseBody> addFavorite(@Body Favorite favorite);

    @FormUrlEncoded
    @POST("android/api/removeFavorite.php")
    Call<ResponseBody> removeFavorite(@Field("userId") int userId, @Field("productId") int productId);

    @GET("android/api/getFavorites.php")
    Call<FavoriteApiResponse> getFavorites(@Query("userId") int userId);

    @POST("android/api/addToCart.php")
    Call<ResponseBody> addToCart(@Body Cart cart);

    @FormUrlEncoded
    @POST("android/api/removeFromCart.php")
    Call<ResponseBody> removeFromCart(@Field("userId") int userId, @Field("productId") int productId);

    @GET("android/api/getProductsInCart.php")
    Call<CartApiResponse> getProductsInCart(@Query("userId") int userId);


    @POST("android/api/addReview.php")
    Call<ResponseBody> addReview(@Body Review review);

    @GET("android/api/getAllReviews.php")
    Call<ReviewApiResponse> getAllReviews(@Query("productId") int productId);

    @GET("posters")
    Call<NewsFeedResponse> getPosters();

    @GET("orders/get")
    Call<OrderApiResponse> getOrders(@Query("userId") int userId);

    @POST("android/api/addShippingAddress.php")
    Call<ResponseBody> addShippingAddress(@Body Shipping shipping);

    @POST("android/api/orderProduct.php")
    Call<ResponseBody> orderProduct(@Body Ordering ordering);
}
