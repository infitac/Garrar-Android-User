package com.garrar.user.app.ui.fragment.view_courier_detail;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.CourierDetail;

public interface ViewCourierDetailIView extends MvpView {

    void onError(Throwable e);

    void onSuccess(CourierDetail courierDetail);
}
