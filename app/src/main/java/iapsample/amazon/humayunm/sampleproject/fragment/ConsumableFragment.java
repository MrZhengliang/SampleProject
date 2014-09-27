package iapsample.amazon.humayunm.sampleproject.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.RequestId;

import iapsample.amazon.humayunm.sampleproject.R;
import iapsample.amazon.humayunm.sampleproject.sku.MySKU;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ConsumableFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConsumableFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "SampleIAPProject";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConsumableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConsumableFragment newInstance(String param1, String param2) {
        ConsumableFragment fragment = new ConsumableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ConsumableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_consumable, container, false);

        final Button eat_orange_button = (Button)view.findViewById(R.id.eat_orange_button);
        eat_orange_button.setOnClickListener(this);

        final Button buy_orange_button = (Button)view.findViewById(R.id.buy_orange_button);
        buy_orange_button.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.buy_orange_button) {
            onBuyOrangeClick();
        }

        if(view.getId() == R.id.eat_orange_button) {
            onEatOrangeClick();
        }

    }

    /**
     * Click handler invoked when user clicks button to buy an orange
     * consumable. This method calls {@link PurchasingService#purchase(String)}
     * with the SKU to initialize the purchase from Amazon Appstore
     */
    private void onBuyOrangeClick() {
        final RequestId requestId = PurchasingService.purchase(MySKU.ORANGE.getSku());
        Log.d(TAG, "onBuyOrangeClick: requestId (" + requestId + ")");
    }

    /**
     * Click handler called when user clicks button to eat an orange consumable.
     */
    private void onEatOrangeClick() {
        try {
            //sampleIapManager.eatOrange();
            Log.d(TAG, "onEatOrangeClick: consuming 1 orange");

//            updateOrangesInView(sampleIapManager.getUserIapData().getRemainingOranges(),
//                    sampleIapManager.getUserIapData().getConsumedOranges());
        } catch (final RuntimeException e) {
            //showMessage("Unknow error when eat Orange");
        }
    }
}
