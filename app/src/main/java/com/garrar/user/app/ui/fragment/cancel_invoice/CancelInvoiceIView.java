package com.garrar.user.app.ui.fragment.cancel_invoice;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.BrainTreeResponse;
import com.garrar.user.app.data.network.model.CheckSumData;
import com.garrar.user.app.data.network.model.Message;
import com.appoets.paytmpayment.PaytmObject;

public interface CancelInvoiceIView extends MvpView {
    void onSuccess(Message message);

    void onSuccess(Object o);

    void onSuccessPayment(Object o);

    void onError(Throwable e);

    void onSuccess(BrainTreeResponse response);

    void onPayumoneyCheckSumSucess(CheckSumData checkSumData);

    void onPayTmCheckSumSucess(PaytmObject payTmResponse);
}
