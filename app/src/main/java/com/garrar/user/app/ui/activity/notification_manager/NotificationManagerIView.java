package com.garrar.user.app.ui.activity.notification_manager;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> notificationManager);

    void onError(Throwable e);

}