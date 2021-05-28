package com.example.sequence_1_rames_leo.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequence_1_rames_leo.*
import com.example.sequence_1_rames_leo.Autres.ListeTodo
import com.example.sequence_1_rames_leo.Autres.postAdapter

class ChoixListeActivity : AppCompatActivity()  , postAdapter.OnItemClickListener {
    private lateinit var Titre: TextView
    private lateinit var ajoutListe: EditText
    private lateinit var adBtn: ImageButton
    private lateinit var PseudoCourant : String
    private lateinit var ListeListe : ArrayList<ListeTodo>
    private lateinit var recyclerview : RecyclerView
    private lateinit var trashToolbar : ImageView
    private var isLongClicked : ArrayList<Int> = ArrayList()
    private lateinit var menuImage :ImageView
    private lateinit var menuLayout : RelativeLayout

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listactivity)



        if (intent.hasExtra("PseudoAdded")){
            PseudoCourant = intent.getStringExtra("PseudoAdded").toString()
        }
         this.ListeListe = mesDonnees.getProfil(PseudoCourant).GetMesListeToDo()


        recyclerview = findViewById<RecyclerView>(R.id.ListeListe)

        Titre = findViewById(R.id.TitreToolBar)
        Titre.setText("Login : $PseudoCourant")
        ajoutListe = findViewById(R.id.AddListe)
        adBtn = findViewById(R.id.addButton)
        trashToolbar = findViewById(R.id.TrashToolBar)
        menuImage = findViewById(R.id.MenuIm)
        menuLayout =findViewById(R.id.MenuLayout)
        menuLayout.isVisible = false


        Log.d("PostAdapter", ListeListe.toString())
        recyclerview.adapter = postAdapter(ListeListe, this)
        recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)




        }
    override fun onResume(){
        super.onResume()
        menuLayout.isVisible = false

    }



    fun switchTashVisibility(){
        if (trashToolbar.isVisible){
            trashToolbar.isVisible = false
        } else{
            trashToolbar.isVisible = true
        }
    }
    fun isInList(pos : Int):Boolean{
        var compteur = 0
        for(k in isLongClicked){
            if(k == pos){
                compteur+=1
            }
        }
        return compteur!=0
    }


    private fun alerter(s: String) {

        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()

    }
    private fun MAJAffichage(){

        ListeListe = mesDonnees.getProfil(PseudoCourant).GetMesListeToDo()
        recyclerview.adapter = postAdapter(ListeListe, this)
      //  recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        Log.d("PostAdapter", ListeListe.toString())

    }

     @RequiresApi(Build.VERSION_CODES.N)
     fun addClick(v : View) {
         //alerter("yop")
        when (v.id) {

            R.id.addButton -> {
                val TitreListe: String = ajoutListe.text.toString()

                if (TitreListe.length == 0) {
                    //mesDonnees.addListe(Login as String)
                    alerter("click sur + sans titre")

                } else {
                    //alerter("click sur + avec ce titre:" + TitreListe )
                    var bool = mesDonnees.addListe(TitreListe,this.PseudoCourant)
                    if (!bool){
                        alerter("Il y a déjà une liste de ce nom")
                    }else{
                        this.MAJAffichage()

                    }
                }

            }
            R.id.TrashToolBar-> {
                for(k in isLongClicked){
                    mesDonnees.RemoveListe(ListeListe[k].GetTitre(),this.PseudoCourant)
                }
                this.MAJAffichage()
                switchTashVisibility()
                Log.i("CAT","click sur la poubelle")
                isLongClicked = ArrayList()
            }
        }
    }
    @SuppressLint("RestrictedApi")
    fun onClick(v:View?){
        when(v!!.id){
            R.id.MenuIm -> {
                Log.i("TEST", "click sur l'image du menu")
                if (menuLayout.isVisible) {
                    menuLayout.isVisible = false
                } else {
                    menuLayout.isVisible = true
                }
            }
            R.id.PreferencesText -> {
                Log.i("TEST", "click sur Preferences")
                val iGP = Intent(this, GestionPreferences::class.java)
                startActivity(iGP)

            }


        }
    }



    override fun onItemClick(position: Int) {

        val clickedItem = ListeListe[position]
        alerter("click sur la liste ${clickedItem.GetTitre()}")
        val intent = Intent(this, ListeItemActivity::class.java)
        intent.putExtra("PseudoAdded",PseudoCourant)
        intent.putExtra("ListeAdded", clickedItem.GetTitre())
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemLongClick(position: Int) {
        Log.i("LONG",isLongClicked.toString())
        if(this.isLongClicked.size == 0){
            isLongClicked.add(position)
            switchTashVisibility()
        }else{
            if(isInList(position)){
                switchTashVisibility()
                isLongClicked = ArrayList()
                MAJAffichage()
            }else{
                isLongClicked.add(position)
            }

        }
        /*val clickedItem = ListeItem[position]
        var bool = mesDonnees.RemoveItem(clickedItem.GetTitre(),this.ListeCourante,this.PseudoCourant)


        this.MAJAffichage()*/





    }
}


