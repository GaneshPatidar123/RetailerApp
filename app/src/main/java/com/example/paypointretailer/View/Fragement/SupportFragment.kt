package com.example.paypointretailer.View.Fragement

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.paypointretailer.Base.BaseFragment
import com.example.paypointretailer.R
import com.example.paypointretailer.databinding.LayoutSupportFragmentBinding

class SupportFragment :
    BaseFragment<LayoutSupportFragmentBinding>(R.layout.layout_support_fragment) {

    override fun setUpViews() {
        setupData()
    }

    override fun setUpListeners() {
        binding.btnUpload.setOnClickListener {
            selectFile()
            /* if (ContextCompat.checkSelfPermission(
                     this@StepperActivity,
                     Manifest.permission.CAMERA
                 ) != PackageManager.PERMISSION_GRANTED
             ) {
                 storagePermision.launch(Manifest.permission.CAMERA)
             } else {
                 selectfile()
             }*/
        }
    }

    private fun setupData() {
        binding.toolBar.ivNotification.visibility = View.VISIBLE
        binding.toolBar.ivHome.visibility = View.VISIBLE
        binding.toolBar.tvTitle.text = getString(R.string.help_and_support)
        binding.toolBar.ivBack.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu))
    }

    private fun selectFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        resultLauncher.launch(intent)
    }

    @SuppressLint("Range")
    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                var fileName = ""
                if (result.data?.data!!.getScheme().equals("content")) {
                    val cursor = requireActivity().contentResolver.query(
                        result.data?.data!!,
                        null,
                        null,
                        null,
                        null
                    )
                    try {
                        if (cursor != null && cursor.moveToFirst()) {
                            fileName =
                                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    } finally {
                        cursor!!.close()
                    }
                    binding.edtAttachment.setText(fileName.toString())
                }
            }
        }
}