package com.garrar.user.app.ui.activity.location_pick;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.AddressResponse;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface LocationPickIView extends MvpView {

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
