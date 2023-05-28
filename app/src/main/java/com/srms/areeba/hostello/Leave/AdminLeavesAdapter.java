package com.srms.areeba.hostello.Leave;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.srms.areeba.hostello.R;
import com.srms.areeba.hostello.User.ProfileActivity;
import com.srms.areeba.hostello.User.UserClass;
import com.srms.areeba.hostello.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AdminLeavesAdapter extends BaseAdapter {

    private Context context;
    private LinearLayout profileLayout;
    private CheckBox approvedByHODCheckbox, approvedByWardenCheckbox;
    private Button approveLeaveButton, denyLeaveButton;
    private ArrayList<Leave> leaves;
    private ArrayList<String> keys;
    Calendar calendar;
    String time;
    FirebaseDatabase db;
    FirebaseAuth firebaseAuth;

    String date;

    public AdminLeavesAdapter(Context context, ArrayList<Leave> leaves, ArrayList<String> keys) {
        this.context = context;
        this.leaves = leaves;
        this.keys = keys;
        db = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public int getCount() {
        return leaves.size();
    }

    @Override
    public Object getItem(int i) {
        return leaves.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.status_item_admin, viewGroup, false);
        }

        calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm:ss a",Locale.US);
        final Leave leave = leaves.get(i);

        TextView complainType=view.findViewById(R.id.status_complain_name);
        TextView complainDate=view.findViewById(R.id.status_complain_date);
        TextView complainTitle=view.findViewById(R.id.status_complain_title);
        TextView complainStatus=view.findViewById(R.id.status_complain);
        TextView leaveType = view.findViewById(R.id.view_leave_type);
        ImageView profileImageView = view.findViewById(R.id.status_profile_pic);
        approvedByHODCheckbox = view.findViewById(R.id.hod_check);
        profileLayout = view.findViewById(R.id.status_profile_layout);
        approvedByWardenCheckbox = view.findViewById(R.id.warden_check);
        approveLeaveButton = view.findViewById(R.id.approve_leave_button);
        denyLeaveButton = view.findViewById(R.id.deny_leave_button);

        // setUserName(leave);

        DatabaseReference userReference2 = db.getReference("Users");
        String email2 = leave.getEmail().replaceAll("[.]", ",");
        userReference2.child(email2).get().addOnCompleteListener( task -> {
            if (task.isSuccessful()) {
                UserClass user = task.getResult().getValue(UserClass.class);
                complainType.setText(user.getName());
                String profilePic = user.getProfilePic();
                RequestOptions requestOption1 = new RequestOptions()
                        .placeholder(R.drawable.image_profile_pic)
                        .error(R.drawable.image_profile_pic)
                        .fitCenter();

                if(!profilePic.isEmpty()){
                    Glide.with(this.context)
                            .load(profilePic)
                            .apply(requestOption1)
                            .listener(new RequestListener<Drawable>() {

                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Toast.makeText(context,"Failed to load profile pic",Toast.LENGTH_SHORT).show();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(profileImageView);
                }
            } else {
                Toast.makeText(this.context, "Error fetching User details", Toast.LENGTH_SHORT).show();
            }
        });



        date=dateFormat.format(leave.getLeaveTime());
        time=timeFormat.format(leave.getLeaveTime());

        // complainType.setText(leave.getEmail());
        complainDate.setText(date);
        // complainType.setText(leaves.getComplainType());
        complainTitle.setText(leave.getReason());
        leaveType.setText(leave.getLeaveType());


        if(leave.getStatus()==1){
            complainStatus.setText("Approved");
            complainStatus.setBackgroundColor(context.getResources().getColor(R.color.green));

        }else if (leave.getStatus() == 0){
            complainStatus.setText("Pending");
            complainStatus.setBackgroundColor(context.getResources().getColor(R.color.golden));
        }else {
            complainStatus.setText("Denied");
            complainStatus.setBackgroundColor(context.getResources().getColor(R.color.red));
        }

        if (leave.isApprovedByHOD()) {
            approvedByHODCheckbox.setChecked(true);
        } else {
            approvedByHODCheckbox.setChecked(false);
        }
        if (leave.isApprovedByWarden()){
            approvedByWardenCheckbox.setChecked(true);
        } else {
            approvedByWardenCheckbox.setChecked(false);
        }

       approveLeaveButton.setOnClickListener( view1 -> {
           DatabaseReference leaveReference = db.getReference("Leave").child("Requests");
           DatabaseReference userReference = db.getReference("Users");
           FirebaseUser currentUser = firebaseAuth.getCurrentUser();
           String email = currentUser.getEmail().replaceAll("[.]", ",");
           userReference.child(email).get().addOnCompleteListener(task -> {
               if (task.isSuccessful()) {
                   UserClass user = task.getResult().getValue(UserClass.class);
                   if (user.getUserType().equals(Constants.HOD)) {
                       leave.setApprovedByHOD(true);
                       if (leave.isApprovedByWarden()) {
                           leave.setStatus(1);
                       }
                   }else if (user.getUserType().equals(Constants.WARDEN)) {
                       leave.setApprovedByWarden(true);
                       if (leave.isApprovedByHOD()) {
                           leave.setStatus(1);
                       }
                   } else {
                       Toast.makeText(context, "Invalid User!", Toast.LENGTH_SHORT).show();
                   }
                   leaveReference.child(keys.get(i)).setValue(leave).addOnCompleteListener(task1 -> {
                       Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
                   });
               } else {
                   Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
               }
           });
           //leaveReference.child(keys.get(i)).child("approvedByHOD").setValue(leave.isApprovedByHOD());
           //leaveReference.child(keys.get(i)).child("approvedByWarden").setValue(leave.isApprovedByWarden());
       });

        denyLeaveButton.setOnClickListener( view1 -> {
            DatabaseReference leaveReference = db.getReference("Leave").child("Requests");
            DatabaseReference userReference = db.getReference("Users");
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            String email = currentUser.getEmail().replaceAll("[.]", ",");
            userReference.child(email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserClass user = task.getResult().getValue(UserClass.class);
                    if (user.getUserType().equals(Constants.HOD)) {
                        leave.setApprovedByHOD(false);
                    }else if (user.getUserType().equals(Constants.WARDEN)) {
                        leave.setApprovedByWarden(false);
                    } else {
                        Toast.makeText(context, "Invalid User!", Toast.LENGTH_SHORT).show();
                    }
                    leave.setStatus(2);
                    leaveReference.child(keys.get(i)).setValue(leave).addOnCompleteListener(task1 -> {
                        Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
                    });
                } else {
                    Toast.makeText(context, "Error!", Toast.LENGTH_SHORT).show();
                }
            });
            //leaveReference.child(keys.get(i)).child("approvedByHOD").setValue(leave.isApprovedByHOD());
            //leaveReference.child(keys.get(i)).child("approvedByWarden").setValue(leave.isApprovedByWarden());
        });

        profileLayout.setOnClickListener(view1 -> {
            Intent profileIntent = new Intent(context.getApplicationContext(), ProfileActivity.class);
            profileIntent.putExtra("email", leave.getEmail());
            context.startActivity(profileIntent);
        });

        return view;
    }
}