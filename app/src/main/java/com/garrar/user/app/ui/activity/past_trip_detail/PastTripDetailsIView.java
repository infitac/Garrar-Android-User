package com.garrar.user.app.ui.activity.past_trip_detail;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.Datum;

import java.util.List;

public interface PastTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> pastTripDetails);

    void onError(Throwable e);
}
