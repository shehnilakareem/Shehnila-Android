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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import teaching.tutor.education.tutor.Database.Advertisement;

public class AdvertiseWithUsActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name, instituteName, officialEmail, physicalAddress, enquiry;
    Button submit;
    AlertDialog.Builder adb;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertise_with_us);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setViews();
        setValues();
        setOnClicks();
    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (EditText) findViewById(R.id.nameEdit);
        instituteName = (EditText) findViewById(R.id.institutionEdit);
        officialEmail = (EditText) findViewById(R.id.emailEdit);
        physicalAddress = (EditText) findViewById(R.id.addressEdit);
        enquiry = (EditText) findViewById(R.id.enquiryEdit);
        submit = (Button) findViewById(R.id.submitBtn);
    }

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

    }

    private void setOnClicks() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.equals("") || name == null || name.length() == 0 ||
                        instituteName.equals("") || instituteName == null || instituteName.length() == 0 ||
                        officialEmail.equals("") || officialEmail == null || officialEmail.length() == 0 ||
                        physicalAddress.equals("") || physicalAddress == null || physicalAddress.length() == 0 ||
                        enquiry.equals("") || enquiry == null || enquiry.length() == 0) {
                    showDialogs(0);
                } else {
                    if (isValidEmail(officialEmail.getText()))
                        setRequest();
                    else showDialogs(1);
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void setRequest() {
        String key = mDatabase.child("Advertisement").push().getKey();

        Advertisement advertisement = new Advertisement(name.getText().toString(), instituteName.getText().toString(), officialEmail.getText().toString(), physicalAddress.getText().toString(), enquiry.getText().toString());
        Map<String, Object> postValues = advertisement.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Advertisement/" + key, postValues);
//        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
//        finish();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"shehnila.kanwal108@gmail.com"});
        email.putExtra(Intent.EXTRA_SUBJECT, "Advertisement Requests");
        String emailBody = "Name:    " + name.getText().toString() + "\n" + "Intitute name:    " + instituteName.getText().toString() + "\n" + "Official Email Address:  " + officialEmail.getText().toString() + "\n" + "Physical Address:  "
                + physicalAddress.getText().toString() + "\n" + "Enquiry:    " + enquiry.getText().toString();
        email.putExtra(Intent.EXTRA_TEXT, emailBody);

//need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void showDialogs(int i) {
        adb = new AlertDialog.Builder(AdvertiseWithUsActivity.this);
        switch (i) {
            case 0:
                adb.setTitle("Input Error!");
                adb.setMessage("Please fill the form properly!");
                break;
            case 1:
                adb.setTitle("Input Error!");
                adb.setMessage("Invalid email format!");
                officialEmail.requestFocus();
                break;
        }
        adb.setPositiveButton("Ok", null);
        adb.show();
    }
}
