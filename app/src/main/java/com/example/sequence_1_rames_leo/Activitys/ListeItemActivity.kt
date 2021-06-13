package com.example.sequence_1_rames_leo.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sequence_1_rames_leo.Autres.*
import com.example.sequence_1_rames_leo.DataBase.Liste
import com.example.sequence_1_rames_leo.R

class ListeItemActivity : AppCompatActivity()  , ItemAdapter.OnItemClickListener {
    private lateinit var Titre: TextView
    private lateinit var ajoutItem: EditText
    private lateinit var adBtn: ImageButton
    private lateinit var PseudoCourant : String
    private lateinit var TitreListeCourante : String
    private lateinit var ListeItem : ArrayList<ItemToDo>
    private lateinit var recyclerview : RecyclerView
    private lateinit var trashToolbar : ImageView
    private var isLongClicked : ArrayList<Int> = ArrayList()
    private lateinit var menuImage :ImageView
    private lateinit var menuLayout : RelativeLayout
    private lateinit var ListeCourante : ListeTodo
    private var internet =true
    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.itemactivity)



        /*if (intent.hasExtra("PseudoAdded")){
            PseudoCourant = intent.getStringExtra("PseudoAdded").toString()
        }*/
       // PseudoCourant = DataProvider.getPseudo()
        val bundle = intent.extras
        if (intent.hasExtra("ListeAdded")){
            TitreListeCourante = intent.getStringExtra("ListeAdded").toString()
        }
        ListeCourante = DataProvider.getUser().getListToDo(TitreListeCourante)

        internet = bundle?.getBoolean("internet?", true) == true


        Log.d("CAT","liste COurante: "+ TitreListeCourante)
        this.ListeItem = ListeCourante.GetListeItem()



        recyclerview = findViewById<RecyclerView>(R.id.ListeItem)

        Titre = findViewById(R.id.TitreToolBar)
        Titre.setText("Liste : $TitreListeCourante")
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
        if(!verifReseau() and internet == true){

            val intent = Intent(this, PopUpDeco::class.java)
            intent.putExtra("popuptext", "Il semblerai qu'il n'y ai pas internet, vous avez été déconecté")
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
        if(!verifReseau() and internet == true){

            val intent = Intent(this, PopUpDeco::class.java)
            intent.putExtra("popuptext", "Il semblerai qu'il n'y ai pas internet, vous avez été déconecté")
            startActivity(intent)
        }
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

        //this.ListeItem = mesDonnees.getListe(PseudoCourant,TitreListeCourante).GetListeItem()


        recyclerview.adapter = ItemAdapter(ListeItem, this)
        //  recyclerview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        Log.d("PostAdapter", ListeItem.toString())

    }

    fun verifReseau(): Boolean {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        val cnMngr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cnMngr.activeNetworkInfo
        var sType = "Aucun réseau détecté"
        var bStatut = false
        if (netInfo != null) {
            val netState = netInfo.state
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true
                val netType = netInfo.type
                when (netType) {
                    ConnectivityManager.TYPE_MOBILE -> sType = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> sType = "Réseau wifi détecté"
                }
            }
        }
        Log.i("RES", bStatut.toString())
        return bStatut
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
                    //var bool = mesDonnees.addItem(TitreItem,this.ListeCourante,this.PseudoCourant)
                    if(verifReseau() and internet==true){
                    var requete = MyAsyncTask()
                    var S = requete.execute(MainActivity.AddItem, TitreItem,TitreListeCourante).get()

                    if (S != "ItemAdded"){
                        alerter("Il y a déjà un item de ce nom")
                    }else{
                        this.MAJAffichage()

                    }}
                    else if(verifReseau() and internet==false){
                        alerter("il n'y a plus internet")
                    }else{
                        ListeCourante.GetItemToDo(TitreItem).SwitchState()
                        viewModel.addListe(Liste(ListeCourante.getID().toString(),ListeCourante.GetTitre(),
                            true.toString(),ListeCourante.GetListeItem().toString()))
                    }
                }

            }

            R.id.TrashToolBar-> {
                for(k in isLongClicked){
                    //mesDonnees.RemoveItem(ListeItem[k].GetTitre(),this.ListeCourante,this.PseudoCourant)
                    var requete = MyAsyncTask()
                    var S = requete.execute(MainActivity.DelItem , ListeItem[k].GetTitre(),TitreListeCourante).get()

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
        if(verifReseau() and internet == true) {
            var requete = MyAsyncTask()
            var S = requete.execute(MainActivity.SwitchState,
                clickedItem.GetTitre(),
                TitreListeCourante).get()

            this.MAJAffichage()
        }else if(!verifReseau() and internet == true){

            val intent = Intent(this, PopUpDeco::class.java)
            intent.putExtra("popuptext", "Il semblerai qu'il n'y ai pas internet, vous avez été déconecté")
            startActivity(intent)
        }else{
            clickedItem.SwitchState()
            this.MAJAffichage()
            viewModel.addListe(Liste(ListeCourante.getID().toString(),
                                        ListeCourante.GetTitre(),
                                "true",
                                        ListeCourante.GetListeItem().toString()))


        }
        //mesDonnees.MAJinfos()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemLongClick(position: Int) {
        if(internet){
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

        }}
        /*val clickedItem = ListeItem[position]
        var bool = mesDonnees.RemoveItem(clickedItem.GetTitre(),this.ListeCourante,this.PseudoCourant)


        this.MAJAffichage()*/





    }
}