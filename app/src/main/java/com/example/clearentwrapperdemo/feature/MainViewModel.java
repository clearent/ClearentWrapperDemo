package com.example.clearentwrapperdemo.feature;

import androidx.lifecycle.ViewModel;

import com.clearent.idtech.android.wrapper.ClearentWrapper;
import com.clearent.idtech.android.wrapper.http.model.ManualEntryCardInfo;
import com.clearent.idtech.android.wrapper.http.model.SaleEntity;
import com.clearent.idtech.android.wrapper.model.ReaderStatus;
import com.example.clearentwrapperdemo.BuildConfig;

public class MainViewModel extends ViewModel {

    private final ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();

    public void searchReaders() {
        clearentWrapper.startSearching(null);
    }

    public void stopSearching() {
        clearentWrapper.stopSearching();
    }

    public void pairReader(ReaderStatus readerStatus) {
        clearentWrapper.selectReader(readerStatus, true);
    }

    public void startTransaction(Double amount) {
        if (amount == null)
            return;

        SaleEntity saleEntity = new SaleEntity(
                SaleEntity.Companion.formatAmount(22.0), // chargeAmount
                SaleEntity.Companion.formatAmount(5.0),  // tipAmount
                null,  // you can also add client information
                null,
                "Insert application name (Optional)",
                BuildConfig.VERSION_NAME, // Application version (Optional)
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );
        ManualEntryCardInfo manualEntryCardInfo = new ManualEntryCardInfo(
                "4111111111111111",
                "0932",
                "999"
        );
        clearentWrapper.startTransaction(saleEntity, null);
    }
}
