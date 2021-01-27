package com.garrar.user.app.ui.activity.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.garrar.user.app.MvpApplication;

import com.appoets.braintreepayment.BrainTreePaymentActivity;
import com.appoets.paytmpayment.PaytmCallback;
import com.appoets.paytmpayment.PaytmObject;
import com.appoets.paytmpayment.PaytmPayment;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseActivity;
import com.garrar.user.app.common.Constants;
import com.garrar.user.app.data.SharedHelper;
import com.garrar.user.app.data.network.model.AddWallet;
import com.garrar.user.app.data.network.model.BrainTreeResponse;
import com.garrar.user.app.ui.activity.payment.PaymentActivity;


import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.garrar.user.app.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class WalletActivity extends BaseActivity implements WalletIView {

    private static final String WALLET_ACTIVITY_PATH = "com.mobiz.user.ui.activity.wallet.WalletActivity";

    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id._199)
    Button _199;
    @BindView(R.id._599)
    Button _599;
    @BindView(R.id._1099)
    Button _1099;
    @BindView(R.id.add_amount)
    Button addAmount;
    @BindView(R.id.cvAddMoneyContainer)
    CardView cvAddMoneyContainer;
    String regexNumber = "^(\\d{0,9}\\.\\d{1,4}|\\d{1,9})$";
    private WalletPresenter<WalletActivity> presenter = new WalletPresenter<>();

    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;


    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {

        ButterKnife.bind(this);
        presenter.attachView(this);
        // Activity title will be updated after the locale has changed in Runtime
        setTitle(getString(R.string.wallet));

        _199.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._199));
        _599.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._599));
        _1099.setText(SharedHelper.getKey(this, "currency") + " " + getString(R.string._1099));
        amount.setTag(SharedHelper.getKey(this, "currency"));

        walletBalance.setText(SharedHelper.getKey(this, "currency") + " " + (int) Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0")));

        if (!MvpApplication.isCard && !MvpApplication.isBraintree && !MvpApplication.isPaytm && !MvpApplication.isPayumoney) {
            cvAddMoneyContainer.setVisibility(View.GONE);
            addAmount.setVisibility(View.GONE);
        }

//        getCardsDetails();

//        Intent telrIntent = getIntent();
//        if (telrIntent != null) {
//            Object object = telrIntent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
//            if (object != null) {
//                HashMap<String, Object> telrResult = TelrHelper.getTelrResponse(WalletActivity.this, object);
//                if (telrResult != null) {
//                    showLoading();
//                    presenter.addMoneyUsingTelr(telrResult);
//                    Toasty.success(WalletActivity.this, "Amount added", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toasty.error(WalletActivity.this, "failed to add money", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id._199, R.id._599, R.id._1099, R.id.add_amount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id._199:
                amount.setText(getString(R.string._199));
                break;
            case R.id._599:
                amount.setText(getString(R.string._599));
                break;
            case R.id._1099:
                amount.setText(getString(R.string._1099));
                break;
            case R.id.add_amount:
                if (!amount.getText().toString().trim().matches(regexNumber)) {
                    Toast.makeText(baseActivity(), getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(baseActivity(), PaymentActivity.class);
                intent.putExtra("hideCash", true);
                startActivityForResult(intent, PICK_PAYMENT_METHOD);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK && data != null)
            switch (data.getStringExtra("payment_mode")) {
                case Constants.PaymentMode.CARD:
                    HashMap<String, Object> map = new HashMap<>();
                    String cardId = data.getStringExtra("card_id");
                    String lastFour = data.getStringExtra("card_last_four");
                    MvpApplication.RIDE_REQUEST.put(CARD_LAST_FOUR, lastFour);
                    map.put("amount", amount.getText().toString());
                    map.put("payment_mode", "CARD");
                    map.put("card_id", cardId);
                    map.put("strCardID", cardId);
                    map.put("is_default", 1);
                    map.put("id", SharedHelper.getKey(Objects.requireNonNull(this), "user_id"));
                    map.put("user_type", "user");
                    map.put("last_four", lastFour);
//                    map.put("user_type", "aistaxi");
                    showLoading();
                    presenter.addMoney(map);
                    break;
                case Constants.PaymentMode.BRAINTREE:
                    presenter.getBrainTreeToken();
                    break;
                case Constants.PaymentMode.PAYTM: {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("amount", amount.getText().toString());
                    hashMap.put("payment_mode", Constants.PaymentMode.PAYTM);
                    hashMap.put("user_type", "aistaxi");
                    showLoading();
                    presenter.addMoneyPaytm(hashMap);
                    break;
                }
            }
        else if (requestCode == BRAINTREE_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                HashMap<String, Object> map = new HashMap<>();
                String nonce = data.getStringExtra(BrainTreePaymentActivity.PAYMENT_NONCE);
                map.put("amount", amount.getText().toString());
                map.put("payment_mode", "BRAINTREE");
                map.put("braintree_nonce", nonce);
                map.put("user_type", "aistaxi");
                showLoading();
                presenter.addMoney(map);
            } else
                Toasty.error(WalletActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSuccess(PaytmObject object) {

        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        new PaytmPayment(WalletActivity.this, object, new PaytmCallback() {
            @Override
            public void onPaytmSuccess(String status, String message, String paymentmode, String txid) {
                Toasty.success(WalletActivity.this, "Amount added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaytmFailure(String errorMessage) {
                Toasty.error(WalletActivity.this, "failed to add money", Toast.LENGTH_SHORT).show();
            }
        }).startPayment();
    }

    @Override
    public void onSuccess(AddWallet wallet) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
//        Toast.makeText(this, wallet.getMessage(), Toast.LENGTH_SHORT).show();
        amount.setText("");
        System.out.println("+++++ onsuccess: " + wallet.getMessage() + " :: " + wallet.getBalance());
        SharedHelper.putKey(this, "walletBalance", String.valueOf(wallet.getBalance()));
        walletBalance.setText(SharedHelper.getKey(this, "currency") + " " + (int) Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0")));
    }


    @Override
    public void onSuccess(BrainTreeResponse response) {
//        Toast.makeText(this, "onSuccess BrainTreeResponse", Toast.LENGTH_SHORT).show();
        if (!response.getToken().isEmpty()) {
            Intent intent = new Intent(WalletActivity.this, BrainTreePaymentActivity.class);
            intent.putExtra(BrainTreePaymentActivity.EXTRAS_TOKEN, response.getToken());
            startActivityForResult(intent, BRAINTREE_PAYMENT_REQUEST_CODE);
        }

    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }


//    private void getCardsDetails() {
//        showLoading();
//        new Handler().postDelayed(() -> {
//            if (MvpApplication.isCard) {
//                cardsRv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//                cardsRv.setItemAnimator(new DefaultItemAnimator());
////                presenter.card();
//                cardsRv.setVisibility(View.VISIBLE);
//            } else {
//                try {
//                    hideLoading();
//                } catch (Exception e1) {
//                    e1.printStackTrace();
//                }
//                cardsRv.setVisibility(View.GONE);
//            }
////            if (MvpApplication.isCash && !isInvoiceCashToCard)
////                llCashContainer.setVisibility(View.VISIBLE);
////            else llCashContainer.setVisibility(View.GONE);
//        }, 3000);
//    }
}
