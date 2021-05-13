package com.example.shoppinglist.ui.shoppinglist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.databinding.ActivityShoppingBinding
import com.example.shoppinglist.other.ShoppingItemAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private val viewModel: ShoppingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityShoppingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val shoppingItemAdapter = ShoppingItemAdapter(listOf(), viewModel).apply { }

        binding.rvShoppingItems.run {
            layoutManager = LinearLayoutManager(this@ShoppingActivity)
            adapter = shoppingItemAdapter
        }

        viewModel.getAllShoppingItems().observe(this) {
            shoppingItemAdapter.shoppingItems = it
            shoppingItemAdapter.notifyDataSetChanged()
        }

        binding.floatingActionButton.setOnClickListener {
            AddShoppingItemDialog(this@ShoppingActivity, object : AddDialogListener {
                override fun onAddButtonClick(item: ShoppingItem) {
                    viewModel.insert(item)
                }
            }).show()
        }
    }
}