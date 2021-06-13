package com.example.sequence_1_rames_leo.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sequence_1_rames_leo.Activitys.MainActivity


@Database(entities = arrayOf(Liste::class), version = 1)
abstract class AppDatabase : RoomDatabase(){



    abstract fun ListeDAO() : ListDAO



}

