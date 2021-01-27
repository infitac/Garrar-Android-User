package com.garrar.user.app.ui.activity.passbook;

import com.garrar.user.app.base.MvpPresenter;

public interface WalletHistoryIPresenter<V extends WalletHistoryIView> extends MvpPresenter<V> {
    void wallet();
}
