package iapsample.amazon.humayunm.sampleproject.sku;

/**
 * Created by humayunm on 9/26/2014.
 */
public enum MySKU {

    ;

    private final String sku;
    private final String availableMarketplace;
    private final SKUType SKUType;

    private MySKU(final String sku, final String availableMarketplace, final SKUType SKUType) {
        this.sku = sku;
        this.availableMarketplace = availableMarketplace;
        this.SKUType = SKUType;
    }

    /**
     * Returns the Sku string of the MySKU object
     *
     * @return
     */
    public String getSku() {
        return this.sku;
    }

    /**
     * Returns the Available Marketplace of the MySKU object
     *
     * @return
     */
    public String getAvailableMarketplace() {
        return this.availableMarketplace;
    }

}
