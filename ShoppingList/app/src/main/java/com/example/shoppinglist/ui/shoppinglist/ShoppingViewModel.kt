package com.example.shoppinglist.ui.shoppinglist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.db.entities.ShoppingItem
import com.example.shoppinglist.data.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(private val repository: ShoppingRepository) : ViewModel() {

    fun insert(item: ShoppingItem) =
        viewModelScope.launch(Dispatchers.IO) { repository.insert(item) }

    fun update(item: ShoppingItem) = viewModelScope.launch(Dispatchers.IO) { repository.update(item) }

    fun delete(item: ShoppingItem) =
        viewModelScope.launch(Dispatchers.IO) { repository.delete(item) }

    fun getAllShoppingItems() = repository.getAllShoppingItems()
}