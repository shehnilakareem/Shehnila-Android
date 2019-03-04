package teaching.tutor.education.tutor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseSignupMethodActivity extends AppCompatActivity {
    Button email,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_signup_method);
        setViews();
        setValues();
        setActions();
    }

    private void setViews() {
        email = (Button) findViewById(R.id.emailBtn);
        phone = (Button) findViewById(R.id.phoneBtn);
    }

    private void setValues() {
    }

    private void setActions() {
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseSignupMethodActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseSignupMethodActivity.this,VerifyPhoneActivity.class);
                startActivity(intent);
            }
        });
    }
}
