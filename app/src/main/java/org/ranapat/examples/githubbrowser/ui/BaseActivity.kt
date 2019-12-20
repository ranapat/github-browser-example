package org.ranapat.examples.githubbrowser.ui

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.ranapat.examples.githubbrowser.management.NetworkManager
import org.ranapat.examples.githubbrowser.ui.notification.OnScreenInformation
import org.ranapat.examples.githubbrowser.ui.notification.TopSnackbar
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessages.LABEL
import org.ranapat.examples.githubbrowser.ui.publishable.ParameterizedMessages.MESSAGE
import org.ranapat.instancefactory.Fi

abstract class BaseActivity @JvmOverloads constructor(
        private val networkManager: NetworkManager = Fi.get(NetworkManager::class.java)
) : AppCompatActivity() {

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    protected lateinit var onScreenInformation: OnScreenInformation

    protected abstract val layoutResource: Int
    protected abstract fun baseViewModel(): BaseViewModel?
    protected abstract fun initializeUi()
    protected abstract fun initializeListeners()

    private var networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            baseViewModel()?.onNetworkStatus()
        }

        override fun onCapabilitiesChanged(network: Network?, networkCapabilities: NetworkCapabilities?) {
            super.onCapabilitiesChanged(network, networkCapabilities)

            if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                baseViewModel()?.onNetworkStatus()
            }
        }

        override fun onLost(network: Network?) {
            super.onLost(network)

            baseViewModel()?.onNetworkStatus()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutResource)

        networkManager.registerNetworkChangeCallback(networkCallback)

        initialize()
        initializeOnScreenInformation()
        initializeUi()
        initializeListeners()
    }

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
        networkManager.unregisterNetworkChangeCallback(networkCallback)
    }

    protected open fun initialize() {
        //
    }

    protected fun initializeOnScreenInformation() {
        onScreenInformation = TopSnackbar(this, resources)

        val baseViewModel = baseViewModel()
        if (baseViewModel != null) {
            subscription(baseViewModel.messages.info
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.warning(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.success
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.success(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.apply
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.apply(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.get(MESSAGE)!!.message,
                                    it.get(LABEL)!!.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.warning
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.warning(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.warningIndefinite
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.warningIndefinite(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.error
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.error(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.errorIndefinite
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.errorIndefinite(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.message
                            )
                        }
                    })
            subscription(baseViewModel.messages.errorWithAction
                    .subscribe {
                        runOnUiThread {
                            onScreenInformation.errorWithAction(
                                    window.decorView.findViewById(android.R.id.content),
                                    it.get(MESSAGE)!!.message,
                                    it.get(LABEL)!!.message
                            )
                        }
                    })
        }
    }

    protected fun subscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}