package com.khbtravel.ardianys.dapoor.pojo;

/**
 * Created by ardianys on 8/25/17.
 */
import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable
{

    private Integer id;
    private Integer position = 0;
    private String shortDescription;
    private String description;
    private String videoURL;
    private String thumbnailURL;

    public Step(Parcel in){
        id = in.readInt();
        position = in.readInt();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    public final static Parcelable.Creator<Step> CREATOR = new Creator<Step>() {

        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return (new Step[size]);
        }

    };

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        if (position == null) position = 0;
        dest.writeInt(position);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }


    public int describeContents() {
        return 0;
    }

}
