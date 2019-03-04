package teaching.tutor.education.tutor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.juanlabrador.badgecounter.BadgeCounter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import teaching.tutor.education.tutor.Adapters.GeneralAdapter;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.GeneralModel;

public class NotificationActivity extends AppCompatActivity {
    Toolbar toolbar;
    GeneralAdapter mAdapter;
    static FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String TAG = LauncherActivity.TAG;
    ListView notificationsLv;
    ArrayList<GeneralModel> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setViews();
        setRequests();
        setValues();
        setOnClicks();


    }

    private void setRequests() {

        Query query = mDatabase.child("applications").orderByChild("read_type");
        Log.e(TAG, "getView: query:" + query);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    GeneralModel object = new GeneralModel();
                    object.setStudentName(snapshot.child("teacherEmail").getValue().toString());
                    object.setStudentEmail(snapshot.child("studentEmail").getValue().toString());
                    object.setEmail(snapshot.child("teacherEmail").getValue().toString());
                    object.setApp_description(snapshot.child("appication_description").getValue().toString());
                    object.setRead_type(snapshot.child("read_type").getValue().toString());
                    object.setUid(snapshot.child("uid").getValue().toString());

                    Log.e(TAG, "onDataChange: app:" + snapshot);

                    if (snapshot.child("studentEmail").getValue().toString().equals(mAuth.getCurrentUser().getEmail())) {
                        Log.e(TAG, "onDataChange: read: " + snapshot.getRef().child("read_type").setValue("read"));
                        list.add(object);

                    }
                }
                Collections.reverse(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        notificationsLv = (ListView) findViewById(R.id.notificationsLv);
    }

    private void setValues() {
        setSupportActionBar(toolbar);
        toolbar.setTitle("Applications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAdapter = new GeneralAdapter(NotificationActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_notification_item, parent, false);
                TextView name = (TextView) convertView.findViewById(R.id.name_tv);
                TextView description = (TextView) convertView.findViewById(R.id.description_tv);
                final GeneralModel model = mAdapter.getItem(position);
                Log.e(TAG, "getView: student:"+model.getStudentName() );
                name.setText(model.getStudentName());
                description.setText(model.getApp_description());
                return convertView;
            }
        });

        mAdapter.setArray(list);
        notificationsLv.setAdapter(mAdapter);
        notificationsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final GeneralModel item = mAdapter.getItem(i);
                String name = item.getStudentName();
                String description = item.getApp_description();
                Intent intent = new Intent(NotificationActivity.this, ViewNotificationsActivity.class);
                intent.putExtra("teacherEmail", item.getEmail());
                intent.putExtra("name", name);
                intent.putExtra("description", description);
                intent.putExtra("uid", item.getUid());
                startActivity(intent);
            }
        });


    }

    private void setOnClicks() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_white_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        setRequests();
        mAdapter.setArray(list);
        notificationsLv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
