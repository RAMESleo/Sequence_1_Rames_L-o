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
import com.example.sequence_1_rames_leo.Autres.ItemAdapter
import com.example.sequence_1_rames_leo.Autres.ItemToDo
import com.example.sequence_1_rames_leo.R

class ListeItemActivity : AppCompatActivity()  , ItemAdapter.OnItemClickListener {
    private lateinit var Titre: TextView
    private lateinit var ajoutItem: EditText
    private lateinit var adBtn: ImageButton
    private lateinit var PseudoCourant : String
    private lateinit var ListeCourante : String
    private lateinit var ListeItem : ArrayList<ItemToDo>
    private lateinit var recyclerview : RecyclerView
    private lateinit var trashToolbar : ImageView
    private var isLongClicked : ArrayList<Int> = ArrayList()
    private lateinit var menuImage :ImageView
    private lateinit var menuLayout : RelativeLayout

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itemactivity)



        if (intent.hasExtra("PseudoAdded")){
            PseudoCourant = intent.getStringExtra("PseudoAdded").toString()
        }
        if (intent.hasExtra("ListeAdded")){
            ListeCourante = intent.getStringExtra("ListeAdded").toString()
        }
        Log.d("CAT","liste COurante"+ListeCourante)
        this.ListeItem = mesDonnees.getListe(PseudoCourant,ListeCourante).GetListeItem()


        recyclerview = findViewById<RecyclerView>(R.id.ListeItem)

        Titre = findViewById(R.id.TitreToolBar)
        Titre.setText("Liste : $ListeCourante")
        ajoutItem = findViewById(R.id.AddItem)
        adBtn = findViewById(R.id.addButtonItem)
        trashToolbar = findViewById(R.id.TrashToolBar)

        menuImage = findViewById(R.id.MenuIm)
        menuLayout =findViewById(R.id.MenuLayout)
        menuLayout.isVisible = false

        Log.d("PostAdapter", ListeItem.toString())

        recyclerview.adapter = ItemAdapter(ListeItem, this)
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

        this.ListeItem = mesDonnees.getListe(PseudoCourant,ListeCourante).GetListeItem()
        recyclerview.adapter = ItemAdapter(ListeItem, this)
        //  recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        Log.d("PostAdapter", ListeItem.toString())

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun addClick(v : View) {
        //alerter("yop")
        when (v.id) {

            R.id.addButtonItem -> {
                val TitreItem: String = ajoutItem.text.toString()

                if (TitreItem.length == 0) {
                    //mesDonnees.addListe(Login as String)
                    alerter("click sur + sans titre")

                } else {
                    //alerter("click sur + avec ce titre:" + TitreListe )
                    var bool = mesDonnees.addItem(TitreItem,this.ListeCourante,this.PseudoCourant)
                    if (!bool){
                        alerter("Il y a déjà un item de ce nom")
                    }else{
                        this.MAJAffichage()

                    }
                }

            }

            R.id.TrashToolBar-> {
                for(k in isLongClicked){
                    mesDonnees.RemoveItem(ListeItem[k].GetTitre(),this.ListeCourante,this.PseudoCourant)
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

        val clickedItem = ListeItem[position]
        alerter("click sur l'item ${clickedItem.GetTitre()}")
        clickedItem.SwitchState()
        //this.MAJAffichage()
        mesDonnees.MAJinfos()
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