package teaching.tutor.education.tutor;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import teaching.tutor.education.tutor.Fragments.SlideOne;
import teaching.tutor.education.tutor.Fragments.SlideTwo;

public class SplashScreen extends AppCompatActivity {
    String TAG = LauncherActivity.TAG;
     ViewPager viewPager;
     FragAdapter adapter;
     String  firstTime = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate:splash activity " );
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
////Remove notification bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
        setContentView(R.layout.activity_splash_screen);
        setViews();
        setupViewPager();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Toast.makeText(SplashScreen.this, "Fragment:"+i, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setViews() {
        viewPager = (ViewPager) findViewById(R.id.splashViewPager);
    }

    private void setupViewPager() {
        Log.e("mainActivity", "setupViewPager: ");
        adapter = new FragAdapter(getSupportFragmentManager());
        adapter.addFragment(new SlideOne(), "One");
        adapter.addFragment(new SlideTwo(), "Two");
        viewPager.setAdapter(adapter);
    }
    public class FragAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragmentList.get(i);
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public int getCount() {
            Log.e("TAG", "getCount: "+mFragmentList.size() );
            return mFragmentList.size();
        }
    }
}
