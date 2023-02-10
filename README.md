# Integrating the Clearent Wrapper

## Table of contents

<!-- TOC -->

* [Overview](#overview)
* [Dependencies](#dependencies---todo2)
* [Supported Android versions](#supported-android-versions)
* [Pairing a reader](#pairing-a-reader)
* [Performing a transaction](#performing-a-transaction)
    * [Performing a transaction using the card reader](#performing-a-transaction-using-the-card-reader)
    * [Performing a transaction using manual card entry](#performing-a-transaction-using-manual-card-entry)
    * [Cancelling, voiding and refunding a transaction](#cancelling-voiding-and-refunding-a-transaction)
* [Getting information related to the card reader status](#getting-information-related-to-the-card-reader-status)
* [Getting information related to previously paired readers](#getting-information-related-to-previously-paired-readers)
* [Uploading a signature](#uploading-a-signature)
* [Offline mode](#offline-mode)
* [Relevant code snippets](#relevant-code-snippets)

<!-- END -->

* **Additional links**
    * [Kotlin Clearent SDK UI Example](https://github.com/clearent/ClearentSDKUIDemo/tree/Kotlin)
    * [Kotlin Clearent Wrapper Example](https://github.com/clearent/ClearentWrapperDemo/tree/Kotlin)
    * [Java Clearent SDK UI Example](https://github.com/clearent/ClearentSDKUIDemo/tree/Java)
    * [Java Clearent Wrapper Example](https://github.com/clearent/ClearentWrapperDemo/tree/Java)

## Overview

*ClearentWrapper* is a wrapper over *ClearentFrameworkSDK* that provides payments capabilities using
the IDTech Android framework to read credit card data using VP3300. Its goal is to ease integration
and fix some of the most common issues.

*ClearentWrapper* is a singleton class and the main interaction point with the SDK.

You will use this class to update the SDK with the needed information to work properly : **API URL**
, **PUBLIC KEY** and the **API KEY** or **MERCHANT HOME CREDENTIALS**.

**Important Note:**

The safe keeping of the **API URL**, **PUBLIC KEY** and the credentials is the integrators
responsibility. The SDK stores this information only in memory!

**ClearentWrapperListener** is the interface you will need to implement in order to receive updates,
errors and notifications from the SDK. Each method from the interface is documented in code.

**ClearentWrapperSharedPrefs** is a user default storage that holds information like currently
paired reader and a list of previously paired readers. You should not save anything here the SDK
handles this for you.

## Dependencies - TODO2

## Supported Android versions

The SDK supports versions of Android starting from api level 29 (Android 10). Currently supporting
versions 29 (Android 10) throughout 33 (Android 13). With unofficial support for versions 21
(Android 5.0) throughout 28 (Android 10).

## Pairing a reader

In order to perform transaction using the VP3300 card reader you will need to pair (connect) the
device using Bluetooth, the Bluetooth connectivity is handled by the SDK .

In this step the SDK performs a Bluetooth search in order to discover the card readers around with
the method ```void startSearching(Integer searchDuration)``` where search duration is the number of
seconds the sdk will search for readers before returning them, the default value is 5 seconds. The
SDK uses continuous search by default, stopping a search is done by connecting to a reader or by
calling ```void stopSearching()```. In order for the device to be discoverable, it needs to be
turned on and in range of the mobile device. The result of the Bluetooth search is a list of devices
of type ReaderStatus and you will get the list from the
method ```void didFindReaders(List<ReaderStatus> readers)```. In case no readers are found, the list
will be empty.

Once you have the list of available readers the next step is to select the reader you want to
connect to using the ```void selectReader(ReaderStatus reader, boolean tryConnect)``` method that
will try to connect the reader if the 'tryConnect' variable is true. Once the SDK manages to connect
to the reader the method ```void deviceDidConnect()``` will get called indicating the connection was
successful.

## Performing a transaction

There are two ways to perform a transaction: using a card reader or by using the card details
directly.

### Performing a transaction using the card reader

A transaction is performed in two steps :

1. Reading the card, the IDTech framework reads the card info and provides a jwt (token).
2. Performing an API call that will send the transaction information together with the JWT token to
   a payment gateway.

You can start a transaction
using ```void startTransaction(SaleEntity saleEntity, ManualEntryCardInfo manualEntryCardInfo)```
method. You need to provide a *SaleEntity* that will contain the amount, you can also specify a tip
and client related information. Providing a *ManualEntryCardInfo*
will start a manual transaction with the card data provided in aforementioned class.

When you call the *startTransaction* method the SDK will guide you through the process by calling
two important methods from the **ClearentWrapperDataSource**:

1. ```void userActionNeeded(UserAction action)``` , indicates that the user needs to do an action
   like swiping the card, removing the card etc.
2. ```void didReceiveInfo(UserInfo info)```, this method presents different information related to
   the transaction.

After the transaction is completed the
method ``` void didFinishTransaction(TransactionResponse response, ResponseError error)``` will get
called. You can check the error parameter to know if the transaction was successful or not.

### Performing a transaction using manual card entry

You can start a transaction using
the ```void startTransaction(SaleEntity saleEntity, ManualEntryCardInfo manualEntryCardInfo)```
method where the *manualEntryCardInfo* parameter will contain the card information.

### Cancelling, voiding and refunding a transaction

If you started a card reader transaction and want to cancel it you can
use ```void cancelTransaction()``` method and after this call the card reader will be ready to take
another transaction. You can use this method only before the card is read by the card reader. Once
the card has been read the transaction will be performed and the transaction will be also registered
by the payment gateway. In this case you can use
the ```void voidTransaction(String transactionId)``` to void the transaction you want (this will
only work if the transaction was not yet processed by the gateway). Another option is to perform a
refund using ```void refundTransaction(String transactionToken, SaleEntity saleEntity)```.

## Getting information related to the card reader status

Sometimes you will need to request and display new information related to the reader like battery
status or signal strength. You can achieve this by using the **startDeviceInfoUpdate()** method,
calling this method will start fetching new information from the connected reader. To receive
updates about the reader you must implement the interface:

```
public interface ReaderStatusListener {
    void onReaderStatusUpdate(ReaderStatus readerStatus);
}
```

After implementing the interface you can register and unregister for updates with the methods:

```
void addReaderStatusListener(ReaderStatusListener listener)
void removeReaderStatusListener(ReaderStatusListener listener)
```

## Getting information related to previously paired readers

Each time you pair a new reader the SDK will save its information in the Shared Preferences. You can
get the list using the method inside the SDK wrapper: **List<ReaderStatus>
getRecentlyPairedReaders()**

You can check if a reader is connected by using the ```boolean isReaderConnected()``` method or by
checking the **isConnected** property of the **currentReader**.

## Uploading a signature

If you want to upload a signature image after a transaction, you can use this:

```
void sendSignatureWithImage(Bitmap signature)
```

After signature has been processed, the following method will be called:

```
void didFinishSignature(@NullableSignatureResponse signatureResponse, @Nullable ResponseError 
responseError)
```

Note that the *sendSignatureWithImage* method will use the latest transaction ID as the ID for the
signature in the API call, which will be lost on application restart.

### Offline mode side note

If the sdk is in offline mode while the *sendSignatureWithImage* is called, then the signature will
only be sent when the offline transaction is processed successfully. If the transaction fails then
no signature will be sent. the following method will be called with the appropriate parameters
inside the **ClearentWrapperListener**:

```
void didAcceptOfflineSignature(OfflineTransactionStoreResult.Error error, Long transactionID)
```

## Sending an email receipt

If you want to send an email receipt for a transaction you can use the method:

```
void sendEmailReceipt(String emailAddress)
```

Calling it will send the receipt for the last successful transaction made to the email address
specified in the parameter. After the email request has been sent the following method will be
called inside the **ClearentWrapperListener**:

```
void didSendEmailReceipt(EmailReceipt response, ResponseError error)
```

### Offline mode side note

If the sdk is in offline mode while the *sendEmailReceipt* is called, then the email will only be
sent when the offline transaction is processed successfully. If the transaction fails then no
receipt will be sent.

```
void didAcceptOfflineEmailReceipt(OfflineTransactionStoreResult.Error error, Long transactionID)
```

## Offline mode

The SDK offers the ability to store transactions and process them at a later time by enabling store
and forward. First of all, you will have to provide an *OfflineModeConfig* to the sdk in the
*initializeSDK* method. Then you can enable it by using the *storeAndForwardEnabled* field in the **
ClearentWrapper**.

## Relevant code snippets

**Initialisation**

```
    ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance()
    clearentWrapper.initializeSDK(
        getApplicationContext(),
        Constants.BASE_URL_SANDBOX,
        Constants.PUBLIC_KEY_SANDBOX,
        Constants.API_KEY_SANDBOX,
        offlineModeConfig,
        enhancedMessages
    );
```

You will also need to implement the ClearentWrapperListener interface and set it as the listener for
the ClearentWrapper:

```
public class ClearentDataSource implements ClearentWrapperListener {
    // implement the methods...
}

// Start listening to data
ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();
clearentWrapper.setListener(ClearentDataSource);

// Stop listening to data when the SDK is not used anymore
clearentWrapper.removeListener();
```

**Pairing a device**

Calling this method will start the process of pairing a card reader with an Android device.

```
ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();
clearentWrapper.startSearching();
```

After the search for readers is completed the SDK will trigger a method inside the listener.

```
@Override
public void didFindReaders(@NonNull List<ReaderStatus> list) {
    // you can display the list of readers on the UI
}
```

If no available readers around are found the SDK will pass in an empty list.

After the user selects one of the readers from the list you need to tell the SDK to connect to it.

```
// readerStatus is a ReaderStatus item
ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();
clearentWrapper.selectReader(readerStatus);
```

The SDK will try to connect to the selected device and it will call the ```deviceDidConnect()```
method when a successful connection is established. Now you can use the paired reader to start
performing transactions.

**Performing a transaction**

Using a card reader

```
// Define a SaleEntity
SaleEntity saleEntity = new SaleEntity(
    SaleEntity.Companion.formatAmount(22.0),
    null,  // you can also add client information
    null,
    null,
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
ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance();
clearentWrapper.startTransaction(saleEntity, null);
```

Using manual card entry

```
// Define a SaleEntity
SaleEntity saleEntity = new SaleEntity(
    SaleEntity.Companion.formatAmount(22.0), // chargeAmount
    SaleEntity.Companion.formatAmount(5.0),  // tipAmount
    null,  // you can also add client information
    null,
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

// Create a manual card entry instance
ManualEntryCardInfo manualEntry = new ManualEntryCardInfo("4111111111111111", "0932", "999");

ClearentWrapper clearentWrapper = ClearentWrapper.Companion.getInstance()
clearentWrapper.startTransaction(saleEntity, manualEntryCardInfo);
```

After starting a transaction feedback messages will be triggered on the listener.

**Receiving feedback from the sdk**

User action needed indicates that the user/client needs to perform an action in order for the
transaction to continue e.g. Insert the card.

```
@Override
public void userActionNeeded(@NonNull UserAction userAction) {
    // here you should check the user action type and display the informtion to the users
}
```

User info contains information related to the transaction status e.g. Processing

```
@Override
public void didReceiveInfo(@NonNull UserInfo userInfo) {
    // you should display the information to the users
}
```

After the transaction is processed a method will inform you about the status.

```
@Override
public void didFinishTransaction(@Nullable TransactionResponse transactionResponse, @Nullable ResponseError responseError) {
    if (responseError == null) {
        // no error
    } else {
        // you should inform about the error
    }
}
```

[Java EXAMPLE](https://github.com/clearent/ClearentWrapperDemo/tree/Java) of the Clearent Wrapper
integration.
