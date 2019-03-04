package teaching.tutor.education.tutor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import teaching.tutor.education.tutor.Adapters.GeneralAdapter;
import teaching.tutor.education.tutor.Adapters.SimpleAdapter;
import teaching.tutor.education.tutor.Database.TutorProfile;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;

public class TutorProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    static FirebaseAuth mAuth;
    int type;
    Spinner spinner, genderSpinner, experienceSpinner, tutorTypyeSpinner;
    ArrayList<String> qualification, gender, experience, tutorType;
    SimpleAdapter arrayAdapter, qualificationAdapter, experienceAdapter, tutorTypeAdapter;
    LinearLayout qualificationLayout, program_of_studdyLayout, subject_to_teachLayout, skillLayout;
    EditText subject_to_teachEdit, program_of_studdyEdit, skill_to_teachEdit, studentfnameEdit, studentlnameEdit,
            cityEdit, phoneEdit, emailEdit, introductionEdit, adviceEdit;
    ScrollView mainScroll;
    Button submitBtt;
    AlertDialog.Builder adb;
    String TAG = LauncherActivity.TAG;
    DatabaseReference mDatabase;
    int PICK_PHOTO_FOR_AVATAR = 1000;
    ImageView dpIv;
    Bitmap newProfilePic;
    FirebaseStorage storage;
    ByteArrayOutputStream baos;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        type = getIntent().getExtras().getInt("type");
        setViews();
        if (type == 0)
            setValues();
        else if (type == 1)
            setEditValues();
        setOnClicks();
    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 1) {
            extras = data.getExtras();
            if (extras != null) {
                //Get image
                newProfilePic = extras.getParcelable("data");
                dpIv.setImageBitmap(newProfilePic);
            }
        }
    }

    private void setEditValues() {
        submitBtt.setText("Save");
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Tutor Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mDatabase.child("tutors").addValueEventListener(new ValueEventListener() {
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
                            studentfnameEdit.setText(postSnapshot.child("first_name").getValue().toString());
                            studentlnameEdit.setText(postSnapshot.child("last_name").getValue().toString());
                            skill_to_teachEdit.setText(postSnapshot.child("skill_to_teach").getValue().toString());
                            cityEdit.setText(postSnapshot.child("city").getValue().toString());
                            phoneEdit.setText(postSnapshot.child("phone").getValue().toString());
                            introductionEdit.setText(postSnapshot.child("introduction").getValue().toString());
                            adviceEdit.setText(postSnapshot.child("advice").getValue().toString());
                            emailEdit.setText(email);
                            emailEdit.setFocusable(false);


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


        qualification = new ArrayList();
        qualification.add("Select");
        qualification.add("Hafiz-e-Quran");
        qualification.add("Matric");
        qualification.add("Intermediate");
        qualification.add("Bechelor");
        qualification.add("Diploma & Certificates");
        qualification.add("Master");
        qualification.add("M.Phil");
        qualification.add("Ph.D");
        qualification.add("Others");
        qualificationAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = qualificationAdapter.getItem(position);
                Log.e(TAG, "getView: " + text);
                item.setText(text);
                return convertView;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spinner.setDropDownVerticalOffset(80);
        }

        spinner.setAdapter(qualificationAdapter);
        qualificationAdapter.setArray(qualification);

        gender = new ArrayList();
        gender.add("Select");
        gender.add("Female");
        gender.add("Male");
        gender.add("Others");

        populateAdapter(genderSpinner, gender);
        experience = new ArrayList();
        experience.add("No experience");
        experience.add("Less than a year");
        for (int i = 1; i < 20; i++)
            experience.add(i + " years");
        experienceAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = experienceAdapter.getItem(position);
                Log.e(TAG, "getView: " + text);
                item.setText(text);
                return convertView;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            experienceSpinner.setDropDownVerticalOffset(20);
        }

        experienceSpinner.setAdapter(experienceAdapter);
        experienceAdapter.setArray(experience);

        tutorType = new ArrayList();
        tutorType.add("Select Type");
        tutorType.add("Educational Teacher");
        tutorType.add("Skill Teacher");
        tutorType.add("Both");

        tutorTypeAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = tutorTypeAdapter.getItem(position);
                item.setText(text);
                return convertView;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tutorTypyeSpinner.setDropDownVerticalOffset(20);
        }

        tutorTypyeSpinner.setAdapter(tutorTypeAdapter);
        tutorTypeAdapter.setArray(tutorType);

        tutorTypyeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        qualificationLayout.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        program_of_studdyLayout.setVisibility(View.VISIBLE);
                        program_of_studdyEdit.setVisibility(View.VISIBLE);
                        subject_to_teachLayout.setVisibility(View.VISIBLE);
                        subject_to_teachEdit.setVisibility(View.VISIBLE);

                        break;
                    case 2:

                        qualificationLayout.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        program_of_studdyLayout.setVisibility(View.GONE);
                        program_of_studdyEdit.setVisibility(View.GONE);
                        subject_to_teachLayout.setVisibility(View.GONE);
                        subject_to_teachEdit.setVisibility(View.GONE);
                        skillLayout.setVisibility(View.VISIBLE);
                        skill_to_teachEdit.setVisibility(View.VISIBLE);
                        break;
                    case 3:

                        qualificationLayout.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        program_of_studdyLayout.setVisibility(View.VISIBLE);
                        program_of_studdyEdit.setVisibility(View.VISIBLE);
                        subject_to_teachLayout.setVisibility(View.VISIBLE);
                        subject_to_teachEdit.setVisibility(View.VISIBLE);
                        skillLayout.setVisibility(View.VISIBLE);
                        skill_to_teachEdit.setVisibility(View.VISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                qualificationLayout.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                program_of_studdyLayout.setVisibility(View.GONE);
                program_of_studdyEdit.setVisibility(View.GONE);
                subject_to_teachLayout.setVisibility(View.GONE);
                subject_to_teachEdit.setVisibility(View.GONE);
                skillLayout.setVisibility(View.GONE);
                skill_to_teachEdit.setVisibility(View.GONE);

            }
        });
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.custom_spinner_item,qualification );
//
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
//
//        spinner.setAdapter(spinnerArrayAdapter);

    }


    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainScroll = (ScrollView) findViewById(R.id.mainScroll);
        spinner = (Spinner) findViewById(R.id.qualification_spinner);
        genderSpinner = (Spinner) findViewById(R.id.gender_spinner);
        experienceSpinner = (Spinner) findViewById(R.id.experience_spinner);
        tutorTypyeSpinner = (Spinner) findViewById(R.id.tutorType_spinner);
        qualificationLayout = (LinearLayout) findViewById(R.id.qualificationLayout);
        program_of_studdyLayout = (LinearLayout) findViewById(R.id.program_of_studdyLayout);
        subject_to_teachLayout = (LinearLayout) findViewById(R.id.subject_to_teachLayout);
        subject_to_teachEdit = (EditText) findViewById(R.id.subject_to_teachEdit);
        program_of_studdyEdit = (EditText) findViewById(R.id.program_of_studdyEdit);
        skillLayout = (LinearLayout) findViewById(R.id.skillLayout);
        skill_to_teachEdit = (EditText) findViewById(R.id.skill_to_teachEdit);
        studentfnameEdit = (EditText) findViewById(R.id.studentfnameEdit);
        studentlnameEdit = (EditText) findViewById(R.id.studentlnameEdit);
        cityEdit = (EditText) findViewById(R.id.phoneEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        phoneEdit = (EditText) findViewById(R.id.phoneEdit);
        introductionEdit = (EditText) findViewById(R.id.introductionEdit);
        adviceEdit = (EditText) findViewById(R.id.adviceEdit);
        submitBtt = (Button) findViewById(R.id.submitBtn);
        dpIv = (ImageView) findViewById(R.id.dpIv);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @NonNull
    private void setValues() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Tutor Profile");
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
                        Log.e(TAG, "onDataChange: email:" + email + " current email:" + currentEmail);
                        if (email.equals(currentEmail)) {

                            studentfnameEdit.setText(postSnapshot.child("first_name").getValue().toString());
                            studentlnameEdit.setText(postSnapshot.child("last_name").getValue().toString());
                            emailEdit.setText(email);
                            emailEdit.setFocusable(false);
                            studentfnameEdit.setFocusable(false);
                            studentlnameEdit.setFocusable(false);

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


        qualification = new ArrayList();
        qualification.add("Select");
        qualification.add("Hafiz-e-Quran");
        qualification.add("Matric");
        qualification.add("Intermediate");
        qualification.add("Bechelor");
        qualification.add("Diploma & Certificates");
        qualification.add("Master");
        qualification.add("M.Phil");
        qualification.add("Ph.D");
        qualification.add("Others");
        qualificationAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = qualificationAdapter.getItem(position);
                Log.e(TAG, "getView: " + text);
                item.setText(text);
                return convertView;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spinner.setDropDownVerticalOffset(80);
        }

        spinner.setAdapter(qualificationAdapter);
        qualificationAdapter.setArray(qualification);

        gender = new ArrayList();
        gender.add("Select");
        gender.add("Female");
        gender.add("Male");
        gender.add("Others");

        populateAdapter(genderSpinner, gender);
        experience = new ArrayList();
        experience.add("No experience");
        experience.add("Less than a year");
        for (int i = 1; i < 20; i++)
            experience.add(i + " years");
        experienceAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = experienceAdapter.getItem(position);
                Log.e(TAG, "getView: " + text);
                item.setText(text);
                return convertView;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            experienceSpinner.setDropDownVerticalOffset(20);
        }

        experienceSpinner.setAdapter(experienceAdapter);
        experienceAdapter.setArray(experience);

        tutorType = new ArrayList();
        tutorType.add("Select Type");
        tutorType.add("Educational Teacher");
        tutorType.add("Skill Teacher");
        tutorType.add("Both");

        tutorTypeAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = tutorTypeAdapter.getItem(position);
                item.setText(text);
                return convertView;
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tutorTypyeSpinner.setDropDownVerticalOffset(20);
        }

        tutorTypyeSpinner.setAdapter(tutorTypeAdapter);
        tutorTypeAdapter.setArray(tutorType);

        tutorTypyeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        qualificationLayout.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        program_of_studdyLayout.setVisibility(View.VISIBLE);
                        program_of_studdyEdit.setVisibility(View.VISIBLE);
                        subject_to_teachLayout.setVisibility(View.VISIBLE);
                        subject_to_teachEdit.setVisibility(View.VISIBLE);

                        break;
                    case 2:

                        qualificationLayout.setVisibility(View.GONE);
                        spinner.setVisibility(View.GONE);
                        program_of_studdyLayout.setVisibility(View.GONE);
                        program_of_studdyEdit.setVisibility(View.GONE);
                        subject_to_teachLayout.setVisibility(View.GONE);
                        subject_to_teachEdit.setVisibility(View.GONE);
                        skillLayout.setVisibility(View.VISIBLE);
                        skill_to_teachEdit.setVisibility(View.VISIBLE);
                        break;
                    case 3:

                        qualificationLayout.setVisibility(View.VISIBLE);
                        spinner.setVisibility(View.VISIBLE);
                        program_of_studdyLayout.setVisibility(View.VISIBLE);
                        program_of_studdyEdit.setVisibility(View.VISIBLE);
                        subject_to_teachLayout.setVisibility(View.VISIBLE);
                        subject_to_teachEdit.setVisibility(View.VISIBLE);
                        skillLayout.setVisibility(View.VISIBLE);
                        skill_to_teachEdit.setVisibility(View.VISIBLE);

                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                qualificationLayout.setVisibility(View.GONE);
                spinner.setVisibility(View.GONE);
                program_of_studdyLayout.setVisibility(View.GONE);
                program_of_studdyEdit.setVisibility(View.GONE);
                subject_to_teachLayout.setVisibility(View.GONE);
                subject_to_teachEdit.setVisibility(View.GONE);
                skillLayout.setVisibility(View.GONE);
                skill_to_teachEdit.setVisibility(View.GONE);

            }
        });
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
//                this,R.layout.custom_spinner_item,qualification );
//
//        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spinner_item);
//
//        spinner.setAdapter(spinnerArrayAdapter);

    }

    private void populateAdapter(Spinner spin, ArrayList<String> spinlist) {
        arrayAdapter = new SimpleAdapter(TutorProfileActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_spinner_item, parent, false);
                TextView item = (TextView) convertView.findViewById(R.id.sp_item);
                String text = arrayAdapter.getItem(position);
                Log.e(TAG, "getView: " + text);
                item.setText(text);

                return convertView;
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            spin.setDropDownVerticalOffset(80);
        }

        spin.setAdapter(arrayAdapter);
        arrayAdapter.setArray(spinlist);
    }

    private void setOnClicks() {
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(TutorProfileActivity.this);
                return false;
            }
        });
        experienceSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(TutorProfileActivity.this);
                return false;
            }
        });
        genderSpinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                hideKeyboard(TutorProfileActivity.this);
                return false;
            }
        });
        submitBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write a message to the database
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
//                DatabaseReference myRef = database.getReference("message");
//                Log.e(TAG, "onClick: " + database);
//                myRef.setValue("Hello, World!");
//                Log.e(TAG, "onClick: " + myRef);

                if (studentfnameEdit.equals("") || studentfnameEdit == null || studentfnameEdit.length() == 0 ||
                        studentlnameEdit.equals("") || studentlnameEdit == null || studentlnameEdit.length() == 0 ||
                        cityEdit.equals("") || cityEdit == null || cityEdit.length() == 0 ||
                        skill_to_teachEdit.equals("") || skill_to_teachEdit == null || skill_to_teachEdit.length() == 0 ||
                        phoneEdit.equals("") || phoneEdit == null || skill_to_teachEdit.length() == 0 ||
                        emailEdit.equals("") || emailEdit == null || emailEdit.length() == 0 ||
                        introductionEdit.equals("") || introductionEdit == null || introductionEdit.length() == 0 ||
                        adviceEdit.equals("") || adviceEdit == null || adviceEdit.length() == 0 || extras == null) {
                    Log.e(TAG, "onClick: extras:" + extras);
                    showDialogs();

                } else {
                    addTutorProfile();
                }
            }
        });
        dpIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
            }
        });
    }

    private void addTutorProfile() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String currentU = mAuth.getCurrentUser().getEmail();
        currentU = currentU.replace(".", "");
        String key = mDatabase.child("tutors").push().getKey();
        TutorProfile post = new TutorProfile(currentU, studentfnameEdit.getText().toString(), studentlnameEdit.getText().toString(),
                genderSpinner.getSelectedItem().toString(), cityEdit.getText().toString(), 0, skill_to_teachEdit.getText().toString(),
                3, phoneEdit.getText().toString(), mAuth.getCurrentUser().getEmail(), "2 hourse", "morning",
                introductionEdit.getText().toString(), adviceEdit.getText().toString(), "null", "false", "0");
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();


        childUpdates.put("/tutors/" + currentU, postValues);
//        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        //save image
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

// Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(currentU + ".jpg");

// Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/" + currentU + ".jpg");

// While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
        baos = new ByteArrayOutputStream();
        newProfilePic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });

        Intent intent = new Intent(TutorProfileActivity.this, ViewProfileActivity.class);
        intent.putExtra("type", 0);
        intent.putExtra("email", mAuth.getCurrentUser().getEmail());
        intent.putExtra("Edit", "true");
        startActivity(intent);
        finish();
    }

    private void showDialogs() {
        adb = new AlertDialog.Builder(TutorProfileActivity.this);
        adb.setTitle("Input Error!");
        adb.setMessage("Please fill the form properly!");
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

}
