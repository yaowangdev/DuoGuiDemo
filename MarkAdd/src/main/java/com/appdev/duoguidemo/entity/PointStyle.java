package com.appdev.duoguidemo.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class PointStyle implements Parcelable {

    private String url;
    private Boolean ifLocal;
    private String name ;
    private Bitmap bitmap;

    public PointStyle() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIfLocal() {
        return ifLocal;
    }

    public void setIfLocal(Boolean ifLocal) {
        this.ifLocal = ifLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeValue(this.ifLocal);
        dest.writeString(this.name);
        dest.writeParcelable(this.bitmap, flags);
    }

    protected PointStyle(Parcel in) {
        this.url = in.readString();
        this.ifLocal = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.name = in.readString();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator<PointStyle> CREATOR = new Parcelable.Creator<PointStyle>() {
        @Override
        public PointStyle createFromParcel(Parcel source) {
            return new PointStyle(source);
        }

        @Override
        public PointStyle[] newArray(int size) {
            return new PointStyle[size];
        }
    };
}
