package iapsample.amazon.humayunm.sampleproject.manager;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.FulfillmentResult;
import com.amazon.device.iap.model.Receipt;

import iapsample.amazon.humayunm.sampleproject.data.ConsumableData;
import iapsample.amazon.humayunm.sampleproject.data.UserData;
import iapsample.amazon.humayunm.sampleproject.fragment.ConsumableFragment;
import iapsample.amazon.humayunm.sampleproject.fragment.IAPFragment;
import iapsample.amazon.humayunm.sampleproject.listener.PurchaseListener;

/**
 * Created by humayunm on 9/26/2014.
 */
public class IAPConsumableManager extends IAPManager {

    private static final String REMAINING = "REMAINING";
    private static final String CONSUMED = "CONSUMED";
    private static final String TAG = "IAPConsumableManager";

    public IAPConsumableManager(IAPFragment fragment) {
        super(fragment);
    }

    @Override
    public void initialize() {
        //
        // Initialize the listener
        //
        PurchasingService.registerListener(applicationContext, new PurchaseListener(this));
        Log.i(TAG, "onCreate: sandbox mode is:" + PurchasingService.IS_SANDBOX_MODE);

        //
        // Initialize the user
        //
        PurchasingService.getUserData();
    }

    public ConsumableData getConsumableData(final UserData userData) {
        final ConsumableData consumableData = new ConsumableData(userData.getAmazonUserId(), userData.getAmazonMarketplace());
        final SharedPreferences orangesSharedPreference = applicationContext.getSharedPreferences("ORANGES_" + userData.getAmazonUserId(),
                Context.MODE_PRIVATE);
        consumableData.setOrangesAvailable(orangesSharedPreference.getInt(REMAINING, 0));
        consumableData.setOrangesConsumed(orangesSharedPreference.getInt(CONSUMED, 0));
        return consumableData;

    }

    /**
     * Save the user data to SharedPreferecnce.
     */
    public boolean updateConsumableData(final ConsumableData consumableData) {
        if (consumableData == null)
            return false;

        if (consumableData.getAmazonUserId() == null)
            return false;

        try {
            final SharedPreferences orangesSharedPreference = applicationContext.getSharedPreferences("ORANGES_" + consumableData.getAmazonUserId(),
                    Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = orangesSharedPreference.edit();
            editor.putInt(REMAINING, consumableData.getOrangesAvailable());
            editor.putInt(CONSUMED, consumableData.getOrangesConsumed());
            editor.commit();
            return  true;
        } catch (final Throwable e) {
            Log.e(TAG, "failed to save user iap data:");
        }

        return false;
    }


    @Override
    public void handleReceipt(Receipt receipt, com.amazon.device.iap.model.UserData userData) {

    }

    private void revokeConsumablePurchase(final Receipt receipt, final UserData userData) {
        // TODO: implement your application-specific logic to handle the
        // consumable purchase.

    }

    private void handleConsumablePurchase(final Receipt receipt, final UserData userData) {
        try {
            if (receipt.isCanceled()) {
                revokeConsumablePurchase(receipt, userData);
            } else {
                // We strongly recommend that you verify the receipt server-side
//                if (!verifyReceiptFromYourService(receipt.getReceiptId(), userData)) {
//                    // if the purchase cannot be verified,
//                    // show relevant error message to the customer.
//                    mainActivity.showMessage("Purchase cannot be verified, please retry later.");
//                    return;
//                }
//                if (receiptAlreadyFulfilled(receipt.getReceiptId(), userData)) {
//                    // if the receipt was fulfilled before, just notify Amazon
//                    // Appstore it's Fulfilled again.
//                    PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
//                    return;
//                }

                grantConsumablePurchase(receipt, userData);
            }
            return;
        } catch (final Throwable e) {
            //mainActivity.showMessage("Purchase cannot be completed, please retry");
        }

        //
    }

    private void grantConsumablePurchase(final Receipt receipt, final UserData userData) {
        try {
            // following sample code is a simple implementation, please
            // implement your own granting logic thread-safe, transactional and
            // robust

            // create the purchase information in your app/your server,
            // And grant the purchase to customer - give one orange to customer
            // in this case
            createPurchase(receipt.getReceiptId(), userData.getUserId());
            final MySku mySku = MySku.fromSku(receipt.getSku(), userIapData.getAmazonMarketplace());
            // Verify that the SKU is still applicable.
            if (mySku == null) {
                Log.w(TAG, "The SKU [" + receipt.getSku() + "] in the receipt is not valid anymore ");
                // if the sku is not applicable anymore, call
                // PurchasingService.notifyFulfillment with status "UNAVAILABLE"
                updatePurchaseStatus(receipt.getReceiptId(), null, PurchaseStatus.UNAVAILABLE);
                PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.UNAVAILABLE);
                return;
            }

            if (updatePurchaseStatus(receipt.getReceiptId(), PurchaseStatus.PAID, PurchaseStatus.FULFILLED)) {
                // Update purchase status in SQLite database success
                userIapData.setRemainingOranges(userIapData.getRemainingOranges() + 1);
                saveUserIapData();
                Log.i(TAG, "Successfuly update purchase from PAID->FULFILLED for receipt id " + receipt.getReceiptId());
                // update the status to Amazon Appstore. Once receive Fulfilled
                // status for the purchase, Amazon will not try to send the
                // purchase receipt to application any more
                PurchasingService.notifyFulfillment(receipt.getReceiptId(), FulfillmentResult.FULFILLED);
            } else {
                // Update purchase status in SQLite database failed - Status
                // already changed.
                // Usually means the same receipt was updated by another
                // onPurchaseResponse or onPurchaseUpdatesResponse callback.
                // simply swallow the error and log it in this sample code
                Log.w(TAG, "Failed to update purchase from PAID->FULFILLED for receipt id " + receipt.getReceiptId()
                        + ", Status already changed.");
            }

        } catch (final Throwable e) {
            // If for any reason the app is not able to fulfill the purchase,
            // add your own error handling code here.
            // Amazon will try to send the consumable purchase receipt again
            // next time you call PurchasingService.getPurchaseUpdates api
            Log.e(TAG, "Failed to grant consumable purchase, with error " + e.getMessage());
        }

    }
}
