package org.ranapat.examples.githubbrowser.ui.user.repos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_user_repos.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.json.JSONArray
import org.ranapat.examples.githubbrowser.R
import java.net.URL

class UserReposFragment : Fragment() {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private lateinit var viewModel: UserReposViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(UserReposViewModel::class.java)

        return inflater.inflate(R.layout.fragment_user_repos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeListeners()
    }

    override fun onStart() {
        super.onStart()

        viewModel.initialize()
    }

    override fun onStop() {
        super.onStop()

        compositeDisposable.clear()
        viewModel.viewStopped()
    }

    override fun onPause() {
        super.onPause()

        compositeDisposable.clear()
        viewModel.viewStopped()
    }

    private fun initializeListeners() {
        subscription(viewModel.url
                .subscribe {
                    try {
                        val jsonArray = JSONArray(URL(it).readText())

                        if (isAdded) {
                            runOnUiThread {
                                if (isAdded) {
                                    rv_json.bindJson(jsonArray)
                                    error.isVisible = false
                                }
                            }
                        }
                    } catch (e:Exception) {
                        if (isAdded) {
                            runOnUiThread {
                                if (isAdded) {
                                    error.isVisible = true
                                }
                            }
                        }
                    }
                }
        )
    }

    private fun subscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}