package teaching.tutor.education.tutor.Utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import teaching.tutor.education.tutor.R;
import teaching.tutor.education.tutor.SignInActivity;

public class Dialogs {
    Context context;
    AlertDialog.Builder adb;

    public Dialogs(Context context) {
        this.context = context;
    }
    public void createDialog(int i){
        adb = new AlertDialog.Builder(context);
        adb.setTitle("Alert");
        adb.setIcon(R.drawable.ic_report_problem_black_24dp);
        switch (i){
            case 1:
                adb.setMessage("Your first name is empty.");
                break;
            case 2:
                adb.setMessage("Your Last name is empty.");
                break;
            case 3:
                adb.setMessage("Your email is empty.");
                break;
            case 4:
                adb.setMessage("Your password is empty.");
                break;
            case 5:
                adb.setMessage("Please fill the form properly to get yourself registered.");
                break;
            case 6:
                adb.setMessage("This user is not registered in our database.");
                break;
            case 7:
                adb.setMessage("Email or password is wrong.");
                break;
        }
        adb.setPositiveButton("Ok",null);
        adb.show();
    }
}
