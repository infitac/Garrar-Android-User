
package com.garrar.user.app.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendRequest {

    @SerializedName("current_provider")
    @Expose
    private Long mCurrentProvider;
    @SerializedName("message")
    @Expose
    private String mMessage;
    @SerializedName("request_id")
    @Expose
    private Long mRequestId;

    public Long getCurrentProvider() {
        return mCurrentProvider;
    }

    public void setCurrentProvider(Long currentProvider) {
        mCurrentProvider = currentProvider;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Long getRequestId() {
        return mRequestId;
    }

    public void setRequestId(Long requestId) {
        mRequestId = requestId;
    }

}
