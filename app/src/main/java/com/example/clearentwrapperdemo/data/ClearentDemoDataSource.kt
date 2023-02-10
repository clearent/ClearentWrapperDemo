package com.example.clearentwrapperdemo.data

import com.clearent.idtech.android.wrapper.http.model.*
import com.clearent.idtech.android.wrapper.listener.ClearentWrapperListener
import com.clearent.idtech.android.wrapper.model.*
import com.clearent.idtech.android.wrapper.offline.model.OfflineTransactionStoreResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

object ClearentDemoDataSource : ClearentWrapperListener {

    private const val generalErrorMessage = "General error encountered"
    private const val transactionLoadingStatusTitle = "Transaction status"

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _dataFlow = MutableSharedFlow<DataStatus>()
    val dataFlow: SharedFlow<DataStatus> = _dataFlow

    override fun deviceDidConnect() {
        coroutineScope.launch {
            _dataFlow.emit(DataStatus.ResultMessage.Success("Device connected."))
        }
    }

    override fun deviceDidDisconnect() {
        coroutineScope.launch {
            _dataFlow.emit(DataStatus.ResultMessage.Error("Device disconnected."))
        }
    }

    override fun didEncounterGeneralError() {
        coroutineScope.launch {
            _dataFlow.emit(DataStatus.ResultMessage.Error(generalErrorMessage))
        }
    }

    override fun didFindReaders(readers: List<ReaderStatus>) {
        coroutineScope.launch {
            _dataFlow.emit(DataStatus.ReadersList(readers))
        }
    }

    override fun didFinishSignature(response: SignatureResponse?, error: ResponseError?) {
        coroutineScope.launch {
            val errorMessage = response?.payload?.error?.errorMessage
                ?: error?.errorMessage

            errorMessage?.also {
                _dataFlow.emit(DataStatus.ResultMessage.Error(errorMessage))
            } ?: run {
                _dataFlow.emit(DataStatus.ResultMessage.Success("Signature success."))
            }
        }
    }

    override fun didFinishTransaction(response: TransactionResponse?, error: ResponseError?) {
        coroutineScope.launch {
            if (response?.payload?.transaction?.result == TransactionResult.APPROVED.name) {
                _dataFlow.emit(DataStatus.ResultMessage.Success("Transaction success."))
            } else {
                val transaction = response?.payload?.transaction
                val errorMessage = transaction?.displayMessage
                    ?: response?.payload?.error?.errorMessage
                    ?: error?.errorMessage
                    ?: generalErrorMessage
                _dataFlow.emit(DataStatus.ResultMessage.Error(errorMessage))
            }
        }
    }

    override fun didReceiveInfo(userInfo: UserInfo) {
        coroutineScope.launch {
            _dataFlow.emit(
                DataStatus.LoadingStatus(
                    transactionLoadingStatusTitle,
                    userInfo.message
                )
            )
        }
    }

    override fun didReceiveTerminalSettings(terminalSettings: TerminalSettings) {}

    override fun didStartBTSearch() {}

    override fun didStartReaderConnection(reader: ReaderStatus) {}

    override fun userActionNeeded(userAction: UserAction) {
        coroutineScope.launch {
            _dataFlow.emit(
                DataStatus.LoadingStatus(
                    transactionLoadingStatusTitle,
                    userAction.message
                )
            )
        }
    }

    override fun didAcceptOfflineEmailReceipt(
        error: OfflineTransactionStoreResult.Error?,
        transactionID: Long?
    ) {}

    override fun didAcceptOfflineSignature(
        error: OfflineTransactionStoreResult.Error?,
        transactionID: Long?
    ) {}

    override fun didAcceptOfflineTransaction(error: OfflineTransactionStoreResult.Error?) {}

    override fun didEncounterWarning(warningType: WarningType) {
        coroutineScope.launch {
            _dataFlow.emit(
                DataStatus.ResultMessage.Error(
                    generalErrorMessage,
                    warningType.name
                )
            )
        }
    }

    override fun didSendEmailReceipt(response: EmailReceipt?, error: ResponseError?) {}

    override fun networkCapabilitiesChanged(networkStatus: NetworkStatus) {}
}