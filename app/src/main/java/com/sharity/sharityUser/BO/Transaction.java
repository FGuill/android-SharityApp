package com.sharity.sharityUser.BO;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import static com.sharity.sharityUser.BO.CISSTransaction.processed;

@ParseClassName("Transaction")
public class Transaction extends ParseObject {


    public static final String approved = "approved";
    public static final String amount = "amount";


    public static String getApproved() {
        return approved;
    }

    public static String getAmount() {
        return amount;
    }
}