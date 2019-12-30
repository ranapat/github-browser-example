package org.ranapat.examples.githubbrowser.ui.main

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
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
    private val icon: ImageView = view.icon
    private val title: TextView = view.title
    private val status: TextView = view.status
    private val progressBar: ProgressBar = view.progressBar

    fun bind(
            user: User,
            position: Int
    ) {
        setUser(user)
        setPosition(position)
    }

    fun setUser(user: User) {
        this.user = user

        title.text = user.login
        status.text = user.type

        setContentDescriptions(user)
        setAvatar(user)
    }

    private fun setContentDescriptions(user: User) {
        icon.contentDescription = view.context.getString(string.user_member_list_icon) + " " + user.login
    }

    private fun setPosition(position: Int) {
        extraItemSpacing.visibility = if (position == 0) View.VISIBLE else View.GONE
    }

    private fun setAvatar(user: User) {
        Glide.with(view)
                .load(user.avatarUrl)
                .into(icon)
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