package teaching.tutor.education.tutor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import teaching.tutor.education.tutor.Database.Account;
import teaching.tutor.education.tutor.Database.TutorProfile;
import teaching.tutor.education.tutor.Utils.Dialogs;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class EmailVerification extends AppCompatActivity {
    Toolbar toolbar;
    AlertDialog.Builder adb;
    TextInputEditText firstName, lastName, email, password;
    Button singupBtn;
    private FirebaseAuth mAuth;
    String TAG = LauncherActivity.TAG;
    Dialogs dialogs;
    TextView alreadyAccount;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.e(TAG, "onCreate: Verify Email" );
        setViews();
        setValues();
        setOnclicks();
    }

    private void setOnclicks() {
        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecks();
            }
        });
        alreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailVerification.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setChecks() {
        if ((firstName.getText().toString().isEmpty() || firstName.getText().toString().length() == 0)
                || (lastName.getText().toString().isEmpty() || lastName.getText().toString().length() == 0)
                || (email.getText().toString().isEmpty() || email.getText().toString().length() == 0)
                || (password.getText().toString().isEmpty() || password.getText().toString().length() == 0)) {
            dialogs.createDialog(5);
        } else if ((!firstName.getText().toString().isEmpty() || firstName.getText().toString().length() != 0)
                && (!lastName.getText().toString().isEmpty() || lastName.getText().toString().length() != 0)
                && (!email.getText().toString().isEmpty() || email.getText().toString().length() != 0)
                && (!password.getText().toString().isEmpty() || password.getText().toString().length() != 0)) {
            setRequest(email.getText().toString(), password.getText().toString());
        }
    }

    private void setRequest(String emails, String passwords) {
        mAuth.createUserWithEmailAndPassword(emails, passwords)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailVerification.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        firstName = (TextInputEditText) findViewById(R.id.nameEdit);
        lastName = (TextInputEditText) findViewById(R.id.lastnameEdit);
        email = (TextInputEditText) findViewById(R.id.emailEdit);
        password = (TextInputEditText) findViewById(R.id.passwordEdit);
        singupBtn = (Button) findViewById(R.id.singupBtn);
        alreadyAccount = (TextView) findViewById(R.id.already);

    }

    private void setValues() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign Up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        dialogs = new Dialogs(EmailVerification.this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
//            dialogs.createDialog(6);
        } else {
            SharedPreffs.setSharedData(EmailVerification.this,"email",currentUser.getEmail());
            addAccounts();
            Intent intent = new Intent(EmailVerification.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void addAccounts() {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("Accounts").push().getKey();
        Account post = new Account(email.getText().toString(),password.getText().toString(),firstName.getText().toString(),lastName.getText().toString());
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Accounts/" + key, postValues);
//        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);

        Intent intent = new Intent(EmailVerification.this, ViewProfileActivity.class);
        startActivity(intent);
    }
}
