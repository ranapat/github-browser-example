package org.ranapat.examples.githubbrowser.ui.user.general

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import org.ranapat.examples.githubbrowser.R

class UserGeneralFragment() : Fragment() {
    private lateinit var viewModel: UserGeneralViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(UserGeneralViewModel::class.java)

        super.onCreate(savedInstanceState)

        viewModel.initialize()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_general, container, false)
    }
}