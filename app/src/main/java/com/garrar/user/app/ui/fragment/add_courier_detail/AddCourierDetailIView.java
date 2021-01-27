package com.garrar.user.app.ui.fragment.add_courier_detail;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.PackageType;

import org.json.JSONObject;

public interface AddCourierDetailIView extends MvpView {

    void onError(Throwable e);

    void onSuccess(PackageType packageType);

    void onUpdateSuccess(JSONObject jsonObject);

    void onUpdateFailure(Throwable throwable);
}
