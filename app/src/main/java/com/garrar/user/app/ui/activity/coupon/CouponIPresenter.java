package com.garrar.user.app.ui.activity.coupon;

import com.garrar.user.app.base.MvpPresenter;

public interface CouponIPresenter<V extends CouponIView> extends MvpPresenter<V> {
    void coupon();
}
