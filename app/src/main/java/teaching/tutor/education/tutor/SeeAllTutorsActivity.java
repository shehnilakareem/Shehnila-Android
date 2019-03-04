package teaching.tutor.education.tutor;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import teaching.tutor.education.tutor.Adapters.GeneralAdapter;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.Favourites;
import teaching.tutor.education.tutor.Utils.GeneralModel;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

import static android.content.ContentValues.TAG;

public class SeeAllTutorsActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView allTutorsLv;
    String TAG = LauncherActivity.TAG;

    GeneralAdapter mAdapter;
    static FirebaseAuth mAuth;

    DatabaseReference mDatabase;

    ArrayList<GeneralModel> list = new ArrayList();
    String email = "", namee = " ", name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_tutors);
        mAuth = FirebaseAuth.getInstance();

        setViews();
        setValues();
        setOnClicks();
    }

    private void setOnClicks() {
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

    private void setValues() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("tutors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChangeViewAll: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
                    if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
                        Log.e(TAG, "onDataChange: if" + postSnapshot.child("city").getValue().toString());
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                        email = postSnapshot.child("email").getValue().toString();
                        name = postSnapshot.child("first_name").getValue().toString();
                        GeneralModel object = new GeneralModel();
                        object.setTutorName(name + " " + postSnapshot.child("last_name").getValue().toString());
                        object.setTutorCity(postSnapshot.child("city").getValue().toString());
                        object.setEmail(email);
                        object.setAvailableTime(postSnapshot.child("available_at").getValue().toString());
                        object.setRatingStars(5);
                        object.setPhoneNum(postSnapshot.child("phone").getValue().toString());
                        object.setTutorDegree("Masters in Computer Science");
                        list.add(object);
                        Log.e(TAG, "setValues: size:" + list.size());

                    } else {
                        Log.e(TAG, "onDataChange: else");
//                        profileCreated = false;
                    }

                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        mAdapter = new GeneralAdapter(SeeAllTutorsActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custome_teacher_item, parent, false);
                TextView tutorName = (TextView) convertView.findViewById(R.id.tutor_name);
                TextView city = (TextView) convertView.findViewById(R.id.residence);
                TextView degree = (TextView) convertView.findViewById(R.id.tutor_degree);
                TextView availibility = (TextView) convertView.findViewById(R.id.availability_time);
                ImageView star = (ImageView) convertView.findViewById(R.id.ratings);
                ImageView phone = (ImageView) convertView.findViewById(R.id.phoneNum);
                ImageView messagebtn = (ImageView) convertView.findViewById(R.id.messagebtn);
                final ImageView favourite = (ImageView) convertView.findViewById(R.id.favourite);
                final GeneralModel model = mAdapter.getItem(position);
                tutorName.setText(model.getTutorName());
                city.setText(model.getTutorCity());
                degree.setText(model.getTutorDegree());
                availibility.setText("Available Time: " + model.getAvailableTime());
//                star.setText(String.valueOf(model.getRatingStars()));
                SharedPreffs sp = new SharedPreffs();
                Log.e(TAG, "onClick: favourite:" + sp.getFavorites(SeeAllTutorsActivity.this, "favourites"));

                if (sp.getFavorites(SeeAllTutorsActivity.this, "favourites").contains(model.getEmail())) {
                    favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                } else {
                    favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }
                //onClicks
                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e(TAG, "onClick: Phone button");
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + model.getPhoneNum()));
                        if (ActivityCompat.checkSelfPermission(SeeAllTutorsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            ActivityCompat.requestPermissions(SeeAllTutorsActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                            return;
                        } else {
                            startActivity(callIntent);

                        }


                    }
                });
                messagebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onShareClick(view);
//                        Intent send = new Intent("android.intent.action.MAIN");
//                        send.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
//                        send.putExtra("jid", PhoneNumberUtils.stripSeparators("03052306609") + "@s.whatsapp.net");//phone number without "+" prefix
//
//                        startActivity(send);
                    }
                });
                favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreffs sp = new SharedPreffs();
                        Log.e(TAG, "onClick: Toggle:" + SharedPreffs.getSharedPrefferences(SeeAllTutorsActivity.this, "toggle"));
                        Favourites favourites = new Favourites();
                        favourites.setFirstName(model.getTutorName());
                        favourites.setCity(model.getTutorCity());
                        favourites.setTutorDegree(model.getTutorDegree());
                        favourites.setAvailableAt(model.getAvailableTime());
                        favourites.setEmail(model.email);
                        Log.e(TAG, "onClick: favourite:" + favourites.getEmail());
                        if (sp.getFavorites(SeeAllTutorsActivity.this, "favourites").contains(model.getEmail())) {
                            favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                            sp.removeFavorite(SeeAllTutorsActivity.this, "favourites", favourites);
                        } else if (!sp.getFavorites(SeeAllTutorsActivity.this, "favourites").contains(model.getTutorName())) {

                            favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                            sp.saveFavorites(SeeAllTutorsActivity.this, "favourites", favourites);
                        }
                    }
                });
                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SeeAllTutorsActivity.this, ViewProfileActivity.class);
                        intent.putExtra("type", "0");
                        intent.putExtra("email", model.getEmail());
                        intent.putExtra("Edit", "false");
                        startActivity(intent);
                    }
                });

                return convertView;
            }
        });
        allTutorsLv.setAdapter(mAdapter);
        mAdapter.setArray(list);
        mAdapter.notifyDataSetChanged();


    }

    public void onShareClick(View v) {
        Resources resources = getResources();

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
                    intent.setType("vnd.android-dir/mms-sms");
                } else if (packageName.contains("whatsapp")) {
                    Log.e(TAG, "onShareClick: Whatsapp");
//                    intent.putExtra(Intent.EXTRA_TEXT, "Share whatsapp");
                    intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    intent.putExtra("jid", PhoneNumberUtils.stripSeparators("03052306609") + "@s.whatsapp.net");//phone number without "+" prefix

                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(SeeAllTutorsActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }


    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        allTutorsLv = (ListView) findViewById(R.id.allTutorsLv);

    }
}
