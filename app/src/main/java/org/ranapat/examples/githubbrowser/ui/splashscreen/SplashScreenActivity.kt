package org.ranapat.examples.githubbrowser.ui.splashscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.Settings
import org.ranapat.examples.githubbrowser.ui.BaseActivity
import org.ranapat.examples.githubbrowser.ui.common.States.*
import org.ranapat.examples.githubbrowser.ui.util.startCleanRedirect
import org.ranapat.examples.githubbrowser.ui.util.startRedirect
import org.ranapat.examples.githubbrowser.ui.util.subscribeUiThread
import java.util.concurrent.TimeUnit

class SplashScreenActivity : BaseActivity() {
    private lateinit var viewModel: SplashScreenViewModel
    private lateinit var nextActivity: Class<out AppCompatActivity>

    private var message: String = ""

    override val layoutResource: Int = R.layout.activity_splash_screen
    override fun baseViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SplashScreenViewModel::class.java)

        super.onCreate(savedInstanceState)

        viewModel.initialize()
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
                        REDIRECT -> startRedirect(nextActivity)
                        CLEAN_REDIRECT -> startCleanRedirect(nextActivity)
                    }
                }
        )
        subscription(viewModel.state
                .filter { !shallThrottle(it) }
                .subscribeUiThread(this) {
                    when (it) {
                        LOADING -> loading()
                        READY -> ready()
                        ERROR -> error(message)
                    }
                }
        )
        subscription(viewModel.next
                .subscribe {
                    nextActivity = it
                }
        )
        subscription(viewModel.message
                .subscribe {
                    message = it.message
                })
    }

    private fun loading() {
        var fragmentTransaction = supportFragmentManager.beginTransaction()

        val loading = LoadingFragment()

        fragmentTransaction.replace(R.id.fragmentContainer, loading)
        fragmentTransaction.commit()
    }

    private fun ready() {
        //
    }

    private fun error(message: String?) {
        var fragmentTransaction = supportFragmentManager.beginTransaction()

        val error = ErrorMessageFragment()

        var bundle = Bundle()
        bundle.putString("message", message)
        error.arguments = bundle

        fragmentTransaction.replace(R.id.fragmentContainer, error)
        fragmentTransaction.commit()
    }
}
