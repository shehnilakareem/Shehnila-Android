package teaching.tutor.education.tutor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

import teaching.tutor.education.tutor.Adapters.JSONAdapter;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.Favourites;
import teaching.tutor.education.tutor.Utils.GeneralModel;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

import static android.content.ContentValues.TAG;

public class FavouriteTutorsActivity extends AppCompatActivity {
    ListView favTutorlv;
    Toolbar toolbar;
    String jsonString;
    JSONAdapter mAdapter;
    String TAG = LauncherActivity.TAG;
    ArrayList<GeneralModel> list = new ArrayList();
    TextView tutorName;
    TextView citytv;
    TextView degree;
    TextView availibility;
    TextView star;
    ImageView phone;
    ImageView favourite;
    ImageView messagebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_tutors);
        setViews();
        try {
            setValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setOnClicks();
    }

    private void setOnClicks() {

    }

    private void setValues() throws JSONException {
        SharedPreffs sp = new SharedPreffs();
        jsonString = sp.getFavorites(FavouriteTutorsActivity.this, "favourites");
        Log.e(TAG, "setValues:JsonString " + jsonString);
        final JSONArray jsonArray = new JSONArray(jsonString);
        mAdapter = new JSONAdapter(FavouriteTutorsActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custome_teacher_item, parent, false);
                setCustomViews(convertView);

                try {

                    final JSONObject item = mAdapter.getItem(position);
                    final String name = item.getString("firstName");
                    final String city = item.getString("city");
                    String experience = item.getString("experience");
                    final String tutorDegree = item.getString("tutorDegree");
                    final String availableAt = item.getString("availableAt");
                    final String email = item.getString("email");
                    tutorName.setText(name);
                    citytv.setText(city);
                    degree.setText(tutorDegree);

                    phone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.e(TAG, "onClick: Phone button");
//                        Intent callIntent = new Intent(Intent.ACTION_CALL);
//                        callIntent.setData(Uri.parse("tel:" + model.getPhoneNum()));
//                        if (ActivityCompat.checkSelfPermission(SeeAllTutorsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                            // TODO: Consider calling
//                            //    ActivityCompat#requestPermissions
//                            // here to request the missing permissions, and then overriding
//                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                            //                                          int[] grantResults)
//                            // to handle the case where the user grants the permission. See the documentation
//                            // for ActivityCompat#requestPermissions for more details.
//                            ActivityCompat.requestPermissions(SeeAllTutorsActivity.this,
//                                    new String[]{Manifest.permission.CALL_PHONE},
//                                    1);
//                            return;
//                        } else {
//                            startActivity(callIntent);
//
//                        }
                        }
                    });
                    favourite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreffs sp = new SharedPreffs();
                            Favourites favourites = new Favourites();
                            favourites.setFirstName(name);
                            favourites.setCity(city);
                            favourites.setTutorDegree(tutorDegree);
                            favourites.setAvailableAt(availableAt);
                            favourites.setEmail(email);
                            sp.removeFavorite(FavouriteTutorsActivity.this, "favourites", favourites);
                            final JSONArray jsonArrayUpdated;
                            try {
                                favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                                jsonString = sp.getFavorites(FavouriteTutorsActivity.this, "favourites");
                                jsonArrayUpdated = new JSONArray(jsonString);
                                Log.e(TAG, "onClick: updated:"+jsonArrayUpdated );
                                mAdapter.setArray(jsonArrayUpdated);
                                mAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } catch (JSONException ex) {

                }
                return convertView;
            }
        });
        favTutorlv.setAdapter(mAdapter);
        mAdapter.setArray(jsonArray);
        mAdapter.notifyDataSetChanged();

    }

    private void setCustomViews(View convertView) {
        tutorName = (TextView) convertView.findViewById(R.id.tutor_name);
        citytv = (TextView) convertView.findViewById(R.id.residence);
        degree = (TextView) convertView.findViewById(R.id.tutor_degree);
        availibility = (TextView) convertView.findViewById(R.id.availability_time);
        phone = (ImageView) convertView.findViewById(R.id.phoneNum);
        messagebtn = (ImageView) convertView.findViewById(R.id.messagebtn);
        favourite = (ImageView) convertView.findViewById(R.id.favourite);

    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        favTutorlv = (ListView) findViewById(R.id.favTutorsLv);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Favourite Tutors");
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
}
