package org.ranapat.examples.githubbrowser.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user_member_list.view.*
import org.ranapat.examples.githubbrowser.R.string
import org.ranapat.examples.githubbrowser.data.entity.User
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private var user: User? = null

    private val extraItemSpacing: View = view.extraItemSpacing
    private val avatar: ImageView = view.avatar
    private val login: TextView = view.login
    private val date: TextView = view.date
    private val progressBar: ProgressBar = view.progressBar
    private val incomplete: ImageView = view.incomplete

    fun bind(
            user: User,
            position: Int,
            incomplete: Boolean
    ) {
        setUser(user)
        setPosition(position)
        if (incomplete) {
            setIncomplete()
        }
    }

    fun setUser(user: User) {
        this.user = user

        login.text = user.login

        val dateString = dateToString(user.details?.remoteCreatedAt)
        if (dateString.isNotEmpty()) {
            progressBar.isVisible = false
            date.isVisible = true
            incomplete.isVisible = false
        } else {
            progressBar.isVisible = true
            date.isVisible = false
            incomplete.isVisible = false
        }
        date.text = dateString

        setContentDescriptions(user)
        setAvatar(user)
    }

    fun setIncomplete() {
        progressBar.isVisible = true
        date.isVisible = false
        incomplete.isVisible = true
    }

    private fun setContentDescriptions(user: User) {
        avatar.contentDescription = view.context.getString(string.user_member_list_icon) + " " + user.login
    }

    private fun setPosition(position: Int) {
        extraItemSpacing.visibility = if (position == 0) View.VISIBLE else View.GONE
    }

    private fun setAvatar(user: User) {
        Glide.with(view)
                .load(user.avatarUrl)
                .into(avatar)
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