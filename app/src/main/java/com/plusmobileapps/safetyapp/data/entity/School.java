package com.plusmobileapps.safetyapp.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by aaronmusengo on 2/7/18.
 */

@Entity(tableName = "schools")
public class School {
    @PrimaryKey(autoGenerate = false)
    private int schoolId;

    @ColumnInfo(name = "schoolName")
    private String schoolName;

    public School(int schoolId, String schoolName) {
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }

    //getters
    public int getSchoolId() { return this.schoolId; }
    public String getSchoolName() { return this.schoolName; }

    //setters
    public void setSchoolId(int schoolId) { this.schoolId = schoolId; }
    public void setSchoolName(String schoolName) { this.schoolName = schoolName;}
}