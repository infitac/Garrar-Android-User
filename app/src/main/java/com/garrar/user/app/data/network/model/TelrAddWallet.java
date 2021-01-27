package com.garrar.user.app.data.network.model;

public class TelrAddWallet {
    private String success;
    private String message;
    private double wallet_balance;

    public TelrAddWallet() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getBalance() {
        return wallet_balance;
    }

    public void setBalance(double balance) {
        this.wallet_balance = balance;
    }
}