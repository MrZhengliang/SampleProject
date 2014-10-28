package iapsample.amazon.humayunm.sampleproject.fragment;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by maazhumayun on 10/1/14.
 */
public abstract class IAPFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }

    public abstract void initialize();
    public abstract void refresh();

}
