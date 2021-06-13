package com.example.sequence_1_rames_leo.DataBase

import android.app.Application

class   ListeRepository (
    private val localDataSource: LocalDataSource
        ){
    suspend fun getAll() : List<Liste>{
        return localDataSource.getALL()
    }

    suspend fun insertListe(liste : Liste) {
        localDataSource.InsertListe(liste)
    }

    suspend fun SaveOrUpdate(listes :List<Liste>){
        localDataSource.SaveOrUpdate(listes)
    }

    suspend fun  Delete(ID : Int){
        localDataSource.Delete(ID)
    }

    suspend fun DelAll(){
        localDataSource.DELALL()
    }


    companion object{
        fun newInstance(application : Application): ListeRepository{
            return ListeRepository(
                localDataSource = LocalDataSource(application)
            )
        }
    }
}