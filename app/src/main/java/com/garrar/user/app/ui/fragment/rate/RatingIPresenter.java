package com.garrar.user.app.ui.fragment.rate;

import com.garrar.user.app.base.MvpPresenter;

import java.util.HashMap;

public interface RatingIPresenter<V extends RatingIView> extends MvpPresenter<V> {

    void rate(HashMap<String, Object> obj);
}
