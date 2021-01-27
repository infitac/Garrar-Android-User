package com.garrar.user.app.base;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.garrar.user.app.R;
import com.garrar.user.app.common.Constants;
import com.garrar.user.app.MvpApplication;

import java.util.Calendar;

import pl.aprilapps.easyphotopicker.EasyImage;

import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_ID;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.CARD_LAST_FOUR;
import static com.garrar.user.app.common.Constants.RIDE_REQUEST.PAYMENT_MODE;

public abstract class BaseFragment extends Fragment implements MvpView {

    private View view;
    private BaseActivity mBaseActivity;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(getLayoutId(), container, false);
            initView(view);
        }

        return view;
    }

    protected abstract int getLayoutId();

    protected abstract View initView(View view);

    @Override
    public FragmentActivity baseActivity() {
        return getActivity();
    }

    @Override
    public void showLoading() {
        if (mBaseActivity != null) {
            mBaseActivity.showLoading();
        }
    }

    @Override
    public void hideLoading() {
        if (mBaseActivity != null) {
            mBaseActivity.hideLoading();
        }
    }

    protected void datePicker(DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(baseActivity(), dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }

    protected void timePicker(TimePickerDialog.OnTimeSetListener timeSetListener) {
        Calendar myCalendar = Calendar.getInstance();
        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), true);
        mTimePicker.show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mBaseActivity = (BaseActivity) context;
        }
    }

    protected void initPayment(TextView paymentMode) {
        if (MvpApplication.RIDE_REQUEST.containsKey(PAYMENT_MODE)) {
            switch (MvpApplication.RIDE_REQUEST.get(PAYMENT_MODE).toString()) {
                case Constants.PaymentMode.CASH:
                    paymentMode.setText(getString(R.string.cash));
                    //    paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
                    break;
                case Constants.PaymentMode.CARD:
                    System.out.println("+++++ base card id: "+MvpApplication.RIDE_REQUEST.get(CARD_ID));
                    if (MvpApplication.RIDE_REQUEST.containsKey(CARD_LAST_FOUR))
                        paymentMode.setText(getString(R.string.card_, MvpApplication.RIDE_REQUEST.get("card_last_four")));
                    else paymentMode.setText(getString(R.string.add_card_));
                    //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card, 0, 0, 0);
                    break;
                case Constants.PaymentMode.PAYPAL:
                    paymentMode.setText(getString(R.string.paypal));
                    //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_paypal, 0, 0, 0);
                    break;
                case Constants.PaymentMode.BRAINTREE:
                    paymentMode.setText(getString(R.string.braintree));
                    break;

                case Constants.PaymentMode.PAYTM:
                    paymentMode.setText(getString(R.string.paytm));
                    break;

                case Constants.PaymentMode.PAYUMONEY:
                    paymentMode.setText(getString(R.string.payumoney));
                    break;

                case Constants.PaymentMode.WALLET:
                    paymentMode.setText(getString(R.string.wallet));
                    break;
            }
        } else {
            if (MvpApplication.isCash) {
                MvpApplication.RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CASH);
                paymentMode.setText(getString(R.string.cash));
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_money, 0, 0, 0);
            } else if (MvpApplication.isCard) {
                paymentMode.setText(R.string.add_card_);
                //  paymentMode.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_card, 0, 0, 0);
                MvpApplication.RIDE_REQUEST.put(PAYMENT_MODE, Constants.PaymentMode.CARD);
            }
        }
    }

    protected void onErrorBase(Throwable t) {
        if (mBaseActivity != null) {
            mBaseActivity.onErrorBase(t);
        }
    }

    protected void handleError(Throwable e) {
        try {
            try {
                hideLoading();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        if (mBaseActivity != null) {
            mBaseActivity.handleError(e);
        }
    }

    @Override
    public void onSuccessLogout(Object object) {
        if (mBaseActivity != null) {
            mBaseActivity.onSuccessLogout(object);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (mBaseActivity != null) {
            mBaseActivity.onError(throwable);
        }
    }

    protected String getNewNumberFormat(double d) {
        return BaseActivity.getNumberFormat().format(d);
    }

    protected String getTwoDecimalNewNumberFormat(double d) {
        return BaseActivity.getTwoDecimalNumberFormat().format(d);
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                mBaseActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(permissions, requestCode);
    }

    protected void pickImage() {
        EasyImage.openChooserWithGallery(this, "", 0);
    }
}
