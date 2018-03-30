package com.huazong.app.huazong.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class PhotoInfo implements Parcelable {
    private String id;
    private String name;
    private String path;
    private long size;
    private String date;
    private boolean check;

    public PhotoInfo(String id, String name, String path, long size, String date) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.size = size;
        this.date = date;
    }

    public PhotoInfo(String path, String date) {
        this.path = path;
        this.date = date;
    }

    public PhotoInfo() {
    }

    protected PhotoInfo(Parcel in) {
        id = in.readString();
        name = in.readString();
        path = in.readString();
        size = in.readLong();
        date = in.readString();
        check = in.readByte() != 0;
    }

    public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
        @Override
        public PhotoInfo createFromParcel(Parcel in) {
            return new PhotoInfo(in);
        }

        @Override
        public PhotoInfo[] newArray(int size) {
            return new PhotoInfo[size];
        }
    };

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(path);
        dest.writeLong(size);
        dest.writeString(date);
        dest.writeByte((byte) (check ? 1 : 0));
    }
}
