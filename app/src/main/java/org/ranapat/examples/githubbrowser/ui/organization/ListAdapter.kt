package org.ranapat.examples.githubbrowser.ui.organization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.ranapat.examples.githubbrowser.R
import org.ranapat.examples.githubbrowser.ui.common.OnItemListener

class ListAdapter : RecyclerView.Adapter<ItemViewHolder>() {
    var onItemClickListener: OnItemListener? = null

    private var users: ArrayList<ListUser> = arrayListOf()

    fun setUsers(it: List<ListUser>) {
        users = ArrayList(it)

        notifyDataSetChanged()
    }

    fun setUser(it: ListUser) {
        val index = findIndexById(it, users)
        if (index == -1) {
            users.add(it)
        } else {
            users[index] = it
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

    private fun findIndexById(user: ListUser, collection: ArrayList<ListUser>): Int {
        for ((index, value) in collection.withIndex()) {
            if (value.user.id == user.user.id) {
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
    }
}