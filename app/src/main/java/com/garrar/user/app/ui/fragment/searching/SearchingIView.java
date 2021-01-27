package com.garrar.user.app.ui.fragment.searching;

import com.garrar.user.app.base.MvpView;

public interface SearchingIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
