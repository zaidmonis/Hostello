package com.srms.areeba.hostello.Leave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.srms.areeba.hostello.Account.LoginActivity;
import com.srms.areeba.hostello.R;
import com.yalantis.taurus.PullToRefreshView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class LeaveStatusActivity extends AppCompatActivity {

    private ArrayList<Leave> leaves;
    private LeavesAdapter statusAdapter;
    private FirebaseUser statusUser;
    public static final int REFRESH_DELAY = 4000;
    private PullToRefreshView mPullToRefreshView;
    private ShimmerFrameLayout mShimmerViewContainer;
    DatabaseReference statusRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);

        ListView listView = findViewById(R.id.leave_status_listview);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        leaves = new ArrayList<>();

        mShimmerViewContainer.startShimmer();

        FirebaseAuth statusAuth = FirebaseAuth.getInstance();
        FirebaseDatabase statusInstance = FirebaseDatabase.getInstance();
        DatabaseReference rootStatusRef = statusInstance.getReference("Leave");
        statusUser=statusAuth.getCurrentUser();


        if(statusUser==null){
            startActivity(new Intent(LeaveStatusActivity.this, LoginActivity.class));
            finish();
        }


        statusAdapter = new LeavesAdapter(this, leaves);
        listView.setAdapter(statusAdapter);

        statusRef = rootStatusRef.child("Requests").getRef();

        statusRef.orderByChild("leaveTime").addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){
                    leaves.clear();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Log.d(TAG, "onDataChange: reached");
                        Leave leave = snapshot.getValue(Leave.class);

                        if(leave!=null){

                            if(Objects.requireNonNull(statusUser.getEmail()).equalsIgnoreCase("admin@srms.ac.in")){
                                leaves.add(leave);

                            }else{
                                if(leave.getEmail().equalsIgnoreCase(statusUser.getEmail())){
                                    leaves.add((leave));
                                }
                            }

                        }


                    }
                    Collections.reverse(leaves);
                    statusAdapter.notifyDataSetChanged();
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    mPullToRefreshView.setVisibility(View.VISIBLE);

                }else{
                    mShimmerViewContainer.stopShimmer();
                    mShimmerViewContainer.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                // Log.e(TAG, "Failed to read value.", databaseError.toException());
                mShimmerViewContainer.stopShimmer();
                mShimmerViewContainer.setVisibility(View.GONE);
            }
        });


        mPullToRefreshView = findViewById(R.id.pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });


    }

    protected void onResume() {
        super.onResume();

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        ActionBar statusActionBar = getSupportActionBar();
        assert statusActionBar != null;
        statusActionBar.setHomeButtonEnabled(true);
        statusActionBar.setDisplayHomeAsUpEnabled(true);
        statusActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5cae80")));
        statusActionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Leave Status</font>"));
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return true;

    }
}