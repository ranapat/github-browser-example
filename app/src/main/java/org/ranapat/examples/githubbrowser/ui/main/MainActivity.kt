package org.ranapat.examples.githubbrowser.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.Settings
import org.ranapat.examples.githubbrowser.data.entity.Configuration
import org.ranapat.examples.githubbrowser.data.entity.Organization
import org.ranapat.examples.githubbrowser.data.entity.User
import org.ranapat.examples.githubbrowser.ui.BaseActivity
import org.ranapat.examples.githubbrowser.ui.common.OnItemListener
import org.ranapat.examples.githubbrowser.ui.common.States.*
import org.ranapat.examples.githubbrowser.ui.util.hideSoftKeyboard
import org.ranapat.examples.githubbrowser.ui.util.startCleanRedirect
import org.ranapat.examples.githubbrowser.ui.util.startRedirect
import org.ranapat.examples.githubbrowser.ui.util.subscribeUiThread
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var nextActivity: Class<out AppCompatActivity>

    override val layoutResource: Int = R.layout.activity_main
    override fun baseViewModel() = viewModel

    private val listAdapter: ListAdapter
        get() = recyclerView.adapter as ListAdapter

    private lateinit var configuration: Configuration
    private lateinit var organization: Organization

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }

    override fun initialize() {
        super.initialize()

        recyclerView.adapter = ListAdapter()
    }

    override fun initializeUi() {
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator = object : DefaultItemAnimator() {
            override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
                return true
            }
        }
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
                        ERROR -> error()
                    }
                }
        )
        subscription(viewModel.next
                .subscribe {
                    nextActivity = it
                }
        )
        subscription(viewModel.configuration
                .subscribeUiThread(this) {
                    configuration = it
                }
        )
        subscription(viewModel.organization
                .subscribeUiThread(this) {
                    organization = it
                }
        )
        subscription(viewModel.users
                .subscribeUiThread(this) {
                    listAdapter.setUsers(it)
                }
        )
        subscription(viewModel.user
                .subscribeUiThread(this) {
                    listAdapter.setUser(it)
                }
        )
        subscription(viewModel.incomplete
                .subscribeUiThread(this) {
                    listAdapter.setIncomplete(it)
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
            viewModel.clearOrganization()
            activity.hideSoftKeyboard()
        }

        listAdapter.onItemClickListener = OnItemListener { position ->
            viewModel.onItemClickListener(position)

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
}