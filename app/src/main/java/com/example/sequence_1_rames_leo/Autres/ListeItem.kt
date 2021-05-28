package com.example.sequence_1_rames_leo.Autres

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.sequence_1_rames_leo.R

class ListeItem : AppCompatActivity() {
    private lateinit var Titre: TextView
    private lateinit var ajoutListe: EditText
    private lateinit var adBtn: ImageButton
    private lateinit var PseudoCourant : String
    private lateinit var ListeListe : ArrayList<ListeTodo>
    private lateinit var recyclerview : RecyclerView

    override fun onCreate(@Nullable savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        setContentView(R.layout.listactivity)


    }
}