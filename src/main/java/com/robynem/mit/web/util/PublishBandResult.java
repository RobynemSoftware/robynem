package com.robynem.mit.web.util;

/**
 * Created by robyn_000 on 16/03/2016.
 */
public class PublishBandResult {

    private boolean success;

    private Long publishedBandId;

    PublishBandErrorCode errorCode;

    public PublishBandResult() {
    }

    public PublishBandResult(boolean success, Long publishedBandId, PublishBandErrorCode errorCode) {
        this.success = success;
        this.publishedBandId = publishedBandId;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getPublishedBandId() {
        return publishedBandId;
    }

    public void setPublishedBandId(Long publishedBandId) {
        this.publishedBandId = publishedBandId;
    }

    public PublishBandErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(PublishBandErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
