package com.garrar.user.app.ui.activity.splash;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.CheckVersion;
import com.garrar.user.app.data.network.model.Service;
import com.garrar.user.app.data.network.model.User;

import java.util.List;

public interface SplashIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onSuccess(User user);

    void onError(Throwable e);

    void onSuccess(CheckVersion checkVersion);
}
