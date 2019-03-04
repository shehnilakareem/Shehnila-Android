package teaching.tutor.education.tutor.Utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import teaching.tutor.education.tutor.LauncherActivity;


public class CustomDrawerButton extends android.support.v7.widget.AppCompatButton implements DrawerLayout.DrawerListener,View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private int side = Gravity.RIGHT;
    private RelativeLayout fulllayout;
    String TAG = LauncherActivity.TAG;
    public CustomDrawerButton(Context context) {
        super(context);
    }

    public CustomDrawerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDrawerButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void changeState() {
        if (mDrawerLayout.isDrawerOpen(side)) {
            mDrawerLayout.closeDrawer(side);
        } else {
            mDrawerLayout.openDrawer(side);
        }
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        Log.e("BUTTOM DRAWER: ", "onDrawerSlide");
        fulllayout.setTranslationX(-slideOffset * drawerView.getWidth());
        mDrawerLayout.setScrimColor(ContextCompat.getColor(getContext(),android.R.color.transparent));
        mDrawerLayout.bringChildToFront(drawerView);
        mDrawerLayout.requestLayout();

    }

    @Override
    public void onDrawerOpened(View drawerView) {
        Log.e("BUTTOM DRAWER: ", "onDrawerOpened");
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        Log.e("BUTTOM DRAWER: ", "onDrawerClosed");
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        Log.e("BUTTOM DRAWER: ", "onDrawerStateChanged");
    }

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    public CustomDrawerButton setDrawerLayout(DrawerLayout mDrawerLayout) {
        this.mDrawerLayout = mDrawerLayout;
        return this;
    }

    public void setFulllayout(RelativeLayout fulllayout) {
        this.fulllayout = fulllayout;
    }

    @Override
    public void onClick(View view) {
        Log.e(TAG, "onClick:onClick " );
    }
}
