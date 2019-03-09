package com.example.viola.vintageviolet.favorite;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "favorites_table")
public class Style {
    @PrimaryKey
    @NonNull
    private int id;
    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "desc")
    private String desc;

    public Style() {
    }

    public Style(@NonNull int id, String url, String desc) {
        this.id = id;
        this.url = url;
        this.desc = desc;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



}
