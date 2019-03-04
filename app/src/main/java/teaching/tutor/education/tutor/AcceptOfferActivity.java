package teaching.tutor.education.tutor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import teaching.tutor.education.tutor.Adapters.GeneralAdapter;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.GeneralModel;

public class AcceptOfferActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    GeneralAdapter mAdapter;
    DatabaseReference mDatabase;
     String TAG = LauncherActivity.TAG;
    FirebaseAuth mAuth;
    ArrayList<GeneralModel> list = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_offer);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setViews();
        setRequests();
        setValues();

    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.acceptedOfferLv);

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

        mAdapter = new GeneralAdapter(AcceptOfferActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_notification_item, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.name_tv);
                TextView description = (TextView) convertView.findViewById(R.id.description_tv);
                final GeneralModel model = mAdapter.getItem(position);
                name.setText(model.getStudentName());
                description.setText(model.getApp_description());
                return convertView;
            }
        });

        mAdapter.setArray(list);
        listView.setAdapter(mAdapter);
    }

    private void setRequests() {


        //check profile
        mDatabase.child("Accepted_applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    GeneralModel object = new GeneralModel();
                    object.setStudentName(snapshot.child("teacherEmail").getValue().toString());
                    object.setStudentEmail(snapshot.child("studentEmail").getValue().toString());
                    object.setEmail(snapshot.child("teacherEmail").getValue().toString());
                    object.setApp_description(snapshot.child("appication_description").getValue().toString());
                    object.setUid(snapshot.child("uid").getValue().toString());

                    Log.e(TAG, "onDataChange: app:" + snapshot);

                    if (snapshot.child("teacherEmail").getValue().toString().equals(mAuth.getCurrentUser().getEmail())) {
                        list.add(object);

                    }
                }
                Collections.reverse(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }
}
