package com.appdev.duoguidemo.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.esri.core.geometry.Geometry;
import com.esri.core.symbol.Symbol;

import java.util.List;

public class Mark implements Parcelable {
    private Integer  id;
    private String markName;
    private String markMemo;
    private Geometry geometry;
    private Symbol symbol;
    private String createPerson;
    private Long createDate;
    private String pointDrawableName; //暂时这么用于解决点标注的问题
    private List<Attachment> mAttachments;


    public Mark() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getMarkMemo() {
        return markMemo;
    }

    public void setMarkMemo(String markMemo) {
        this.markMemo = markMemo;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getPointDrawableName() {
        return pointDrawableName;
    }

    public void setPointDrawableName(String pointDrawableName) {
        this.pointDrawableName = pointDrawableName;
    }

    public List<Attachment> getmAttachments() {
        return mAttachments;
    }

    public void setmAttachments(List<Attachment> mAttachments) {
        this.mAttachments = mAttachments;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.markName);
        dest.writeString(this.markMemo);
        dest.writeSerializable(this.geometry);
        dest.writeSerializable(this.symbol);
        dest.writeString(this.createPerson);
        dest.writeValue(this.createDate);
        dest.writeString(this.pointDrawableName);
        dest.writeTypedList(this.mAttachments);
    }

    protected Mark(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.markName = in.readString();
        this.markMemo = in.readString();
        this.geometry = (Geometry) in.readSerializable();
        this.symbol = (Symbol) in.readSerializable();
        this.createPerson = in.readString();
        this.createDate = (Long) in.readValue(Long.class.getClassLoader());
        this.pointDrawableName = in.readString();
        this.mAttachments = in.createTypedArrayList(Attachment.CREATOR);
    }

    public static final Creator<Mark> CREATOR = new Creator<Mark>() {
        @Override
        public Mark createFromParcel(Parcel source) {
            return new Mark(source);
        }

        @Override
        public Mark[] newArray(int size) {
            return new Mark[size];
        }
    };
}
