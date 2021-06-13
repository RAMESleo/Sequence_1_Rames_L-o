package com.example.sequence_1_rames_leo.Activitys

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import androidx.preference.PreferenceManager
import androidx.room.Database
import androidx.room.Room
import com.example.sequence_1_rames_leo.Autres.*

import com.example.sequence_1_rames_leo.DataBase.AppDatabase
import com.example.sequence_1_rames_leo.DataBase.ListDAO
import com.example.sequence_1_rames_leo.DataBase.Liste
import com.example.sequence_1_rames_leo.R
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import kotlin.concurrent.thread


lateinit var chemin : File
lateinit var mesDonnees : Localdata

class MainActivity : AppCompatActivity(), View.OnClickListener {






    private lateinit  var sp : SharedPreferences
    private lateinit  var  editor: SharedPreferences.Editor
    private lateinit  var edtPseudo: EditText
    private lateinit var edtMdP : EditText
  //  public lateinit var mesDonnees: DonneesLocales
    private lateinit var fichier: File
    private lateinit var Titre : TextView
    private lateinit var menuImage :ImageView
    private lateinit var menuLayout : RelativeLayout
    private lateinit var btnOK : Button
    private val viewModel by viewModels<MainViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("CAT", "onCreate") // trace d'exécution

        super.onCreate(savedInstanceState)

        context = this



        //set le xml de l'activité
        setContentView(R.layout.activity_main)
        //préférence
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit();

        //composant du xml
        edtPseudo = this.findViewById(R.id.PseudoEdit)
        edtMdP = this.findViewById(R.id.MDPEdit)
        Titre = findViewById(R.id.TitreToolBar)
            Titre.setText("Main Activity")
        btnOK = findViewById(R.id.boutonOK)



        edtPseudo.setText( sp.getString("login","tom").toString())
        edtMdP.setText( sp.getString("MdP","web").toString())

        if(verifReseau()){
            btnOK.isEnabled = true


        }else{

            btnOK.isEnabled = false

            //test()
            nointernet()
            //doInBg()
            val intent = Intent(this, PopUpWindow::class.java)
            intent.putExtra("popuptext", "Il semblerai qu'il n'y ai pas internet, voulez-vous utiliser les listes du derniers utilisateurs?")
            startActivity(intent)
        }
        //menu
        menuImage = findViewById(R.id.MenuIm)
        menuLayout =findViewById(R.id.MenuLayout)
            menuLayout.isVisible = false




        var variable = getPreferences(MODE_PRIVATE).toString();

        //chemin = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
        //mesDonnees = Localdata(titre = "GDL_PMR", chemin = chemin)
        //alerter(mesDonnees.getJSONToString())
        DataProvider.isLOG = false



    }

    private fun nointernet(){

        DataProvider.setPseudo(sp.getString("login","leo").toString())
        DataProvider.setMdP(sp.getString("MdP","pepe").toString())


        viewModel.getListes()
        viewModel.Listes.observe(this) { viewState ->
            when (viewState) {
                is MainViewModel.ViewState.Content -> {

                    for(k in viewState.Listes){
                        Log.i("TestSQL", k.toString())


                        var id = k.uid
                        var titre = k.Titre_List
                        val ListeItem: ArrayList<ItemToDo> = ArrayList()

                        var ListfJSON = JSONArray(k.ListItem)
                        for (k in 0..(ListfJSON.length() - 1)) {
                            //Log.i("LOAD", k.toString())
                            val nextI = ListfJSON.getJSONObject(k)
                            val label = nextI.getString("Titre")
                            val stateString = nextI.getString("state")
                            val idI = nextI.getString("id")
                            var state = false
                            if(stateString == "true"){
                                state = true
                            }
                            ListeItem.add(ItemToDo(label,state,idI))
                        }
                        DataProvider.addList(ListeTodo(titre.toString(),ListeItem,id))

                    }

                }

                is MainViewModel.ViewState.Error -> {
                    Log.i("TestSQL", viewState.message)
                }
            }
        }
    }

    fun updateFromDB() {


        viewModel.getListes()
        viewModel.Listes.observe(this) { viewState ->
            when (viewState) {
                is MainViewModel.ViewState.Content -> {
                    Log.i("Tes",viewState.Listes.toString())
                    for (j in viewState.Listes) {
                        Log.i("Tes",j.modifie.toString())
                        if (j.modifie.toString() == "true") {

                            val ListeItem: ArrayList<ItemToDo> = ArrayList()

                            var ListfJSON = JSONArray(j.ListItem)
                            Log.i("Tes","json: "+ ListfJSON.length().toString())

                                for (k in 0..(ListfJSON.length() - 1)) {
                                    Log.i("Tes", "dans le for")
                                    val nextI = ListfJSON.getJSONObject(k)

                                    val label = nextI.getString("Titre")
                                    Log.i("tes", "label: " + label)
                                    val stateString = nextI.getString("state")
                                    val idI = nextI.getString("id")

                                    var state = false
                                    if (stateString == "true") {
                                        state = true
                                    }
                                    Log.i("tes", "trucs item" + label + state + idI)
                                    ListeItem.add(ItemToDo(label, state, idI))
                                    //ListeItem.add(ItemToDo("Test",true,"1000"))

                                }
                            //Log.i("Tes",ListeItem.toString())
                                for (i in ListeItem) {
                                        Log.i("tes", "item: " + i)
                                if (i.GetState()==true) {
                                    var requete = MyAsyncTask(i)
                                    requete.execute(CheckItem,i.getID(),j.uid).get()
                                }else{
                                    var requete = MyAsyncTask(i)
                                    requete.execute(UnCheckItem,i.getID(),j.uid).get()
                                }
                            }


                        }
                    }
                }
                is MainViewModel.ViewState.Error -> {
                    Log.i("TestSQL","inupdate: \t" + viewState.message)
                }

            }
        }
    }

    private fun alerter(s: String) {

        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()

    }

    override fun onResume(){
        super.onResume()
        menuLayout.isVisible = false

        DataProvider.isLOG = false
        if(!verifReseau()) {
            nointernet()
            //doInBg()
            val intent = Intent(this, PopUpWindow::class.java)
            intent.putExtra("popuptext",
                "Il semblerai qu'il n'y ai pas internet, voulez-vous utiliser les listes du derniers utilisateurs?")
            startActivity(intent)

        }
    }

    override fun onPause() {
        super.onPause()
        if(!verifReseau()) {
            nointernet()
            //doInBg()
            val intent = Intent(this, PopUpWindow::class.java)
            intent.putExtra("popuptext",
                "Il semblerai qu'il n'y ai pas internet, voulez-vous utiliser les listes du derniers utilisateurs?")
            startActivity(intent)
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.boutonOK -> {
                val pseudoToAdd: String = edtPseudo.getText().toString();
                val MdPToAdd : String = edtMdP.text.toString()


                //editor.putString("login", pseudoToAdd)
                //editor.commit()
                //mesDonnees.addProfil(pseudoToAdd)
                //alerter(mesDonnees.getJSONToString())
                //mesDonnees.MAJinfos()


                DataProvider.setPseudo(pseudoToAdd)
                DataProvider.setMdP(MdPToAdd)
                var requete = MyAsyncTask()
                requete.execute(LOG).get()
                if(DataProvider.isLOG) {
                    if(pseudoToAdd==sp.getString("login","pziugb").toString() && MdPToAdd == sp.getString("MdP","iugrh,ie").toString()){
                       Log.i("Tes","update")
                        updateFromDB()
                    }else{
                        viewModel.DELALL()

                    }


                    editor.putString("login", DataProvider.getPseudo())

                    editor.putString("MdP", DataProvider.getMdP())

                    editor.putString("hashCodeCourant", DataProvider.getUser().HASH)

                    editor.commit()

                    viewModel.addListefromDataProvider()
                    val intent = Intent(this, ChoixListeActivity::class.java)
                    intent.putExtra("PseudoAdded", pseudoToAdd)
                    startActivity(intent)
                }



            }
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




companion object{

    lateinit var context : Context


    public val LOG : String = "LOG"
    public val AddList : String = "AddList"
    public val DelList :String = "DelList"
    public val AddItem : String = "AddItem"
    public val DelItem : String = "DelItem"
    public val GetItems : String = "GetItems"
    public val CheckItem : String = "CheckItem"
    public val UnCheckItem : String = "UnCheckItem"
    public val SwitchState : String = "SwitchState"
    public val LOGFromPref : String = "LOGFromPref"




}





    }




