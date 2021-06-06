package com.example.sequence_1_rames_leo.Autres

import android.os.Build
import androidx.annotation.RequiresApi

class ListeTodo(titre: String, ListeItem: ArrayList<ItemToDo>?, var id: String? = null) {
    private var Titre : String
    private var ListeItemsToDo :ArrayList<ItemToDo> = ArrayList()

    init{
        this.Titre = titre
        if (ListeItem != null) {
            this.ListeItemsToDo = ListeItem
        }
    }

    fun SetTitre(s : String){
        this.Titre = s
    }
    fun GetTitre(): String {
        return this.Titre
    }

    fun SetListeItem(Array : ArrayList<ItemToDo>){
        this.ListeItemsToDo = Array
    }

    fun GetListeItem(): ArrayList<ItemToDo> {
        return this.ListeItemsToDo
    }

    fun pasDejaDedansItem(TitreItem : String): Boolean {
        var compteur = 0
        for(k in this.ListeItemsToDo){
            if(k.GetTitre() == TitreItem) compteur += 1
        }
        return compteur ==0
    }

    fun AddItem(newItem: ItemToDo): Boolean {
        if(pasDejaDedansItem(newItem.GetTitre())) {
            this.ListeItemsToDo.add(newItem)
            return true
        }
        return false
    }
    @RequiresApi(Build.VERSION_CODES.N)
    fun RemoveItem(Titre :String){
        ListeItemsToDo.removeIf { x -> x.GetTitre() == Titre}

    }
    fun getID(): String? {
        return this.id
    }

    fun GetItemToDo(Titre: String): ItemToDo {
        for(k in ListeItemsToDo){
            if(k.GetTitre() == Titre ){
                return k
            }
        }
        return ListeItemsToDo[0]
    }

    override fun toString()  : String{
        return this.Titre;
    }
}