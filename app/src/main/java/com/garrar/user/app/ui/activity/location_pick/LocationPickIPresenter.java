package com.garrar.user.app.ui.activity.location_pick;

import com.garrar.user.app.base.MvpPresenter;

public interface LocationPickIPresenter<V extends LocationPickIView> extends MvpPresenter<V> {
    void address();
}
