package com.example.campusmarket.cart;

public class CartItemsActivity {

    private String name;
    private String price;
    private String user;
    private String refnum;

    // we will be calling this DashItemsActivity object --
    // in the main activity to get all of the necessary fields of an item

    /**
     * This object is called in the main activity to get all of the necessary fields of an item
     *
     * @param name   name of item
     * @param price  price of item
     *               //     * @param condition condition of item
     *               //     * @param category category of item
     * @param user   seller of item
     * @param refnum refnum of item
     */
    public CartItemsActivity(String name, String price, String user, String refnum) {
        this.name = name;
        this.price = price;
        this.user = user;
        this.refnum = refnum;
    }

    /**
     * Will return the name of the item
     *
     * @return name of item
     */
    public String getName() {
        return name;
    }

    /**
     * Can update the name of the item
     *
     * @param name name of item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Will return the price of the item
     *
     * @return price of item
     */
    public String getPrice() {
        return price;
    }

    /**
     * Can update the price of the item
     *
     * @param price price of item
     */
    public void setPrice(String price) {
        this.price = price;
    }

    /**
     * Will return the seller of the item
     *
     * @return seller of item
     */
    public String getUser() {
        return user;
    }

    /**
     * Can update the seller of the item
     *
     * @param user seller of the item
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Will return the refnum of the item
     *
     * @return refnum of item
     */
    public String getRefnum() {
        return refnum;
    }

    /**
     * Can update the refnum of the item
     *
     * @param refnum of the item
     */
    public void setRefnum(String refnum) {
        this.refnum = refnum;
    }

}


