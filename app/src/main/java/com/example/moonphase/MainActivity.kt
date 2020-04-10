package com.example.moonphase

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream
import java.lang.Math.abs
import kotlin.math.round


//TODO na podstawie pliku ustaw radiobutton !

class MainActivity : AppCompatActivity() {

    val SOUTH_BUTTON = "south_button";
    val NORTH_BUTTON = "north_button";
    val SIMPLE_BUTTON = "simple_button";
    val CONWAY_BUTTON = "conway_button";
    val TRIG1_BUTTON = "trig1_button";
    val TRIG2_BUTTON = "trig2_button";

    val namesMap = mapOf(SOUTH_BUTTON to "południowa", NORTH_BUTTON to "północna", SIMPLE_BUTTON to "prosty", CONWAY_BUTTON to "conway'a", TRIG1_BUTTON to "trygonometryczny 1", TRIG2_BUTTON to "trygonometryczny 2");

    var currentAlgo: String = TRIG1_BUTTON;
    var currentHemisphere:String = NORTH_BUTTON;
    var currentPercantage: Double = 0.0;

    val phaseCalculator: PhaseCalculator = PhaseCalculator();

    inner class myDate {
        @RequiresApi(Build.VERSION_CODES.N)
        val c = Calendar.getInstance();
        @RequiresApi(Build.VERSION_CODES.N)
        val year: Int = c.get(Calendar.YEAR);
        @RequiresApi(Build.VERSION_CODES.N)
        val month: Int = c.get(Calendar.MONTH);
        @RequiresApi(Build.VERSION_CODES.N)
        val day: Int = c.get(Calendar.DAY_OF_MONTH);
    }

    val date: myDate = myDate();

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = getIntent().getExtras()
        if (null != extras) {
            currentHemisphere = extras.getString("hemisphere").toString()
            currentAlgo = extras.getString("algorithm").toString()
            //The key argument here must match that used in the other activity
        }

        val preferences: SharedPreferences = getSharedPreferences("PREFS", Context.MODE_PRIVATE);

        val south = preferences.getBoolean(SOUTH_BUTTON, false);
        val north = preferences.getBoolean(NORTH_BUTTON, true);
        val simple = preferences.getBoolean(SIMPLE_BUTTON, false);
        val conway = preferences.getBoolean(CONWAY_BUTTON, false);
        val trigOne = preferences.getBoolean(TRIG1_BUTTON, true);
        val trigTwo = preferences.getBoolean(TRIG2_BUTTON, false);

        if(south){
            currentHemisphere = SOUTH_BUTTON;
        } else if (north){
            currentHemisphere = NORTH_BUTTON;
        }

        if(simple){
            currentAlgo = SIMPLE_BUTTON;
        } else if(conway){
            currentAlgo = CONWAY_BUTTON;
        } else if(trigOne){
            currentAlgo = TRIG1_BUTTON;
        } else if(trigTwo){
            currentAlgo = TRIG2_BUTTON;
        }


        // jakies zapisywanie wczytywanie tej daty?
        val y = date.year;
        val m = date.month;
        Log.i("month", m.toString());
        val d = date.day;
        calculateBasedOnAlgorithm(y,m,d);
        updateInfoViews(y,m,d);
        hemiTextView.setText("Wyświetlana półkula - ${namesMap[currentHemisphere]}.");
        algoTextView.setText("Używany algorytm - ${namesMap[currentAlgo]}.");
        dateTextView.setText("Data: $d/${m+1}/$y");


//        dateTextView.setOnClickListener {
//            dateTextView.setText("");
//        }

        dateButton.setOnClickListener {
            val dPD = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                    calculateBasedOnAlgorithm(mYear,mMonth,mDay);
                    updateInfoViews(mYear, mMonth, mDay);
                },
                date.year,
                date.month,
                date.day
            );

            val y = date.year;
            val m = date.month;
            val d = date.day;

            date.c.set(1900,0,1);
            dPD.getDatePicker().setMinDate(date.c.timeInMillis);
            date.c.set(2200,12, 0);
            dPD.getDatePicker().setMaxDate(date.c.timeInMillis);
            date.c.set(y,m,d);
            dPD.show();
        }
    }



    fun goToSettings(view: View){
        val intent = Intent(this, SettingsActivity::class.java);
        startActivity(intent);
        //setContentView(R.layout.activity_settings);
    }


    fun calculateBasedOnAlgorithm(year: Int, month: Int, day: Int){
        val y = year;
        val m = month;
        val d = day;

        dateTextView.setText("Data: $d/${m+1}/$y");

        var days: Int = 0;
        if(currentAlgo == SIMPLE_BUTTON){
            days = phaseCalculator.Simple(y,m,d);
        } else if (currentAlgo == CONWAY_BUTTON) {
            days = phaseCalculator.Conway(y,m,d);
        } else if (currentAlgo == TRIG1_BUTTON) {
            days = phaseCalculator.Trig1(y,m,d);
        } else if (currentAlgo == TRIG2_BUTTON) {
            days = phaseCalculator.Trig2(y,m,d);
        }
        updatePercentageBasedOnDays(days);
        percentageTextView.setText("Procent fazy od nowiu: " + this.currentPercantage.toString() + "%");
        setPhotoBasedOnPercentage();
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.N)
    fun updateInfoViews(y: Int, m: Int, d: Int){

        val c = java.util.Calendar.getInstance();
        c.set(y, m, d);
        val monthLength: Int = java.util.Calendar.getInstance().getActualMaximum(java.util.Calendar.DAY_OF_MONTH);

        var days: Int = 0;
        if(currentAlgo == SIMPLE_BUTTON){
            days = phaseCalculator.Simple(y,m,d);
        } else if (currentAlgo == CONWAY_BUTTON) {
            days = phaseCalculator.Conway(y,m,d);
        } else if (currentAlgo == TRIG1_BUTTON) {
            days = phaseCalculator.Trig1(y,m,d);
        } else if (currentAlgo == TRIG2_BUTTON) {
            days = phaseCalculator.Trig2(y,m,d);
        }
        var daysLeft: Int = 0;
        if(days < 15){
            daysLeft = 15 - days;
            c.add(Calendar.DATE, -days);
            var yy = c.get(Calendar.YEAR)
            var dd = c.get(Calendar.DAY_OF_MONTH)
            var mm = c.get(Calendar.MONTH)
            beforeTextView.setText("Poprzedni nów - $dd/${mm+1}/$yy");
            c.add(Calendar.DATE, days+daysLeft-1);
            yy = c.get(Calendar.YEAR)
            dd = c.get(Calendar.DAY_OF_MONTH)
            mm = c.get(Calendar.MONTH)
            afterTextView.setText("Następna pełnia - $dd/${mm+1}/$yy");
        } else {
            daysLeft = days-15;
            c.add(Calendar.DATE, -daysLeft);
            var yy = c.get(Calendar.YEAR)
            var dd = c.get(Calendar.DAY_OF_MONTH)
            var mm = c.get(Calendar.MONTH)
            beforeTextView.setText("Poprzednia pełnia - $dd/${mm+1}/$yy");
            c.add(Calendar.DATE, daysLeft+28-days);
            yy = c.get(Calendar.YEAR)
            dd = c.get(Calendar.DAY_OF_MONTH)
            mm = c.get(Calendar.MONTH)
            for(i in 1..5){
                var res:Int = -1;
                if(currentAlgo == SIMPLE_BUTTON){
                    res = phaseCalculator.Simple(yy,mm,dd)
                } else if (currentAlgo == CONWAY_BUTTON) {
                    res = phaseCalculator.Conway(yy,mm,dd);
                } else if (currentAlgo == TRIG1_BUTTON) {
                    res = phaseCalculator.Trig1(yy,mm,dd);
                } else if (currentAlgo == TRIG2_BUTTON) {
                    res = phaseCalculator.Trig2(yy,mm,dd);
                }
                if(res == 0){
                    break;
                }
                c.add(Calendar.DATE, 1);
                yy = c.get(Calendar.YEAR)
                dd = c.get(Calendar.DAY_OF_MONTH)
                mm = c.get(Calendar.MONTH)
            }
            afterTextView.setText("Następny nów - $dd/${mm+1}/$yy");
        }
    }


    fun updatePercentageBasedOnDays(day: Int) {
        val days = 30;
        val p: Double = (day)*100.0/days;
        this.currentPercantage = round(p);
    }

    fun setPhotoBasedOnPercentage(){
        var files: Array<String>? = null;
        var bestPhotoPath: String = "";
        var distance: Double = 9999.0;
        var choiceHemi:String = ""
        if(currentHemisphere == NORTH_BUTTON){
            files = assets.list("north");
            choiceHemi = "north";
        } else if (currentHemisphere == SOUTH_BUTTON){
            files = assets.list("south");
            choiceHemi = "south";
        }
        if (files != null) {
            for(i in files){
                var dist:Double = 9999.0;
                if(this.currentPercantage<=50.0){
                    if(i[i.length-5] == '_'){
                        continue;
                    }
                    val regex = "[0-9]+_?[0-9]*".toRegex();
                    val match = regex.find(i, 0);
                    val photoValue: Double = match!!.value.replace("_", ".").toDouble();
                    dist = abs(currentPercantage-photoValue/2)
                } else {
                    if(i[i.length-5] != '_'){
                        continue;
                    }
                    val regex = "[0-9]+_?[0-9]*_".toRegex();
                    val match = regex.find(i, 0);
                    val photoValue: Double = match!!.value.dropLast(1).replace("_", ".").toDouble();
                    dist = abs(currentPercantage-(100-photoValue));
                }
                if (dist < distance){
                    distance = dist;
                    bestPhotoPath = i;
                    Log.i("distancePath", distance.toString() + " " + bestPhotoPath);
                }
            }
            var inputStream: InputStream = assets.open(choiceHemi + File.separator + bestPhotoPath);
            var bitMap: Bitmap = BitmapFactory.decodeStream(inputStream);
            moonImageView.setImageBitmap(bitMap);
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun goToYearActivity(view: View){
        val intent = Intent(this, YearActivity::class.java);
        intent.putExtra("algorithm", currentAlgo);
        intent.putExtra("year", date.c.get(Calendar.YEAR).toInt());
        startActivity(intent);
    }

}

