package com.garrar.user.app.ui.activity.main;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.AddressResponse;
import com.garrar.user.app.data.network.model.CancelResponse;
import com.garrar.user.app.data.network.model.Card;
import com.garrar.user.app.data.network.model.DataResponse;
import com.garrar.user.app.data.network.model.Message;
import com.garrar.user.app.data.network.model.Provider;
import com.garrar.user.app.data.network.model.SettingsResponse;
import com.garrar.user.app.data.network.model.User;

import java.util.List;

public interface MainIView extends MvpView {

    void onSuccess(User user);

    void onSuccess(DataResponse dataResponse);

    void onDestinationSuccess(Object object);

    void onSuccessLogout(Object object);

    void onSuccess(AddressResponse response);

    void onSuccess(List<Provider> objects);

    void onError(Throwable e);

    void onSuccess(SettingsResponse response);

    void onSettingError(Throwable e);

    void onSuccess(Message message);

    void onSuccess(List<CancelResponse> response, boolean mTemp);

    void onSuccessCard(List<Card> cards);
}
