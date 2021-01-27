package com.garrar.user.app.ui.fragment.rate;

import com.garrar.user.app.base.MvpView;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface RatingIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
