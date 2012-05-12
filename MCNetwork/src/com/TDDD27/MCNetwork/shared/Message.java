package com.TDDD27.MCNetwork.shared;



import java.util.Calendar;

import java.util.Date;



public class Message {

private MCUser sender;

private MCUser resiever;

private String message;

private Boolean offentlig;

private Date datum;



public Message() {

Calendar rightNow = Calendar.getInstance();

datum = rightNow.getTime();

}

public Message(MCUser sender, MCUser resiever, String message, Boolean offentlig) {

sender = this.sender;

resiever = this.resiever;

message = this.message;

offentlig = this.offentlig;

Calendar rightNow = Calendar.getInstance();

datum = rightNow.getTime();

}



}