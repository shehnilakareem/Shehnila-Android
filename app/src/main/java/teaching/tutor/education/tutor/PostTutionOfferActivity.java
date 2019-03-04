package teaching.tutor.education.tutor;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import teaching.tutor.education.tutor.Database.Post;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class PostTutionOfferActivity extends AppCompatActivity {
    Toolbar toolbar;
    Button submitBtn,cancelBtn;
    EditText explainOffer_value, minQual_value,fees_value,subjectOrSkill_value;
    static FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    AlertDialog.Builder adb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_tution_offer);
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
                if (explainOffer_value.equals("") || explainOffer_value == null || explainOffer_value.length() == 0 ||
                        fees_value.equals("") || fees_value == null || fees_value.length() == 0 ||
                        subjectOrSkill_value.equals("") || subjectOrSkill_value == null || subjectOrSkill_value.length() == 0 ) {
                    showDialogs();

                } else {
                    addStudentProfile();
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
        adb = new AlertDialog.Builder(PostTutionOfferActivity.this);
        adb.setTitle("Input Error!");
        adb.setMessage("Please fill the form properly!");
        adb.setPositiveButton("Ok", null);
        adb.show();
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        explainOffer_value = (EditText) findViewById(R.id.explainOffer_value);
        minQual_value = (EditText) findViewById(R.id.qualificationEdit);
        fees_value = (EditText) findViewById(R.id.feesEdit);
        subjectOrSkill_value = (EditText) findViewById(R.id.subjectEdit);
        cancelBtn = (Button) findViewById(R.id.cancelBtn);
    }

    private void setValues() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Post an Offer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void addStudentProfile() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("Posts").push().getKey();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String email = currentUser.getEmail();
        String name = SharedPreffs.getSharedPrefferences(PostTutionOfferActivity.this,"tName");
        Random r = new Random();
        int i1 = r.nextInt(1000 - 0) + 0;
        Post post = new Post(""+i1,name,email,explainOffer_value.getText().toString(),minQual_value.getText().toString(),fees_value.getText().toString(),subjectOrSkill_value.getText().toString()) ;
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Posts/" + key, postValues);
//        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
        Intent intent = new Intent(PostTutionOfferActivity.this, MainTutorActivity.class);
        startActivity(intent);
        finish();
    }
}
