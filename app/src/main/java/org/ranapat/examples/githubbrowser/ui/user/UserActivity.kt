package org.ranapat.examples.githubbrowser.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.fragment_top_menu_generic.*
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.Settings
import org.ranapat.examples.githubbrowser.data.entity.User
import org.ranapat.examples.githubbrowser.ui.BaseActivity
import org.ranapat.examples.githubbrowser.ui.common.IntentParameters
import org.ranapat.examples.githubbrowser.ui.common.States.*
import org.ranapat.examples.githubbrowser.ui.util.subscribeUiThread
import java.util.concurrent.TimeUnit

class UserActivity : BaseActivity() {
    private lateinit var viewModel: UserViewModel
    private lateinit var nextActivity: Class<out AppCompatActivity>

    override val layoutResource: Int = R.layout.activity_user
    override fun baseViewModel() = viewModel

    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

        super.onCreate(savedInstanceState)

        @Suppress("UNCHECKED_CAST")
        val user = intent.getSerializableExtra(IntentParameters.USER) as User
        viewModel.initialize(user)
    }

    override fun initialize() {
        super.initialize()

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.user_general_tab)))

        pager.adapter = PagerAdapter(supportFragmentManager, tabLayout.tabCount)
    }

    override fun initializeUi() {
        setSupportActionBar(toolbar)

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
    }

    override fun initializeListeners() {
        subscription(viewModel.state
                .filter { shallThrottle(it) }
                .throttleFirst(Settings.debounceNavigationInMilliseconds, TimeUnit.MILLISECONDS)
                .subscribeUiThread(this) {
                    //
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
        subscription(viewModel.user
                .subscribeUiThread(this) {
                    user = it

                    headerTitle.text = it.login
                }
        )

        headerBack.setOnClickListener {
            onBackPressed()
        }

        pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //
            }

        })
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