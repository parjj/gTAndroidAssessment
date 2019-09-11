package com.example.pinme.model.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity (tableName = "PinDetails")
data class PinDetails(@PrimaryKey(autoGenerate = true) val id:Int,
                      @ColumnInfo(name = "Name")  val name:String,
                      @ColumnInfo(name = "Latitude")  val latitude:Double,
                      @ColumnInfo(name = "Longitude")  val longitude:Double,
                      @ColumnInfo(name = "Description")  val description:String) {



}