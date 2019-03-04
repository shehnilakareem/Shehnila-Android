package teaching.tutor.education.tutor;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.ui.auth.AuthUI;
import com.github.juanlabrador.badgecounter.BadgeCounter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teaching.tutor.education.tutor.Adapters.GeneralAdapter;
import teaching.tutor.education.tutor.Database.Ratings;
import teaching.tutor.education.tutor.Database.TutorProfile;
import teaching.tutor.education.tutor.Listeners.GeneralAdapterListener;
import teaching.tutor.education.tutor.Utils.Favourites;
import teaching.tutor.education.tutor.Utils.GeneralModel;
import teaching.tutor.education.tutor.Utils.SharedPreffs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, RatingDialogListener {
    static FirebaseAuth mAuth;
    String TAG = LauncherActivity.TAG;
    CoordinatorLayout fullLayout;
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView navigationView;
    ListView offers;
    private SliderLayout mDemoSlider;
    GeneralAdapter mAdapter;
    DatabaseReference mDatabase;
    boolean profileCreated, stPofileCreated, rated;
    View headerLayout;
    TextView headerName, headerEmail, viewall;
    ArrayList<GeneralModel> list = new ArrayList();
    String email = "", namee = " ", name = "", stEmail = "";
    Menu menu;
    MenuItem menuItem;
    SwipeRefreshLayout pullToReferesh;
    int count = 0, p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "onCreate: MainaActivity");
        mAuth = FirebaseAuth.getInstance();
//        if ()
        setViews();
        setValues();
        setActions();


    }


    private void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home - Student");
        viewall = (TextView) findViewById(R.id.viewall);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        fullLayout = (CoordinatorLayout) findViewById(R.id.fullLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        offers = (ListView) findViewById(R.id.tution_offers);
        menu = navigationView.getMenu();
        menuItem = menu.findItem(R.id.nav_tutor_profile);
        headerLayout = navigationView.getHeaderView(0);
        headerName = (TextView) headerLayout.findViewById(R.id.headerName);
        headerEmail = (TextView) headerLayout.findViewById(R.id.headerEmail);
        pullToReferesh = (SwipeRefreshLayout) findViewById(R.id.pullToReferesh);
//        to disable the tint
//        navigationView.setItemIconTintList(null);
//        menuItem.setCheckable(false);
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
                        SharedPreffs.setSharedData(MainActivity.this, "tName", namee);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //check profile
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
//                            break;

                        } else if (profileCreated != true) {
                            Log.e(TAG, "onDataChange: profile not created: email:" + email + " and curentUser:" + currentUser.getEmail());
                            profileCreated = false;
                        }
                        name = postSnapshot.child("first_name").getValue().toString();
                        GeneralModel object = new GeneralModel();
                        object.setTutorName(name + " " + postSnapshot.child("last_name").getValue().toString());
                        object.setTutorCity(postSnapshot.child("city").getValue().toString());
                        object.setAvailableTime(postSnapshot.child("available_at").getValue().toString());
                        object.setEmail(email);
                        object.setRatingStars(5);
                        object.setPhoneNum(postSnapshot.child("phone").getValue().toString());
                        object.setTutorDegree("Masters in Computer Science");
                        if (list.size() < 10) {
                            list.add(object);
                        }
                        Log.e(TAG, "setValues: size:" + list.size());
                        Log.e(TAG, "setValues: profile:" + profileCreated);
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
                        stEmail = postSnapshot.child("email").getValue().toString();
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

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                Log.e(TAG, "onDrawerSlide: ");
                fullLayout.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.setScrimColor(ContextCompat.getColor(MainActivity.this, android.R.color.transparent));
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                headerName.setText(namee);
                headerEmail.setText(mAuth.getCurrentUser().getEmail());
//                if (profileCreated)
//                    menuItem.setTitle("View Tutor Profile");
//                else
//                    menuItem.setTitle("Create Tutor Profile");
//
//                if (stPofileCreated)
//                    menuItem.setTitle("View Student Profile");
//                else
//                    menuItem.setTitle("Create Student Profile");


            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        Log.e(TAG, "setValues: profile created:" + SharedPreffs.getSharedPrefferences(MainActivity.this, "prof_created"));


        //Slider


        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal", R.drawable.pone);
        file_maps.put("Big Bang Theory", R.drawable.ptwo);
        file_maps.put("House of Cards", R.drawable.pthree);
        file_maps.put("Game of Thrones", R.drawable.pfour);

        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(MainActivity.this);

        mAdapter = new GeneralAdapter(MainActivity.this, new GeneralAdapterListener() {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                convertView = getLayoutInflater().inflate(R.layout.custome_teacher_item, parent, false);
                TextView tutorName = (TextView) convertView.findViewById(R.id.tutor_name);
                TextView city = (TextView) convertView.findViewById(R.id.residence);
                TextView degree = (TextView) convertView.findViewById(R.id.tutor_degree);
                TextView availibility = (TextView) convertView.findViewById(R.id.availability_time);
                final ImageView star = (ImageView) convertView.findViewById(R.id.ratings);
                ImageView messagebtn = (ImageView) convertView.findViewById(R.id.messagebtn);
                ImageView phone = (ImageView) convertView.findViewById(R.id.phoneNum);
                final ImageView favourite = (ImageView) convertView.findViewById(R.id.favourite);
                final GeneralModel model = mAdapter.getItem(position);
                tutorName.setText(model.getTutorName());
                city.setText(model.getTutorCity());
                degree.setText(model.getTutorDegree());
                availibility.setText("Available Time: " + model.getAvailableTime());
                SharedPreffs sp = new SharedPreffs();
                Log.e(TAG, "onClick: favourite:" + sp.getFavorites(MainActivity.this, "favourites"));

                if (sp.getFavorites(MainActivity.this, "favourites").contains(model.getEmail())) {
                    favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                } else {
                    favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                }

                phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + "03052306609"));
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CALL_PHONE},
                                    1);
                            return;
                        } else {
                            startActivity(callIntent);
                        }
                    }
                });
                favourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreffs sp = new SharedPreffs();
                        Log.e(TAG, "onClick: Toggle:" + SharedPreffs.getSharedPrefferences(MainActivity.this, "toggle"));
                        Favourites favourites = new Favourites();
                        favourites.setFirstName(model.getTutorName());
                        favourites.setCity(model.getTutorCity());
                        favourites.setTutorDegree(model.getTutorDegree());
                        favourites.setAvailableAt(model.getAvailableTime());
                        favourites.setEmail(model.email);
                        Log.e(TAG, "onClick: favourite:" + favourites.getEmail());
                        if (sp.getFavorites(MainActivity.this, "favourites").contains(model.getEmail())) {
                            favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                            sp.removeFavorite(MainActivity.this, "favourites", favourites);
                        } else if (!sp.getFavorites(MainActivity.this, "favourites").contains(model.getTutorName())) {
                            favourite.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
                            sp.saveFavorites(MainActivity.this, "favourites", favourites);
                        }
                    }
                });
                messagebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onShareClick(view);
                    }
                });
                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        showDialogRating(position);
//                        star.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_star_green_24dp, 0, 0, 0);
                        Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                        intent.putExtra("type", "0");
                        intent.putExtra("email", model.getEmail());
                        intent.putExtra("Edit", "false");
                        startActivity(intent);

                    }
                });

                return convertView;
            }
        });
        offers.setAdapter(mAdapter);
        mAdapter.setArray(list);
//        mAdapter.notifyDataSetChanged();
        pullToReferesh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                pullToReferesh.setRefreshing(false);
            }
        });

    }

    private void showDialogRating(int pos) {
        p = pos;
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNeutralButtonText("Later")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
                .setDefaultRating(2)
                .setTitle("Rate this application")
                .setDescription("Please select some stars and give your feedback")
                .setCommentInputEnabled(true)
                .setDefaultComment("This app is pretty cool !")
                .setStarColor(R.color.colorAccent)
                .setNoteDescriptionTextColor(R.color.design_default_color_primary)
                .setTitleTextColor(R.color.yellow)
                .setDescriptionTextColor(R.color.colorRed)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.blackcolor)
                .setCommentTextColor(R.color.blueish)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.MyDialogFadeAnimation)
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)

                .create(MainActivity.this)
                .show();
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
                    intent.putExtra("address", "03052306609");
                    intent.setType("vnd.android-dir/mms-sms");
                } else if (packageName.contains("whatsapp")) {
                    Log.e(TAG, "onShareClick: Whatsapp");
//                    intent.putExtra(Intent.EXTRA_TEXT, "Share whatsapp");
                    intent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
                    intent.putExtra("uid", PhoneNumberUtils.stripSeparators("03052306609") + "@s.whatsapp.net");//phone number without "+" prefix
                }

                intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
            }
        }

        // convert intentList to array
        LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
        openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
        startActivity(openInChooser);
    }

    private void setActions() {
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SeeAllTutorsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
            Log.e(TAG, "onBackPressed: ");
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Log.e(TAG, "onCreateOptionsMenu: ");

        // Create a condition (hide it if the count == 0)
//        if (mNotificationCounter > 0) {
        mDatabase.child("applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e(TAG, "onDataChange: children:" + dataSnapshot.getChildrenCount());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count = 0;
                    String email = snapshot.child("studentEmail").getValue().toString();
                    if (snapshot.child("studentEmail").getValue().toString().equals(mAuth.getCurrentUser().getEmail())) {
                        Log.e(TAG, "onDataChange: readType:" + snapshot.child("read_type").getValue().toString());
                        if (snapshot.child("read_type").getValue().toString().equals("unread")) {
                            count = count + 1;
                            Log.e(TAG, "onDataChange: count:" + count);
//                            createBadge(count);
                            return;
                        } else {
                            Log.e(TAG, "onDataChange: read count else");
                            count = 0;
                        }
                    }
                    BadgeCounter.update(MainActivity.this,
                            menu.findItem(R.id.action_settings),
                            R.drawable.ic_notifications_none_black_24dp,
                            BadgeCounter.BadgeColor.BLUE,
                            count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        } else {
//            BadgeCounter.hide(menu.findItem(R.id.notification));
//        }
        return true;
    }
//    public void createBadge(int count){
//        BadgeCounter.update(MainActivity.this,
//                menu.findItem(R.id.action_settings),
//                R.drawable.ic_notifications_none_black_24dp,
//                BadgeCounter.BadgeColor.BLUE,
//                8);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            BadgeCounter.update(item, count);
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mDatabase.child("applications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    count = 0;
                    if (snapshot.child("studentEmail").getValue().toString().equals(mAuth.getCurrentUser().getEmail())) {
                        Log.e(TAG, "onDataChange: readType:" + snapshot.child("read_type").getValue().toString());
                        if (snapshot.child("read_type").getValue().toString().equals("unread")) {
                            count = count + 1;
                            Log.e(TAG, "onDataChange: count:" + count);
                            Log.e(TAG, "onDataChange: count:" + count);
                        } else {
                            count = 0;
                        }
                    }
                    BadgeCounter.update(MainActivity.this,
                            menu.findItem(R.id.action_settings),
                            R.drawable.ic_notifications_none_black_24dp,
                            BadgeCounter.BadgeColor.BLUE,
                            count);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
        mAdapter.notifyDataSetChanged();

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
                Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                intent.putExtra("email", mAuth.getCurrentUser().getEmail());
                intent.putExtra("Edit", "true");
                intent.putExtra("type", 0);
                startActivity(intent);
            } else {
                Log.e(TAG, "onNavigationItemSelected: profileCreated2:" + profileCreated);
                Intent intent = new Intent(MainActivity.this, TutorProfileActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("Edit", "true");
                startActivity(intent);
            }

        } else if (id == R.id.nav_student_profile) {
            // Handle the camera action
            if (stPofileCreated) {
                Log.e(TAG, "onNavigationItemSelected: stprofileCreated:" + stPofileCreated);
                Intent intent = new Intent(MainActivity.this, ViewProfileActivity.class);
                intent.putExtra("email", mAuth.getCurrentUser().getEmail());
                intent.putExtra("type", 1);
                startActivity(intent);
            } else {
                Log.e(TAG, "onNavigationItemSelected: stPofileCreated2:" + stPofileCreated);
                Intent intent = new Intent(MainActivity.this, StudentProfileActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }

        } else if (id == R.id.nav_advertise) {
            Intent intent = new Intent(MainActivity.this, AdvertiseWithUsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_fav) {
            Intent intent = new Intent(MainActivity.this, FavouriteTutorsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.post_tution_offer) {
            Intent intent = new Intent(MainActivity.this, PostTutionOfferActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_switch) {

            Intent intent = new Intent(MainActivity.this, MainTutorActivity.class);
            startActivity(intent);
            SharedPreffs.setSharedData(MainActivity.this, "main", "teacher");
            finish();
        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_logout) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Log.e(TAG, "onComplete: logout completed");
                            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                            startActivity(intent);
                        }
                    });

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
            Intent authIntent = new Intent(MainActivity.this, VerifyPhoneActivity.class);
            startActivity(authIntent);
            finish();

        } else Toast.makeText(this, "User is already signed in", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onNegativeButtonClicked() {
        Log.e(TAG, "onNegativeButtonClicked: ");
    }

    @Override
    public void onNeutralButtonClicked() {
        Log.e(TAG, "onNeutralButtonClicked: ");

    }

    @Override
    public void onPositiveButtonClicked(int i, @NotNull String s) {
        Log.e(TAG, "onPositiveButtonClicked: position:" + p);
        GeneralModel item = list.get(p);
        String tEmail = item.getEmail();
        String ratingEmail = mAuth.getCurrentUser().getEmail();
        String selectedTuotor = list.get(p).getEmail();
        String dotReplaced = selectedTuotor.replace(".", "");

        String key = mDatabase.child("tutors").getRef().getKey();
        mDatabase.child("tutors").child(dotReplaced).child("stars").setValue("" + i);
        mDatabase.child("tutors").child(dotReplaced).child("ratingEmail").setValue(ratingEmail);
        mDatabase.child("tutors").child(dotReplaced).child("calculatedRating").setValue("0");

//        Query query = mDatabase.child("tutors");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                list.clear();
//                Log.e(TAG, "onDataChange: " + dataSnapshot.getChildrenCount());
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    // TODO: handle the post
//                    Log.e(TAG, "onDataChange: postSnapshot:" + postSnapshot.child("email").getValue());
//                    if (!postSnapshot.child("email").getValue().toString().isEmpty()) {
//                        FirebaseUser currentUser = mAuth.getCurrentUser();
//                        Log.e(TAG, "onDataChange: current user:" + currentUser.getEmail());
//                        email = postSnapshot.child("email").getValue().toString();
//                        if (email.equals(currentUser.getEmail())) {
//                            Log.e(TAG, "onDataChange: rating email:" + email + " and curentUser:" + currentUser.getEmail());
//
//                            rated = true;
//
////                            break;
//
//                        } else if (rated != true) {
//                            Log.e(TAG, "onDataChange: not created: Rating email:" + email + " and curentUser:" + currentUser.getEmail());
//                            rated = false;
//                        }
//                    } else {
//                        Log.e(TAG, "onDataChange: Rating emailelse");
////                        profileCreated = false;
//                    }
//
//                }
////                mAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        });
//        mDatabase.child("tutors").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e(TAG, "onDataChange: snapshotSingle:"+dataSnapshot );
//                dataSnapshot.getRef().child("ratingEmail").setValue(ratingEmail);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
////        float calculatedRating =
        Log.e(TAG, "onPositiveButtonClicked: rated:" + rated);
//        Ratings ratings = new Ratings("1",ratingEmail,""+i,"0",tEmail);
//        Map<String, Object> postValues = ratings.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/ratings/" + key, postValues);
////        childUpdates.put("/user-posts/" + 0 + "/" + key, postValues);
//
//        mDatabase.updateChildren(childUpdates);


    }
}
