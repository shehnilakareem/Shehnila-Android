package teaching.tutor.education.tutor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import teaching.tutor.education.tutor.Utils.Dialogs;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class SignInActivity extends AppCompatActivity {
    TextView singuptxt;
    private FirebaseAuth mAuth;
    String TAG = LauncherActivity.TAG;
    TextInputEditText email,password;
    Button loginBtn;
    Dialogs dialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        setViews();
        setValues();
        setActions();
    }

    private void setViews() {
        email = (TextInputEditText) findViewById(R.id.emailEdit);
        password = (TextInputEditText) findViewById(R.id.passwordEdit);
        singuptxt = (TextView) findViewById(R.id.singuptxt);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    private void setValues() {
        dialogs = new Dialogs(SignInActivity.this);
    }

    private void setActions() {
        singuptxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,EmailVerification.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setChecks();
            }
        });
    }

    private void setChecks() {
        if ((email.getText().toString().isEmpty()||email.getText().toString().length()==0)
                || (password.getText().toString().isEmpty()||password.getText().toString().length()==0)){
            dialogs.createDialog(7);
        }else if ((!email.getText().toString().isEmpty()||email.getText().toString().length()!=0)
                && (!password.getText().toString().isEmpty()||password.getText().toString().length()!=0)){
            setRequest(email.getText().toString(),password.getText().toString());
        }
    }

    private void setRequest(String s, String s1) {
        mAuth.signInWithEmailAndPassword(s, s1)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Toast.makeText(SignInActivity.this, "SignIn Successful", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updatUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            dialogs.createDialog(6);
                        }

                        // ...
                    }
                });
    }

    private void updatUI(FirebaseUser currentUser) {
        if (currentUser==null){
        }else {
            SharedPreffs.setSharedData(SignInActivity.this,"email",currentUser.getEmail());
            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
