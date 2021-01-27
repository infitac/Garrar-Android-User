package com.garrar.user.app.ui.activity.upcoming_trip_detail;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.Datum;

import java.util.List;

public interface UpcomingTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> upcomingTripDetails);

    void onError(Throwable e);
}
