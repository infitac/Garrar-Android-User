package com.garrar.user.app.ui.activity.card;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.Card;

import java.util.List;

/**
 * Created by santhosh@appoets.com on 19-05-2018.
 */
public interface CardsIView extends MvpView {
    void onSuccess(List<Card> cardList);

    void onError(Throwable e);
}
