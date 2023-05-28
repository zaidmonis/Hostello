package com.srms.areeba.hostello;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srms.areeba.hostello.Complaints.ComplainStatus;
import com.srms.areeba.hostello.Complaints.ComplaintsActivity;
import com.srms.areeba.hostello.Council.CouncilActivity;
import com.srms.areeba.hostello.Leave.AdminLeaveActivity;
import com.srms.areeba.hostello.Leave.LeaveActivity;
import com.srms.areeba.hostello.Mess.MessActivity;
import com.srms.areeba.hostello.Resources.ResourceActivity;
import com.srms.areeba.hostello.User.UserClass;
import com.srms.areeba.hostello.Utils.CommonFunctions;
import com.srms.areeba.hostello.Utils.Constants;

import java.util.Objects;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{

    private ImageButton heartRateImageBt;
    private ImageButton bloodPressureImageBt;
    private ImageButton leaveButton;
    private ImageButton emergencyConImageBt;
    LinearLayout viewMess, viewComplaints, viewLeave, viewContact;
    ProgressBar progressMain;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        findViewById(R.id.include_home).setVisibility(View.VISIBLE);

        CommonFunctions.setUser(this);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        homeInit();

        heartRateImageBt.setOnClickListener(this);
        bloodPressureImageBt.setOnClickListener(this);
        leaveButton.setOnClickListener(this);
        emergencyConImageBt.setOnClickListener(this);


    }

    private static long back_pressed;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back_pressed + 2000 > System.currentTimeMillis()){
                moveTaskToBack(true);            }
            else{
                Toast.makeText(getBaseContext(), "Press twice to exit", Toast.LENGTH_SHORT).show();
                back_pressed = System.currentTimeMillis();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return CommonFunctions.navigationItemSelect(item, this);

    }

    private void homeInit(){

        heartRateImageBt = findViewById(R.id.imagebt_hr);
        bloodPressureImageBt = findViewById(R.id.imagebt_bp);
        leaveButton = findViewById(R.id.leave_button);
        emergencyConImageBt = findViewById(R.id.image_ec);

        viewComplaints = findViewById(R.id.view_complaint);
        viewLeave = findViewById(R.id.view_leave);
        viewMess = findViewById(R.id.view_mess);
        viewContact = findViewById(R.id.view_contact);
        progressMain = findViewById(R.id.progress_main);


        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        DatabaseReference reference = db.getReference().child("Users");

        String email = currentUser.getEmail().replaceAll("[.]", ",");
        reference.child(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                progressMain.setVisibility(View.GONE);
                UserClass user = task.getResult().getValue(UserClass.class);
                if (user.getUserType().equals(Constants.HOD) || user.getUserType().equals(Constants.WARDEN) || user.getUserType().equals(Constants.STAFF)) {
                    viewContact.setVisibility(View.INVISIBLE);
                }
                if (user.getUserType().equals(Constants.HOD)) {
                    viewMess.setVisibility(View.INVISIBLE);
                }
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.imagebt_hr:
                startActivity(new Intent(HomeActivity.this,MessActivity.class));
                break;
            case R.id.imagebt_bp:
                openComplaintActivity();
                break;

            case R.id.leave_button: {
                openLeaveActivity();
                break;
            }
            case R.id.image_ec:
                startActivity(new Intent(HomeActivity.this,CouncilActivity.class));
                break;

            default:
                    startActivity(new Intent(HomeActivity.this,MessActivity.class));

        }

    }

    private void openLeaveActivity() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        DatabaseReference reference = db.getReference().child("Users");

        String email = currentUser.getEmail().replaceAll("[.]", ",");
        reference.child(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserClass user = task.getResult().getValue(UserClass.class);
                if (user.getUserType().equals(Constants.HOD) || user.getUserType().equals(Constants.WARDEN) || user.getUserType().equals(Constants.STAFF)) {
                    startActivity(new Intent(HomeActivity.this, AdminLeaveActivity.class));
                } else if (user.getUserType().equals(Constants.STUDENT)) {
                    startActivity(new Intent(HomeActivity.this, LeaveActivity.class));
                } else {
                    Toast.makeText(this, "Invalid User Type. Please contact administrator", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openComplaintActivity() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        DatabaseReference reference = db.getReference().child("Users");

        String email = currentUser.getEmail().replaceAll("[.]", ",");
        reference.child(email).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                UserClass user = task.getResult().getValue(UserClass.class);
                if (user.getUserType().equals(Constants.HOD) || user.getUserType().equals(Constants.WARDEN) || user.getUserType().equals(Constants.STAFF)) {
                    startActivity(new Intent(HomeActivity.this, ComplainStatus.class));
                } else if (user.getUserType().equals(Constants.STUDENT)) {
                    startActivity(new Intent(HomeActivity.this, ComplaintsActivity.class));
                } else {
                    Toast.makeText(this, "Invalid User Type. Please contact administrator", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
