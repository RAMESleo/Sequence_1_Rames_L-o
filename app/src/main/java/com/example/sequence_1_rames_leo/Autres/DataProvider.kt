package com.example.sequence_1_rames_leo.Autres

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


object DataProvider {
    private val user = User("1")
    public var firstLog : Boolean = true
    public var isLOG : Boolean = false



    fun getUser(): User {
        return user
    }

    fun setHash(S : String){
        user.HASH = S
    }

    fun setList(liste: ArrayList<ListeTodo>){
        user.List = liste
    }

    fun getList(): ArrayList<ListeTodo> {
        return user.List
    }

    fun addList(Titre :String): Boolean {

        var newid : String = user.newid()
        return user.AddList( ListeTodo(Titre ,null , newid ))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun delList(Titre : String){
        user.DelList(Titre)
    }

    fun setPseudo(s :String){
        user.Pseudo = s
    }

    fun setMdP(s :String){
        user.MdP = s
    }

    fun getPseudo(): String {
        return user.Pseudo
    }

    fun getMdP(): String {
        return user.MdP
    }






    data class User(var  HASH : String){
        var List : ArrayList<ListeTodo> = ArrayList()
        lateinit var Pseudo : String
        lateinit var MdP : String

         var idListCourante : String = "1"


        private fun pasDejaDedansListe(uneListe : String): Boolean {
            var compteur = 0
            for(k in this.List){
                if(uneListe == k.GetTitre()){
                    compteur +=1
                }
            }
            return compteur == 0
        }

        fun AddList( uneListe : ListeTodo): Boolean {
            if (pasDejaDedansListe(uneListe.GetTitre())) {
                this.List.add(uneListe)
                return true
            }
            return false
        }

        private fun DejaDedansID(i: Int): Boolean {
            var compteur = 0
            for(k in this.List){
                if(i.toString() == k.getID()) {
                    compteur +=1
                }
            }
            return compteur != 0
        }

        fun newid(): String {
            var int = 0
            while(DejaDedansID(int)){
                int ++
            }
            return int.toString()
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun DelList(Titre: String) {

            if(!pasDejaDedansListe(Titre)){
                this.List.removeIf { x -> x.GetTitre() == Titre}
            }

        }

        fun getListToDo( Titre : String): ListeTodo {
            for(k in List){
                if(k.GetTitre() == Titre ){
                    return k
                }
            }
            return List[0]
        }





}
}



