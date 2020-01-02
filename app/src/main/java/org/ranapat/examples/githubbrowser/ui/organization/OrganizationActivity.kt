package org.ranapat.examples.githubbrowser.ui.organization

import android.content.Intent
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
import kotlinx.android.synthetic.main.layout_loading_users_list.*
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.Settings
import org.ranapat.examples.githubbrowser.data.entity.Configuration
import org.ranapat.examples.githubbrowser.data.entity.Organization
import org.ranapat.examples.githubbrowser.data.entity.User
import org.ranapat.examples.githubbrowser.ui.BaseActivity
import org.ranapat.examples.githubbrowser.ui.common.IntentParameters
import org.ranapat.examples.githubbrowser.ui.common.OnItemListener
import org.ranapat.examples.githubbrowser.ui.common.States.*
import org.ranapat.examples.githubbrowser.ui.organization.OrganizationViewModel.*
import org.ranapat.examples.githubbrowser.ui.util.hideSoftKeyboard
import org.ranapat.examples.githubbrowser.ui.util.subscribeUiThread
import java.util.concurrent.TimeUnit

class OrganizationActivity : BaseActivity() {
    private lateinit var viewModel: OrganizationViewModel
    private lateinit var nextActivity: Class<out AppCompatActivity>
    private lateinit var nextUser: User

    override val layoutResource: Int = R.layout.activity_organization
    override fun baseViewModel() = viewModel

    private var sortBy: String = ""
    private var sortDirection: String = ""
    private var limit: String = ""

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

    override fun onDestroy() {
        super.onDestroy()

        viewModel.viewDestroyed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.organization, menu)

        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.isEnabled = sortBy != SORT_BY_NAME
        menu?.getItem(1)?.isEnabled = sortBy != SORT_BY_PUBLIC_REPOS
        menu?.getItem(2)?.isEnabled = sortBy != SORT_BY_FOLLOWERS
        menu?.getItem(3)?.isEnabled = sortBy != SORT_BY_PUBLIC_GISTS

        menu?.getItem(4)?.isEnabled = sortDirection != SORT_DIRECTION_ASC
        menu?.getItem(5)?.isEnabled = sortDirection != SORT_DIRECTION_DESC

        menu?.getItem(6)?.isEnabled = limit != UP_TO_LIMIT
        menu?.getItem(7)?.isEnabled = limit != NO_LIMIT

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_name -> {
                viewModel.sortBy(SORT_BY_NAME)
            }
            R.id.sort_by_public_repos -> {
                viewModel.sortBy(SORT_BY_PUBLIC_REPOS)
            }
            R.id.sort_by_followers -> {
                viewModel.sortBy(SORT_BY_FOLLOWERS)
            }
            R.id.sort_by_public_gists -> {
                viewModel.sortBy(SORT_BY_PUBLIC_GISTS)
            }
            R.id.sort_asc -> {
                viewModel.sortDirection(SORT_DIRECTION_ASC)
            }
            R.id.sort_desc -> {
                viewModel.sortDirection(SORT_DIRECTION_DESC)
            }
            R.id.show_up_to_limit -> {
                viewModel.showUpToLimit()
            }
            R.id.no_limit -> {
                viewModel.showNoLimit()
            }
        }
        return true
    }

    override fun initialize() {
        super.initialize()

        recyclerView.adapter = ListAdapter()
    }

    override fun initializeUi() {
        loadingUserList.isVisible = true

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
        subscription(viewModel.nextUser
                .subscribe {
                    nextUser = it
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
        subscription(viewModel.sortBy
                .subscribeUiThread(this) {
                    sortBy = it

                    listAdapter.setSearchBy(it)

                    invalidateOptionsMenu()
                }
        )
        subscription(viewModel.sortDirection
                .subscribeUiThread(this) {
                    sortDirection = it

                    invalidateOptionsMenu()
                }
        )
        subscription(viewModel.limit
                .subscribeUiThread(this) {
                    limit = it

                    invalidateOptionsMenu()
                }
        )
        subscription(viewModel.loading
                .subscribeUiThread(this) {
                    if (it.complete) {
                        loadingUserListContainer.isVisible = false
                    } else {
                        textView.text = getString(R.string.user_member_list_loading_in_progress_details).format(
                                it.loaded, it.total, it.failed
                        )
                    }
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

    private fun navigate() {
        val launchIntent = Intent()
        launchIntent.setClass(applicationContext, nextActivity)
        launchIntent.putExtra(IntentParameters.USER, nextUser as User)
        startActivity(launchIntent)

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}