package com.garrar.user.app.ui.activity.change_password;

import com.garrar.user.app.base.MvpView;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface ChangePasswordIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
