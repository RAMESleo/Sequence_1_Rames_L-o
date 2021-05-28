package com.example.sequence_1_rames_leo.Activitys

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.example.sequence_1_rames_leo.Autres.Localdata
import com.example.sequence_1_rames_leo.R
import java.io.File

lateinit var chemin : File
lateinit var mesDonnees : Localdata

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit  var sp : SharedPreferences
    private lateinit  var  editor: SharedPreferences.Editor
    private lateinit  var edtPseudo: EditText
  //  public lateinit var mesDonnees: DonneesLocales
    private lateinit var fichier: File
    private lateinit var Titre : TextView
    private lateinit var menuImage :ImageView
    private lateinit var menuLayout : RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("CAT", "onCreate") // trace d'exécution

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sp = PreferenceManager.getDefaultSharedPreferences(this)
        editor = sp.edit();
        edtPseudo = this.findViewById(R.id.PseudoEdit)
        Titre = findViewById(R.id.TitreToolBar)
        Titre.setText("Main Activity")



        menuImage = findViewById(R.id.MenuIm)
        menuLayout =findViewById(R.id.MenuLayout)
        menuLayout.isVisible = false


        var variable = getPreferences(Context.MODE_PRIVATE).toString();



        chemin = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!


        mesDonnees = Localdata(titre = "GDL_PMR", chemin = chemin)
        //alerter(mesDonnees.getJSONToString())


    }





    private fun alerter(s: String) {

        val t = Toast.makeText(this, s, Toast.LENGTH_SHORT)
        t.show()

    }

    override fun onResume(){
        super.onResume()
        menuLayout.isVisible = false

    }

    @SuppressLint("RestrictedApi")
    override fun onClick(v: View?) {

        when (v!!.id) {

            R.id.boutonOK -> {
                val pseudoToAdd: String = edtPseudo.getText().toString();

                editor.putString("login", pseudoToAdd)
                editor.commit()
                mesDonnees.addProfil(pseudoToAdd)
                //alerter(mesDonnees.getJSONToString())
                mesDonnees.MAJinfos()

                val intent = Intent(this, ChoixListeActivity::class.java)
                intent.putExtra("PseudoAdded", pseudoToAdd)
                startActivity(intent)

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
    }



