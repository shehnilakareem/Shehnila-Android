package teaching.tutor.education.tutor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class VerifyPhoneActivity extends AppCompatActivity {
    String TAG = LauncherActivity.TAG;
    TextInputEditText number;
    Button verify;
    TextView skipPhoneVerification;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    AlertDialog.Builder adb;
    CountryCodePicker countryCodePicker;
    Toolbar toolbar;
    ImageView gif;
    ProgressDialog pd;
    TextView tip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        number = (TextInputEditText) findViewById(R.id.phoneNumber);
        verify = (Button) findViewById(R.id.verify);
        skipPhoneVerification = (TextView) findViewById(R.id.skip);
        countryCodePicker = (CountryCodePicker) findViewById(R.id.phone_code_spinner);
        gif = (ImageView) findViewById(R.id.gif);




        Glide.with(VerifyPhoneActivity.this).load(R.drawable.horizontal_gif).into(gif);
        createProgressDialog();
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number.getText().equals(null) || number.getText().toString().isEmpty()) {
                    createDialog(1);
                } else {
                    pd.show();
                    String phonenumber = number.getText().toString();
                    Log.e(TAG, "onClick: "+countryCodePicker.getSelectedCountryCode()+phonenumber );
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+"+countryCodePicker.getSelectedCountryCode()+phonenumber,
                            60,
                            TimeUnit.SECONDS,
                            VerifyPhoneActivity.this,
                            mCallbacks
                    );

                }
            }
        });
        skipPhoneVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick:Verify Phone ");
                Intent intent = new Intent(VerifyPhoneActivity.this,EmailVerification.class);
                startActivity(intent);
            }
        });
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                pd.dismiss();

//                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                pd.dismiss();
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Log.e(TAG, "onVerificationFailed: invalid request" );
                    createDialog(2);
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Log.e(TAG, "onVerificationFailed: project qouta" );
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);
                pd.dismiss();
                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;

                // ...
                Intent intent = new Intent(VerifyPhoneActivity.this, CodeVerificationActivity.class);
                startActivity(intent);
            }
        };

    }

    private void createProgressDialog() {
        pd=new ProgressDialog(this);
        pd.setTitle("Loading...");
        pd.setMessage("Please Wait..");

    }

    private void createDialog(int i) {
        adb = new AlertDialog.Builder(VerifyPhoneActivity.this);
        adb.setTitle("Alert!");
        switch (i) {
            case 1:
                adb.setMessage("Phone Number Cannot be empty.");

                break;
            case 2:
                adb.setMessage("Phone Number is Invalid.");

                break;
        }
        adb.setPositiveButton("Ok", null);
        adb.show();

    }

    public class MyClickableSpan extends ClickableSpan {
        private final String mText;
        protected MyClickableSpan(final String text) {
            mText = text;
        }

        @Override
        public void onClick(final View widget) {
            Uri uriUrl = Uri.parse(mText);
            Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
            startActivity(launchBrowser);
//            sl.onTagClicked(mText);
            Log.e(TAG, "onClick:Text: "+mText );
//            SharedPreffs.setSharedData(getActivity(),"keyword",mText.replace("-"," ").toLowerCase().trim());
//            SharedPreffs.setSharedData(getActivity(),"city","");
//            SharedPreffs.setSharedData(getActivity(),"displayName",mText);
//            CatagoryDetailFragment cdf = new CatagoryDetailFragment();
//            Bundle bundle = new Bundle();
//            bundle.putString("type","1");
//            cdf.setArguments(bundle);
//            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.layoutt,cdf).addToBackStack(null).commit();

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(VerifyPhoneActivity.this.getResources().getColor(R.color.colorPrimary));

            ds.setUnderlineText(false);
        }
    }
}
