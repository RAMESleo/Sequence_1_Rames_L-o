package com.example.sequence_1_rames_leo.DataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Liste(@PrimaryKey val uid: String,
                @ColumnInfo(name = "Titre_List") val Titre_List: String?,
                @ColumnInfo(name = "modifie") val modifie: String?,
                @ColumnInfo(name = "ListItems") val ListItem: String?
    )
