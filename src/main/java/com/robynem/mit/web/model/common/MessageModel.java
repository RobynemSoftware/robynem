package com.robynem.mit.web.model.common;

import com.robynem.mit.web.util.MessageSeverity;

import java.io.Serializable;

/**
 * Created by robyn_000 on 16/12/2015.
 */
public class MessageModel implements Serializable {

    protected MessageSeverity severity;

    protected String message;

    protected String link;

    public MessageModel() {

    }

    public MessageModel(String message, MessageSeverity severity) {
        this.message = message;
        this.severity = severity;
    }

    public MessageModel(String message, MessageSeverity severity, String link) {
        this.message = message;
        this.severity = severity;
        this.link = link;
    }

    public MessageSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(MessageSeverity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
