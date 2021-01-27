package com.garrar.user.app.ui.fragment.book_ride;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.garrar.user.app.MvpApplication;
import com.garrar.user.app.data.SharedHelper;
import com.garrar.user.app.data.network.model.SendRequest;
import com.garrar.user.app.ui.activity.main.MainActivity;
import com.garrar.user.app.ui.activity.payment.PaymentActivity;
import com.garrar.user.app.ui.adapter.CouponAdapter;
import com.garrar.user.app.ui.fragment.add_courier_detail.AddCourierDetailFragment;
import com.garrar.user.app.ui.fragment.schedule.ScheduleFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseFragment;
import com.garrar.user.app.common.Constants;
import com.garrar.user.app.common.EqualSpacingItemDecoration;
import com.garrar.user.app.data.network.model.EstimateFare;
import com.garrar.user.app.data.network.model.PromoList;
import com.garrar.user.app.data.network.model.PromoResponse;
import com.garrar.user.app.data.network.model.Service;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.garrar.user.app.common.Constants.BroadcastReceiver.INTENT_FILTER;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_ID;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_ADD0;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LAT0;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DEST_LONG0;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.DISTANCE_VAL;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.SRC_ADD;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.SRC_LAT;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.SRC_LONG;
import static com.garrar.user.app.data.SharedHelper.getKey;

public class BookRideFragment extends BaseFragment implements BookRideIView {

    Unbinder unbinder;
    @BindView(R.id.schedule_ride)
    Button scheduleRide;
    @BindView(R.id.ride_now)
    Button rideNow;
    @BindView(R.id.tvEstimatedFare)
    TextView tvEstimatedFare;
    @BindView(R.id.use_wallet)
    CheckBox useWallet;
    @BindView(R.id.estimated_image)
    ImageView estimatedImage;
    @BindView(R.id.view_coupons)
    TextView viewCoupons;
    @BindView(R.id.estimated_payment_mode)
    TextView estimatedPaymentMode;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.llEstimatedFareContainer)
    LinearLayout llEstimatedFareContainer;
    @BindView(R.id.tv_detail_change)
    TextView tv_detail_change;
    private int lastSelectCoupon = 0;
    private String mCouponStatus;
    private String paymentMode;
    private Double estimatedFare;
    Service service;
    EstimateFare estimateFare;
    double walletAmount;
    private BookRidePresenter<BookRideFragment> presenter = new BookRidePresenter<>();
    private CouponListener mCouponListener = new CouponListener() {
        @Override
        public void couponClicked(int pos, PromoList promoList, String promoStatus) {
            if (!promoStatus.equalsIgnoreCase(getString(R.string.remove))) {
                lastSelectCoupon = promoList.getId();
                viewCoupons.setText(promoList.getPromoCode());
                viewCoupons.setTextColor(getResources().getColor(R.color.colorAccent));
                viewCoupons.setBackgroundResource(R.drawable.coupon_transparent);
                mCouponStatus = viewCoupons.getText().toString();
                Double discountFare = (estimatedFare * promoList.getPercentage()) / 100;

                if (discountFare > promoList.getMaxAmount()) {
//                    tvEstimatedFare.setText(String.format("%s %s",
//                            SharedHelper.getKey(Objects.requireNonNull(getContext()), "currency"),
//                            getNewNumberFormat(estimatedFare - 3 - promoList.getMaxAmount()) + "-" + getTwoDecimalNewNumberFormat(estimatedFare + 5 - promoList.getMaxAmount())));
                    tvEstimatedFare.setText(String.format("%s %s-%s", getKey(Objects.requireNonNull(getContext()), "currency"), String.valueOf(estimatedFare - 3 - promoList.getMaxAmount()),
                            String.valueOf(estimatedFare + 5 - promoList.getMaxAmount())));
                } else {
//                    tvEstimatedFare.setText(String.format("%s %s",
//                            SharedHelper.getKey(Objects.requireNonNull(getContext()), "currency"),
//                            getNewNumberFormat(estimatedFare - 3 - discountFare) + "-" + getTwoDecimalNewNumberFormat(estimatedFare + 5 - promoList.getMaxAmount())));
                    tvEstimatedFare.setText(String.format("%s %s-%s", getKey(Objects.requireNonNull(getContext()), "currency"), String.valueOf(estimatedFare - 3 - discountFare), String.valueOf(estimatedFare + 5 - promoList.getMaxAmount())));
                }
            } else {
                scaleView(viewCoupons, 0f, 0.9f);
                viewCoupons.setText(getString(R.string.view_coupon));
                viewCoupons.setBackgroundResource(R.drawable.button_round_accent);
                viewCoupons.setTextColor(getResources().getColor(R.color.white));
                mCouponStatus = viewCoupons.getText().toString();
//                tvEstimatedFare.setText(String.format("%s %s",
//                        SharedHelper.getKey(Objects.requireNonNull(getContext()), "currency"),
//                        getNewNumberFormat(estimatedFare)));

                tvEstimatedFare.setText(String.format("%s %s", getKey(Objects.requireNonNull(getContext()), "currency"), estimatedFare));
            }
        }
    };

    public BookRideFragment() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_book_ride;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View initView(View view) {
        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        Bundle args = getArguments();
        if (args != null) {
            String serviceName = args.getString("service_name");
            service = (Service) args.getSerializable("mService");
            estimateFare = (EstimateFare) args.getSerializable("estimate_fare");
            walletAmount = Objects.requireNonNull(estimateFare).getWalletBalance();
            if (serviceName != null && !serviceName.isEmpty()) {
                Glide
                        .with(Objects.requireNonNull(getContext()))
                        .load(Objects.requireNonNull(service).getImage())
                        .apply(RequestOptions
                                .placeholderOf(R.drawable.ic_car)
                                .dontAnimate()
                                .override(100, 100)
                                .error(R.drawable.ic_car))
                        .into(estimatedImage);
                estimatedFare = estimateFare.getEstimatedFare();
//                tvEstimatedFare.setText(SharedHelper.getKey(getContext(), "currency") + " " +
//                        getNewNumberFormat(estimatedFare - 3) + "-" + getTwoDecimalNewNumberFormat(estimatedFare + 5));
                tvEstimatedFare.setText(getKey(Objects.requireNonNull(getContext()), "currency")+" "+String.valueOf(estimatedFare - 3) + "-" + String.valueOf(estimatedFare + 5));

                if (walletAmount == 0) {
                    useWallet.setVisibility(View.GONE);
                    walletBalance.setVisibility(View.GONE);
                } else {
                    useWallet.setVisibility(View.VISIBLE);
                    walletBalance.setVisibility(View.VISIBLE);
                    //walletBalance.setText(getNewNumberFormat(Double.parseDouble(String.valueOf(walletAmount))));
                    walletBalance.setText(getKey(Objects.requireNonNull(getContext()), "currency")+" "+walletAmount);

                }
                MvpApplication.RIDE_REQUEST.put(DISTANCE_VAL, estimateFare.getDistance());
            }
        }
        scaleView(viewCoupons, 0f, 0.9f);
        paymentMode = MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE).toString();
        return view;
    }

    public void scaleView(View v, float startScale, float endScale) {
        Animation anim = new ScaleAnimation(
                1f, 1f, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 1f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(1000);
        v.startAnimation(anim);
    }

    @Override
    public void onDestroyView() {
//        presenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @OnClick({R.id.schedule_ride, R.id.ride_now, R.id.view_coupons, R.id.tv_change, R.id.tv_detail_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_detail_change:{
                Bundle bundle = new Bundle();
                bundle.putString("service_name", service.getName());
                bundle.putSerializable("mService", service);
                bundle.putSerializable("estimate_fare", estimateFare);
                bundle.putDouble("use_wallet", walletAmount);
                bundle.putString("type", "1");
                AddCourierDetailFragment addCourierDetailFragment = new AddCourierDetailFragment();
                addCourierDetailFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().remove(this);
                ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(addCourierDetailFragment);
                break;
            }
            case R.id.schedule_ride:
                ((MainActivity) Objects.requireNonNull(getActivity())).changeFragment(new ScheduleFragment());
                break;
            case R.id.ride_now:
                System.out.println("+++ ride now: payment: " + Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE)).toString());
                System.out.println("+++ ride now: payment: " + MvpApplication.RIDE_REQUEST.get(Constants.RIDE_REQUEST.PAYMENT_MODE_SELECTED));
                // have to handle this properly
                // there's a bug
                // biplob
                if (Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(Constants.RIDE_REQUEST.PAYMENT_MODE)).toString()
                        .equals(Constants.PaymentMode.CASH)) {
                    if (useWallet.isChecked()) {
                        sendCashWithWalletRequest();
                    } else {
                        sendCashRequest();
                    }
//                    System.out.println("++++ cash");
                } else if (Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(Constants.RIDE_REQUEST.PAYMENT_MODE)).toString()
                        .equals(Constants.PaymentMode.CARD)) {
                    System.out.println("+++++ card id: " + MvpApplication.RIDE_REQUEST.get(CARD_ID));
                    if (useWallet.isChecked()) {
                        sendCardWithWalletRequest();
                    } else {
                        sendCardRequest();
                    }
//                    System.out.println("++++ card");
//                    System.out.println("++++ card: " + MvpApplication.RIDE_REQUEST.get(LAST_FOUR));
                }
//                try {
//                    if (Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE)).toString()
//                            .equals(Constants.PaymentMode.CARD)) {
//                        if (MvpApplication.RIDE_REQUEST.containsKey(CARD_LAST_FOUR))
//                            sendRequest();
//                        else
//                            Toast.makeText(getActivity().getApplicationContext(),
//                                    getResources().getString(R.string.choose_card), Toast.LENGTH_SHORT)
//                                    .show();
//                    }
//                } catch (Exception e) {
//                    Log.e("Error", e.toString());
//                }
//
//                if (Objects.requireNonNull(MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE)).toString()
//                        .equals(Constants.PaymentMode.TELR)) {
//                    sendRequest();
//                }
//
//                MvpApplication.isCash = true;
//                Log.e("Payment mode is cash, ", MvpApplication.isCash + " ride_now action");
//
//                if (MvpApplication.isCash) {
//                    sendRequest();
//                }
                break;
            case R.id.view_coupons:
                showLoading();
                try {
                    presenter.getCouponList();
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        hideLoading();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
                break;
            case R.id.tv_change:
                ((MainActivity) Objects.requireNonNull(getActivity())).updatePaymentEntities();
                startActivityForResult(new Intent(getActivity(), PaymentActivity.class), PaymentActivity.PICK_PAYMENT_METHOD);
                break;
        }
    }

    private void sendCashRequest() {
        HashMap<String, Object> map = new HashMap<>(MvpApplication.RIDE_REQUEST);
        map.put(DEST_ADD0,MvpApplication.RIDE_REQUEST.get(SRC_ADD));
        map.put(DEST_LAT0,MvpApplication.RIDE_REQUEST.get(SRC_LAT));
        map.put(DEST_LONG0,MvpApplication.RIDE_REQUEST.get(SRC_LONG));
        map.put("payment_mode", Constants.PaymentMode.CASH);
        map.put("total_stop", MvpApplication.total_stop);
        showLoading();
        try {
            presenter.rideNow(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCashWithWalletRequest() {
        HashMap<String, Object> map = new HashMap<>(MvpApplication.RIDE_REQUEST);
        map.put(DEST_ADD0,MvpApplication.RIDE_REQUEST.get(SRC_ADD));
        map.put(DEST_LAT0,MvpApplication.RIDE_REQUEST.get(SRC_LAT));
        map.put(DEST_LONG0,MvpApplication.RIDE_REQUEST.get(SRC_LONG));
        map.put("payment_mode", Constants.PaymentMode.CASH);
        map.put("use_wallet", 1);
        map.put("total_stop", MvpApplication.total_stop);
        showLoading();
        try {
            presenter.rideNow(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCardRequest() {
        HashMap<String, Object> map = new HashMap<>(MvpApplication.RIDE_REQUEST);
        map.put(DEST_ADD0,MvpApplication.RIDE_REQUEST.get(SRC_ADD));
        map.put(DEST_LAT0,MvpApplication.RIDE_REQUEST.get(SRC_LAT));
        map.put(DEST_LONG0,MvpApplication.RIDE_REQUEST.get(SRC_LONG));
        map.put("payment_mode", Constants.PaymentMode.CARD);
        map.put("card_id", MvpApplication.RIDE_REQUEST.get(CARD_ID));
        map.put("total_stop", MvpApplication.total_stop);
        showLoading();
        try {
            presenter.rideNow(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCardWithWalletRequest() {
        HashMap<String, Object> map = new HashMap<>(MvpApplication.RIDE_REQUEST);
        map.put(DEST_ADD0,MvpApplication.RIDE_REQUEST.get(SRC_ADD));
        map.put(DEST_LAT0,MvpApplication.RIDE_REQUEST.get(SRC_LAT));
        map.put(DEST_LONG0,MvpApplication.RIDE_REQUEST.get(SRC_LONG));
        map.put("payment_mode", Constants.PaymentMode.CARD);
        map.put("card_id", MvpApplication.RIDE_REQUEST.get(CARD_ID));
        map.put("use_wallet", 1);
        map.put("total_stop", MvpApplication.total_stop);
        showLoading();
        try {
            presenter.rideNow(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Dialog couponDialog(PromoResponse promoResponse) {
        BottomSheetDialog couponDialog = new BottomSheetDialog(Objects.requireNonNull(getContext()), R.style.SheetDialog);
        couponDialog.setCanceledOnTouchOutside(true);
        couponDialog.setCancelable(true);
        couponDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        couponDialog.setContentView(R.layout.activity_coupon_dialog);
        RecyclerView couponView = couponDialog.findViewById(R.id.coupon_rv);
        IndefinitePagerIndicator indicator = couponDialog.findViewById(R.id.recyclerview_pager_indicator);
        List<PromoList> couponList = promoResponse.getPromoList();
        if (couponList != null && !couponList.isEmpty()) {
            CouponAdapter couponAdapter = new CouponAdapter(getActivity(), couponList,
                    mCouponListener, couponDialog, lastSelectCoupon, mCouponStatus);
            assert couponView != null;
            couponView.setLayoutManager(new LinearLayoutManager(getActivity(),
                    LinearLayoutManager.HORIZONTAL, false));
            couponView.setItemAnimator(new DefaultItemAnimator());
            couponView.addItemDecoration(new EqualSpacingItemDecoration(16,
                    EqualSpacingItemDecoration.HORIZONTAL));
            Objects.requireNonNull(indicator).attachToRecyclerView(couponView);
            couponView.setAdapter(couponAdapter);
            couponAdapter.notifyDataSetChanged();
        }
        couponDialog.setOnKeyListener((dialogInterface, keyCode, keyEvent) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                new BottomSheetDialog(getContext()).dismiss();
                Log.d("TAG", "--------- Do Something -----------");
                return true;
            }
            return false;
        });
        Window window = couponDialog.getWindow();
        assert window != null;
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.CENTER | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(param);
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        couponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        return couponDialog;
    }

    public void sendRequest() {
        HashMap<String, Object> map = new HashMap<>(MvpApplication.RIDE_REQUEST);
        map.put("use_wallet", useWallet.isChecked() ? 1 : 0);
        map.put("promocode_id", lastSelectCoupon);
//        map.put("payment_mode", Constants.PaymentMode.CARD);
        if (paymentMode != null && !paymentMode.equalsIgnoreCase("")) {
//            if (paymentMode.equals(Constants.PaymentMode.TELR)) {
//                map.put("payment_mode", Constants.PaymentMode.TELR);
//            } else {
//                map.put("payment_mode", paymentMode);
//            }
            map.put("payment_mode", Constants.PaymentMode.CARD);
        } else {
            map.put("payment_mode", "CASH");
        }
        showLoading();
        try {
            presenter.rideNow(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void onSuccess(@NonNull Object object) {
//        try {
//            hideLoading();
//            if (object instanceof LinkedTreeMap) {
//                LinkedTreeMap responseMap = (LinkedTreeMap) object;
//                if (responseMap.get("request_id") != null) {
//                    Toast.makeText(getActivity(), "requested id:" + responseMap.get("request_id"), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        getResources().getString(R.string.lost_item_error), Toast.LENGTH_SHORT)
//                        .show();
//            }
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        baseActivity().sendBroadcast(new Intent(INTENT_FILTER));
//    }

    @Override
    public void onSuccess(SendRequest object) {
        try {
            hideLoading();
            //Toast.makeText(getActivity(), "requested id:" + object.getRequestId().toString(), Toast.LENGTH_SHORT).show();
            MvpApplication.RIDE_REQUEST.put(Constants.RIDE_REQUEST.REQUESSTID, object.getRequestId());
            SharedHelper.putKey(getActivity(), "request_id", object.getRequestId().toString());
            HashMap<String, RequestBody> map = new HashMap<>(MvpApplication.PACKAGE_DETAIL);
            map.put("user_request_id",RequestBody.create(MediaType.parse("text/plain"), object.getRequestId().toString()));
            MultipartBody.Part filePart = null;
            if (MvpApplication.imgFile != null)
                try {
                    File compressedImageFile = new Compressor(getActivity()).compressToFile(MvpApplication.imgFile);
                    filePart = MultipartBody.Part.createFormData("image", compressedImageFile.getName(),
                            RequestBody.create(MediaType.parse("image*//*"), compressedImageFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            presenter.update(map, filePart);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //baseActivity().sendBroadcast(new Intent(INTENT_FILTER));
    }

    @Override
    public void onUpdateSuccess(JSONObject jsonObject) {
        //Toast.makeText(getActivity(), jsonObject.toString(), Toast.LENGTH_SHORT).show();
        baseActivity().sendBroadcast(new Intent(INTENT_FILTER));
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {
        handleError(throwable);
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    public void onSuccessCoupon(PromoResponse promoResponse) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (promoResponse != null && promoResponse.getPromoList() != null
                && !promoResponse.getPromoList().isEmpty()) couponDialog(promoResponse).show();
        else Toast.makeText(baseActivity(), "Coupon is empty", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PaymentActivity.PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK) {
            MvpApplication.RIDE_REQUEST.put(PAYMENT_MODE, data.getStringExtra("payment_mode"));
            paymentMode = data.getStringExtra("payment_mode");
            if (data.getStringExtra("payment_mode").equals("CARD")) {
                MvpApplication.RIDE_REQUEST.put(CARD_ID, data.getStringExtra("card_id"));
                MvpApplication.RIDE_REQUEST.put(CARD_LAST_FOUR, data.getStringExtra("card_last_four"));
            } else {
                MvpApplication.isCash = true;
            }
            System.out.println("+++++ cardid: " + data.getStringExtra("card_id"));
            System.out.println("+++++ four: " + data.getStringExtra("card_last_four"));
            initPayment(estimatedPaymentMode);
        } else {
            MvpApplication.isCash = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initPayment(estimatedPaymentMode);
    }

    public interface CouponListener {
        void couponClicked(int pos, PromoList promoList, String promoStatus);
    }
}
