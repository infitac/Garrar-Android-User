package com.garrar.user.app.ui.fragment.book_ride;

import com.garrar.user.app.base.MvpPresenter;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;


public interface BookRideIPresenter<V extends BookRideIView> extends MvpPresenter<V> {
//    void rideNow(SendRequest obj);
    void rideNow(HashMap<String, Object> obj);

    void getCouponList();

    void update(HashMap<String, RequestBody> obj, @Part MultipartBody.Part filename);
}
