package iapsample.amazon.humayunm.sampleproject.data;

/**
 * Created by maazhumayun on 9/30/14.
 */

public class ConsumableData extends UserData {

    public ConsumableData(String amazonUserId, String amazonMarketplace) {
        super(amazonUserId, amazonMarketplace);
    }

    private int orangesConsumed;
    private int orangesAvailable;

    public int getOrangesConsumed() {
        return orangesConsumed;
    }

    public void setOrangesConsumed(int orangesConsumed) {
        this.orangesConsumed = orangesConsumed;
    }

    public int getOrangesAvailable() {
        return orangesAvailable;
    }

    public void setOrangesAvailable(int orangesAvailable) {
        this.orangesAvailable = orangesAvailable;
    }
}
