package org.ranapat.examples.githubbrowser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.Settings
import org.ranapat.examples.githubbrowser.data.entity.Organization
import org.ranapat.examples.githubbrowser.ui.BaseActivity
import org.ranapat.examples.githubbrowser.ui.common.IntentParameters
import org.ranapat.examples.githubbrowser.ui.common.States.*
import org.ranapat.examples.githubbrowser.ui.util.hideSoftKeyboard
import org.ranapat.examples.githubbrowser.ui.util.subscribeUiThread
import java.io.Serializable
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var nextActivity: Class<out AppCompatActivity>

    override val layoutResource: Int = R.layout.activity_main
    override fun baseViewModel() = viewModel

    private lateinit var organization: Organization

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }

    override fun onResume() {
        super.onResume()

        search.setText("")
    }

    override fun initializeUi() {
        //
    }

    override fun initializeListeners() {
        subscription(viewModel.state
                .filter { shallThrottle(it) }
                .throttleFirst(Settings.debounceNavigationInMilliseconds, TimeUnit.MILLISECONDS)
                .subscribeUiThread(this) {
                    when (it) {
                        NAVIGATE -> navigate()
                    }
                }
        )
        subscription(viewModel.state
                .filter { !shallThrottle(it) }
                .subscribeUiThread(this) {
                    when (it) {
                        LOADING -> loading()
                        READY -> ready()
                        ERROR -> error()
                    }
                }
        )
        subscription(viewModel.next
                .subscribe {
                    nextActivity = it
                }
        )
        subscription(viewModel.organization
                .subscribeUiThread(this) {
                    organization = it
                }
        )

        search.textChangedListener {
            afterTextChanged {
                clear.isVisible = search.text.toString().isNotEmpty()
            }
        }
        search.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.searchForOrganization(search.text.toString())
                activity.hideSoftKeyboard()

                return@OnKeyListener true
            }
            false
        })
        clear.setOnClickListener {
            search.setText("")
            activity.hideSoftKeyboard()
        }
    }

    private fun loading() {
        loading.isVisible = true
    }

    private fun ready() {
        loading.isVisible = false
    }

    private fun error() {
        loading.isVisible = false
    }

    private fun navigate() {
        ready()

        val launchIntent = Intent()
        launchIntent.setClass(applicationContext, nextActivity)
        launchIntent.putExtra(IntentParameters.ORGANIZATION, organization as Serializable)
        startActivity(launchIntent)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}