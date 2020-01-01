package org.ranapat.examples.githubbrowser.ui.organization

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_organization.*
import kotlinx.android.synthetic.main.fragment_top_menu_generic.*
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.Settings
import org.ranapat.examples.githubbrowser.data.entity.Configuration
import org.ranapat.examples.githubbrowser.data.entity.Organization
import org.ranapat.examples.githubbrowser.ui.BaseActivity
import org.ranapat.examples.githubbrowser.ui.common.IntentParameters
import org.ranapat.examples.githubbrowser.ui.common.OnItemListener
import org.ranapat.examples.githubbrowser.ui.common.States.*
import org.ranapat.examples.githubbrowser.ui.util.hideSoftKeyboard
import org.ranapat.examples.githubbrowser.ui.util.startCleanRedirect
import org.ranapat.examples.githubbrowser.ui.util.startRedirect
import org.ranapat.examples.githubbrowser.ui.util.subscribeUiThread
import java.util.concurrent.TimeUnit

class OrganizationActivity : BaseActivity() {
    private lateinit var viewModel: OrganizationViewModel
    private lateinit var nextActivity: Class<out AppCompatActivity>

    override val layoutResource: Int = R.layout.activity_organization
    override fun baseViewModel() = viewModel

    private var upToLimit: Boolean = true

    private val listAdapter: ListAdapter
        get() = recyclerView.adapter as ListAdapter

    private lateinit var configuration: Configuration
    private lateinit var organization: Organization

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(OrganizationViewModel::class.java)

        super.onCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val organization = intent.getSerializableExtra(IntentParameters.ORGANIZATION) as Organization
        viewModel.initialize(organization)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.organization, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(2)?.isEnabled = !upToLimit
        menu?.getItem(3)?.isEnabled = upToLimit

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_name_asc -> {
                viewModel.sortByNameAsc()
            }
            R.id.sort_by_name_desc -> {
                viewModel.sortByNameDesc()
            }
            R.id.show_up_to_limit -> {
                viewModel.showUpToLimit()
            }
            R.id.show_up_to_no_limit -> {
                viewModel.showUpToNoLimit()
            }
        }
        return true
    }

    override fun initialize() {
        super.initialize()

        recyclerView.adapter = ListAdapter()

        invalidateOptionsMenu()
    }

    override fun initializeUi() {
        setSupportActionBar(toolbar)

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

                    headerTitle.text = it.login
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

        headerBack.setOnClickListener {
            onBackPressed()
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