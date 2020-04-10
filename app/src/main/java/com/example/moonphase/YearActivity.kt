package com.example.moonphase

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_year.*
import java.util.*

class YearActivity : AppCompatActivity() {

    val SIMPLE_BUTTON = "simple_button";
    val CONWAY_BUTTON = "conway_button";
    val TRIG1_BUTTON = "trig1_button";
    val TRIG2_BUTTON = "trig2_button";

    var year: Int = 0;
    var currentAlgorithm: String = "";
    val phaseCalculator: PhaseCalculator = PhaseCalculator();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_year)

        val extras = getIntent().getExtras()
        if (null != extras) {
            this.currentAlgorithm = extras.getString("algorithm").toString()
            this.year = extras.getInt("year");
        }
        yearEditText.setText("Pełnie w roku " + year.toString());
        fillListView()

        yearEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (yearEditText.hasFocus()) {
                    var t = yearEditText.text;
                    val re = Regex("[^0-9]")
                    val match = re.replace(t, "");
                    var y: Int = -1;
                    Log.i("length", match.length.toString());
                    if( (match != "") && (match.length<5) ){
                        y = match.toInt();
                        if( (y>=1900) && (y<=2200) ){
                            year = y;
                            yearEditText.clearFocus();
                            yearEditText.setText("Pełnie w roku: " + match);
                            fillListView();
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    fun plusYear(view: View){
        if(this.year < 2200) {
            this.year += 1;
            yearEditText.setText("Pełnie w roku " + year.toString());
            fillListView()
        }
    }

    fun minusYear(view: View){
        if(this.year > 1900) {
            this.year -= 1;
            yearEditText.setText("Pełnie w roku " + year.toString());
            fillListView()
        }
    }

    fun fillListView() {
        var listOfFullMoons: MutableList<String> = ArrayList();
        var c = Calendar.getInstance();
        val y = this.year;
        var d = 1;
        for(m in 1..12){
            c.set(y, m, d);
            val monthLength: Int = c.getActualMaximum(Calendar.DAY_OF_MONTH);
            for(d in 1..monthLength){
                var day = -1;
                if(this.currentAlgorithm == SIMPLE_BUTTON){
                    day = phaseCalculator.Simple(y,m,d);
                } else if (this.currentAlgorithm == CONWAY_BUTTON) {
                    day = phaseCalculator.Conway(y,m,d);
                } else if (this.currentAlgorithm == TRIG1_BUTTON) {
                    day = phaseCalculator.Trig1(y,m,d);
                } else if (this.currentAlgorithm == TRIG2_BUTTON) {
                    day = phaseCalculator.Trig2(y,m,d);
                }
                if(day == 15){
                    listOfFullMoons.add("$d/${m}/$y");
                }
            }
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfFullMoons)
        this.daysListView.setAdapter(adapter);
    }


    fun goBackToMain(view: View){
        val intent = Intent(this, MainActivity::class.java);
        startActivity(intent);
    }

    fun clearScreen(){

    }

}
