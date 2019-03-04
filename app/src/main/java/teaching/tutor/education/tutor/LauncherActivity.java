package teaching.tutor.education.tutor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class LauncherActivity extends AppCompatActivity {
    public static String TAG = "TAG";
    Intent intent;
    ImageView myview2;
    RelativeLayout background;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);
        mAuth = FirebaseAuth.getInstance();

        setViews();
        setanimations();
    }

    private void setValues() {
        SharedPreferences settings = this.getSharedPreferences("appInfo", 0);


        boolean firstTime = settings.getBoolean("first_time", true);
        if (firstTime) {
            FirebaseAuth.getInstance().signOut();

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("first_time", false);
            editor.commit();
            Log.e(TAG, "setValues:setting: " + firstTime);
            intent = new Intent(LauncherActivity.this, SplashScreen.class);
            startActivity(intent);

        } else {
            Log.e(TAG, "setValues: mauth:" + mAuth.getCurrentUser());
            if (mAuth.getCurrentUser() == null || mAuth.getCurrentUser().equals("null")) {
                Toast.makeText(this, "mauth null", Toast.LENGTH_SHORT).show();
                Intent gotoEmail = new Intent(LauncherActivity.this, SignInActivity.class);
                startActivity(gotoEmail);
            } else {

                Log.e(TAG, "setValues:setting2: " + firstTime);
                if (SharedPreffs.getSharedPrefferences(LauncherActivity.this, "main").equals("student") || SharedPreffs.getSharedPrefferences(LauncherActivity.this, "main") == null) {
                    Intent intent2 = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(intent2);
                } else {
                    Intent intent2 = new Intent(LauncherActivity.this, MainTutorActivity.class);
                    startActivity(intent2);
                }

            }

        }
    }

    private void setanimations() {
        ObjectAnimator fadein = ObjectAnimator.ofFloat(myview2, "alpha", .0f, 1f);
        fadein.setDuration(5000);
        ObjectAnimator fadeout = ObjectAnimator.ofFloat(myview2, "alpha", 1f, 0.0f);
        fadeout.setDuration(5000);

        final AnimatorSet mAnimationSet = new AnimatorSet();

        mAnimationSet.play(fadein);

        mAnimationSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
//                mAnimationSet.start();
//                Intent i=new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(i);
                setValues();
                finish();
            }

            /**
             * {@inheritDoc}
             *
             * @param animation
             */
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
//                myview2.setVisibility(View.VISIBLE);
                Bitmap photo = BitmapFactory.decodeResource(LauncherActivity.this.getResources(), R.drawable.logo);
                myview2.setImageBitmap(photo);
            }
        });
        mAnimationSet.start();

    }

    private void setViews() {
        background = findViewById(R.id.background);
        myview2 = (ImageView) findViewById(R.id.inner_logo);
    }
}
