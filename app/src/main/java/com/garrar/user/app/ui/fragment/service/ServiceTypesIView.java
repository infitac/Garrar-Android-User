package com.garrar.user.app.ui.fragment.service;

import com.garrar.user.app.base.MvpView;
import com.garrar.user.app.data.network.model.Service;

import java.util.List;

public interface ServiceTypesIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onError(Throwable e);

    void onSuccess(Object object);
}
