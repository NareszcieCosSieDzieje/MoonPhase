package com.example.moonphase


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*


class SettingsActivity : AppCompatActivity() {

    val SOUTH_BUTTON = "south_button";
    val NORTH_BUTTON = "north_button";
    val SIMPLE_BUTTON = "simple_button";
    val CONWAY_BUTTON = "conway_button";
    val TRIG1_BUTTON = "trig1_button";
    val TRIG2_BUTTON = "trig2_button";

    var currentAlgo: String = "";
    var currentHemisphere:String = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val preferences: SharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        val editor = preferences.edit();

        this.southRadioButton.isChecked = preferences.getBoolean(SOUTH_BUTTON, false);
        this.northRadioButton.isChecked = preferences.getBoolean(NORTH_BUTTON, true);
        this.radioButtonSimple.isChecked = preferences.getBoolean(SIMPLE_BUTTON, false);
        this.radioButtonConway.isChecked = preferences.getBoolean(CONWAY_BUTTON, false);
        this.radioButtonTrig1.isChecked = preferences.getBoolean(TRIG1_BUTTON, true);
        this.radioButtonTrig2.isChecked = preferences.getBoolean(TRIG2_BUTTON, false);


        if(this.southRadioButton.isChecked){
            currentHemisphere = SOUTH_BUTTON;
        } else if (this.northRadioButton.isChecked){
            currentHemisphere = NORTH_BUTTON;
        }

        if(this.radioButtonSimple.isChecked){
            currentAlgo = SIMPLE_BUTTON;
        } else if(this.radioButtonConway.isChecked){
            currentAlgo = CONWAY_BUTTON;
        } else if(this.radioButtonTrig1.isChecked){
            currentAlgo = TRIG1_BUTTON;
        } else if(this.radioButtonTrig2.isChecked){
            currentAlgo = TRIG2_BUTTON;
        }
        

        this.algorithmRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonSimple -> {
                    editor.putBoolean(SIMPLE_BUTTON, true).apply();
                    editor.putBoolean(CONWAY_BUTTON, false).apply();
                    editor.putBoolean(TRIG1_BUTTON, false).apply();
                    editor.putBoolean(TRIG2_BUTTON, false).apply();
                    currentAlgo = SIMPLE_BUTTON;
                }
                R.id.radioButtonConway -> {
                    editor.putBoolean(SIMPLE_BUTTON, false).apply();
                    editor.putBoolean(CONWAY_BUTTON, true).apply();
                    editor.putBoolean(TRIG1_BUTTON, false).apply();
                    editor.putBoolean(TRIG2_BUTTON, false).apply();
                    currentAlgo = CONWAY_BUTTON;
                }
                R.id.radioButtonTrig1 -> {
                    editor.putBoolean(SIMPLE_BUTTON, false).apply();
                    editor.putBoolean(CONWAY_BUTTON, false).apply();
                    editor.putBoolean(TRIG1_BUTTON, true).apply();
                    editor.putBoolean(TRIG2_BUTTON, false).apply();
                    currentAlgo = TRIG1_BUTTON;
                }
                R.id.radioButtonTrig2 -> {
                    editor.putBoolean(SIMPLE_BUTTON, false).apply();
                    editor.putBoolean(CONWAY_BUTTON, false).apply();
                    editor.putBoolean(TRIG1_BUTTON, false).apply();
                    editor.putBoolean(TRIG2_BUTTON, true).apply();
                    currentAlgo = TRIG2_BUTTON;
                }
            }
        }


        this.hemisphereRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.northRadioButton -> {
                    editor.putBoolean(NORTH_BUTTON, true).apply();
                    editor.putBoolean(SOUTH_BUTTON, false).apply();
                    currentHemisphere = NORTH_BUTTON;
                }
                R.id.southRadioButton -> {
                    editor.putBoolean(NORTH_BUTTON, false).apply();
                    editor.putBoolean(SOUTH_BUTTON, true).apply();
                    currentHemisphere = SOUTH_BUTTON;
                }
            }
        }


    }


    fun goToMainActivity(view: View){
        val intent = Intent(this, MainActivity::class.java);
        intent.putExtra("hemisphere", currentHemisphere);
        intent.putExtra("algorithm", currentAlgo );
        startActivity(intent);
        //setContentView(R.layout.activity_main);
    }


}
