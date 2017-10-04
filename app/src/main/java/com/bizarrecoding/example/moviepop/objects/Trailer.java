package com.bizarrecoding.example.moviepop.objects;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Herik on 30/9/2017.
 */

public class Trailer implements Parcelable {
    private String key;
    private String site;
    private String type;
    private String name;

    public Trailer(String key,String site,String type,String name){
        this.key = key;
        this.site = site;
        this.type = type;
        this.name = name;
    }


    private Trailer(Parcel in) {
        key = in.readString();
        site = in.readString();
        type = in.readString();
        name = in.readString();
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl(){
        if (site.equals("YouTube")) {
            return "https://www.youtube.com/watch?v=" + key;
        }else{
            return "";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(site);
        dest.writeString(type);
        dest.writeString(name);
    }
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
