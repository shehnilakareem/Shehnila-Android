package teaching.tutor.education.tutor;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import teaching.tutor.education.tutor.Database.StudentProfile;
import teaching.tutor.education.tutor.Database.TutorProfile;
import teaching.tutor.education.tutor.MainActivity;
import teaching.tutor.education.tutor.R;
import teaching.tutor.education.tutor.ViewProfileActivity;

public class StudentProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText firstname_value, lastName_value, programOfStudy_value, subjectToStudy_value, phone_value, email_value, intro_value, seeking_value;
    Button submitBtn;
    AlertDialog.Builder adb;
    DatabaseReference mDatabase;
    static FirebaseAuth mAuth;
String TAG =LauncherActivity.TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setViews();
        setValues();
        setOnClicks();
    }

    private void setOnClicks() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstname_value.equals("") || firstname_value == null || firstname_value.length() == 0 ||
                        lastName_value.equals("") || lastName_value == null || lastName_value.length() == 0 ||
                        programOfStudy_value.equals("") || programOfStudy_value == null || programOfStudy_value.length() == 0 ||
                        subjectToStudy_value.equals("") || subjectToStudy_value == null || subjectToStudy_value.length() == 0 ||
                        phone_value.equals("") || phone_value == null || phone_value.length() == 0 ||
                        email_value.equals("") || email_value == null || email_value.length() == 0 ||
                        intro_value.equals("") || intro_value == null || intro_value.length() == 0 ||
                        seeking_value.equals("") || seeking_value == null || seeking_value.length() == 0) {
                    showDialogs();

                } else {
                    addStudentProfile();
                }
            }
        });
    }

    private void addStudentProfile() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("students").push().getKey();
        StudentProfile post = new StudentProfile("student", firstname_value.getText().toString(), lastName_value.getText().toString(), programOfStudy_value.getText().toString(), subjectToStudy_value.getText().toString(), phone_value.getText().toString(), email_value.getText().toString(), "I am an android app development student", "I am seeking for a very skillfull android teacher." );
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/students/" + key, postValues);
//        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Intent intent = new Intent(StudentProfileActivity.this, ViewProfileActivity.class);
        intent.putExtra("type",1);
        intent.putExtra("email",mAuth.getCurrentUser().getEmail());
        intent.putExtra("Edit","true");
        startActivity(intent);
        finish();
    }

    private void showDialogs() {
        adb = new AlertDialog.Builder(StudentProfileActivity.this);
        adb.setTitle("Input Error!");
        adb.setMessage("Please fill the form properly!");
        adb.setPositiveButton("Ok", null);
        adb.show();
    }
    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstname_value = (EditText) findViewById(R.id.studentfnameEdit);
        lastName_value = (EditText) findViewById(R.id.studentlnameEdit);
        programOfStudy_value = (EditText) findViewById(R.id.program_of_studdyEdit);
        subjectToStudy_value = (EditText) findViewById(R.id.subjectTostudyEdit);
        phone_value = (EditText) findViewById(R.id.phoneEdit);
        email_value = (EditText) findViewById(R.id.emailEdit);
        intro_value = (EditText) findViewById(R.id.introductionEdit);
        seeking_value = (EditText) findViewById(R.id.seekingEdit);
        submitBtn = (Button) findViewById(R.id.submitBtn);


    }

    private void setValues() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Student Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase.child("Accounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: profile " + dataSnapshot.getChildrenCount());
                FirebaseUser currentUser = mAuth.getCurrentUser();
                Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                String currentEmail = currentUser.getEmail();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue().toString());
                    if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
                        Log.e(TAG, "onDataChange: if");
                        String email = postSnapshot.child("email").getValue().toString();
                        Log.e(TAG, "onDataChange: email:" + email);
                        if (email.equals(currentEmail)) {
                            firstname_value.setText(postSnapshot.child("first_name").getValue().toString());
                            lastName_value.setText(postSnapshot.child("last_name").getValue().toString());
                            email_value.setText(email);
                            email_value.setFocusable(false);
                            firstname_value.setFocusable(false);
                            lastName_value.setFocusable(false);

                        }

                    } else {
                        Log.e(TAG, "onDataChange: else");
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

    }
}
