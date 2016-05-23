package com.robynem.mit.web.util;

/**
 * Created by rrennaloiacono on 23/05/2016.
 */
public class PublishClubResult {

    private boolean success;

    private Long publishedClubId;

    PublishClubErrorCode errorCode;

    public PublishClubResult() {
    }

    public PublishClubResult(boolean success, Long publishedBandId, PublishClubErrorCode errorCode) {
        this.success = success;
        this.publishedClubId = publishedBandId;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getPublishedClubId() {
        return publishedClubId;
    }

    public void setPublishedClubId(Long publishedClubId) {
        this.publishedClubId = publishedClubId;
    }

    public PublishClubErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(PublishClubErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
