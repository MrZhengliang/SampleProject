package iapsample.amazon.humayunm.sampleproject.manager;

import android.content.Context;

import com.amazon.device.iap.model.Receipt;
import com.amazon.device.iap.model.UserData;

import iapsample.amazon.humayunm.sampleproject.fragment.IAPFragment;

/**
 * Created by maazhumayun on 10/1/14.
 */
public abstract class IAPManager {

    final IAPFragment fragment;
    final Context applicationContext;

    public IAPManager(final IAPFragment fragment) {
        this.fragment = fragment;
        this.applicationContext = fragment.getActivity().getApplicationContext();
        initialize();
    }

    public abstract void handleReceipt(final Receipt receipt, final UserData userData);
    public abstract void initialize();

}

