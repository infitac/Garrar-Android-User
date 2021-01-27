package com.garrar.user.app.ui.activity.view_courie_detail;



import com.garrar.user.app.base.BasePresenter;
import com.garrar.user.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ViewCourierDetailPresenter<V extends ViewCourierDetailIView> extends BasePresenter<V> implements ViewCourierDetailIPresenter<V> {

    @Override
    public void getCourierdetail(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .getCourierDetail(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::OnError));
    }
}
