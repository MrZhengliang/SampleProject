package iapsample.amazon.humayunm.sampleproject.data;

/**
 * Created by humayunm on 9/26/2014.
 */
public class UserData {

    private final String amazonUserId;
    private final String amazonMarketplace;

    public UserData(final String amazonUserId, final String amazonMarketplace) {
        this.amazonUserId = amazonUserId;
        this.amazonMarketplace = amazonMarketplace;
    }

    public String getAmazonUserId() {
        return amazonUserId;
    }

    public String getAmazonMarketplace() {
        return amazonMarketplace;
    }
}
