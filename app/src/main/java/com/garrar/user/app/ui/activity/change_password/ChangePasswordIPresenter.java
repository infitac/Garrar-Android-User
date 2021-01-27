package com.garrar.user.app.ui.activity.change_password;


import com.garrar.user.app.base.MvpPresenter;

import java.util.HashMap;


public interface ChangePasswordIPresenter<V extends ChangePasswordIView> extends MvpPresenter<V> {
    void changePassword(HashMap<String, Object> parms);
}
