package com.zoho.todo.appdatabase

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zoho.todo.appdatabase.dao.ToDoTaskDao
import com.zoho.todo.appdatabase.dao.TodoTaskRemoteKeysDao
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskEntity
import com.zoho.todo.appdatabase.databaseentity.ToDoTaskRemoteKeys
import java.util.Date




@Database(
    entities = [ToDoTaskRemoteKeys::class,ToDoTaskEntity::class],
    version = 1, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun toDoRemoteKeys():TodoTaskRemoteKeysDao

    abstract fun toDoTaskDao():ToDoTaskDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "ToDoDatabase.db"
            ).fallbackToDestructiveMigration().addCallback(
                object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        INSTANCE?.let { database -> }
                    }
                }
            ).build()
    }





}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}
