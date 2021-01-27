package com.garrar.user.app.ui.activity.past_trip_detail;

import com.garrar.user.app.base.BasePresenter;
import com.garrar.user.app.data.network.APIClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class PastTripDetailsPresenter<V extends PastTripDetailsIView> extends BasePresenter<V>
        implements PastTripDetailsIPresenter<V> {

    @Override
    public void getPastTripDetails(Integer requestId) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .pastTripDetails(requestId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(getMvpView()::onSuccess, getMvpView()::onError));
    }
}
