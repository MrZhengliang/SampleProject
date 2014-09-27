package iapsample.amazon.humayunm.sampleproject.manager;

import android.app.Activity;
import android.content.Context;
import iapsample.amazon.humayunm.sampleproject.data.UserData;

/**
 * Created by humayunm on 9/26/2014.
 */
public class AbstractIAPManager {

    final private Activity mainActivity;
    final private Context context;
    private UserData userIapData;

    public AbstractIAPManager(final Activity activity) {
        this.mainActivity = activity;
        context = activity.getApplicationContext();

    }

    public void setAmazonUserId(final String newAmazonUserId, final String newAmazonMarketplace) {
        // Reload everything if the Amazon user has changed.
        if (newAmazonUserId == null) {
            // A null user id typically means there is no registered Amazon
            // account.
            if (userIapData != null) {
                userIapData = null;
                //mainActivity.updateOrangesInView(0, 0);
            }
        } else if (userIapData == null || !newAmazonUserId.equals(userIapData.getAmazonUserId())) {
            // If there was no existing Amazon user then either no customer was
            // previously registered or the application has just started.

            // If the user id does not match then another Amazon user has
            // registered.
            //userIapData = reloadUserData(newAmazonUserId, newAmazonMarketplace);
           // mainActivity.updateOrangesInView(userIapData.getRemainingOranges(), userIapData.getConsumedOranges());
        }
    }
}
