package com.example.pinme.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.example.pinme.model.entity.PinDetails


@Dao
interface DaoAccess {

    //Insert data
    @Insert
    fun insertPinData(pinDetails: PinDetails)

    //Fetch all datas
    @Query("SELECT * FROM PinDetails")
    fun fetchAllPinDetails(): List<PinDetails>

    //Delete all data
    @Query("DELETE FROM PinDetails")
    fun deleteAllPinDetails()



}