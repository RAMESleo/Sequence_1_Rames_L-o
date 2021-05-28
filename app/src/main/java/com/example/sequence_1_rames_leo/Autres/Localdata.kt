package com.example.sequence_1_rames_leo.Autres

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter

class Localdata(titre: String, chemin: File?) {

    private lateinit var DonneesJson : JSONObject
    private val fichier: File = File(chemin, "$titre.txt")
    private var ListeProfil : ArrayList<ProfilListeToDo> = ArrayList()
    init{
        fichier.createNewFile()
        /*val fileWrite = FileWriter(fichier, false)
        fileWrite.write(" ")
        fileWrite.close()*/
        Log.i("CAT", "lecture du fichier")
        val lecture = fichier.readText()
        if (lecture.length == 0){
            Log.i("CAT", "le fichier est vide")
        }else{
            //Log.i("CAT",lecture)
            //DonneesJson = JSONObject(lecture)
            val gson = GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create()



            DonneesJson = JSONObject(lecture)
            //val S=gson.toJson(DonneesJson)
            Log.i("CAT",DonneesJson.toString())

            // 2) récupérer le tableau des profs
            val Profils: JSONArray = DonneesJson.getJSONArray("Profils")
            // 3) parcourir le tableau
            for (i in 0..(Profils.length()-1)) {
                val nextP = Profils.getJSONObject(i)

                // afficher le prénom de chaque prof

                // afficher le prénom de chaque prof
                Log.i("CAT", "pseudo:" + nextP.getString("pseudo"))
                Log.i("CAT", "listes:" + nextP.getString("mesListesToDo"))
                var ListeArray = nextP.getJSONArray("mesListesToDo")
                var ListeListe : ArrayList<ListeTodo> = ArrayList()

                Log.i("CAT", " listes Array:" + ListeArray.toString())
                for(j in 0..(ListeArray.length() - 1)){

                    val nextL = ListeArray.getJSONObject(j)




                    var ItemArray = nextL.getJSONArray("ListeItemsToDo")
                    var ItemListe : ArrayList<ItemToDo> = ArrayList()
                    for(k in 0..(ItemArray.length()-1)){
                        val nextI = ItemArray.getJSONObject(k)
                        //Log.i("CAT","titre item : ${nextI.getString("Titre")} et son état : ${nextI.getBoolean("State")}")
                        ItemListe.add(ItemToDo(nextI.getString("Titre"),nextI.getBoolean("State")))
                    }




                    ListeListe.add(ListeTodo(nextL.getString("Titre"),ItemListe))

                }
                this.ListeProfil.add(ProfilListeToDo(nextP.getString("pseudo"),ListeListe))
            }
        }

    }

    fun MAJinfos(){
        var S : String
        val fileWrite = FileWriter(fichier, false)
        val gson = GsonBuilder()
            .serializeNulls()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create()
        S=gson.toJson(this.ListeProfil)
        Log.i("CAT", "{\"Profils\" : $S }")
        fileWrite.write("{\"Profils\" : $S }")
        fileWrite.close()
    }


    fun addProfil(s: String){
        if(this.pasdejadedansLog(s))
        {
            this.ListeProfil.add(ProfilListeToDo(s, null))
        }
    }

    fun addListe(newListe: String, ProfilToAdd: String): Boolean {
        Log.i("CAT", "liste à ajouter : $newListe au profil $ProfilToAdd")
        for(k in this.ListeProfil){
            if(k.GetLogin() == ProfilToAdd){
                if(k.pasDejaDedansListe(newListe)){
                k.AddListe(ListeTodo(newListe, null))
                this.MAJinfos()
                return true}
                else return false
            }

        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun RemoveListe(newListe: String, ProfilToAdd: String): Boolean {
        Log.i("CAT", "liste à ajouter : $newListe au profil $ProfilToAdd")
        for(k in this.ListeProfil){
            if(k.GetLogin() == ProfilToAdd){
                if(!k.pasDejaDedansListe(newListe)){
                    k.RemoveListe(newListe)
                    this.MAJinfos()
                    return true}
                else return false
            }

        }
        return false
    }


    fun addItem(newItem: String, ListeToAdd: String, ProfilToAdd: String): Boolean {
        Log.i("CAT", "Item à ajouter : $newItem à la liste : $ListeToAdd au profil $ProfilToAdd")
        for(k in this.ListeProfil){
            Log.i("CAT","dans le premier for")
            if(k.GetLogin() == ProfilToAdd){
                Log.i("CAT","le login a ${k.GetLogin()}")
                for(j in k.GetMesListeToDo()){
                    if(j.GetTitre() == ListeToAdd){
                    Log.i("CAT","la liste ${j.GetTitre()}")
                    if(j.pasDejaDedansItem(newItem)) {

                        j.AddItem(ItemToDo(newItem, false))
                        this.MAJinfos()
                        return true
                    }
                    }
                }
            }
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun RemoveItem(newItem: String, ListeToAdd: String, ProfilToAdd: String): Boolean {
        Log.i("CAT", "Item à enlever : $newItem à la liste : $ListeToAdd au profil $ProfilToAdd")
        for(k in this.ListeProfil){
            //Log.i("CAT","dans le premier for")
            if(k.GetLogin() == ProfilToAdd){
                //Log.i("CAT","le login a été trouvé")
                for(j in k.GetMesListeToDo()){
                    //Log.i("CAT","dans le deuxieme for")
                    if(j.GetTitre() == ListeToAdd)
                    if(!j.pasDejaDedansItem(newItem)){

                        j.RemoveItem(newItem)
                        this.MAJinfos()
                        return true
                    }
                }
            }
        }
        return false
    }

    fun getProfil(ProfilName: String): ProfilListeToDo {
        for(k in this.ListeProfil) if(k.GetLogin() == ProfilName){
            return k
        }
        return this.ListeProfil[0]
        }

    fun getListe(ProfilName: String, ListeName: String): ListeTodo {

        var Profil = this.getProfil(ProfilName)
        for(k in Profil.GetMesListeToDo()) if(k.GetTitre() == ListeName){
            return k
        }
        return Profil.GetMesListeToDo()[0]
    }


    private fun pasdejadedansLog(newLogin: String): Boolean {
        if(this.ListeProfil.isEmpty()){
            return true
        }
        var compteur = 0
        for (k in this.ListeProfil) {
            if (k.GetLogin() == newLogin) {
                compteur += 1
            }
        }
        return compteur == 0
    }


}