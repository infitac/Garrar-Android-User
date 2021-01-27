package com.garrar.user.app.ui.activity.social;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.Token;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface SocialIView extends MvpView {
    void onSuccess(Token token);

    void onError(Throwable e);
}
