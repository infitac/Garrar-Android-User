package com.garrar.user.app.ui.activity.passbook;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.WalletResponse;

public interface WalletHistoryIView extends MvpView {
    void onSuccess(WalletResponse response);

    void onError(Throwable e);
}
