package com.example.sequence_1_rames_leo.Autres

class ItemToDo (titre : String , state : Boolean?){
    private lateinit var Titre: String
    private var State : Boolean = false


    init{
        this.Titre = titre
        if (state != null) {
            this.State = state
        }

    }

    fun SetTitre(s : String){
        this.Titre = s
    }
    fun GetTitre(): String {
        return this.Titre
    }

    fun SetState(b : Boolean){
        this.State = b
    }
    fun GetState(): Boolean {
        return this.State
    }

    fun SwitchState() {
        if(this.State){
            this.State = false
        }else{
            this.State = true
        }
    }


    override fun toString(): String {
        return "ItemToDo(Titre='$Titre', State=$State)"
    }


}