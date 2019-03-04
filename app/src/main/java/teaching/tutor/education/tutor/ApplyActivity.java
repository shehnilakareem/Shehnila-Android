package teaching.tutor.education.tutor;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import teaching.tutor.education.tutor.Database.ApplicationPost;
import teaching.tutor.education.tutor.Utils.GeneralModel;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class ApplyActivity extends AppCompatActivity {
    RelativeLayout relativelayout;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    EditText applicationDescription;
    Button applyBtn, cancelBtn;
    AlertDialog.Builder adb;
    String uid, studentName, studentEmail, teacherEmail, teacherName, TAG = LauncherActivity.TAG, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        studentName = getIntent().getExtras().getString("studentName");
        studentEmail = getIntent().getExtras().getString("studentEmail");
        uid = getIntent().getExtras().getString("uid");
        teacherEmail = mAuth.getCurrentUser().getEmail();
        description = SharedPreffs.getSharedPrefferences(ApplyActivity.this,"description");
        Log.e(TAG, "onCreate: applyApp: " + uid+" description:"+description);
        setViews();
        setValues();
        setOnClicks();

    }

    private void setViews() {
        relativelayout = (RelativeLayout) findViewById(R.id.relativelayout);
        applicationDescription = (EditText) findViewById(R.id.applicationDescription_value);
        applyBtn = (Button) findViewById(R.id.applyBtn);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
    }

    private void setValues() {
        Query query = mDatabase.child("tutors");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
                    if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
                        Log.e(TAG, "onDataChange: if" + postSnapshot.child("city").getValue().toString());
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                        String email = postSnapshot.child("email").getValue().toString();
                        if (email.equals(currentUser.getEmail())) {
                            Log.e(TAG, "onDataChange: profile created: email:" + email + " and curentUser:" + currentUser.getEmail());
                            teacherName = postSnapshot.child("first_name").getValue().toString()+" "+ postSnapshot.child("last_name").getValue().toString();

                            break;

                        }
                    } else {
                        Log.e(TAG, "onDataChange: else");
//                        profileCreated = false;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
        mDatabase.child("applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
//                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
//                        Log.e(TAG, "onDataChange: if" + postSnapshot.child("first_name").getValue().toString());
                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                    Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                    //don't need this code
                    String senderEmail = postSnapshot.child("teacherEmail").getValue().toString();
                    String uidGet = postSnapshot.child("uid").getValue().toString();
                    Log.e(TAG, "onDataChange: email matched:" + senderEmail + " current:" + currentUser.getEmail());
                    if (senderEmail.equals(currentUser.getEmail()) && uidGet.equals(uid)) {


                        Snackbar.make(relativelayout, "You've already applied to this job", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null).show();
                        applyBtn.setClickable(false);
                        applyBtn.setTextColor(Color.GRAY);

                        break;
                    } else Log.e(TAG, "onDataChange: else");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


    }

    private void setOnClicks() {
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (applicationDescription.equals("") || applicationDescription == null || applicationDescription.length() == 0) {
                    showDialogs();
                } else {
                    addApplications();
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showDialogs() {
        adb = new AlertDialog.Builder(ApplyActivity.this);
        adb.setTitle("Input Error!");
        adb.setMessage("Please fill the form properly!");
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    private void addApplications() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String key = mDatabase.child("applications").push().getKey();
        ApplicationPost post = new ApplicationPost(uid, applicationDescription.getText().toString(), studentName, studentEmail, teacherEmail, getCurrentTimeUsingDate(), "unread", description);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/applications/" + key, postValues);
//        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        finish();
    }

    public static String getCurrentTimeUsingDate() {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate = dateFormat.format(date);
        System.out.println("Current time of the day using Date - 12 hour format: " + formattedDate);
        return formattedDate;
    }
}
