package com.garrar.user.app.ui.activity.setting;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.AddressResponse;

public interface SettingsIView extends MvpView {

    void onSuccessAddress(Object object);

    void onLanguageChanged(Object object);

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
