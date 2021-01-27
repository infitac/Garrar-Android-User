package com.garrar.user.app.ui.activity.invite_friend;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.User;

public interface InviteFriendIView extends MvpView {

    void onSuccess(User user);

    void onError(Throwable e);

}
