package teaching.tutor.education.tutor.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import teaching.tutor.education.tutor.ChooseSignupMethodActivity;
import teaching.tutor.education.tutor.LauncherActivity;
import teaching.tutor.education.tutor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideTwo extends Fragment {

    View view;
    Button startAppBtn;
    String TAG = LauncherActivity.TAG;
    public SlideTwo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_slide_two, container, false);
        Log.e(TAG, "onCreateView: Two" );
        setViews();
        setOnClick();
        return view;
    }

    private void setOnClick() {
        startAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChooseSignupMethodActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void setViews() {
        startAppBtn = (Button) view.findViewById(R.id.startApp_btn);
    }

}
