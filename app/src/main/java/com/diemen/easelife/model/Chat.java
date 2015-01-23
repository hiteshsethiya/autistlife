package com.diemen.easelife.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by user on 19-01-2015.
 */
public class Chat {
    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String Sender;

    @DatabaseField
    private String SenderPhone;

    @DatabaseField
    private String Receiver;

    @DatabaseField
    private String ReceiverPhone;

    @DatabaseField
    private String Message;

    @DatabaseField(dataType = DataType.DATE_STRING, format = "yyyy-MM-dd hh:mm:ss")
    private Date ReceivedOn;

    public Chat(){}

    public int getId() {
        return id;
    }

    public Date getReceivedOn() {
        return ReceivedOn;
    }

    public String getMessage() {
        return Message;
    }

    public String getReceiver() {
        return Receiver;
    }

    public String getSender() {
        return Sender;
    }

    public String getSenderPhone() {
        return SenderPhone;
    }

    public String getReceiverPhone() {
        return ReceiverPhone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReceivedOn(Date receivedOn) {
        ReceivedOn = receivedOn;
    }

    public void setReceiver(String receiver) {
        Receiver = receiver;
    }

    public void setReceiverPhone(String receiverPhone) {
        ReceiverPhone = receiverPhone;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setSenderPhone(String senderPhone) {
        SenderPhone = senderPhone;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public Chat(int id, String sender, String senderPhone, String receiver, String receiverPhone, String message, Date receivedOn) {
        this.id = id;
        Sender = sender;
        SenderPhone = senderPhone;
        Receiver = receiver;
        ReceiverPhone = receiverPhone;
        Message = message;
        ReceivedOn = receivedOn;
    }
}
