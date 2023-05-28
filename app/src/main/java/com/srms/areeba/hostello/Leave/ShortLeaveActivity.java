package com.srms.areeba.hostello.Leave;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.srms.areeba.hostello.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShortLeaveActivity extends AppCompatActivity {

    EditText editShortName, editShortReason, editStartDate, editEndDate;
    Spinner leaveTypeSpinner;
    Button submitButton;
    private Calendar myCalendar;
    private DatabaseReference leaveReference, leaveRootReference;
    private FirebaseUser user;
    private ProgressDialog complainProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_leave);

        initShortLeaveActivity();

    }

    private void initShortLeaveActivity() {
        editShortName = findViewById(R.id.edit_short_name);
        editShortReason = findViewById(R.id.edit_short_reason);
        editStartDate = findViewById(R.id.edit_start_date);
        editEndDate = findViewById(R.id.edit_end_date);
        submitButton = findViewById(R.id.leave_submit_button);
        leaveTypeSpinner = findViewById(R.id.pro_edit_leave_type);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        editShortName.setText(user.getEmail());
        editShortName.setFocusable(false);

        FirebaseDatabase leaveInstance = FirebaseDatabase.getInstance();
        leaveRootReference= leaveInstance.getReference("Leave");


        myCalendar =Calendar.getInstance();
        editStartDate.setOnClickListener(v -> new DatePickerDialog(ShortLeaveActivity.this, startDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        editEndDate.setOnClickListener(v -> new DatePickerDialog(ShortLeaveActivity.this, endDate, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Leave shortLeave = new Leave(editShortName.getText().toString(),
                        editShortReason.getText().toString(),
                        leaveTypeSpinner.getSelectedItem().toString(),
                        editStartDate.getText().toString(),
                        editEndDate.getText().toString());

                leaveReference = leaveRootReference.child("Requests").push().getRef();

                leaveReference.setValue(shortLeave).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            leaveReference.child("leaveTime").setValue(ServerValue.TIMESTAMP);
                            // hideProgressDialog();
                            Toast.makeText(getApplicationContext(),"Complaint added.",Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(ShortLeaveActivity.this, ComplaintsActivity.class));

                        }else
                        {
                            // hideProgressDialog();
                            Toast.makeText(getApplicationContext(),"Failed to add Complaint.",Toast.LENGTH_SHORT).show();
                            // startActivity(new Intent(ComplainRegister.this,ComplaintsActivity.class));


                        }
                    }
                });
            }
        });

    }

    final DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateStartDate();
        }

    };

    final DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateEndDate();
        }

    };

    private void updateStartDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndDate.setText(sdf.format(myCalendar.getTime()));
    }

    private boolean isNetWorkAvailable(){
        ConnectivityManager connectivityManager =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo= connectivityManager.getActiveNetworkInfo();
        return networkInfo !=null && networkInfo.isConnected();
    }
}