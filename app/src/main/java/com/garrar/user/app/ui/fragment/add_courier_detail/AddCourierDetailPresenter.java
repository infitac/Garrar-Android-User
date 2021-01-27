package com.garrar.user.app.ui.fragment.add_courier_detail;

import com.garrar.user.app.base.BasePresenter;
import com.garrar.user.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public class AddCourierDetailPresenter<V extends AddCourierDetailIView> extends BasePresenter<V> implements AddCourierDetailIPresenter<V> {

    @Override
    public void get_package_type() {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getPackageType()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }

    @Override
    public void update(HashMap<String, RequestBody> obj, @Part MultipartBody.Part filename) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .updatepackagedata(obj, filename)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onUpdateSuccess, getMvpView()::onUpdateFailure));
    }
}
