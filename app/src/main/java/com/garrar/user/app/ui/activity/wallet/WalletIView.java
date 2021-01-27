package com.garrar.user.app.ui.activity.wallet;

import com.appoets.paytmpayment.PaytmObject;
import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.AddWallet;
import com.garrar.user.app.data.network.model.BrainTreeResponse;

public interface WalletIView extends MvpView {
    void onSuccess(AddWallet object);
    void onSuccess(PaytmObject object);
    void onSuccess(BrainTreeResponse response);
    void onError(Throwable e);
}
