package com.garrar.user.app.ui.activity.profile;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.User;

public interface ProfileIView extends MvpView {

    void onSuccess(User user);

    void onUpdateSuccess(User user);

    void onUpdateFailure(Throwable e);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);
}
