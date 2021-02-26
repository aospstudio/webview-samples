package com.aospstudio.sample.webview

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.net.http.SslError
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebSettingsCompat.DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
import androidx.webkit.WebViewClientCompat
import androidx.webkit.WebViewFeature
import com.aospstudio.sample.webview.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.Bitmap

class MainActivity : AppCompatActivity() {

    private class MyWebViewClient : WebViewClientCompat() {
        override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler, error: SslError) {
            /*
            Create custom SSL Error page
            */
        }

        override fun onReceivedError(
            view: WebView?,
            errorCod: Int,
            description: String?,
            failingUrl: String?
        ) {
            /*
            Create custom WebView Error page
             */
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("market://")
                || url.startsWith("intent://")
                || url.startsWith("linkedin://")
                || url.startsWith("medium://")
                || url.startsWith("fb-messenger://")
                || url.startsWith("sharesample://")
                || url.startsWith("skype://")
                || url.startsWith("whatsapp://")
                || url.startsWith("twitter://")
                || url.startsWith("dlive://")
                || url.startsWith("tg://")
                || url.startsWith("tel:")
                || url.startsWith("sms:")
                || url.startsWith("mailto:")
                || url.startsWith("https://m.me/")
            ) {
                /*
                 Create custom start intent
                 */
            } else {
                view.loadUrl(url)
                return true
            }
            return true
        }

        override fun onPageStarted(view: WebView?, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }
    }

    private class MyChromeClient : WebChromeClient() {
        override fun onJsConfirm(
            view: WebView?,
            url: String?,
            message: String?,
            result: JsResult
        ): Boolean {
            /*
            Create custom JS confirm
             */
            return true
        }

        override fun onGeolocationPermissionsShowPrompt(
            origin: String?,
            callback: GeolocationPermissions.Callback
        ) {
            callback.invoke(origin, true, false)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nightModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlag == Configuration.UI_MODE_NIGHT_YES) {
            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(
                    binding.webview.settings,
                    WebSettingsCompat.FORCE_DARK_ON
                )
            }

            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK_STRATEGY)) {
                WebSettingsCompat.setForceDarkStrategy(
                    binding.webview.settings,
                    DARK_STRATEGY_PREFER_WEB_THEME_OVER_USER_AGENT_DARKENING
                )
            }
        }

        binding.webview.webViewClient = MyWebViewClient()
        binding.webview.webChromeClient = MyChromeClient()

        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.javaScriptCanOpenWindowsAutomatically = true
        binding.webview.settings.mediaPlaybackRequiresUserGesture = true
        binding.webview.settings.useWideViewPort = true
        binding.webview.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING
        binding.webview.settings.loadsImagesAutomatically = true
        binding.webview.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        binding.webview.settings.setGeolocationEnabled(true)
        binding.webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        binding.webview.setInitialScale(1)
        binding.webview.isFocusable = true
        binding.webview.isFocusableInTouchMode = true
        binding.webview.requestFocus(View.FOCUSABLES_ALL)
        binding.webview.loadUrl("https://google.com/")
    }

    override fun onResume() {
        if (webview != null) {
            webview.onResume();
            webview.resumeTimers();
        }
        super.onResume()
    }

    override fun onPause() {
        if (webview != null) {
            webview.onPause()
        }
        super.onPause()
    }

    override fun onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack()
        } else {
            webview.destroy()
            finish()
        }
    }
}
