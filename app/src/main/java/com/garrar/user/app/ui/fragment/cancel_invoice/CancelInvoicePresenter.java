package com.garrar.user.app.ui.fragment.cancel_invoice;

import com.garrar.user.app.base.BasePresenter;
import com.garrar.user.app.data.network.APIClient;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CancelInvoicePresenter<V extends CancelInvoiceIView> extends BasePresenter<V> implements CancelInvoiceIPresenter<V> {

    @Override
    public void payment(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .payment(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        paymentResponse -> getMvpView().onSuccess(paymentResponse),
                        throwable -> getMvpView().onError(throwable)));
    }

    @Override
    public void updateRide(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .updateRequest(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        object -> getMvpView().onSuccess(object),
                        throwable -> getMvpView().onError(throwable)));

    }


    @Override
    public void payuMoneyChecksum() {

        getCompositeDisposable().add(APIClient.getAPIClient().payuMoneyChecksum()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(checkSumData -> getMvpView().onPayumoneyCheckSumSucess(checkSumData),
                        throwable -> getMvpView().onError(throwable)));
    }


    public void paytmCheckSum(String request_id, String payment_mode) {

        getCompositeDisposable().add(APIClient.getAPIClient().payTmChecksum(request_id, payment_mode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(payTmResponse -> getMvpView().onPayTmCheckSumSucess(payTmResponse),
                        throwable -> getMvpView().onError(throwable)));

    }

    @Override
    public void getBrainTreeToken() {
        getCompositeDisposable().add(APIClient.getAPIClient().getBraintreeToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> getMvpView().onSuccess(response),
                        throwable -> getMvpView().onError(throwable)));
    }

    @Override
    public void updatePayment(HashMap<String, Object> obj) {
        getCompositeDisposable().add(APIClient
                .getAPIClient()
                .paymentSuccess(obj)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        paymentResponse -> getMvpView().onSuccessPayment(paymentResponse),
                        throwable -> getMvpView().onError(throwable)));
    }

    @Override
    public void getTelrLastTransactionId() {
        getCompositeDisposable().add(APIClient.getAPIClient().getTelrLastTransactionId()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(response -> getMvpView().onSuccess(response),
                        throwable -> getMvpView().onError(throwable)));
    }
}