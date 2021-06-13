package com.example.sequence_1_rames_leo.Autres

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import androidx.lifecycle.viewModelScope
import com.example.sequence_1_rames_leo.Activitys.MainActivity
import com.example.sequence_1_rames_leo.Activitys.MainActivity.Companion.GetItems
import com.example.sequence_1_rames_leo.DataBase.Liste
import com.example.sequence_1_rames_leo.DataBase.ListeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel (application: Application) : AndroidViewModel(application){

    private val listeRepository by lazy { ListeRepository.newInstance(application) }

    val Listes = MutableLiveData<ViewState>()

    fun getListes() {

        viewModelScope.launch {
            Listes.value = ViewState.Loading
            try {
                var listesgetted = listeRepository.getAll()
                Listes.value = ViewState.Content(Listes = listesgetted)

            } catch (e: Exception) {
                Listes.value = ViewState.Error(e.message.orEmpty())
                Log.i("TestSQL","in getListe:\t" + e.message.orEmpty())
            }

        }
    }

    fun addListe(liste : Liste){
        viewModelScope.launch {
            Listes.value = ViewState.Loading
            try {

                listeRepository.insertListe(liste)
            } catch (e: Exception) {
                Log.i("TestSQL", "in addListe:\t" +e.message.toString())
            }

        }
    }

    fun addListefromDataProvider(){
        viewModelScope.launch {
            Log.i("MAJ", "in parcours Liste")
            for (k in DataProvider.getList()) {
                Log.i("MAJ", k.GetTitre() + "\t" + k.id)
                var requete = MyAsyncTask()
                requete.execute(GetItems, k.GetTitre()).get()
                addListe(Liste(k.id.toString(), k.GetTitre(), "false", k.GetListeItem().toString()))
            }
        }
    }

    fun DeletewID(ID :Int){
        viewModelScope.launch {
            listeRepository.Delete(ID)
        }
    }

    fun DELALL(){
        viewModelScope.launch {
            listeRepository.DelAll()
        }
    }


    sealed class ViewState {
        object Loading : ViewState()
        data class Content(val Listes: List<Liste>) : ViewState()
        data class Error(val message: String) : ViewState()
    }
}