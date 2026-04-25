package com.app.neura.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.neura.data.local.dao.ChallengeDao
import com.app.neura.data.local.dao.PackDao
import com.app.neura.data.local.entity.ChallengeEntity
import com.app.neura.data.local.entity.PackEntity

@Database(
    entities = [ChallengeEntity::class, PackEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RoomConverters::class)
abstract class NeuraDatabase : RoomDatabase() {

    abstract fun challengeDao(): ChallengeDao
    abstract fun packDao(): PackDao

    companion object {
        @Volatile
        private var INSTANCE: NeuraDatabase? = null

        fun getInstance(context: Context): NeuraDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    NeuraDatabase::class.java,
                    "neura_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}