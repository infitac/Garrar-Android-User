package com.garrar.user.app.ui.fragment.view_courier_detail;

import com.garrar.user.app.base.MvpPresenter;

import java.util.HashMap;

public interface ViewCourierDetailIPresenter<V extends ViewCourierDetailIView> extends MvpPresenter<V> {

void getCourierdetail(HashMap<String, Object> obj);
}
