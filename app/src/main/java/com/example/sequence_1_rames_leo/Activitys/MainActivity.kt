package com.example.sequence_1_rames_leo.Activitys

import android.annotation.SuppressLint
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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.example.sequence_1_rames_leo.Autres.DataProvider
import com.example.sequence_1_rames_leo.Autres.ListeTodo
import com.example.sequence_1_rames_leo.Autres.Localdata
import com.example.sequence_1_rames_leo.Autres.MyAsyncTask
import com.example.sequence_1_rames_leo.R
import kotlinx.coroutines.*
import okhttp3.*
import org.json.JSONObject
import java.io.*


lateinit var chemin : File
lateinit var mesDonnees : Localdata

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //pour async
    private val activityScope = CoroutineScope(
        SupervisorJob() +
                Dispatchers.Main
    )
    var job : Job? = null




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

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("CAT", "onCreate") // trace d'exécution

        super.onCreate(savedInstanceState)

        MainActivity.Companion.context = this


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
            btnOK.isEnabled = verifReseau()

        //menu
        menuImage = findViewById(R.id.MenuIm)
        menuLayout =findViewById(R.id.MenuLayout)
            menuLayout.isVisible = false




        var variable = getPreferences(Context.MODE_PRIVATE).toString();




        chemin = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!


        mesDonnees = Localdata(titre = "GDL_PMR", chemin = chemin)
        //alerter(mesDonnees.getJSONToString())
        DataProvider.isLOG = false



    }











    private fun alerter(s: String) {

        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()

    }

    override fun onResume(){
        super.onResume()
        menuLayout.isVisible = false
        btnOK.isEnabled = verifReseau()
        DataProvider.isLOG = false

    }

    @SuppressLint("RestrictedApi")
    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.boutonOK -> {
                val pseudoToAdd: String = edtPseudo.getText().toString();
                val MdPToAdd : String = edtMdP.text.toString()


                //editor.putString("login", pseudoToAdd)
                //editor.commit()
                mesDonnees.addProfil(pseudoToAdd)
                //alerter(mesDonnees.getJSONToString())
                mesDonnees.MAJinfos()


                DataProvider.setPseudo(pseudoToAdd)
                DataProvider.setMdP(MdPToAdd)
                var requete = MyAsyncTask()
                requete.execute(LOG).get()
                if(DataProvider.isLOG) {
                    editor.putString("login", DataProvider.getPseudo())

                    editor.putString("MdP", DataProvider.getMdP())

                    editor.putString("hashCodeCourant", DataProvider.getUser().HASH)

                    editor.commit()


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


}





    }




