package com.example.sequence_1_rames_leo.Activitys;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;

import androidx.annotation.Nullable;

import com.example.sequence_1_rames_leo.Autres.ProfilListeToDo;
import com.example.sequence_1_rames_leo.R;

import java.util.List;


public class GestionPreferences extends PreferenceActivity  {
    CheckBoxPreference cbp;
    EditTextPreference edtpl;
private List<ProfilListeToDo> listePseudo;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        cbp = (CheckBoxPreference) findPreference("remember");
        edtpl = (EditTextPreference) findPreference("login");

    }
/*
    private boolean pasDejaDedans(ProfilListeToDo pseudo){
        int compteur = 0;
        for(ProfilListeToDo k : this.listePseudo){
            if(k.equalTo(pseudo)){
                compteur += 1;
            }

        }
        return compteur==0;
    }

    public void adProfil(ProfilListeToDo Profil){
        if (pasDejaDedans(Profil)){
            this.listePseudo.add(Profil);
        }
        System.out.println(this.listePseudo);
    }*/



}
