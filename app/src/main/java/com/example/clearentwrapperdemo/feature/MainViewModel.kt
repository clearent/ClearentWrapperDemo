package com.example.clearentwrapperdemo.feature

import androidx.lifecycle.ViewModel
import com.clearent.idtech.android.wrapper.ClearentWrapper
import com.clearent.idtech.android.wrapper.http.model.SaleEntity
import com.clearent.idtech.android.wrapper.model.ReaderStatus
import com.example.clearentwrapperdemo.data.ClearentDemoDataSource

class MainViewModel : ViewModel() {

    private val clearentWrapper = ClearentWrapper.getInstance()

    val dataFlow = ClearentDemoDataSource.dataFlow

    fun searchReaders() = clearentWrapper.startSearching()
    fun stopSearching() = clearentWrapper.stopSearching()
    fun pairReader(readerStatus: ReaderStatus) = clearentWrapper.selectReader(readerStatus)

    fun startTransaction(amount: Double) =
        clearentWrapper.startTransaction(
            SaleEntity(
                amount = SaleEntity.formatAmount(amount)
            )
        )
}