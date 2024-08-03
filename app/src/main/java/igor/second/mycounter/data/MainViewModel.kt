package igor.second.mycounter.data

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import kotlinx.coroutines.launch

class MainViewModel(private val database: MainDb): ViewModel() {

    val itemList = database.dao.getAllItems()

    val newText = mutableStateOf("")
    val newPrice = mutableStateOf("")
    val newCurrency = mutableStateOf("BY")
    val newCategory = mutableStateOf("SHOP")
    val newShowMyDialog = mutableStateOf(false)

    var nameEntity: NameEntity? = null

    fun insertItem() = viewModelScope.launch {
        val nameItem = nameEntity?.copy(
            name = newText.value,
            price = newPrice.value,
            nextCurrency = newCurrency.value,
            nextCategory = newCategory.value,
            showMyDialog = newShowMyDialog.value
        ) ?: NameEntity(
            name = newText.value,
            price = newPrice.value,
            nextCurrency = newCurrency.value,
            nextCategory = newCategory.value,
            showMyDialog = newShowMyDialog.value
        )
        database.dao.insertItem(nameItem)
        nameEntity = null
        newText.value = ""
        newPrice.value = ""
        newCurrency.value = newCurrency.value
        newCategory.value = newCategory.value
        newShowMyDialog.value = false
    }

    fun deleteItem(item: NameEntity) = viewModelScope.launch {
        database.dao.deleteItem(item)
    }

    companion object{
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory{
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras): T {
                val database = (checkNotNull(extras[APPLICATION_KEY]) as App).database
                return MainViewModel(database) as T
            }
        }
    }
}