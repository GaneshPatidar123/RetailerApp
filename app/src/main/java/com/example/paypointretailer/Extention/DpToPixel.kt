package com.example.paypointretailer.Extention

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar


fun Context.dpToPx(dp: Int): Int {
    return (dp * resources.displayMetrics.density).toInt()
}
 fun showErrorMessage(context  : Activity, message: String) {
  //  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

     try {
         val snack =
             Snackbar.make(context.window.decorView.rootView, message, Snackbar.LENGTH_LONG)
         val sbview = snack.view
         sbview.setBackgroundColor(
             ContextCompat.getColor(
                 context,
                 android.R.color.holo_red_dark
             )
         )
         val textView =
             sbview.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
         textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
         textView.setMaxLines(5);

         snack.show()

     } catch (e: Exception) {
         e.printStackTrace()
     }
}

fun showSuccessMessage(context  : Activity, message: String) {
    //  Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    try {
        val snack =
            Snackbar.make(context.window.decorView.rootView, message, Snackbar.LENGTH_LONG)
        val sbview = snack.view
        sbview.setBackgroundColor(
            ContextCompat.getColor(
                context,
                android.R.color.black
            )
        )
        val textView =
            sbview.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        textView.setMaxLines(5);

        snack.show()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun logD(tag: String = "SAMPLE_APP", message: String) {
    Log.d(tag, message)
}

fun logE(tag: String = "SAMPLE_APP", message: String) {
    Log.e(tag, message)
}

fun logI(tag: String = "SAMPLE_APP", message: String = "") {
    Log.i(tag, message)
}
