package com.example.sequence_1_rames_leo.DataBase

import android.app.Application
import androidx.room.Room

class LocalDataSource(
    application: Application
) {
    private val roomDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "room-database").build()

    private val ListDao = roomDatabase.ListeDAO()

    suspend fun getALL() = ListDao.getAll()

    suspend fun InsertListe(liste : Liste) = ListDao.insertList( liste)

    suspend fun SaveOrUpdate(listes : List<Liste>) = ListDao.saveOrUpdate(listes)

    suspend fun Delete(ID : Int) = ListDao.deleteItem(ID)

    suspend fun DELALL() = ListDao.DELALL()
}