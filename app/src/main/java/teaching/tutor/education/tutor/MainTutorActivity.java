package teaching.tutor.education.tutor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import teaching.tutor.education.tutor.Adapters.GeneralAdapter;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.GeneralModel;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class MainTutorActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    FirebaseAuth mAuth;

    String TAG = LauncherActivity.TAG;
    CoordinatorLayout fullLayout;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ListView offers;
    private SliderLayout mDemoSlider;
    GeneralAdapter mAdapter;
    DatabaseReference mDatabase;
    String email, name, namee = " ";
    Boolean profileCreated = false, stPofileCreated = false;
    View headerLayout;
    TextView headerName, headerEmail;
    Menu menu;
    MenuItem menuItem;
    SwipeRefreshLayout pullToReferesh;

    int postActive;
    ArrayList<GeneralModel> list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);
        mAuth = FirebaseAuth.getInstance();
        setViews();
        setValues();
        setActions();
        mAdapter.notifyDataSetChanged();


    }

    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fullLayout = (CoordinatorLayout) findViewById(R.id.fullLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        offers = (ListView) findViewById(R.id.tution_offers);
//        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_student_profile);
        headerLayout = navigationView.getHeaderView(0);
        headerName = (TextView) headerLayout.findViewById(R.id.headerName);
        headerEmail = (TextView) headerLayout.findViewById(R.id.headerEmail);
        pullToReferesh = (SwipeRefreshLayout) findViewById(R.id.pullToReferesh);

    }

    private void setValues() {


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Accounts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    namee = snapshot.child("first_name").getValue().toString()+" "+snapshot.child("last_name").getValue().toString();
//                    Log.e(TAG, "onDataChange: namee:"+namee +"current User:"+mAuth.getCurrentUser().getEmail());
                    if (snapshot.child("email").getValue().toString().equals(mAuth.getCurrentUser().getEmail())) {
                        namee = snapshot.child("first_name").getValue().toString() + " " + snapshot.child("last_name").getValue().toString();
                        Log.e(TAG, "onDataChange: namee:" + namee);
                        SharedPreffs.setSharedData(MainTutorActivity.this, "tName", namee);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query = mDatabase.child("tutors");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
                    if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
                        Log.e(TAG, "onDataChange: if" + postSnapshot.child("city").getValue().toString());
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                        email = postSnapshot.child("email").getValue().toString();
                        if (email.equals(currentUser.getEmail())) {
                            Log.e(TAG, "onDataChange: profile created: email:" + email + " and curentUser:" + currentUser.getEmail());

                            profileCreated = true;
                            break;

                        } else if (profileCreated != true) {
                            Log.e(TAG, "onDataChange: profile not created: email:" + email + " and curentUser:" + currentUser.getEmail());
                            profileCreated = false;
                        }
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
        mDatabase.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
                    if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                        String stEmail = postSnapshot.child("email").getValue().toString();
                        if (stEmail.equals(currentUser.getEmail())) {
                            Log.e(TAG, "onDataChange: profile created: email:" + stEmail + " and curentUser:" + currentUser.getEmail());

                            stPofileCreated = true;
                            break;

                        } else if (stPofileCreated != true) {
                            Log.e(TAG, "onDataChange: profile not created: email:" + stEmail + " and curentUser:" + currentUser.getEmail());
                            stPofileCreated = false;
                        }
                    } else {
                        Log.e(TAG, "onDataChange: else");
//                        st = false;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        //check profile
        mDatabase.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    // TODO: handle the post
//                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
//                        Log.e(TAG, "onDataChange: if" + postSnapshot.child("first_name").getValue().toString());
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
                    //don't need this code
//                        name = postSnapshot.child("first_name").getValue().toString();
                    postActive = Integer.valueOf(postSnapshot.child("active").getValue().toString());
                    Log.e(TAG, "onDataChange: active Post:" + postActive);
                    if (postActive == 1) {
                        GeneralModel object = new GeneralModel();
                        object.setStudentName(postSnapshot.child("name").getValue().toString());
                        object.setExplainOffer(postSnapshot.child("explainOffer").getValue().toString());
                        object.setFeesPerMonth(postSnapshot.child("fees_per_month").getValue().toString());
                        object.setMinQual(postSnapshot.child("min_qualification").getValue().toString());
                        object.setSubjectOrSkill(postSnapshot.child("subject_or_skill").getValue().toString());
                        object.setEmail(postSnapshot.child("email").getValue().toString());
                        object.setUid(postSnapshot.child("uid").getValue().toString());
//                        object.setStudentSubjectToStudy(postSnapshot.child("subject_to_study").getValue().toString());
//                        object.setStudentSeeking(postSnapshot.child("seeking").getValue().toString());
                        list.add(object);
                        Log.e(TAG, "setValues: size:" + list.size());
                        Log.e(TAG, "setValues: profile:" + profileCreated);
                    } else if (postActive == 0) {
                        postSnapshot.getRef().removeValue();
                    }
                    Collections.reverse(list);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                Log.e(TAG, "onDrawerSlide: ");
                fullLayout.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.setScrimColor(ContextCompat.getColor(MainTutorActivity.this, android.R.color.transparent));
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                headerName.setText(namee);
                headerEmail.setText(mAuth.getCurrentUser().getEmail());
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
//        ArrayList<OffersModel> list = new ArrayList();
//        for (int i = 0; i < 10; i++) {
//            OffersModel object = new OffersModel();
//            object.setStudentName("Shen Kareem");
//            object.setOfferDescription("I need a Android developer to teach me graphics. I want to take classes on weekends.");
//            object.setExpectedFees("5000/m");
//            object.setMinQualification("BSCS");
//            object.setSkills("Android knowledge");
//            list.add(object);
//            Log.e(TAG, "setValues: size:" + list.size());
//        }
        mAdapter = new GeneralAdapter(MainTutorActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custom_item_tution_offer, parent, false);
                final TextView studentName = (TextView) convertView.findViewById(R.id.student_name_tv);
                TextView description = (TextView) convertView.findViewById(R.id.description);
                TextView fees = (TextView) convertView.findViewById(R.id.expected_fees_tv);
                TextView minQualification = (TextView) convertView.findViewById(R.id.qualificaiton_tv);
                TextView skill = (TextView) convertView.findViewById(R.id.skill_tv);
                TextView applyTv = (TextView) convertView.findViewById(R.id.apply_tv);
                final GeneralModel model = mAdapter.getItem(position);
                studentName.setText(model.getStudentName());
                description.setText(model.getExplainOffer());
                fees.setText(model.getFeesPerMonth());
                minQualification.setText(model.getMinQual());
                skill.setText(String.valueOf(model.getSubjectOrSkill()));
                if (model.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                    Toast.makeText(MainTutorActivity.this, "Can't apply", Toast.LENGTH_SHORT).show();
                    applyTv.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(MainTutorActivity.this, "You can apply", Toast.LENGTH_SHORT).show();
                    applyTv.setVisibility(View.VISIBLE);
                    applyTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainTutorActivity.this, ApplyActivity.class);
                            Log.e(TAG, "onClick: position:"+position );
                            intent.putExtra("uid", model.getUid());
                            intent.putExtra("studentEmail", model.getEmail());
                            intent.putExtra("studentName", model.getStudentName());
                            SharedPreffs.setSharedData(MainTutorActivity.this, "description",model.getExplainOffer());
                            startActivity(intent);
                        }
                    });
                }

                return convertView;
            }
        });
        offers.setAdapter(mAdapter);
        mAdapter.setArray(list);
        mAdapter.notifyDataSetChanged();
        pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                pullToReferesh.setRefreshing(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Log.e(TAG, "onBackPressed: ");
//            super.onBackPressed();
            Log.e(TAG, "onBackPressed: ");
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tutor_profile) {
            // Handle the camera action
            if (profileCreated) {
                Log.e(TAG, "onNavigationItemSelected: profileCreated:" + profileCreated);
                Intent intent = new Intent(MainTutorActivity.this, ViewProfileActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
            } else {
                Log.e(TAG, "onNavigationItemSelected: profileCreated2:" + profileCreated);
                Intent intent = new Intent(MainTutorActivity.this, TutorProfileActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_student_profile) {
            if (stPofileCreated) {
                Log.e(TAG, "onNavigationItemSelected: stprofileCreated:" + stPofileCreated);
                Intent intent = new Intent(MainTutorActivity.this, ViewProfileActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            } else {
                Log.e(TAG, "onNavigationItemSelected: stPofileCreated2:" + stPofileCreated);
                Intent intent = new Intent(MainTutorActivity.this, StudentProfileActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }

        } else if (id == R.id.nav_advertise) {
            Intent intent = new Intent(MainTutorActivity.this, AdvertiseWithUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.post_tution_offer) {
            item.setTitle("Switch to Student");
            Intent intent = new Intent(MainTutorActivity.this, PostTutionOfferActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_switch) {
            Intent intent = new Intent(MainTutorActivity.this, MainActivity.class);
            startActivity(intent);
            SharedPreffs.setSharedData(MainTutorActivity.this, "main", "student");

            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent authIntent = new Intent(MainTutorActivity.this, VerifyPhoneActivity.class);
            startActivity(authIntent);
            finish();

        } else Toast.makeText(this, "User is already signed in", Toast.LENGTH_SHORT).show();
    }

    private void setActions() {
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
