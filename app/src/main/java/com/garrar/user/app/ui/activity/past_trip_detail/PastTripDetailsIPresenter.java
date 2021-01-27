package com.garrar.user.app.ui.activity.past_trip_detail;

import com.garrar.user.app.base.MvpPresenter;

public interface PastTripDetailsIPresenter<V extends PastTripDetailsIView> extends MvpPresenter<V> {

    void getPastTripDetails(Integer requestId);
}
