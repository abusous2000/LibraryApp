package com.sample.libraryapplication.utils

import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Html
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.sample.libraryapplication.view.MainActivity

fun showColoredToast(info: String){
    val mainActivity = ActivityWeakMapRef.get(MainActivity.TAG) as MainActivity
    var toast = Toast.makeText(mainActivity.baseContext,
            Html.fromHtml("<font color='red' ><b>" + info + "</b></font>", Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG)
    toast.show()

}
class CommonUtils{
    companion object{
        var cnt = 0;
        val images = arrayListOf<String>("https://www.palestineremembered.com/Acre/al-Bassa/Picture907.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture909.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture913.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture917.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture176.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture2983.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture2985.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture2987.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture7177.jpg",
                "https://www.palestineremembered.com/Acre/al-Bassa/Picture7179.jpg")

        fun getRandomImageURL(): String? {
            return images.get((cnt++) % images.size)
        }
        fun initWebView(webView: WebView, fragment: Fragment? = null){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setHorizontalScrollBarEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true)
            webView.getSettings().setUseWideViewPort(true);
            webView.setInitialScale(180);
            fragment?.let {
                webView.webViewClient = WebViewClientImpl(fragment)
            }
            val handler: Handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(message: Message) {
                    when (message.what) {
                        1 -> {
                            webView.goBack()
                        }
                    }
                }
            }

            webView.setOnKeyListener(object : View.OnKeyListener {

                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                        handler.sendEmptyMessage(1);
                        return true;
                    }

                    return false;
                }

            });
        }
    }
    class WebViewClientImpl(fragment: Fragment) : WebViewClient() {
        private var fragment: Fragment? = null
        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            if (url.toLowerCase().indexOf("palestineremembered.com") > -1) return false
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            fragment!!.startActivity(intent)
            return true
        }
        override fun onReceivedSslError(
            view: WebView?, handler: SslErrorHandler, error: SslError
                                       ) {
            when (error.primaryError) {
                SslError.SSL_UNTRUSTED   -> Log.d(TAG, "SslError : The certificate authority is not trusted.")
                SslError.SSL_EXPIRED     -> Log.d(TAG, "SslError : The certificate has expired.")
                SslError.SSL_IDMISMATCH  -> Log.d(TAG, "The certificate Hostname mismatch.")
                SslError.SSL_NOTYETVALID -> Log.d(TAG, "The certificate is not yet valid.")
            }
            handler.proceed()
        }
        init {
            this.fragment = fragment
        }

        companion object {
            const val TAG = "CommonUtils"
        }
    }
}