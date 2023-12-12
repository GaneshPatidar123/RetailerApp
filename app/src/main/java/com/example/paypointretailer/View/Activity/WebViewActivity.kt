package com.example.paypointretailer.View.Activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.example.paypointretailer.Base.BaseActivity
import com.example.paypointretailer.R
import com.example.paypointretailer.Utils.AppConstant
import com.example.paypointretailer.databinding.ActivityWebViewBinding


class WebViewActivity : BaseActivity<ActivityWebViewBinding>(R.layout.activity_web_view) {
    private lateinit var gestureDetector: GestureDetector
    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews() {
        getDataFromIntents(intent)
      //  binding.webView.webViewClient = MyWebViewClient()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDataFromIntents(intent: Intent?) {
        val bundle = intent?.extras
        if (bundle != null) {
            var url = bundle.getString(AppConstant.IS_FROM)!!
            var isClick = bundle.getString("isClick")
            if (isClick.equals("Train") || isClick.equals("Jio Savan") ) {

                val pgURL: String = url.split('?')[0]
                val pgToken: String = url.split('?')[1]
                val pgContent = """
   <html><head></head><body><form id="PostForm" name="PostForm" action="$pgURL" method="POST"><input type="hidden" name="Token" value="$pgToken"></form></body></html><script language='javascript'>var vPostForm = document.PostForm;vPostForm.submit();</script>
""".trimIndent()
                try {
                   // binding.webView.webChromeClient = WebChromeClient()
                    binding.webView.webViewClient = WebViewClient()
                    binding.webView.settings.mixedContentMode =
                        WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                    binding.webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
                    val base64EncodedHtmlString = encodeToBase64(pgContent)
                    try {
                        val decodedBytes = Base64.decode(base64EncodedHtmlString, Base64.DEFAULT)
                        val decodedHtmlString = String(decodedBytes, Charsets.UTF_8)
                        binding.webView.settings.javaScriptEnabled = true
                        binding.webView.loadData(
                            "data:text/html;base64," + decodedHtmlString,
                            "text/html",
                            "UTF-8"
                        )
                       /* binding.webView.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                                // Handle URL loading as needed
                                return super.shouldOverrideUrlLoading(view, request)
                            }
                        }*/
                        // Continue with processing the decoded data
                    } catch (e: IllegalArgumentException) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                binding.webView.settings.javaScriptEnabled = true
                // Set a WebViewClient to handle redirects within the WebView
                binding.webView.webViewClient = WebViewClient()
                // Set a WebChromeClient to handle progress and other UI-related events
                binding.webView.webChromeClient = WebChromeClient()
                // Load a URL in the WebView

                /*binding.webView.webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                        // Handle URL loading as needed
                        return super.shouldOverrideUrlLoading(view, request)
                    }
                }*/
                binding.webView.loadUrl(url)
            }



        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encodeToBase64(input: String): String {
         return Base64.encodeToString(input.toByteArray(), Base64.DEFAULT)

    }

    override fun setUpListeners() {
        /*gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
             override fun onSingleTapConfirmed(p0: MotionEvent): Boolean {
                // Handle the click event
                // You can perform actions when a click is detected in the WebView
                return super.onSingleTapConfirmed(p0!!)
            }
        })
        binding.webView.setOnTouchListener { _, event ->
            // Pass the touch event to the GestureDetector for processing
            gestureDetector.onTouchEvent(event)
        }*/
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            // If there is history in the WebView, go back
            binding.webView.goBack()
        } else {
            // If there is no history, handle the back press as usual
            super.onBackPressed()
        }
    }
    private inner class MyWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            // Get the URL of the clicked link
            val url = request?.url.toString()

            // Get the position of the click (if needed)
            val position = getPositionFromUrl(url)

            // Handle the click based on the URL and position
            handleUrlClick(url, position)

            // Return true to indicate that the WebView should not handle the URL
            return true
        }
    }
    private fun getPositionFromUrl(url: String): Int {
        // Implement your logic to extract position information from the URL
        // For example, if your URLs contain positions like "https://example.com/?position=3"
        // you can extract the position parameter and convert it to an integer
        // Return the extracted position or a default value if not found
        Log.d("TAG", "getPositionFromUrl: "+url)
        return 0
    }
    private fun handleUrlClick(url: String, position: Int) {
        // Implement your logic to handle the URL click based on the URL and position
        // For example, open a new activity or perform some other action
        Log.d("TAG", "getPositionFromUrl: "+url+ position)
    }
}