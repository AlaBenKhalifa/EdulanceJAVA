package com.example.edulancejava.Controller.Offre;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class send_SMS {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC07750a961cb0daec3d886928fe7f5887";
    public static final String AUTH_TOKEN = "ea1951b9544b3103fb41a5b4bfaabc89";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new PhoneNumber("+21656139665"),
                new PhoneNumber("+12176155863"),
                "jawek bahi, nchallah zina"
        ).create();

        //System.out.println(message.getSid());
    }
}
