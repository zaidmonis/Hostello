package com.srms.areeba.hostello.Leave;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Leave implements Parcelable {
    private String email;
    private String reason;
    private String startDate;
    private String endDate;
    private int status;
    private long leaveTime;
    private String leaveType = "Short Leave";
    private boolean approvedByHOD;
    private boolean approvedByWarden;

    Leave(){

    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected Leave(Parcel in) {
        email = in.readString();
        reason = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        status = in.readInt();
        leaveType = in.readString();
        leaveTime = in.readLong();
        approvedByHOD = in.readBoolean();
        approvedByWarden = in.readBoolean();
    }

    public static final Creator<Leave> CREATOR = new Creator<Leave>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public Leave createFromParcel(Parcel in) {
            return new Leave(in);
        }

        @Override
        public Leave[] newArray(int size) {
            return new Leave[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isApprovedByHOD() {
        return approvedByHOD;
    }

    public void setApprovedByHOD(boolean approvedByHOD) {
        this.approvedByHOD = approvedByHOD;
    }

    public boolean isApprovedByWarden() {
            return approvedByWarden;
    }

    public void setApprovedByWarden(boolean approvedByWarden) {
        this.approvedByWarden = approvedByWarden;
    }

    public Leave(String email, String reason, String startDate, String endDate, int status, long leaveTime, String leaveType, boolean approvedByHOD, boolean approvedByWarden) {
        this.email = email;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.leaveTime = leaveTime;
        this.leaveType = leaveType;
        this.approvedByWarden = approvedByWarden;
        this.approvedByHOD = approvedByHOD;
    }

    public Leave(String email, String reason, String leaveType, String startDate, String endDate) {
        this.email = email;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.approvedByHOD = false;
        this.approvedByWarden = false;
        this.leaveType = leaveType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(reason);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(status);
        parcel.writeLong(leaveTime);
        parcel.writeString(leaveType);
        parcel.writeBoolean(approvedByHOD);
        parcel.writeBoolean(approvedByWarden);
    }


}
