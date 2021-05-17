package com.example.shoppinglist.other

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.databinding.ShoppingItemBinding
import com.example.shoppinglist.ui.shoppinglist.ShoppingViewModel

class ShoppingItemAdapter(
    var shoppingItems: List<ShoppingItem>,
    private val shoppingViewModel: ShoppingViewModel
) : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingViewHolder>() {

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val binding = ShoppingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val clickListener = object : ShoppingItemClickHandler {
            override var shoppingItem: ShoppingItem? = null

            override fun onDeleteClick() {
                shoppingItem?.let { shoppingViewModel.delete(it) }
            }

            override fun onPlusClick() {
                shoppingItem?.let {
                    it.amount++
                    shoppingViewModel.update(it)
                }
            }

            override fun onMinusClick() {
                shoppingItem?.let {
                    if (it.amount > 0) {
                        it.amount--
                        shoppingViewModel.update(it)
                    }
                }
            }
        }

        return ShoppingViewHolder(binding, clickListener)
    }

    override fun getItemId(position: Int): Long {
        return shoppingItems[position].id
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        holder.bindTo(shoppingItems[position])
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    inner class ShoppingViewHolder(
        private val binding: ShoppingItemBinding,
        private val clickListener: ShoppingItemClickHandler
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.run {
                imageViewDelete.setOnClickListener { clickListener.onDeleteClick() }
                imageViewPlus.setOnClickListener { clickListener.onPlusClick() }
                imageViewMinus.setOnClickListener { clickListener.onMinusClick() }
            }
        }

        fun bindTo(item: ShoppingItem) {
            binding.shoppingItem = item
            clickListener.shoppingItem = item
        }
    }

    interface ShoppingItemClickHandler {
        var shoppingItem: ShoppingItem?

        fun onDeleteClick()
        fun onPlusClick()
        fun onMinusClick()
    }
}