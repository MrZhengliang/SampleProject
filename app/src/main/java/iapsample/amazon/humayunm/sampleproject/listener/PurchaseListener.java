package iapsample.amazon.humayunm.sampleproject.listener;

import android.util.Log;

import com.amazon.device.iap.PurchasingListener;
import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.ProductDataResponse;
import com.amazon.device.iap.model.PurchaseResponse;
import com.amazon.device.iap.model.PurchaseUpdatesResponse;
import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserDataResponse;

import java.util.Set;

/**
 * Created by humayunm on 9/25/2014.
 */
public class PurchaseListener implements PurchasingListener{

    private static final String TAG = "SampleIAPConsumablesApp";


    @Override
    public void onUserDataResponse(UserDataResponse userDataResponse) {
        final UserDataResponse.RequestStatus status = userDataResponse.getRequestStatus();
        Log.d(TAG, "onUserDataResponse");


        //TODO: Implementation
        switch (status) {
            case SUCCESSFUL:
                break;
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
    public void onPurchaseResponse(PurchaseResponse purchaseResponse) {
        Log.d(TAG, "onPurchasesResponse");
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
