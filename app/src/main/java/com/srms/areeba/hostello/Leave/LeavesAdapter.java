package com.srms.areeba.hostello.Leave;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.srms.areeba.hostello.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class LeavesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Leave> leaves;
    Calendar calendar;
    String time;

    String date;

    public LeavesAdapter(Context context, ArrayList<Leave> leaves) {
        this.context = context;
        this.leaves = leaves;
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
            view = LayoutInflater.from(context).inflate(R.layout.status_item, viewGroup, false);
        }

        calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        SimpleDateFormat timeFormat=new SimpleDateFormat("hh:mm:ss a",Locale.US);
        final Leave leave = leaves.get(i);

        TextView complainType=view.findViewById(R.id.status_complain_name);
        TextView complainDate=view.findViewById(R.id.status_complain_date);
        TextView complainTitle=view.findViewById(R.id.status_complain_title);
        TextView complainStatus=view.findViewById(R.id.status_complain);
        TextView complainDetails=view.findViewById(R.id.status_complain_details);
        TextView leaveType = view.findViewById(R.id.view_leave_type_student);



        date=dateFormat.format(leave.getLeaveTime());
        time=timeFormat.format(leave.getLeaveTime());

        complainType.setText(leave.getEmail());
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

        /* complainDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mcontext, StatusDetails.class);
                intent.putExtra("complain", complainList);
                mcontext.startActivity(intent);
            }
        }); */

        return view;
    }
}
