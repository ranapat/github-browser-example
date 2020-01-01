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
    private var incomplete: ArrayList<User> = arrayListOf()

    fun setUsers(it: List<User>) {
        users = ArrayList(it)
        incomplete.clear()

        notifyDataSetChanged()
    }

    fun setUser(it: User) {
        val index = findIndexById(it, users)
        if (index == -1) {
            users.add(it)
        } else {
            users[index] = it
        }
        notifyItemChanged(users.indexOf(it), POPULATE_USER)
    }

    fun setIncomplete(it: User) {
        val index = findIndexById(it, incomplete)
        if (index == -1) {
            incomplete.add(it)
        } else {
            incomplete[index] = it
        }
        notifyItemChanged(users.indexOf(it), POPULATE_INCOMPLETE)
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
                position,
                findIndexById(user, incomplete) != -1
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
                    POPULATE_INCOMPLETE -> holder.setIncomplete()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    private fun findIndexById(user: User, collection: ArrayList<User>): Int {
        for ((index, value) in collection.withIndex()) {
            if (value.id == user.id) {
                return index
            }
        }
        return -1
    }

    private fun initializeEventHandlers(viewHolder: ItemViewHolder) {
        viewHolder.itemView.setOnClickListener {
            val position = viewHolder.adapterPosition
            onItemClickListener?.onItem(position)
        }
    }

    companion object {
        private const val POPULATE_USER = "populateUser"
        private const val POPULATE_INCOMPLETE = "populateIncomplete"
    }
}