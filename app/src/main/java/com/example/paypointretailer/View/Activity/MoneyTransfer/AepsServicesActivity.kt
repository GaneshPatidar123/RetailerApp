package com.example.paypointretailer.View.Activity.MoneyTransfer

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.MyRDServiceListener
import com.example.paypointretailer.databinding.ActivityAepsServicesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AepsServicesActivity :
    BaseActivity<ActivityAepsServicesBinding>(R.layout.activity_aeps_services) {
    var installListData: MutableList<String> = mutableListOf()
    var packageNames: MutableList<String> = mutableListOf()
    var selectPackageName = ""
    val AUTHENTICATION_REQUEST: Int = 123

    @Inject
    lateinit var sharedPrefs: SharedPreferences
    override fun setUpViews() {
        getAllPackageName()

        val adapter =
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, installListData)
        binding.edtChoose.threshold = 1
        binding.edtChoose.setAdapter(adapter)

    }

    override fun setUpListeners() {
        super.setUpListeners()
    }

    override fun setUpObservers() {
        binding.edtChoose.setOnTouchListener(View.OnTouchListener { v, event ->
            binding.edtChoose.showDropDown()
            false
        })

        binding.edtChoose.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            setUpData()
        })
        binding.btnCapture.setOnClickListener {
            val responseXml: String = """<PidOptions ver=""><Opts env="P" fCount="" fType="" iCount="" iType="" pCount="" pType="" format="" pidVer="" timeout="" otp="" wadh="" posh=""/><Demo></Demo><CustOpts></CustOpts></PidOptions>
"""

            val intent = Intent("in.gov.uidai.rdservice.fp.CAPTURE")
            intent.setPackage(selectPackageName)
            intent.putExtra("PID_OPTIONS", responseXml)
            startActivityForResult(intent, AUTHENTICATION_REQUEST)
        }
    }

    private fun setUpData() {
        when (binding.edtChoose.text.toString()) {
            "Mantra RD Service" -> {
                selectPackageName = " com.mantra.rdservice"
            }
            "Evolute RD Service" -> {
                selectPackageName = "com.evolute.rdservice"
            }
            "SecuGen RD Service" -> {
                selectPackageName = "com.secugen.rdservice"
            }
            "Mantra Management Client" -> {
                selectPackageName = "com.mantra.clientmanagement"
            }
            "Iris RD Service" -> {
                selectPackageName = "com.iritech.rdservice"
            }
            "Aratek A600 RD Service" -> {
                selectPackageName = "co.aratek.asix_gms.rdservice"
            }
            "Morpho SCL RDService" -> {
                selectPackageName = "com.scl.rdservice"
            }
            "BioEnable Nitgen RDService" -> {
                selectPackageName = "in.bioenable.rdservice"
            }
            "PB510 RDService" -> {
                selectPackageName = "com.precision.pb510.rdservice"
            }
            "ACPL FM220 Registered Device" -> {
                selectPackageName = "com.acpl.registersdk"
            }
            "Tatvik TMF20 RDService" -> {
                selectPackageName = "com.tatvik.bio.tmf20"
            }
            "MIS100V2 RDService" -> {
                selectPackageName = "com.mantra.mis100v2.rdservice"
            }
        }


    }

    private fun getAllPackageName() {
        val pm: PackageManager = packageManager
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }
        for (resolveInfo in resolvedInfos) {
            val packageName = resolveInfo.activityInfo.packageName
            packageNames.add(packageName)
            when (packageName) {
                "com.mantra.rdservice" -> {
                    installListData.add("Mantra RD Service")
                }

                "com.evolute.rdservice" -> {
                    installListData.add("Evolute RD Service")
                }

                "com.secugen.rdservice" -> {
                    installListData.add("SecuGen RD Service")
                }

                "com.mantra.clientmanagement" -> {
                    installListData.add("Mantra Management Client")
                }

                "com.iritech.rdservice" -> {
                    installListData.add("Iris RD Service")
                }

                "co.aratek.asix_gms.rdservice" -> {
                    installListData.add("Aratek A600 RD Service")
                }

                "com.scl.rdservice" -> {
                    installListData.add("Morpho SCL RDService")
                }

                "in.bioenable.rdservice" -> {
                    installListData.add("BioEnable Nitgen RDService")
                }

                "com.precision.pb510.rdservice" -> {
                    installListData.add("PB510 RDService")
                }

                "com.acpl.registersdk" -> {
                    installListData.add("ACPL FM220 Registered Device")
                }

                "com.tatvik.bio.tmf20" -> {
                    installListData.add("Tatvik TMF20 RDService")
                }

                "com.mantra.mis100v2.rdservice" -> {
                    installListData.add("MIS100V2 RDService")
                }
                /* "" -> {
                     packageNames.add("Anonymous RD Service")
                 }*/
            }

        }
        /*if (installListData.size != 0) {
            installListData.add("Anonymous RD Service")
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AUTHENTICATION_REQUEST) {
            if (resultCode == RESULT_OK) {
                var bundle = data?.extras
                if (bundle != null) {
                    val pidData: String = bundle.getString("PID_DATA")!!
                    val dnc = bundle.getString("DNC", "")
                    val dnr = bundle.getString("DNR", "")
                }
            }
        }
    }
}