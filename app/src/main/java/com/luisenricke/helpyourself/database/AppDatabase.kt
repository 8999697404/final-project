package com.luisenricke.helpyourself.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.luisenricke.helpyourself.database.dao.AlertContactDAO
import com.luisenricke.helpyourself.database.dao.AlertDAO
import com.luisenricke.helpyourself.database.dao.ContactDAO
import com.luisenricke.helpyourself.database.entity.Alert
import com.luisenricke.helpyourself.database.entity.AlertContact
import com.luisenricke.helpyourself.database.entity.Contact
import com.luisenricke.room.converter.Date
import com.luisenricke.room.ioThread

@Database(entities = [Contact::class, Alert::class, AlertContact::class], version = 1, exportSchema = false)
@TypeConverters(Date::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun contactDAO(): ContactDAO

    abstract fun alertDAO(): AlertDAO

    abstract fun alertContactDAO(): AlertContactDAO

    companion object {
        private const val NAME = "Database.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: build(context).also { INSTANCE = it }
            }
        }

        private fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, NAME).addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    ioThread {

                    }
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)

                }
            }).allowMainThreadQueries().build()
        }
    }
}
