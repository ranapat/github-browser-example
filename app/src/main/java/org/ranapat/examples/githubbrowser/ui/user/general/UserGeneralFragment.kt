package org.ranapat.examples.githubbrowser.ui.user.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_user_general.*
import org.jetbrains.anko.support.v4.runOnUiThread
import org.ranapat.examples.githubbrowser.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class UserGeneralFragment : Fragment() {
    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    private lateinit var viewModel: UserGeneralViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this).get(UserGeneralViewModel::class.java)

        return inflater.inflate(R.layout.fragment_user_general, container, false)
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
        subscription(viewModel.user
                .subscribe {
                    runOnUiThread {
                        Glide.with(view!!)
                                .load(it.avatarUrl)
                                .into(avatar)

                        login.text = getString(R.string.user_general_tab_login).format(it.login)
                        name.text = getString(R.string.user_general_tab_name).format(it.details?.name)
                        createdAt.text = getString(R.string.user_general_tab_created_at).format(dateToString(it.details?.remoteCreatedAt))
                        location.text = getString(R.string.user_general_tab_location).format(it.details?.location)
                        company.text = getString(R.string.user_general_tab_company).format(it.details?.company)
                        publicRepos.text = getString(R.string.user_general_tab_public_repos).format(it.details?.publicRepos)
                        followers.text = getString(R.string.user_general_tab_followers).format(it.details?.followers)
                        publicGists.text = getString(R.string.user_general_tab_public_gitsts).format(it.details?.publicGists)
                        bio.text = it.details?.bio
                    }
                }
        )
    }

    private fun subscription(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    private fun dateToString(date: Date?): String {
        return if (date != null) {
            val formatterUTC: DateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            formatterUTC.timeZone = TimeZone.getTimeZone("UTC")

            formatterUTC.format(date)
        } else {
            ""
        }
    }

}