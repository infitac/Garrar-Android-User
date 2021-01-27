package com.garrar.user.app.ui.fragment.book_ride;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.PromoResponse;
import com.garrar.user.app.data.network.model.SendRequest;

import org.json.JSONObject;

public interface BookRideIView extends MvpView {
    void onSuccess(SendRequest object);

    void onError(Throwable e);

    void onSuccessCoupon(PromoResponse promoResponse);

    void onUpdateSuccess(JSONObject jsonObject);

    void onUpdateFailure(Throwable throwable);
}
