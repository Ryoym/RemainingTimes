package com.shima.remainingtimes

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Database(entities = [Schedule::class], version = 1, exportSchema = false)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun scheduleDao(): ScheduleDao

    companion object {
        @Volatile
        private var INSTANCE: ScheduleDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ScheduleDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ScheduleDatabase::class.java,
                    "schedule_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(ScheduleDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class ScheduleDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.scheduleDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(scheduleDao: ScheduleDao) {
            val date = Schedule(1, "Test", 10, 20, "test")
            scheduleDao.insert(date)
        }
    }
}
