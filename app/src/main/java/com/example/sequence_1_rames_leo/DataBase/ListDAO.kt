package com.example.sequence_1_rames_leo.DataBase

import androidx.room.*
import retrofit2.http.DELETE

@Dao
interface ListDAO {

    @Query("SELECT * FROM Liste")
    suspend fun getAll(): List<Liste>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdate(Listes: List<Liste>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(liste : Liste)

    @Query("DELETE FROM Liste WHERE uid = :ListeID")
    suspend fun deleteItem(ListeID : Int): Int

    @Query("DELETE FROM Liste")
    suspend fun DELALL()

}