package teaching.tutor.education.tutor.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teaching.tutor.education.tutor.LauncherActivity;
import teaching.tutor.education.tutor.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class SlideOne extends Fragment {
    View view;
    String TAG = LauncherActivity.TAG;
    public SlideOne() {
        // Required empty public constructor
    }
    // newInstance constructor for creating fragment with arguments
    public static SlideOne newInstance(int page, String title) {
        SlideOne fragmentFirst = new SlideOne();
//        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
//        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_slid_one, container, false);
        Log.e(TAG, "onCreateView: One" );
        return view;
    }

}
