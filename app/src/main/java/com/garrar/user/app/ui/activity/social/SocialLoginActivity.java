package com.garrar.user.app.ui.activity.social;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.garrar.user.app.BuildConfig;
import com.garrar.user.app.ui.activity.main.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.garrar.user.app.R;
import com.garrar.user.app.base.BaseActivity;
import com.garrar.user.app.data.SharedHelper;
import com.garrar.user.app.data.network.model.Token;
import com.garrar.user.app.ui.countrypicker.Country;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class SocialLoginActivity extends BaseActivity implements SocialIView {

    private GoogleSignInClient mGoogleSignInClient;
    private SocialPresenter<SocialLoginActivity> presenter = new SocialPresenter<>();
    private CallbackManager callbackManager;
    private HashMap<String, Object> map = new HashMap<>();
    String codeSend;
    FirebaseAuth mAuth;
    AlertDialog phoneDialog;
    Dialog otpDialog;
    String mobile = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_social_login;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        callbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();

        map.put("device_token", SharedHelper.getKey(this, "device_token"));
        map.put("device_id", SharedHelper.getKey(this, "device_id"));
        map.put("device_type", BuildConfig.DEVICE_TYPE);

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_signin_server_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.garrar.user.app",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", hash);
            }
        } catch (PackageManager.NameNotFoundException e) {


        } catch (NoSuchAlgorithmException e) {

        }

    }

    @OnClick({R.id.facebook, R.id.google})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.facebook:
                fbLogin();
                break;
            case R.id.google:
                showLoading();
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_GOOGLE_LOGIN);
                break;
        }
    }

    void fbLogin() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            public void onSuccess(LoginResult loginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    map.put("login_by", "facebook");
                    map.put("accessToken", loginResult.getAccessToken().getToken());
                    Country mCountry = getDeviceCountry(SocialLoginActivity.this);
//                    fbOtpVerify(mCountry.getCode(), mCountry.getDialCode(), "");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPhoneVerificationAlertDialog();
                        }
                    });
                }
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
                exception.printStackTrace();
                String s = exception.getMessage();
                if (exception instanceof FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null)
                        LoginManager.getInstance().logOut();
                } else if (s.contains("GraphQLHttpFailureDomain"))
                    Toasty.info(SocialLoginActivity.this, getString(R.string.fb_session_expired), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_GOOGLE_LOGIN) {
            try {
                hideLoading();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            String TAG = "REQUEST_GOOGLE_LOGIN";
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //String token = account.getIdToken();
                map.put("login_by", "google");
                Runnable runnable = () -> {
                    try {
                        String scope = "oauth2:" + Scopes.EMAIL + " " + Scopes.PROFILE;
                        String accessToken = GoogleAuthUtil.getToken(getApplicationContext(), Objects.requireNonNull(Objects.requireNonNull(account).getAccount()), scope, new Bundle());
                        Log.d(TAG, "accessToken:" + accessToken);
                        map.put("accessToken", accessToken);
                        Country mCountry = getDeviceCountry(SocialLoginActivity.this);
//                        fbOtpVerify(mCountry.getCode(), mCountry.getDialCode(), "");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showPhoneVerificationAlertDialog();
                            }
                        });
                    } catch (IOException | GoogleAuthException e) {
                        e.printStackTrace();
                    }
                };
                AsyncTask.execute(runnable);
            } catch (ApiException e) {
                Log.w(TAG, "signInResult:failed code = " + e.getStatusCode());
            }
        } else if (requestCode == REQUEST_ACCOUNT_KIT && data != null) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (!loginResult.wasCancelled())
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.d("REQUEST_ACCOUNT_KIT", "onSuccess: Account Kit" + Objects.requireNonNull(AccountKit.getCurrentAccessToken()).getToken());
                        if (Objects.requireNonNull(AccountKit.getCurrentAccessToken()).getToken() != null) {
                            PhoneNumber phoneNumber = account.getPhoneNumber();
                            SharedHelper.putKey(SocialLoginActivity.this, "country_code", "+" + phoneNumber.getCountryCode());
                            SharedHelper.putKey(SocialLoginActivity.this, "mobile", phoneNumber.getPhoneNumber());
                            register();
                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("REQUEST_ACCOUNT_KIT", "onError: Account Kit" + accountKitError);
                    }
                });
        }
    }

    private void register() {
        map.put("mobile", SharedHelper.getKey(this, "mobile"));
      //  map.put("country_code", SharedHelper.getKey(this, "country_code"));

        //map.put("country_code", "+91");
        if (Objects.equals(map.get("login_by"), "google")) presenter.loginGoogle(map);
        else if (Objects.equals(map.get("login_by"), "facebook")) presenter.loginFacebook(map);

        showLoading();
    }

    @Override
    public void onSuccess(Token token) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        String accessToken = token.getTokenType() + " " + token.getAccessToken();
        SharedHelper.putKey(this, "access_token", accessToken);
        SharedHelper.putKey(this, "logged_in", true);
        finishAffinity();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    private void showPhoneVerificationAlertDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.activity_phone_number, null);
        EditText etPhoneNumber = alertLayout.findViewById(R.id.etPhoneNumber);
        AppCompatButton sendOTP = alertLayout.findViewById(R.id.btnSendOTP);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(etPhoneNumber.getText().toString())) {
                    Toast.makeText(SocialLoginActivity.this, "Phone number required .", Toast.LENGTH_SHORT).show();
                } else if (etPhoneNumber.getText().toString().length() < 10) {
                    Toast.makeText(SocialLoginActivity.this, "Insert valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    mobile = etPhoneNumber.getText().toString();
                    sendVerificationCode(mobile);
                }
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Verify Phone Number");
        alert.setView(alertLayout);
        phoneDialog = alert.create();
        phoneDialog.setCanceledOnTouchOutside(false);
        phoneDialog.show();
    }

    private void sendVerificationCode(String phoneNumber) {


        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

        showOTPVerificationAlertDialog();
    }


    private void resendVerificationCode() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mobile,        // Phone number to verify
                120,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void showOTPVerificationAlertDialog() {
        otpDialog = new Dialog(this);
        otpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        otpDialog.setCancelable(false);
        otpDialog.setContentView(R.layout.verify_number_dialog);


        TextView resend_otp = otpDialog.findViewById(R.id.resend_otp);
        Button dialogButton = otpDialog.findViewById(R.id.btnVerify);
        Button btnCancel = otpDialog.findViewById(R.id.btnCancel);
        EditText edtVerification_code = otpDialog.findViewById(R.id.edtVerification_code);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtVerification_code.getText().toString())) {

                    Toast.makeText(SocialLoginActivity.this, "Enter Verification code .", Toast.LENGTH_SHORT).show();

                } else {
                    verifyPhoneNumber(edtVerification_code.getText().toString());

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpDialog.dismiss();
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                resendVerificationCode();
            }
        });

        otpDialog.show();

    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {


        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            codeSend = s;


            Toast.makeText(SocialLoginActivity.this, "Check your phone for OTP code ", Toast.LENGTH_SHORT).show();

        }
    };

    private void verifyPhoneNumber(String code) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSend, code);

        signInWithPhoneAuthCredential(credential);


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();

                            System.out.println("Phone_num" + user.getPhoneNumber());

                            Toast.makeText(SocialLoginActivity.this, "Your number is verified .", Toast.LENGTH_SHORT).show();
                            otpDialog.dismiss();
                            phoneDialog.dismiss();


                            SharedHelper.putKey(SocialLoginActivity.this,"phone_with_code",user.getPhoneNumber());
                            SharedHelper.putKey(SocialLoginActivity.this, "mobile", user.getPhoneNumber());
                            register();

//                            Intent intent = new Intent(SocialLoginActivity.this, RegisterActivity.class);
//                            startActivity(intent);




                        } else {

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(SocialLoginActivity.this, "Verification failed .", Toast.LENGTH_SHORT).show();

                            }
                        }


                        if (!task.isSuccessful()){
                            Toast.makeText(SocialLoginActivity.this, "Wrong OTP", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
