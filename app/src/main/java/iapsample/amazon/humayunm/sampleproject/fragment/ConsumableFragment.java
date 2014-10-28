package iapsample.amazon.humayunm.sampleproject.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.amazon.device.iap.PurchasingService;
import com.amazon.device.iap.model.RequestId;

import iapsample.amazon.humayunm.sampleproject.R;
import iapsample.amazon.humayunm.sampleproject.data.ConsumableData;
import iapsample.amazon.humayunm.sampleproject.data.UserData;
import iapsample.amazon.humayunm.sampleproject.listener.PurchaseListener;
import iapsample.amazon.humayunm.sampleproject.manager.IAPConsumableManager;
import iapsample.amazon.humayunm.sampleproject.sku.MySku;
import iapsample.amazon.humayunm.sampleproject.util.UserUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link ConsumableFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ConsumableFragment extends IAPFragment implements View.OnClickListener {

    private static final String TAG = "SampleIAPProject";

    private TextView availableOrangesField;
    private TextView consumedOrangesField;

    private Button eatOrangeButton;
    private Button buyOrangeButton;

    private Handler handler;
    private IAPConsumableManager manager;

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
        return fragment;
    }

    public ConsumableFragment() {
        // Required empty public constructor
        //initialize();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize();
    }
    @Override
    public void initialize() {
        //
        // Initialize the manager
        //
        manager = new IAPConsumableManager(this);
        handler = new Handler();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_consumable, container, false);

        //
        // Initialize the UI elements
        //
        availableOrangesField = (TextView) view.findViewById(R.id.num_oranges);
        consumedOrangesField = (TextView) view.findViewById(R.id.num_oranges_consumed);
        buyOrangeButton = (Button) view.findViewById(R.id.buy_orange_button);
        eatOrangeButton = (Button)view.findViewById(R.id.eat_orange_button);

        eatOrangeButton.setOnClickListener(this);
        buyOrangeButton.setOnClickListener(this);

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
        final RequestId requestId = PurchasingService.purchase(MySku.ORANGE.getSku());
        Log.d(TAG, "onBuyOrangeClick: requestId (" + requestId + ")");

        //
        // View will get updated after the call-back from the listener completes
        //
    }

    /**
     * Click handler called when user clicks button to eat an orange consumable.
     */
    private void onEatOrangeClick() {
        try {
            //sampleIapManager.eatOrange();
            Log.d(TAG, "onEatOrangeClick: consuming 1 orange");
            eatOrange();

        } catch (final RuntimeException e) {
            Log.e(TAG,"Unknown error when eat Orange");
        }
    }


    private void eatOrange() {
        //
        // Get user details
        //
        final UserData currentUser = UserUtil.getCurrentUserDetails();
        if(currentUser == null)
        {
            Log.e(TAG, "User data not available");
            return;
        }

        //
        // User data is available
        //
        final ConsumableData data = manager.getConsumableData(currentUser);

        //
        // Check if we can eat on orange
        //
        if (data.getOrangesAvailable() <= 0) {
            Log.i(TAG,"No oranges available");
            return;
        }

        //
        // If we get here then we are able to eat an orange
        //
        final int newAvailableOranges = data.getOrangesAvailable() - 1;
        final int newOrangesConsumed = data.getOrangesConsumed() + 1;
        data.setOrangesAvailable(newAvailableOranges);
        data.setOrangesConsumed(newOrangesConsumed);

        boolean success = manager.updateConsumableData(data);

        if(!success) {
            Log.e(TAG, "Failed to update the data");
            return;
        }
        //
        // Update the view
        //
        updateView(newAvailableOranges, newOrangesConsumed);
    }

    @Override
    public void refresh() {
        final UserData currentUser = UserUtil.getCurrentUserDetails();
        if(currentUser == null)
        {
            Log.e(TAG, "User data not available. Can't refresh");
            return;
        }

        //
        // Retrieve the iap data
        //
        final ConsumableData consumableData = manager.getConsumableData(currentUser);
        updateView(consumableData.getOrangesAvailable(), consumableData.getOrangesConsumed());
    }

    private void updateView(final int orangesAvailable, final int orangesConsumed) {
        Log.d(TAG, "updateOrangesInView with haveQuantity (" + orangesAvailable
                + ") and consumedQuantity ("
                + orangesConsumed
                + ")");

        handler.postAtFrontOfQueue(new Runnable() {
            @Override
            public void run() {
                availableOrangesField.setText(String.valueOf(orangesAvailable));
                consumedOrangesField.setText(String.valueOf(orangesAvailable));
            }
        });
    }


}
