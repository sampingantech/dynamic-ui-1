package id.adeds.dynamic_ui.util

import android.util.Log

fun String?.isValid() = this != null && this.isNotEmpty() && !this.equals("null",true)

fun Any?.print(){
    Log.d("LogTagKoin", " $this")
}