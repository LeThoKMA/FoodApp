package com.example.footapp.ui.user

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.footapp.R
import com.example.footapp.interface1.UserInterface
import com.example.footapp.databinding.ItemUserBinding
import com.example.footapp.model.User
import com.example.footapp.presenter.UserPresenter

class UserAdapter(
    var list: ArrayList<User?>,
    var callback: UserInterface?,
    var userPresenter: UserPresenter
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_user,
            parent,
            false
        ) as ItemUserBinding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (list[position] != null) {
            callback?.let {
                (holder as ViewHolder).bind(
                    list[position]!!,
                    it,
                    position,
                    userPresenter
                )
            }
        } else {

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, callback: UserInterface, position: Int, presenter: UserPresenter) {
            binding.tvName.text = user.name
            binding.tvSalary.text = user.salary.toString()
            binding.imvDel.setOnClickListener {
                presenter.deleteUser(position,user)
                callback.notify("Đã xóa")
            }
            binding.parent.setOnClickListener {
                var intent = Intent(binding.root.context, UserDetailActivity::class.java)
                intent.putExtra("user", user)
                binding.root.context.startActivity(intent)
            }
        }

    }

    fun deleteUser(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)

    }

}