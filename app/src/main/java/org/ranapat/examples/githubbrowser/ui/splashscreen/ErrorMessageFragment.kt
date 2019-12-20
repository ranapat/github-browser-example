package org.ranapat.examples.githubbrowser.ui.splashscreen

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_error_message.view.*
import org.ranapat.examples.githubbrowser.R

class ErrorMessageFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_error_message, container, false)

        view.message.text = arguments!!.getString("message", "")
        view.retry.setOnClickListener {
            val intent = Intent()
            intent.action = ConnectivityManager.EXTRA_NETWORK_TYPE

            activity?.sendBroadcast(intent)
        }
        view.errorIcon.setOnClickListener {
            //
        }

        return view
    }
}
