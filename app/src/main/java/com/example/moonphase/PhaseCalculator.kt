package com.example.moonphase

import android.util.Log
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.sin

class PhaseCalculator {

    fun Simple(year: Int, month: Int, day: Int): Int {
        val lp = 2551443;
        var now = Date(year,month-1,day,20,35,0);
        var new_moon = Date(1970, 0, 7, 20, 35, 0);
        var phase = ((now.getTime() - new_moon.getTime())/1000) % lp;
        val diff = ((phase /(24*3600)) + 1);
        val res = floor(diff.toDouble());
        return res.toInt();
    }


    fun Conway(year: Int, month: Int, day: Int): Int  {
        var r: Double = (year % 100).toDouble();
        r %= 19;
        if (r>9){
            r -= 19;
        }
        r = ((r * 11) % 30) + month + day;
        if (month<3){
            r += 2;
        }
        if(year<2000){
            r -= 4;
        } else {
            r -= 8.3;
        }
        r = floor(r+0.5)%30;
        if (r < 0){
            return (r+30).toInt();
        } else {
            return r.toInt();
        }
    }


    fun Trig1(year: Int, month: Int, day: Int): Int {
        var thisJD = julday(year,month,day);
        var degToRad = 3.14159265 / 180;
        var K0: Double = floor((year-1900)*12.3685);
        var T: Double = (year-1899.5) / 100;
        var T2: Double = T*T;
        var T3: Double = T*T*T;
        var J0: Double = 2415020 + 29*K0;
        var F0: Double = 0.0001178*T2 - 0.000000155*T3 + (0.75933 + 0.53058868*K0) - (0.000837*T + 0.000335*T2);
        var M0: Double = 360*(getFrac(K0*0.08084821133)) + 359.2242 - 0.0000333*T2 - 0.00000347*T3;
        var M1: Double = 360*(getFrac(K0*0.07171366128)) + 306.0253 + 0.0107306*T2 + 0.00001236*T3;
        var B1: Double = 360*(getFrac(K0*0.08519585128)) + 21.2964 - (0.0016528*T2) - (0.00000239*T3);
        var phase: Double = 0.0;
        var jday: Double = 0.0;
        var oldJ: Double = 0.0;
        while (jday < thisJD) {
            var F = F0 + 1.530588*phase;
            var M5 = (M0 + phase*29.10535608)*degToRad;
            var M6 = (M1 + phase*385.81691806)*degToRad;
            var B6 = (B1 + phase*390.67050646)*degToRad;
            F -= 0.4068*sin(M6) + (0.1734 - 0.000393*T)*sin(M5);
            F += 0.0161*sin(2*M6) + 0.0104*sin(2*B6);
            F -= 0.0074*sin(M5 - M6) - 0.0051*sin(M5 + M6);
            F += 0.0021*sin(2*M5) + 0.0010*sin(2*B6-M6);
            F += 0.5 / 1440;
            oldJ = jday;
            jday = J0 + 28*phase + floor(F);
            phase++;
        }
        Log.i("PHASE", "jd = $thisJD, oldJ = $oldJ, difference = ${thisJD-oldJ}")
        return ((thisJD-oldJ)%30).toInt();
    }


    fun getFrac(fr: Double):Double {
        return (fr - floor(fr));
    }

    fun Trig2(year: Int, month: Int, day: Int): Int {
        val n: Double = floor(12.37 * (year -1900 + ((1.0 * month - 0.5)/12.0)));
        val RAD: Double = 3.14159265/180.0;
        val t: Double = n / 1236.85;
        val t2: Double = t * t;
        val aS: Double = 359.2242 + 29.105356 * n;
        val aM: Double = 306.0253 + 385.816918 * n + 0.010730 * t2;
        var xtra: Double = 0.75933 + 1.53058868 * n + ((1.178e-4) - (1.55e-7) * t) * t2;
        xtra += (0.1734 - 3.93e-4 * t) * sin(RAD * aS) - 0.4068 * sin(RAD * aM);
        var i: Double = 0.0;
        if(xtra > 0.0){
            i = floor(xtra);
        } else {
            i = ceil(xtra - 1.0);
        }
        val j1 = julday(year,month,day);
        val jd = (2415020 + 28 * n) + i;
        return ((j1-jd + 30)%30).toInt();
    }


    fun julday(year: Int, month: Int, day: Int): Double {
        var y = year;
        if (y < 0) {
            y++;
        }
        var jy: Int = year;
        var jm: Int = month +1;
        if (month <= 2) {
            jy--;
            jm += 12;
        }
        var jul:Double = floor(365.25 *jy) + floor(30.6001 * jm) + day + 1720995;
        if (day+31*(month+12*year) >= (15+31*(10+12*1582))) {
            var ja: Double = floor(0.01 * jy);
            jul = jul + 2 - ja + floor(0.25 * ja);
        }
        return jul;
    }


}