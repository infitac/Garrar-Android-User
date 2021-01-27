package com.garrar.user.app.ui.fragment.cancel_invoice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.garrar.user.app.BuildConfig;
import com.garrar.user.app.MvpApplication;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseFragment;
import com.garrar.user.app.common.Constants;
import com.garrar.user.app.data.SharedHelper;
import com.garrar.user.app.data.network.model.BrainTreeResponse;
import com.garrar.user.app.data.network.model.CheckSumData;
import com.garrar.user.app.data.network.model.Datum;
import com.garrar.user.app.data.network.model.Message;
import com.garrar.user.app.data.network.model.Payment;

import com.garrar.user.app.ui.activity.main.MainActivity;
import com.garrar.user.app.ui.activity.payment.PaymentActivity;
import com.appoets.braintreepayment.BrainTreePaymentActivity;
import com.appoets.paytmpayment.PaytmCallback;
import com.appoets.paytmpayment.PaytmConstants;
import com.appoets.paytmpayment.PaytmObject;
import com.appoets.paytmpayment.PaytmPayment;
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_ID;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;
import static com.garrar.user.app.common.Constants.Status.RATING;
import static com.garrar.user.app.data.SharedHelper.getKey;

public class CancelInvoiceFragment extends BaseFragment implements CancelInvoiceIView {

    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;
    private static final String MAIN_ACTIVITY_PATH = "com.mobiz.user.ui.activity.main.MainActivity";

    public static boolean isInvoiceCashToCard = false;
    @BindView(R.id.fragment_invoice)
    NestedScrollView containerScroll;
    @BindView(R.id.payment_mode)
    TextView tvPaymentMode;
    @BindView(R.id.pay_now)
    Button payNow;
    @BindView(R.id.done)
    Button done;
    @BindView(R.id.booking_id)
    TextView bookingId;
    @BindView(R.id.distance)
    TextView distance;
    @BindView(R.id.travel_time)
    TextView travelTime;
    @BindView(R.id.fixed)
    TextView fixed;
    @BindView(R.id.distance_fare)
    TextView distanceFare;
    @BindView(R.id.tax)
    TextView tax;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.payable)
    TextView payable;
    @BindView(R.id.wallet_detection)
    TextView walletDetection;
    @BindView(R.id.time_fare)
    TextView timeFare;
    @BindView(R.id.llDistanceFareContainer)
    LinearLayout llDistanceFareContainer;
    @BindView(R.id.llTimeFareContainer)
    LinearLayout llTimeFareContainer;
    @BindView(R.id.llTipContainer)
    LinearLayout llTipContainer;
    @BindView(R.id.llWalletDeductionContainer)
    LinearLayout llWalletDeductionContainer;
    @BindView(R.id.llDiscountContainer)
    LinearLayout llDiscountContainer;
    @BindView(R.id.llPaymentContainer)
    LinearLayout llPaymentContainer;
    @BindView(R.id.llTravelTime)
    LinearLayout llTravelTime;
    @BindView(R.id.llBaseFare)
    LinearLayout llBaseFare;
    @BindView(R.id.llPayable)
    LinearLayout llPayable;
    @BindView(R.id.llWaitingAmountContainer)
    LinearLayout llWaitingAmountContainer;
    @BindView(R.id.llTolleChargeContainer)
    LinearLayout llTolleChargeContainer;
    @BindView(R.id.llRoundOffContainer)
    LinearLayout llRoundOffContainer;
    @BindView(R.id.tvChange)
    TextView tvChange;
    @BindView(R.id.tvGiveTip)
    TextView tvGiveTip;
    @BindView(R.id.tvTipAmt)
    TextView tvTipAmt;
    @BindView(R.id.tvDiscount)
    TextView tvDiscount;
    @BindView(R.id.tvWaitingAmount)
    TextView tvWaitingAmount;
    @BindView(R.id.tvTollCharges)
    TextView tvTollCharges;
    @BindView(R.id.tvRoundOff)
    TextView tvRoundOff;
    @BindView(R.id.tvWaitingTimeDesc)
    TextView tvWaitingTimeDesc;
    @BindView(R.id.tvWaitingTime)
    TextView tvWaitingTime;
    @BindView(R.id.llSurchargeContainer)
    LinearLayout llSurchageContainer;
    private CancelInvoicePresenter<CancelInvoiceFragment> presenter = new CancelInvoicePresenter<>();
    private BraintreeFragment mBrainTreeFragment;
    private Payment payment;
    private String payingMode;
    private Double tips = 0.0;

    // @msvhora changes starts
    @BindView(R.id.llBaseFare_label)
    TextView llBaseFareLabel;
    // @msvhora changes ends

    public CancelInvoiceFragment() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_invoice;
    }

    @Override
    public View initView(View view) {
        ButterKnife.bind(this, view);
        presenter.attachView(this);
        try {
            mBrainTreeFragment = BraintreeFragment.newInstance(baseActivity(), BuildConfig.PAYPAL_CLIENT_TOKEN);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        if (MvpApplication.DATUM != null) {
            containerScroll.setVisibility(View.VISIBLE);
//            initView(MvpApplication.DATUM);
            hideExtraInvoiceViews(MvpApplication.DATUM);
        } else {
            containerScroll.setVisibility(View.GONE);
        }
//        SharedHelper.putKey(getContext(), TelrHelper.TELR_TIPS_KEY, "0");


        return view;
    }

    // @msvhora Changes starts
    private void hideExtraInvoiceViews(@NonNull Datum datum) {

        llBaseFareLabel.setText(getString(R.string.cancellation_charges));

        bookingId.setText(datum.getBookingId());

        fixed.setText(String.format("%s %s",
                getKey(Objects.requireNonNull(getContext()), "currency"),
                20.00));

        tax.setText(String.format("%s %s",
                getKey(Objects.requireNonNull(getContext()), "currency"),
                0.00));

        total.setText(String.format("%s %s",
                getKey(Objects.requireNonNull(getContext()), "currency"),
                20.00));

        tvRoundOff.setText(String.format("%s %s",
                getKey(Objects.requireNonNull(getContext()), "currency"),
                20.00));

        payable.setText(String.format("%s %s",
                getKey(Objects.requireNonNull(getContext()), "currency"),
                20.00));

        llTimeFareContainer.setVisibility(View.GONE);
        llDistanceFareContainer.setVisibility(View.GONE);
        llWaitingAmountContainer.setVisibility(View.GONE);
        llTolleChargeContainer.setVisibility(View.GONE);
        llTipContainer.setVisibility(View.GONE);
        llWalletDeductionContainer.setVisibility(View.GONE);
        llSurchageContainer.setVisibility(View.GONE);
        tvChange.setVisibility(View.GONE);
        done.setVisibility(View.GONE);

    }
    // @msvhora Ends starts

    @SuppressLint("StringFormatInvalid")
    private void initView(@NonNull Datum datum) {
        bookingId.setText(datum.getBookingId());
        if (SharedHelper.getKey(getContext(), "measurementType").equalsIgnoreCase(Constants.MeasurementType.KM)) {
            if (datum.getDistance() > 1 || datum.getDistance() > 1.0)
                distance.setText(String.format("%s %s", datum.getDistance(), Constants.MeasurementType.KM));
            else
                distance.setText(String.format("%s %s", datum.getDistance(), getString(R.string.km)));
        } else {
            if (datum.getDistance() > 1 || datum.getDistance() > 1.0)
                distance.setText(String.format("%s %s", datum.getDistance(), Constants.MeasurementType.MILES));
            else
                distance.setText(String.format("%s %s", datum.getDistance(), getString(R.string.mile)));
        }
        try {
            if (datum.getTravelTime() != null && Double.parseDouble(datum.getTravelTime()) > 0) {
                llTravelTime.setVisibility(View.VISIBLE);
                travelTime.setText(getString(R.string._min, datum.getTravelTime()));
            } else llTravelTime.setVisibility(View.GONE);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            llTravelTime.setVisibility(View.VISIBLE);
            travelTime.setText(getString(R.string._min, datum.getTravelTime()));
        }
        initPaymentView(datum.getPaymentMode(), "", false);

        payment = datum.getPayment();
        try {
            if (payment != null) {
                if (payment.getFixed() > 0) {
                    llBaseFare.setVisibility(View.VISIBLE);
                    fixed.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getFixed()));
                } else llBaseFare.setVisibility(View.GONE);
                tax.setText(String.format("%s %s",
                        getKey(Objects.requireNonNull(getContext()), "currency"),
                        payment.getTax()));
                total.setText(String.format("%s %s",
                        getKey(Objects.requireNonNull(getContext()), "currency"),
                        payment.getTotal()));

                if (payment.getPayable() > 0) {
                    llPayable.setVisibility(View.VISIBLE);
                    payable.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getPayable()));
                } else llPayable.setVisibility(View.GONE);

                if (payment.getWallet() > 0) {
//                    llWalletDeductionContainer.setVisibility(View.VISIBLE);
                    walletDetection.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getWallet()));
                }
//                else llWalletDeductionContainer.setVisibility(View.GONE);

                if (payment.getDiscount() > 0) {
                    llDiscountContainer.setVisibility(View.VISIBLE);
                    tvDiscount.setText(String.format("%s -%s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getDiscount()));
                } else llDiscountContainer.setVisibility(View.GONE);

                if (payment.getWaitingAmount() > 0) {
                    tvWaitingTimeDesc.setVisibility(View.GONE);
//                    llWaitingAmountContainer.setVisibility(View.VISIBLE);
                    tvWaitingTime.setText(getString(R.string.waiting_amount));
                    tvWaitingAmount.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getWaitingAmount()));
                } else if (payment.getWaitingAmount() == 0
                        && datum.getServiceType().getWaitingMinCharge() == 0) {
                    tvWaitingTimeDesc.setVisibility(View.VISIBLE);
//                    llWaitingAmountContainer.setVisibility(View.VISIBLE);
                    tvWaitingTime.setText(getString(R.string.waiting_amount_star));
                    tvWaitingAmount.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"), "0"));
                } else {
                    tvWaitingTimeDesc.setVisibility(View.GONE);
                    tvWaitingTime.setText(getString(R.string.waiting_amount));
//                    llWaitingAmountContainer.setVisibility(View.GONE);
                }

                if (payment.getToll_charge() > 0) {
//                    llTolleChargeContainer.setVisibility(View.VISIBLE);
                    tvTollCharges.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getToll_charge()));
                }
//                else llTolleChargeContainer.setVisibility(View.GONE);

                if (payment.getRoundOf() != 0) {
                    llRoundOffContainer.setVisibility(View.VISIBLE);
                    tvRoundOff.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getRoundOf()));
                } else llRoundOffContainer.setVisibility(View.GONE);

                //      MIN,    HOUR,   DISTANCE,   DISTANCEMIN,    DISTANCEHOUR
                if (datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.MINUTE)
                        || datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.HOUR)) {
//                    llTimeFareContainer.setVisibility(View.VISIBLE);
//                    llDistanceFareContainer.setVisibility(View.GONE);
                    distanceFare.setText(R.string.time_fare);
                    if (datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.MINUTE)) {
                        if (payment.getMinute() > 0) {
//                            llTimeFareContainer.setVisibility(View.VISIBLE);
                            timeFare.setText(String.format("%s %s",
                                    getKey(Objects.requireNonNull(getContext()), "currency"),
                                    payment.getMinute()));
                        }
//                        else llTimeFareContainer.setVisibility(View.GONE);

                    } else if (datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.HOUR)) {
                        if (payment.getHour() > 0) {
//                            llTimeFareContainer.setVisibility(View.VISIBLE);
                            timeFare.setText(String.format("%s %s",
                                    getKey(Objects.requireNonNull(getContext()), "currency"),
                                    payment.getHour()));
                        }
//                        else llTimeFareContainer.setVisibility(View.GONE);
                    }
                } else if (datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.DISTANCE)) {
//                    llTimeFareContainer.setVisibility(View.GONE);
                    if (payment.getDistance() == 0.0 || payment.getDistance() == 0) {
                    }
//                        llDistanceFareContainer.setVisibility(View.GONE);
                    else {
//                        llDistanceFareContainer.setVisibility(View.VISIBLE);
                        distanceFare.setText(String.format("%s %s",
                                getKey(Objects.requireNonNull(getContext()), "currency"),
                                payment.getDistance()));
                    }
                } else if (datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.DISTANCE_MIN)) {
                    if (payment.getDistance() == 0.0 || payment.getDistance() == 0) {
                    }
//                        llDistanceFareContainer.setVisibility(View.GONE);
                    else {
//                        llDistanceFareContainer.setVisibility(View.VISIBLE);
                        distanceFare.setText(String.format("%s %s",
                                getKey(Objects.requireNonNull(getContext()), "currency"),
                                payment.getDistance()));
                    }
                    if (payment.getMinute() > 0) {
//                        llTimeFareContainer.setVisibility(View.VISIBLE);
                        timeFare.setText(String.format("%s %s",
                                getKey(Objects.requireNonNull(getContext()), "currency"),
                                payment.getMinute()));
                    }
//                    else llTimeFareContainer.setVisibility(View.GONE);
                } else if (datum.getServiceType().getCalculator().equalsIgnoreCase(Constants.InvoiceFare.DISTANCE_HOUR)) {
                    if (payment.getDistance() == 0.0 || payment.getDistance() == 0) {
//                        llDistanceFareContainer.setVisibility(View.GONE);
                    } else {
//                        llDistanceFareContainer.setVisibility(View.VISIBLE);
                        distanceFare.setText(String.format("%s %s",
                                getKey(Objects.requireNonNull(getContext()), "currency"),
                                payment.getDistance()));
                    }
                    if (payment.getHour() > 0) {
//                        llTimeFareContainer.setVisibility(View.VISIBLE);
                        timeFare.setText(String.format("%s %s",
                                getKey(Objects.requireNonNull(getContext()), "currency"),
                                payment.getHour()));
                    }
//                    else llTimeFareContainer.setVisibility(View.GONE);
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Payment is null",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Datum datum = MvpApplication.DATUM;
        if (datum.getPaymentMode() != null) payingMode = datum.getPaymentMode();
        llPaymentContainer.setVisibility(datum.getPaid() == 1 ? View.GONE : View.VISIBLE);

        if (datum.getPaid() == 0 && payingMode.equalsIgnoreCase("CASH")) {
//            done.setVisibility(View.VISIBLE);
//            payNow.setVisibility(View.GONE);
//            llTipContainer.setVisibility(View.GONE);
//            tvChange.setVisibility(View.VISIBLE);
//            done.setOnClickListener(v -> Toasty.info(getContext(),
//                    getString(R.string.payment_not_confirmed), Toast.LENGTH_SHORT).show());
        } else if (datum.getPaid() == 0 && !payingMode.equalsIgnoreCase("CASH")) {
//            done.setVisibility(View.GONE);
//            payNow.setVisibility(View.VISIBLE);
//            llTipContainer.setVisibility(View.VISIBLE);
//            tvChange.setVisibility(View.GONE);
        } else if (datum.getPaid() == 1 && payingMode.equalsIgnoreCase("CASH")) {
//            done.setVisibility(View.VISIBLE);
//            payNow.setVisibility(View.GONE);
//            llTipContainer.setVisibility(View.GONE);
//            tvChange.setVisibility(View.GONE);
        } else if (datum.getPaid() == 1 && !payingMode.equalsIgnoreCase("CASH")) {
//            done.setVisibility(View.VISIBLE);
//            payNow.setVisibility(View.GONE);
//            llTipContainer.setVisibility(View.GONE);
//            tvChange.setVisibility(View.GONE);
        }

        //		By Rajaganapathi(Cross check it)
//        tvChange.setVisibility((!MvpApplication.isCard && MvpApplication.isCash) ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onSuccess(Object obj) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onSuccessPayment(Object o) {
        Toast.makeText(getContext(), R.string.you_have_successfully_paid, Toast.LENGTH_SHORT).show();
        ((MainActivity) Objects.requireNonNull(getContext())).changeFlow("RATING");
    }

    @Override
    public void onSuccess(Message message) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Toast.makeText(getContext(), R.string.you_have_successfully_paid, Toast.LENGTH_SHORT).show();
        ((MainActivity) Objects.requireNonNull(getContext())).changeFlow(RATING);
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @OnClick({R.id.payment_mode, R.id.pay_now, R.id.done, R.id.tvChange, R.id.tvGiveTip, R.id.tvTipAmt, R.id.ivInvoice})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.tvChange:
//            case R.id.payment_mode:
//                startActivityForResult(new Intent(getActivity(), PaymentActivity.class), PaymentActivity.PICK_PAYMENT_METHOD);
//                break;
            case R.id.pay_now:
//                if (MvpApplication.DATUM != null) {
//                    Datum datum = MvpApplication.DATUM;
//                    switch (datum.getPaymentMode()) {
//                        case Constants.PaymentMode.CARD:
//                            showLoading();
//                            HashMap<String, Object> cardHashMap = new HashMap<>();
//                            cardHashMap.put("request_id", MvpApplication.DATUM.getId());
//                            cardHashMap.put("tips", tips);
//                            cardHashMap.put("payment_type", Constants.PaymentMode.CARD);
//                            presenter.payment(cardHashMap);
//                            break;
//                        case Constants.PaymentMode.PAYPAL:
//                            PayPalRequest request = new PayPalRequest(String.valueOf(datum.getPayment().getPayable()))
//                                    .currencyCode(getKey(baseActivity(), "currency_code"))
//                                    .intent(PayPalRequest.INTENT_AUTHORIZE);
//                            PayPal.requestOneTimePayment(mBrainTreeFragment, request);
//                            break;
//                        case Constants.PaymentMode.CASH:
//                            if (isInvoiceCashToCard) {
//                                showLoading();
//                                HashMap<String, Object> cashHashMap = new HashMap<>();
//                                cashHashMap.put("request_id", MvpApplication.DATUM.getId());
//                                cashHashMap.put("tips", tips);
//                                cashHashMap.put("payment_type", Constants.PaymentMode.CASH);
//                                presenter.payment(cashHashMap);
//                            }
//                            break;
//                        case Constants.PaymentMode.BRAINTREE:
//                            presenter.getBrainTreeToken();
//                            break;
//                        case Constants.PaymentMode.PAYTM:
//                            presenter.paytmCheckSum(MvpApplication.DATUM.getId() + "", "PAYTM");
//                            break;
//
//                        case Constants.PaymentMode.TELR:
//                            presenter.getTelrLastTransactionId();
//                            break;
//                    }
//                }

                HashMap<String, Object> hashM = new HashMap<>();
                hashM.put("request_id", MvpApplication.RIDE_REQUEST.get(Constants.RIDE_REQUEST.REQUESSTID));
                hashM.put("payment_mode", Constants.PaymentMode.CARD);
                presenter.payment(hashM);
//                presenter.getTelrLastTransactionId();
                break;
            case R.id.done:
            case R.id.ivInvoice:
                ((MainActivity) Objects.requireNonNull(getContext())).changeFlow(RATING);
                break;
            case R.id.tvTipAmt:
            case R.id.tvGiveTip:
                showTipDialog(payment.getPayable());
                break;
        }
    }

    private void showTipDialog(double totalAmount) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_tip);
        EditText etAmount = dialog.findViewById(R.id.etAmount);
        Button percent10 = dialog.findViewById(R.id.bt10Percent);
        Button percent15 = dialog.findViewById(R.id.bt15Percent);
        Button percent20 = dialog.findViewById(R.id.bt20Percent);
        TextView tvSubmit = dialog.findViewById(R.id.tvSubmit);

        percent10.setOnClickListener(v -> etAmount.setText(String.valueOf((totalAmount * 10) / 100)));
        percent15.setOnClickListener(v -> etAmount.setText(String.valueOf((totalAmount * 15) / 100)));
        percent20.setOnClickListener(v -> etAmount.setText(String.valueOf((totalAmount * 20) / 100)));

        tvSubmit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etAmount.getText()) && Double.parseDouble(etAmount.getText().toString()) > 0) {
                tvGiveTip.setVisibility(View.GONE);
                tvTipAmt.setVisibility(View.VISIBLE);
                tips = Double.parseDouble(etAmount.getText().toString());
                double payableCal = payment.getPayable() + tips;
                tvTipAmt.setText(String.format("%s %s",
                        getKey(Objects.requireNonNull(getContext()), "currency"),
                        tips));
                if (payableCal > 0) {
                    llPayable.setVisibility(View.VISIBLE);
                    payable.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payableCal));
                } else llPayable.setVisibility(View.GONE);
                dialog.dismiss();
            } else {
                tvGiveTip.setVisibility(View.VISIBLE);
                tvTipAmt.setVisibility(View.GONE);
                if (payment.getPayable() > 0) {
                    llPayable.setVisibility(View.VISIBLE);
                    payable.setText(String.format("%s %s",
                            getKey(Objects.requireNonNull(getContext()), "currency"),
                            payment.getPayable()));
                    tips = 0.0;
                } else llPayable.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        HashMap<String, Object> map = new HashMap<>();
        switch (requestCode) {
            case PaymentActivity.PICK_PAYMENT_METHOD:
                if (resultCode == Activity.RESULT_OK) {
                    MvpApplication.RIDE_REQUEST.put(PAYMENT_MODE, data.getStringExtra("payment_mode"));
                    if (data.getStringExtra("payment_mode").equals(Constants.PaymentMode.CARD)) {
                        MvpApplication.RIDE_REQUEST.put(CARD_ID, data.getStringExtra("card_id"));
                        MvpApplication.RIDE_REQUEST.put(CARD_LAST_FOUR, data.getStringExtra("card_last_four"));
//                        llTipContainer.setVisibility(View.VISIBLE);
//                        tvChange.setVisibility(View.GONE);
                        isInvoiceCashToCard = true;
                    } else if (data.getStringExtra("payment_mode").equals(Constants.PaymentMode.CASH)) {
                        MvpApplication.RIDE_REQUEST.put(CARD_ID, null);
                        MvpApplication.RIDE_REQUEST.put(CARD_LAST_FOUR, null);
//                        llTipContainer.setVisibility(View.GONE);
//                        tvChange.setVisibility(View.VISIBLE);
                        isInvoiceCashToCard = false;
                    }

                    initPaymentView(data.getStringExtra("payment_mode"),
                            data.getStringExtra("card_last_four"), true);

                    showLoading();

                    map.put("request_id", MvpApplication.DATUM.getId());

                    if (data.getStringExtra("payment_mode").equals(Constants.PaymentMode.CARD)) {
                        map.put("payment_mode", Constants.PaymentMode.CARD);
                    } else
                        map.put("payment_mode", data.getStringExtra("payment_mode"));

                    if (data.getStringExtra("payment_mode").equals(Constants.PaymentMode.CARD))
                        map.put("card_id", data.getStringExtra("card_id"));

                    presenter.updateRide(map);
                }
                break;

            case BRAINTREE_PAYMENT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String paymentNonce = data.getStringExtra(BrainTreePaymentActivity.PAYMENT_NONCE);
                    HashMap<String, Object> brainHashMap = new HashMap<>();
                    brainHashMap.put("request_id", MvpApplication.DATUM.getId());
                    brainHashMap.put("tips", tips);
                    brainHashMap.put("braintree_nonce", paymentNonce);
                    brainHashMap.put("payment_type", Constants.PaymentMode.BRAINTREE);

                    presenter.payment(brainHashMap);
                } else
                    Toasty.error(getActivity(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    void initPaymentView(String paymentMode, String value, boolean payment) {

        switch (paymentMode) {
            case Constants.PaymentMode.CASH:
                tvPaymentMode.setText(paymentMode);
                break;
            case Constants.PaymentMode.CARD:
                if (payment)
                    if (!value.equals("")) tvPaymentMode.setText(getString(R.string.card_, value));
                    else tvPaymentMode.setText(Constants.PaymentMode.CARD);
                else tvPaymentMode.setText(paymentMode);
                break;
            case Constants.PaymentMode.BRAINTREE:
                tvPaymentMode.setText(getString(R.string.braintree));
                break;
            case Constants.PaymentMode.PAYTM:
                tvPaymentMode.setText(getString(R.string.paytm));
                break;

            case Constants.PaymentMode.PAYUMONEY:
                tvPaymentMode.setText(getString(R.string.payumoney));
                break;
            case Constants.PaymentMode.WALLET:
                tvPaymentMode.setText(getString(R.string.wallet));
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccess(BrainTreeResponse response) {
        if (!response.getToken().isEmpty()) {
            Intent intent = new Intent(getActivity(), BrainTreePaymentActivity.class);
            intent.putExtra(BrainTreePaymentActivity.EXTRAS_TOKEN, response.getToken());
            startActivityForResult(intent, BRAINTREE_PAYMENT_REQUEST_CODE);
        }
    }

    @Override
    public void onPayumoneyCheckSumSucess(CheckSumData checkSumData) {

    }

    @Override
    public void onPayTmCheckSumSucess(PaytmObject payTmResponse) {
        new PaytmPayment(Objects.requireNonNull(getActivity()), payTmResponse, new PaytmCallback() {
            @Override
            public void onPaytmSuccess(String status, String message, String paymentmode, String txid) {
                HashMap<String, Object> hashPaytmHashMap = new HashMap<>();
                hashPaytmHashMap.put("id", MvpApplication.DATUM.getId());
                hashPaytmHashMap.put("pay", txid);
                hashPaytmHashMap.put("type", "aistaxi");
                hashPaytmHashMap.put("wallet", "0");
                presenter.updatePayment(hashPaytmHashMap);
            }

            @Override
            public void onPaytmFailure(String errorMessage) {
                Toasty.error(Objects.requireNonNull(getContext()), errorMessage, Toast.LENGTH_SHORT).show();
            }
        }).setEnvironment(PaytmConstants.ENVIRONMENT_STAGING).startPayment();
    }
}
