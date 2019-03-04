package teaching.tutor.education.tutor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import teaching.tutor.education.tutor.Database.ApplicationPost;

public class ViewNotificationsActivity extends AppCompatActivity {
    Toolbar toolbar;
    String TAG = LauncherActivity.TAG;
    static FirebaseAuth mAuth;
    String studentEmail, name, description, uid;
    TextView studentNameTv, descriptionTv, viewDetailsTv, acceptTv;
    AlertDialog.Builder adb;
    DatabaseReference mDatabase;
    Boolean matched = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_notifications);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        studentEmail = getIntent().getExtras().getString("teacherEmail");
        name = getIntent().getExtras().getString("name");
        description = getIntent().getExtras().getString("description");
        uid = getIntent().getExtras().getString("uid");
        Log.e(TAG, "onCreate: teacherEmail:"+studentEmail+" name:"+name+" description:"+descriptionTv+" uid:"+uid );
        setViews();
        setValues();
        setOnClicks();
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        studentNameTv = (TextView) findViewById(R.id.tutorName);
        descriptionTv = (TextView) findViewById(R.id.requestDescription);
        viewDetailsTv = (TextView) findViewById(R.id.viewDetailstv);
        acceptTv = (TextView) findViewById(R.id.acceptTv);
    }

    private void setValues() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Accept Tution Offer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        studentNameTv.setText(name);
        descriptionTv.setText(description);
    }

    private void setOnClicks() {
        matched = false;
        viewDetailsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("tutors").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
//                FirebaseUser currentUser = mAuth.getCurrentUser();
//                Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
//                String currentEmail = currentUser.getEmail();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            // TODO: handle the post
                            Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
                            if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
                                Log.e(TAG, "onDataChange: if" + postSnapshot.child("city").getValue().toString());
                                String emailf = postSnapshot.child("email").getValue().toString();
                                Log.e(TAG, "onDataChange: emailIf:" + emailf + " email:" + studentEmail);
                                if (emailf.equals(studentEmail)) {
                                    matched = true;
                                    break;
                                } else {

                                }
                            } else {
                                Log.e(TAG, "onDataChange: else");
                            }


                        }
                        if (matched) {

                            Intent intent = new Intent(ViewNotificationsActivity.this, ViewProfileActivity.class);
                            intent.putExtra("email", studentEmail);
                            intent.putExtra("Edit", "false");
                            intent.putExtra("type", 0);
                            startActivity(intent);
                        } else {
                            Log.e(TAG, "onDataChange: else email i f");
                            Intent intent = new Intent(ViewNotificationsActivity.this, ProfileNotCreated.class);
                            startActivity(intent);
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
        });
        acceptTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogs();
            }
        });
    }

    private void showDialogs() {
        adb = new android.app.AlertDialog.Builder(ViewNotificationsActivity.this);
        adb.setTitle("Accept Tution");
        adb.setMessage("Are you sure you want " + name + " as your tutor?");
        adb.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                addApplications();
                deleteApplication();
            }
        });
        adb.setNegativeButton("No", null);
        adb.show();
    }

    private void deleteApplication() {
        Query query = mDatabase.child("applications").orderByChild("teacherEmail").equalTo(studentEmail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: dataSnapshot:applications:" + dataSnapshot);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uidGet = snapshot.child("uid").getValue().toString();

                    if (uidGet.equals(uid)) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Query query2 = mDatabase.child("Posts").orderByChild("email").equalTo(mAuth.getCurrentUser().getEmail());
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: dataSnapshot:" + dataSnapshot);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uidGet = snapshot.child("uid").getValue().toString();
                    Log.e(TAG, "onDataChange: UID:"+uid );
                    if (uidGet.equals(uid)) {
                        snapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addApplications() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously

        String key = mDatabase.child("Accepted_applications").push().getKey();
        ApplicationPost post = new ApplicationPost(uid, description, name, studentEmail, mAuth.getCurrentUser().getEmail(), getCurrentTimeUsingDate(), null);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Accepted_applications/" + key, postValues);
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
