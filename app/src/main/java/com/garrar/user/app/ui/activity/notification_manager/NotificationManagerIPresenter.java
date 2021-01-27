package com.garrar.user.app.ui.activity.notification_manager;

import com.garrar.user.app.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
