package com.example.sumit.expandablelist;

import java.util.ArrayList;

/**
 * Created by sumit on 18-Aug-16.
 */
public class HeaderInfo {


    private String name;
    private ArrayList<DetailInfo> productList = new ArrayList<DetailInfo>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ArrayList<DetailInfo> getProductList() {
        return productList;
    }
    public void setProductList(ArrayList<DetailInfo> productList) {
        this.productList = productList;
    }

}
