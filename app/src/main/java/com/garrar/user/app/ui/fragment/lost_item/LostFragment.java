package com.garrar.user.app.ui.fragment.lost_item;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.garrar.user.app.MvpApplication;
import com.google.gson.internal.LinkedTreeMap;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseBottomSheetDialogFragment;
import com.garrar.user.app.data.SharedHelper;
import com.garrar.user.app.data.network.model.Datum;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LostFragment extends BaseBottomSheetDialogFragment implements LostIView {

    @BindView(R.id.lost_item_description)
    EditText lost_item_description;
    private LostPresenter<LostFragment> presenter = new LostPresenter<>();

    public LostFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_lost_dialog;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        presenter.attachView(this);
    }

    @OnClick({R.id.dismiss, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dismiss:
                dismiss();
                break;
            case R.id.submit:
                if (lost_item_description.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter lost item details", Toast.LENGTH_SHORT).show();
                    return;
                }
                postLostItemDescription();


                break;
        }
    }

    private void postLostItemDescription() {
        if (MvpApplication.DATUM != null) {
            Datum datum = MvpApplication.DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("request_id", datum.getId());
            map.put("user_id", SharedHelper.getKey(Objects.requireNonNull(getContext()), "user_id"));
            map.put("first_name", SharedHelper.getKey(Objects.requireNonNull(getContext()), "first_name"));
            map.put("lost_item_name", lost_item_description.getText().toString());
            showLoading();
            presenter.postLostItem(map);
        }
    }

    @Override
    public void onSuccess(Object object) {
        try {
            hideLoading();
            if (object instanceof LinkedTreeMap) {
                LinkedTreeMap responseMap = (LinkedTreeMap) object;
                if (responseMap.get("message") != null) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            responseMap.get("message").toString(), Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        getResources().getString(R.string.lost_item_error), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        dismiss();
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

}
