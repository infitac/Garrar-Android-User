package com.garrar.user.app.ui.activity.login;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.ForgotResponse;
import com.garrar.user.app.data.network.model.Token;

public interface LoginIView extends MvpView {
    void onSuccess(Token token);

    void onSuccess(ForgotResponse object);

    void onError(Throwable e);
}
