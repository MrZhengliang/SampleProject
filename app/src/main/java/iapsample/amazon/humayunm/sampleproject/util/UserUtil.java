package iapsample.amazon.humayunm.sampleproject.util;

import android.util.Log;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.RequestId;

import iapsample.amazon.humayunm.sampleproject.data.UserData;


/**
 * Created by maazhumayun on 9/30/14.
 */
public class UserUtil {

    private static final String TAG = "UserUtil";

    private static UserData currentUser = null;

    public static UserData getCurrentUserDetails() {
        return currentUser;
    }

    public static void setCurrentUser(final String userId, final String marketplace) {
        currentUser = new UserData(userId,marketplace);
    }

    public static boolean isCurrentUserSet() {
        return (currentUser == null) ? false : true;

    }
}
