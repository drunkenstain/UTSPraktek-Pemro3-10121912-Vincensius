package com.example.uts.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.uts.R

class FindmeFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_find_me, container, false)

        webView = view.findViewById(R.id.webView)
        val buttonFindMe: Button = view.findViewById(R.id.buttonFindMe)
        buttonFindMe.setOnClickListener {
            loadMap()
        }

        return view
    }

    private fun loadMap() {
        val bingMapsApiKey = "AqOhU4NLfOfQ0TueOPW9b0twQeHNLUNsRGqrDGnQu8R9B9Q-RRF-m92ERRuO2oLQ"
        val bingMapsUrl = "https://www.bing.com/maps/embed?h=400&w=500&cp=-6.9175~107.6191&lvl=15&typ=d&sty=r&src=SHELL&FORM=MBEDV8&key=$bingMapsApiKey"

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(bingMapsUrl)
    }
}
