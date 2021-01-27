package com.garrar.user.app.ui.activity.coupon;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.PromoResponse;

public interface CouponIView extends MvpView {
    void onSuccess(PromoResponse object);

    void onError(Throwable e);
}
