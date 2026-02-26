package ir.unalzo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.unalzo.database.dao.TaskDao
import ir.unalzo.database.entities.TaskEntity


@Database(
    entities = [TaskEntity::class ],
    version = 2
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao(): TaskDao
}