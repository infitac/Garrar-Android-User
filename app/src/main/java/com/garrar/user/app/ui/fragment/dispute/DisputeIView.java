package com.garrar.user.app.ui.fragment.dispute;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.DisputeResponse;
import com.garrar.user.app.data.network.model.Help;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccess(Object object);

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onError(Throwable e);

    void onSuccess(Help help);
}
