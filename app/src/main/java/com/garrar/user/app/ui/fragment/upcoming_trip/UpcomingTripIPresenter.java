package com.garrar.user.app.ui.fragment.upcoming_trip;

import com.garrar.user.app.base.MvpPresenter;

import java.util.HashMap;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface UpcomingTripIPresenter<V extends UpcomingTripIView> extends MvpPresenter<V> {
    void upcomingTrip();

    void cancelRequest(HashMap<String, Object> params);
}
