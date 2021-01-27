package com.garrar.user.app.ui.fragment.add_courier_detail;

import com.garrar.user.app.base.MvpPresenter;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public interface AddCourierDetailIPresenter<V extends AddCourierDetailIView> extends MvpPresenter<V> {
 void get_package_type();
 void update(HashMap<String, RequestBody> obj, @Part MultipartBody.Part filename);
}
