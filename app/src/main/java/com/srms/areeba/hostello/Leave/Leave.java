package com.srms.areeba.hostello.Leave;

import android.os.Parcel;
import android.os.Parcelable;

public class Leave implements Parcelable {
    private String name;
    private String reason;
    private String startDate;
    private String endDate;
    private int status;
    private long leaveTime;

    Leave(){

    }

    protected Leave(Parcel in) {
        name = in.readString();
        reason = in.readString();
        startDate = in.readString();
        endDate = in.readString();
        status = in.readInt();
        leaveTime = in.readLong();
    }

    public static final Creator<Leave> CREATOR = new Creator<Leave>() {
        @Override
        public Leave createFromParcel(Parcel in) {
            return new Leave(in);
        }

        @Override
        public Leave[] newArray(int size) {
            return new Leave[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Leave(String name, String reason, String startDate, String endDate, int status, long leaveTime) {
        this.name = name;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.leaveTime = leaveTime;
    }

    public Leave(String name, String reason, String startDate, String endDate) {
        this.name = name;
        this.reason = reason;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(reason);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(status);
        parcel.writeLong(leaveTime);
    }


}
