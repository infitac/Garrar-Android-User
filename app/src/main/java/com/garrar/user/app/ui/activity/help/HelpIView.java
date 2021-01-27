package com.garrar.user.app.ui.activity.help;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help help);

    void onError(Throwable e);
}
