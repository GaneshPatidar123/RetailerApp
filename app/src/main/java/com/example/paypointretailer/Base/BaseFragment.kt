package com.example.paypointretailer.Base

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppSharedPref
import com.example.paypointretailer.Utils.AppUtils
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

open class BaseFragment<DB : ViewDataBinding>(private val layoutId: Int) : Fragment() {

    private lateinit var _binding: DB
    val binding get() = _binding
    var selecteddate: String? = null

    @Inject
    lateinit var pref: AppSharedPref

    private var mProgressDialog: Dialog? = null
    var callBack: onDateSelect? = null

    interface onDateSelect {
        fun date(date: String, editText: AppCompatEditText?)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (layoutId != 0) {
            _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            binding.lifecycleOwner = viewLifecycleOwner
            return binding.root
        } else {
            throw IllegalArgumentException("Layout resource can't be null")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpListeners()
        performApiCall()
        setUpObservers()
    }

    open fun showProgressDialog() {
        mProgressDialog?.let {
            if (!it.isShowing) it.show()
        } ?: run {
            mProgressDialog = Dialog(requireContext()).apply {
                window?.let {
                    it.requestFeature(Window.FEATURE_NO_TITLE)
                    it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    //it.setDimAmount(0.2f)
                }
                setContentView(R.layout.progress_dialog)
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
            if (mProgressDialog?.isShowing == false) mProgressDialog?.show()
        }
    }

    open fun hideProgressDialog() {
        mProgressDialog?.let {
            if (it.isShowing) it.dismiss()
        }
    }

    open fun setUpViews() {}

    open fun setUpListeners() {}

    open fun performApiCall() {}

    open fun setUpObservers() {}

    fun showToast(title: String) = Toast.makeText(requireContext(), title, Toast.LENGTH_LONG).show()

    fun showShortToast(title: String) =
        Toast.makeText(requireContext(), title, Toast.LENGTH_SHORT).show()

    fun showDatePicker(call: onDateSelect, edtFromDate: AppCompatEditText) {
        callBack = call
        val calendar = Calendar.getInstance()
        val minDate = Calendar.getInstance()
        minDate.set(2020, Calendar.JANUARY, 1) // Set your minimum date here

        val datePickerDialog = DatePickerDialog(
            AppUtils.activity,
            R.style.datepicker,
            { DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                callBack?.date(formattedDate, edtFromDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = minDate.timeInMillis
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}