package com.bizarrecoding.example.moviepop.Objects;

import android.util.Log;

/**
 * Created by Herik on 30/9/2017.
 */

public class Trailer {
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
        Log.d("INTENT URL TYPE",type);
        if (site.equals("YouTube")) {
            Log.d("INTENT URL", "https://www.youtube.com/watch?v=" + key);
            return "https://www.youtube.com/watch?v=" + key;
        }else{
            Log.d("INTENT URL", "null");
            return "";
        }
    }
}
