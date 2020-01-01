package org.ranapat.examples.githubbrowser.ui.organization

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user_member_list.view.*
import org.ranapat.examples.githubbrowser.R.string
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    private var user: ListUser? = null

    private val extraItemSpacing: View = view.extraItemSpacing
    private val avatar: ImageView = view.avatar
    private val login: TextView = view.login
    private val date: TextView = view.date
    private val progressBar: ProgressBar = view.progressBar
    private val incomplete: ImageView = view.incomplete

    fun bind(
            user: ListUser,
            position: Int
    ) {
        setUser(user)
        setPosition(position)
    }

    fun setUser(user: ListUser) {
        this.user = user

        if (user.user.details != null) {
            login.text = "${user.user.login} - ${user.user.details.name}"
        } else {
            login.text = user.user.login
        }

        if (user.incomplete) {
            progressBar.isVisible = true
            date.isVisible = false
            incomplete.isVisible = true
        } else {
            val dateString = dateToString(user.user.details?.remoteCreatedAt)
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
        }

        setContentDescriptions(user)
        setAvatar(user)
    }

    private fun setContentDescriptions(user: ListUser) {
        avatar.contentDescription = view.context.getString(string.user_member_list_icon) + " " + user.user.login
    }

    private fun setPosition(position: Int) {
        extraItemSpacing.visibility = if (position == 0) View.VISIBLE else View.GONE
    }

    private fun setAvatar(user: ListUser) {
        Glide.with(view)
                .load(user.user.avatarUrl)
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