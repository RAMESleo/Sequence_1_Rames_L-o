package com.example.sequence_1_rames_leo.Autres

import android.os.Build
import androidx.annotation.RequiresApi

class ProfilListeToDo(Pseudo: String , ListeListe : ArrayList<ListeTodo>? = null , var  HASH : String? =null) {
    private var mesListesToDo: ArrayList<ListeTodo> = ArrayList()
    var pseudo : String
    lateinit var MdP : String

    init {
        pseudo = Pseudo
        if (ListeListe != null) {
            this.mesListesToDo = ListeListe
        }

    }

    fun SetLogin(s : String){
        this.pseudo = s
    }
    fun GetLogin(): String {
        return this.pseudo
    }

    fun SetMesListeToDo( Array : ArrayList<ListeTodo>){
        this.mesListesToDo = Array
    }

    fun GetMesListeToDo() : ArrayList<ListeTodo>{
        return this.mesListesToDo
    }

    fun AddListe( uneListe : ListeTodo){
        this.mesListesToDo.add(uneListe)
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun RemoveListe(Titre :String){
        mesListesToDo.removeIf { x -> x.GetTitre() == Titre }
    }








    fun pasDejaDedansListe(uneListe : String): Boolean {
        var compteur = 0
        for(k in this.mesListesToDo){
            if(uneListe == k.GetTitre()){
                compteur +=1
            }
        }
        return compteur == 0
    }

    override fun toString(): String {
        val toString = pseudo /* + "\n" +
                "Il y a les liste suivante: ");
        for(ListeTodo k : this.mesListesToDo){
            toString = toString + k.toString() + "\n";
        }*/
        return toString
    }


}