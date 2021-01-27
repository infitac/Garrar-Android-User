package com.garrar.user.app.ui.activity.view_courie_detail;


import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.CourierDetail;

public interface ViewCourierDetailIView extends MvpView {

    void OnError(Throwable e);

    void onSuccess(CourierDetail courierDetail);
}
