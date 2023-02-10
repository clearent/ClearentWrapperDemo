package com.example.clearentwrapperdemo.data

import com.clearent.idtech.android.wrapper.model.ReaderStatus

sealed class DataStatus {

    data class ReadersList(val list: List<ReaderStatus>) : DataStatus()
    data class LoadingStatus(val title: String, val message: String) : DataStatus()
    sealed class ResultMessage : DataStatus() {
        data class Success(val title: String, val message: String? = null) : ResultMessage()
        data class Error(val title: String, val message: String? = null) : ResultMessage()
    }
}
