package com.garrar.user.app.ui.activity.upcoming_trip_detail;

import com.garrar.user.app.base.MvpPresenter;

public interface UpcomingTripDetailsIPresenter<V extends UpcomingTripDetailsIView> extends MvpPresenter<V> {

    void getUpcomingTripDetails(Integer requestId);
}
