package com.example.testapp.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.testapp.R
import com.example.testapp.network.ItemDescription
import com.example.testapp.network.TradeItem
import com.example.testapp.repository.AppRepository
import com.example.testapp.ui.home.data.Category
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TradeItemsViewModel(private val repository: AppRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _latest = MutableLiveData<List<TradeItem>>()
    val latest: LiveData<List<TradeItem>>
        get() = _latest

    private val _flashSale = MutableLiveData<List<TradeItem>>()
    val flashSale: LiveData<List<TradeItem>>
        get() = _flashSale

    private val _brands = MutableLiveData<List<TradeItem>>()
    val brands: LiveData<List<TradeItem>>
        get() = _brands

    private val _readyToShow = MutableLiveData<Boolean>()
    val readyToShow: LiveData<Boolean>
        get() = _readyToShow

    private val _itemDescription = MutableLiveData<ItemDescription>()
    val itemDescription: LiveData<ItemDescription>
        get() = _itemDescription

//    init {
//        getCategories()
//        getTradeItems()
//    }

    fun getCategories() {
        _categories.value = listOf(
            Category(R.string.phones, R.drawable.ic_phones),
            Category(R.string.headphones, R.drawable.ic_headphones),
            Category(R.string.games, R.drawable.ic_games),
            Category(R.string.cars, R.drawable.ic_cars),
            Category(R.string.furniture, R.drawable.ic_furniture),
            Category(R.string.kids, R.drawable.ic_kids)
        )
    }

    fun getTradeItems() = viewModelScope.launch {
        _readyToShow.value = false
        val loadData = async {
            try {
                _latest.value = repository.getLatest().itemsList
                _flashSale.value = repository.getFlashSale().itemsList
                _brands.value = _latest.value
                true
            } catch (e: java.lang.Exception) {
                Log.e("TradeItemsViewModel", "Error: ${e.message}")
                false
            }
        }
        _readyToShow.value = loadData.await()
    }

    fun getItemDescription() = viewModelScope.launch {
        _itemDescription.value = repository.getItemDescription()
    }
}

class TradeItemsViewModelFactory(private val repository: AppRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TradeItemsViewModel::class.java)) {
            return TradeItemsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

