package iapsample.amazon.humayunm.sampleproject.listener;

import android.util.Log;

import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;

import java.util.HashSet;
import java.util.Set;

import iapsample.amazon.humayunm.sampleproject.manager.IAPManager;
import iapsample.amazon.humayunm.sampleproject.util.UserUtil;

/**
 * Created by humayunm on 9/25/2014.
 */
public class PurchaseListener implements PurchasingListener {

    private static final String TAG = "SampleIAPConsumablesApp";
    private final IAPManager iapManager;

    public PurchaseListener(final IAPManager iapManager) {
        super();
        this.iapManager = iapManager;
    }

    @Override
    public void onUserDataResponse(UserDataResponse userDataResponse) {
        final UserDataResponse.RequestStatus status = userDataResponse.getRequestStatus();
        Log.d(TAG, "onUserDataResponse");

        switch (status) {
            case SUCCESSFUL:
                UserUtil.setCurrentUser(userDataResponse.getUserData().getUserId(), userDataResponse.getUserData().getMarketplace());
            case FAILED:
                break;
            case NOT_SUPPORTED:
                break;
        }
    }

    @Override
    public void onProductDataResponse(ProductDataResponse response) {
        final ProductDataResponse.RequestStatus status = response.getRequestStatus();
        Log.d(TAG, "onProductDataResponse: RequestStatus (" + status + ")");

        switch (status) {
            case SUCCESSFUL:
                Log.d(TAG, "onProductDataResponse: successful.  The item data map in this response includes the valid SKUs");
                final Set<String> unavailableSkus = response.getUnavailableSkus();
                Log.d(TAG, "onProductDataResponse: " + unavailableSkus.size() + " unavailable skus");

                //iapManager.enablePurchaseForSkus(response.getProductData());
                //iapManager.disablePurchaseForSkus(response.getUnavailableSkus());

                break;
            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onProductDataResponse: failed, should retry request");
                //iapManager.disableAllPurchases();
                break;
        }
    }

    @Override
    public void onPurchaseResponse(PurchaseResponse response) {
        Log.d(TAG, "onPurchasesResponse");
        final String requestId = response.getRequestId().toString();
        final String userId = response.getUserData().getUserId();
        final PurchaseResponse.RequestStatus status = response.getRequestStatus();
        Log.d(TAG, "onPurchaseResponse: requestId (" + requestId
                + ") userId ("
                + userId
                + ") purchaseRequestStatus ("
                + status
                + ")");

        switch (status) {
            case SUCCESSFUL:
                final Receipt receipt = response.getReceipt();
                Log.d(TAG, "onPurchaseResponse: receipt json:" + receipt.toJSON());
                iapManager.handleReceipt(receipt, response.getUserData());
                break;
            case ALREADY_PURCHASED:
                Log.d(TAG, "onPurchaseResponse: already purchased, should never get here for a consumable.");
                // This is not applicable for consumable item. It is only
                // application for entitlement and subscription.
                // check related samples for more details.
                break;
            case INVALID_SKU:
                Log.d(TAG,
                        "onPurchaseResponse: invalid SKU!  onProductDataResponse should have disabled buy button already.");
                final Set<String> unavailableSkus = new HashSet<String>();
                unavailableSkus.add(response.getReceipt().getSku());
                //iapManager.disablePurchaseForSkus(unavailableSkus);
                break;
            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onPurchaseResponse: failed so remove purchase request from local storage");
                //iapManager.purchaseFailed(response.getReceipt().getSku());
                break;
        }
    }

    @Override
    public void onPurchaseUpdatesResponse(PurchaseUpdatesResponse response) {
        Log.d(TAG, "onPurchaseUpdatesResponse: requestId (" + response.getRequestId()
                + ") purchaseUpdatesResponseStatus ("
                + response.getRequestStatus()
                + ") userId ("
                + response.getUserData().getUserId()
                + ")");
        final PurchaseUpdatesResponse.RequestStatus status = response.getRequestStatus();
        switch (status) {
            case SUCCESSFUL:
                //iapManager.setAmazonUserId(response.getUserData().getUserId(), response.getUserData().getMarketplace());
                for (final Receipt receipt : response.getReceipts()) {
                  //  iapManager.handleReceipt(receipt, response.getUserData());
                }
                if (response.hasMore()) {
                    PurchasingService.getPurchaseUpdates(false);
                }
                //iapManager.refreshOranges();
                break;
            case FAILED:
            case NOT_SUPPORTED:
                Log.d(TAG, "onProductDataResponse: failed, should retry request");
               // iapManager.disableAllPurchases();
                break;
        }
    }
}
