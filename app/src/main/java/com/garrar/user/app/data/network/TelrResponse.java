package com.garrar.user.app.data.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TelrResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("transaction_id")
    @Expose
    private String transactionId;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}