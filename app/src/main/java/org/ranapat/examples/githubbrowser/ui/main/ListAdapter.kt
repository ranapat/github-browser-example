package org.ranapat.examples.githubbrowser.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.data.entity.User
import org.ranapat.examples.githubbrowser.ui.common.OnItemListener

class ListAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var onItemClickListener: OnItemListener? = null

    private var users: ArrayList<User> = arrayListOf()

    fun setUsers(it: List<User>) {
        users = ArrayList(it)

        notifyDataSetChanged()
    }

    fun setUser(it: User) {
        if (!users.contains(it)) {
            users.add(it)
        }
        notifyItemChanged(users.indexOf(it), POPULATE_USER)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(
                    R.layout.item_user_member_list, parent, false
            )).also { viewHolder ->
                initializeEventHandlers(viewHolder)
            }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val user = users[position]

        holder.bind(
                user,
                position
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            payloads.reversed().distinct().reversed().forEach {
                val payload = it
                val user = users[position]

                when (payload) {
                    POPULATE_USER -> holder.setUser(user)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    private fun initializeEventHandlers(viewHolder: ItemViewHolder) {
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClickListener?.onItem(position)
        }
    }

    companion object {
        private const val POPULATE_USER = "populateUser"
    }
}