package com.example.footapp.ui.Order.offline

import androidx.lifecycle.viewModelScope
import com.example.footapp.base.BaseViewModel
import com.example.footapp.model.Bill
import com.example.footapp.model.ItemBill
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class OfflineConfirmViewModel : BaseViewModel() {
    @Inject
    lateinit var offlineRepository: OfflineRepository

    private val _uiState = MutableStateFlow<UiState>(UiState.Message())
    val uiState: StateFlow<UiState> = _uiState

    fun insertBill(items: List<ItemBill>) {
        viewModelScope.launch {
            val bill = Bill()
            offlineRepository.insertBillWithItems(bill, items).onStart { onRetrievePostListStart() }
                .catch {
                    _uiState.value = UiState.Message(it.message)
                }
                .onCompletion { onRetrievePostListFinish() }
                .collect {
                    _uiState.value = UiState.SuccessInsert("Hoàn thành đơn hàng")
                }
        }
    }

    sealed class UiState {
        data class Message(val message: String? = "") : UiState()
        data class SuccessInsert(val message: String? = "") : UiState()
    }
}
