package com.example.paypointretailer.Utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.paypointretailer.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.InetAddress
import java.net.NetworkInterface
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class AppUtils {

    companion object {
        lateinit var activity: Activity
        internal var dialog: Dialog? = null


        private var uniqueID: String? = ""
        private val PREF_UNIQUE_ID = "PREF_UNIQUE_ID"
        fun setActivtiy(activtiy: Activity) {
            activity = activtiy
        }


        @SuppressLint("Range")
        open fun getFileName(uri: Uri, context: Context): String? {
            var result: String? = null
            if (uri.scheme == "content") {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        result =
                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                } finally {
                    cursor!!.close()
                }
            }
            if (result == null) {
                result = uri.path
                val cut = result!!.lastIndexOf('/')
                if (cut != -1) {
                    result = result!!.substring(cut + 1)
                }
            }
            return result
        }

        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun isApplicationRunning(context: Context, packageName: String): Boolean {
            val activityManager =
                context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.runningAppProcesses?.apply {
                for (processInfo in this) {
                    if (processInfo.processName == packageName) {
                        return true
                    }
                }
            }
            return false
        }

        fun showSnackBar(activity: Activity, s: String) {
            val snack = Snackbar.make(activity.window.decorView.rootView, s, Snackbar.LENGTH_LONG)
            val sbview = snack.view
            sbview.setBackgroundColor(ContextCompat.getColor(activity, android.R.color.black))
            val textView =
                sbview.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
            textView.setTextColor(ContextCompat.getColor(activity, android.R.color.white))
            snack.show()
        }

             fun hasInternet(context: Context): Boolean {
                 var result = false
                 val connectivityManager =
                     context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                     val networkCapabilities = connectivityManager.activeNetwork ?: return false
                     val actNw =
                         connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                     result = when {
                         actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                         actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                         actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                         else -> false
                     }
                 } else {
                     connectivityManager.run {
                         connectivityManager.activeNetworkInfo?.run {
                             result = when (type) {
                                 ConnectivityManager.TYPE_WIFI -> true
                                 ConnectivityManager.TYPE_MOBILE -> true
                                 ConnectivityManager.TYPE_ETHERNET -> true
                                 else -> false
                             }

                         }
                     }
                 }

                 return result
             }

        fun hideKeyboard(activity: Activity) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showErrorSnackBar(context: Activity, message: String) {
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

        fun changeMobileFormatForAPI(mobileNumber: String): String {
            /*val new  = mobileNumber.replace(" ", "")
            return new.replace("(", "").replace(")","")*/
            val regex = "\\s+".toRegex()
            return regex.replace(mobileNumber, "")
        }

        fun mobileNumber(mobile_no: String): String {
            if (mobile_no.length > 0 && mobile_no.length == 10)
                return mobile_no.substring(0, 3) + " " + mobile_no.substring(
                    3,
                    6
                ) + " " + mobile_no.substring(6, 10);
            else return mobile_no as String
        }

        fun requestParsing(request: Any?): RequestBody {

//        val request = instance as LoginRequest
            val requestSting = Gson().toJson(request)
            return requestSting.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        }

        fun formatTwoDecimalPlace(value: Double): String {
            return String.format("%.2f", value)
        }

        fun convertStringTodate(dateStr: String): Date? {

            val formatter = SimpleDateFormat("MM-dd-yyyy")
            var date: Date? = null
            try {
                date = formatter.parse(dateStr)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return date
        }

        @Synchronized
        fun getUUID(sharedPreferences: SharedPreferences): String {
            if (uniqueID!!.isEmpty()) {
                uniqueID = sharedPreferences.getString(PREF_UNIQUE_ID, null)
                if (uniqueID == null) {
                    uniqueID = UUID.randomUUID().toString()
                    val editor = sharedPreferences.edit()
                    editor.putString(PREF_UNIQUE_ID, uniqueID)
                    editor.apply()
                }
            }
            return uniqueID as String
//            return "0Xylasw";
        }


        fun String.getAllIndexOf(pattern: String): List<Int> {
            val allRecordsOfText = mutableListOf<Int>()

            var index = 0
            while (index >= 0) {
                val newStart = if (allRecordsOfText.isEmpty()) {
                    0
                } else {
                    allRecordsOfText.last() + pattern.length
                }
                index = this.subSequence(newStart, this.length).indexOf(pattern)

                if (index >= 0) {
                    allRecordsOfText.add(newStart + index)
                }
            }

            return allRecordsOfText.toList()
        }

        fun showDatePicker(): String {
            // Create a DatePickerDialog

            var date: String = ""
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                activity,
                R.style.datepicker,
                { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                    // Create a new Calendar instance to hold the selected date
                    val selectedDate = Calendar.getInstance()
                    // Set the selected date using the values received from the DatePicker dialog
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    // Create a SimpleDateFormat to format the date as "dd/MM/yyyy"
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    // Format the selected date into a string
                    val formattedDate = dateFormat.format(selectedDate.time)
                    // Update the TextView to display the selected date with the "Selected Date: " prefix
                    date = formattedDate
                   // callBack?.date(date)

                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
            return date
        }


        fun uTCToLocalPublishDates(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy hh:mm a")
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                //   logE("Date", "uTCToLocalDateOnly: " + e.toString())
            }
            return dateToReturn
        }

        fun uTCToLocalForChat(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }


        fun localToUtcFilter(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US)
            sdf.timeZone = TimeZone.getDefault()
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun UtcToLocatFilter(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun utcToLocalChatFormat(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdfOutPutToSend.timeZone = TimeZone.getTimeZone("UTC")
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun getCurrentUTCTimeFormatted(): String {
            val cal: Calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"))
            val currentLocalTime: Date = cal.time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")

// you can get seconds by adding  "...:ss" to it
// you can get seconds by adding  "...:ss" to it
//            dateFormat.timeZone = TimeZone.getTimeZone("GMT+00:00")

            Log.d("date", dateFormat.format(currentLocalTime))
            return dateFormat.format(currentLocalTime)
        }

        fun apiDatePassConvert(strDate: String): String {
            var finalDate: String? = null
            try {
                //current date format
                val dateFormat = SimpleDateFormat("MM-dd-yyyy")

                val objDate = dateFormat.parse(strDate)

                //Expected date format
                val dateFormat2 = SimpleDateFormat("yyyy-MM-dd")

                finalDate = dateFormat2.format(objDate)
                Log.d("Date Format:", "Final Date:" + finalDate)


            } catch (e: Exception) {
                e.printStackTrace()
            }
            return finalDate!!
        }


        fun filterDataSetup(strDate: String): String {
            var finalDate: String? = null
            try {
                //current date format
                val dateFormat = SimpleDateFormat("yyyy-MM-dd")

                val objDate = dateFormat.parse(strDate)

                //Expected date format
                val dateFormat2 = SimpleDateFormat("MM-dd-yyyy")

                finalDate = dateFormat2.format(objDate)
                Log.d("Date Format:", "Final Date:" + finalDate)


            } catch (e: Exception) {
                e.printStackTrace()
            }
            return finalDate!!
        }

        fun utcToReportAbuseListDate(dateToBeConverted: String): String {
            var dateToReturn = dateToBeConverted
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy HH:mm a")
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(dateToBeConverted)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                // logE("Date", "uTCToLocalDateOnly: " + e.toString())
            }
            return dateToReturn
        }

        fun uTCToLocalForMyContributions(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun uTCToLocalForTransactionHistory(datesToConvert: String): String {
            var dateToReturn = datesToConvert
//            val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy hh:mm a", Locale.US)
            sdfOutPutToSend.timeZone = TimeZone.getDefault()
            try {
                gmt = sdf.parse(datesToConvert)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun getIPAddress(useIPv4: Boolean): String? {
            try {
                val interfaces: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (intf in interfaces) {
                    val addrs: List<InetAddress> = Collections.list(intf.getInetAddresses())
                    for (addr in addrs) {
                        if (!addr.isLoopbackAddress()) {
                            val sAddr: String = addr.getHostAddress()
                            //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                            val isIPv4 = sAddr.indexOf(':') < 0
                            if (useIPv4) {
                                if (isIPv4) return sAddr
                            } else {
                                if (!isIPv4) {
                                    val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                    return if (delim < 0) sAddr.toUpperCase() else sAddr.substring(
                                        0,
                                        delim
                                    ).toUpperCase()
                                }
                            }
                        }
                    }
                }
            } catch (ignored: java.lang.Exception) {
            } // for now eat exceptions
            return ""
        }

        fun convertYMDToMDY(date: String): String {
            // Change by Ganesh date format change ask by dhwani -  only for third line change
            val inputsdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MM-dd-yyyy", Locale.US)

            var dateToReturn = date
            try {
                gmt = inputsdf.parse(date)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun convertYMDToMDYY(date: String): String {

            val inputsdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("MMM dd,yyyy", Locale.US)

            var dateToReturn = date
            try {
                gmt = inputsdf.parse(date)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }

        fun convertMDYToYDM(date: String): String {
            // Change by Ganesh date format change ask by dhwani -  only for first line change
            val inputsdf = SimpleDateFormat("MM-dd-yyyy", Locale.US)
            val gmt: Date
            val sdfOutPutToSend = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            var dateToReturn = date
            try {
                gmt = inputsdf.parse(date)
                dateToReturn = sdfOutPutToSend.format(gmt)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return dateToReturn
        }


        //to set in pref and navigate to home screen
        fun setTooltipViewStatus() {
            //Prefs.getInstance(activity).setIsTooltipShown(true)
            val intent = Intent()
            activity.setResult(Activity.RESULT_OK, intent)
            activity.finish()
        }


    }
}