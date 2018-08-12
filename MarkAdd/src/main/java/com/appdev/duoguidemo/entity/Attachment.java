package com.appdev.duoguidemo.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class Attachment implements Parcelable {

    private int attachmentId;
    private String filePath;
    private String createDate;
    private String fileSize;
    private String fileName;
    private int fileType;


    public Attachment() {
    }

    public int getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(int attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.attachmentId);
        dest.writeString(this.filePath);
        dest.writeString(this.createDate);
        dest.writeString(this.fileSize);
        dest.writeString(this.fileName);
        dest.writeInt(this.fileType);
    }

    protected Attachment(Parcel in) {
        this.attachmentId = in.readInt();
        this.filePath = in.readString();
        this.createDate = in.readString();
        this.fileSize = in.readString();
        this.fileName = in.readString();
        this.fileType = in.readInt();
    }

    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
        @Override
        public Attachment createFromParcel(Parcel source) {
            return new Attachment(source);
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
        }
    };
}
