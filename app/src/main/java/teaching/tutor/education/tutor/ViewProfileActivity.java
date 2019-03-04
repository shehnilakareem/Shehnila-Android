package teaching.tutor.education.tutor;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class ViewProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    public static String TAG = "TAG";
    static FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ImageView student_pp, editProfile, phoneIv, message;
    String email = "", edit = "";
    int type;
    String phone = "";
    //    String email;
    TextView tutor_name, tutor_degree, residence, gender_value, experience_value, availibility_value, subject_value, email_value, mobile_value, introduction_value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        mAuth = FirebaseAuth.getInstance();
        SharedPreffs.setSharedData(this, "prof_created", "true");
        type = getIntent().getExtras().getInt("type");
        email = getIntent().getExtras().getString("email");
        edit = getIntent().getExtras().getString("Edit");

        Log.e(TAG, "onCreate: type:" + type);
        Log.e(TAG, "onCreate: email:" + email);
        setViews();
        setValues();

        if (type == 0) {

            setRequest(email);
        } else if (type == 1) {
            setRequestStudent(email);
        }
        setOnclicks();
    }

    private void setOnclicks() {
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    Intent editProfileIntent = new Intent(ViewProfileActivity.this, TutorProfileActivity.class);
                    editProfileIntent.putExtra("type", 1);
                    startActivity(editProfileIntent);
                } else if (type == 1) {
                    Intent editProfileIntent = new Intent(ViewProfileActivity.this, StudentProfileActivity.class);
                    editProfileIntent.putExtra("type", 1);
                    startActivity(editProfileIntent);
                }
            }
        });
        phoneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!phone.equals("") || phone != null) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(ViewProfileActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ViewProfileActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                1);
                        return;
                    } else {
                        startActivity(callIntent);
                    }
                }
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onShareClick(view);
            }
        });
    }

    private void setRequestStudent(final String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("students").addValueEventListener(new ValueEventListener() {
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
//                        Log.e(TAG, "onDataChange: if" + postSnapshot.child("city").getValue().toString());
                        String emailf = postSnapshot.child("email").getValue().toString();

                        if (emailf.equals(email)) {
                            String firstName = postSnapshot.child("first_name").getValue().toString();
                            String lastName = postSnapshot.child("last_name").getValue().toString();
//                            String availableTime = postSnapshot.child("available_at").getValue().toString();
                            phone = postSnapshot.child("phone").getValue().toString();
//                            String skillToTeach = postSnapshot.child("skill_to_teach").getValue().toString();
//                            String gender = postSnapshot.child("gender").getValue().toString();
//                            String experience = postSnapshot.child("experience").getValue().toString();

                            String intro = postSnapshot.child("introduction").getValue().toString();
                            String program_of_study = postSnapshot.child("program_of_study").getValue().toString();
                            String seeking = postSnapshot.child("seeking").getValue().toString();
                            String subject_to_study = postSnapshot.child("subject_to_study").getValue().toString();


                            //fill profile
                            tutor_name.setText(firstName + " " + lastName);
//                            availibility_value.setText(availableTime);
                            mobile_value.setText(phone);
//                            gender_value.setText(gender);
//                            experience_value.setText(experience);
//                            subject_value.setText(skillToTeach);
                            introduction_value.setText(intro);
//                            residence.setText(city);

                        } else {

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

    public void onShareClick(View v) {

        Intent emailIntent = new Intent();
        emailIntent.setAction(Intent.ACTION_SEND);
        // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("hi"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "hello");
        emailIntent.setType("message/rfc822");
        PackageManager pm = getPackageManager();
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");


        Intent openInChooser = Intent.createChooser(emailIntent, "Please choose an app");
        List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
        Log.e(TAG, "onShareClick: resInfo:" + resInfo);
        List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
        for (int i = 0; i < resInfo.size(); i++) {
            // Extract the label, append it, and repackage it in a LabeledIntent
            ResolveInfo ri = resInfo.get(i);
            String packageName = ri.activityInfo.packageName;
            Log.e(TAG, "onShareClick: packagename:" + packageName);
            if (packageName.contains("android.email")) {
                emailIntent.setPackage(packageName);
            } else if (packageName.contains("messaging") || packageName.contains("whatsapp")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                if (packageName.contains("messaging")) {
                    Log.e(TAG, "onShareClick: messaging");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.putExtra("address", phone);
                    intent.setType("vnd.android-dir/mms-sms");
                } else if (packageName.contains("whatsapp")) {
                    Log.e(TAG, "onShareClick: Whatsapp");
//                    intent.putExtra(Intent.EXTRA_TEXT, "Share whatsapp");
                    intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    intent.putExtra("uid", PhoneNumberUtils.stripSeparators(phone) + "@s.whatsapp.net");//phone number without "+" prefix
                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    private void setRequest(final String email) {
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                        Log.e(TAG, "onDataChange: emailIf:" + emailf + " email:" + email);
                        if (emailf.equals(email)) {
                            String firstName = postSnapshot.child("first_name").getValue().toString();
                            String lastName = postSnapshot.child("last_name").getValue().toString();
                            String availableTime = postSnapshot.child("available_at").getValue().toString();
                            phone = postSnapshot.child("phone").getValue().toString();
                            String skillToTeach = postSnapshot.child("skill_to_teach").getValue().toString();
                            String gender = postSnapshot.child("gender").getValue().toString();
                            String experience = postSnapshot.child("experience").getValue().toString();
                            String intro = postSnapshot.child("introduction").getValue().toString();
                            String city = postSnapshot.child("city").getValue().toString();
                            String advice = postSnapshot.child("advice").getValue().toString();
                            String tutionTimeDuration = postSnapshot.child("tution_time_duration").getValue().toString();


                            //fill profile
                            tutor_name.setText(firstName + " " + lastName);
                            availibility_value.setText(availableTime);
                            mobile_value.setText(phone);
                            gender_value.setText(gender);
                            experience_value.setText(experience);
                            subject_value.setText(skillToTeach);
                            introduction_value.setText(intro);
                            residence.setText(city);


                        } else {
//                            Log.e(TAG, "onDataChange: else email i f" );
//                            Intent intent = new Intent(ViewProfileActivity.this,ProfileNotCreated.class);
//                            startActivity(intent);
                        }
//                        GeneralModel object = new GeneralModel();
//                        object.setTutorName(postSnapshot.child("first_name").getValue().toString());
//                        object.setTutorCity(postSnapshot.child("city").getValue().toString());
//                        object.setAvailableTime(postSnapshot.child("available_at").getValue().toString());
//                        object.setRatingStars(5);
//                        object.setPhoneNum();
//                        object.setTutorDegree("Masters in Computer Science");
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

    private void setViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tutor_name = (TextView) findViewById(R.id.tutor_name);
        tutor_degree = (TextView) findViewById(R.id.tutor_degree);
        residence = (TextView) findViewById(R.id.residence);
        gender_value = (TextView) findViewById(R.id.gender_value);
        availibility_value = (TextView) findViewById(R.id.availibility_value);
        subject_value = (TextView) findViewById(R.id.subject_value);
        email_value = (TextView) findViewById(R.id.email_value);
        mobile_value = (TextView) findViewById(R.id.mobile_value);
        introduction_value = (TextView) findViewById(R.id.introduction_value);
        experience_value = (TextView) findViewById(R.id.experience_value);
        editProfile = (ImageView) findViewById(R.id.editProfile);
        phoneIv = (ImageView) findViewById(R.id.phoneNum);
        message = (ImageView) findViewById(R.id.mesageIv);
    }

    private void setValues() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ViewProfileActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
        if (edit.equals("false")) {
            editProfile.setVisibility(View.GONE);
        } else if (edit.equals("true")) {
            editProfile.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
