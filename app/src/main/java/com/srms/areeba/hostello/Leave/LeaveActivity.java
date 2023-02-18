package com.srms.areeba.hostello.Leave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.srms.areeba.hostello.R;

public class LeaveActivity extends AppCompatActivity {


    Button viewLeaves, addLeave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);
        viewLeaves = findViewById(R.id.view_leaves);
        addLeave = findViewById(R.id.add_leave_button);

        viewLeaves.setOnClickListener(view -> {
            startActivity(new Intent(LeaveActivity.this, LeaveStatusActivity.class));
        });

        addLeave.setOnClickListener(view -> startActivity(new Intent(LeaveActivity.this, ShortLeaveActivity.class)));
    }
}