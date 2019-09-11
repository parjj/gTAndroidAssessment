package com.example.pinme.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.pinme.model.entity.PinDetails


@Database(entities = [PinDetails::class], version = 1,exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun dao(): DaoAccess
    companion object {

        private var db: LocalDatabase? = null

        fun getDB(context: Context): LocalDatabase? {

            if (db == null) {
                db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
            }
            return db
        }
    }
}