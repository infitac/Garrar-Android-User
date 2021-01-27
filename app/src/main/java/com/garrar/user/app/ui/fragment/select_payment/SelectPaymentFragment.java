package com.garrar.user.app.ui.fragment.select_payment;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.garrar.user.app.MvpApplication;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseFragment;
import com.garrar.user.app.base.BasePresenter;
import com.garrar.user.app.common.Constants;
import com.garrar.user.app.data.network.model.EstimateFare;
import com.garrar.user.app.data.network.model.Service;
import com.garrar.user.app.ui.activity.main.MainActivity;
import com.garrar.user.app.ui.fragment.book_ride.BookRideFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.garrar.user.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;

public class SelectPaymentFragment extends BaseFragment {

    @BindView(R.id.telr_saved_card)
    TextView telrSavedCard;

    String serviceName;
    Service service;
    EstimateFare estimateFare;
    Double walletAmount;

    BasePresenter<SelectPaymentFragment> presenter = new BasePresenter<>();

    public SelectPaymentFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_select_payment;
    }

    @Override
    protected View initView(View view) {
        ButterKnife.bind(this, view);
        presenter.attachView(this);

 //       String savedCard = SharedHelper.getKey(getContext(), TelrHelper.CARD_LAST_FOUR);
 //       if (savedCard != null && savedCard.length() > 0)
        //telrSavedCard.setText(getString(R.string.card_, savedCard));

        Bundle args = getArguments();
        if (args != null) {
            serviceName = args.getString("service_name");
            service = (Service) args.getSerializable("mService");
            estimateFare = (EstimateFare) args.getSerializable("estimate_fare");
            walletAmount = Objects.requireNonNull(estimateFare).getWalletBalance();
        }
        return view;
    }

    @OnClick({R.id.select_card, R.id.select_cash})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.select_cash:
                MvpApplication.RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CASH);
                MvpApplication.RIDE_REQUEST.put(Constants.RIDE_REQUEST.PAYMENT_MODE_SELECTED, Constants.PaymentMode.CASH);
                System.out.println("+++++ payment mode: " + MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE));
                break;

            case R.id.select_card:
                MvpApplication.RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CARD);
                MvpApplication.RIDE_REQUEST.put(Constants.RIDE_REQUEST.PAYMENT_MODE_SELECTED, Constants.PaymentMode.CARD);
                System.out.println("+++++ payment mode: " + MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE));
                break;
        }

        Bundle bundle = new Bundle();
        bundle.putString("service_name", serviceName);
        bundle.putSerializable("mService", service);
        bundle.putSerializable("estimate_fare", estimateFare);
        bundle.putDouble("use_wallet", walletAmount);
        BookRideFragment bookRideFragment = new BookRideFragment();
        bookRideFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().remove(this);
        ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(bookRideFragment);

    }
}
