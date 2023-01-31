package com.ken42;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class Whatsapp {
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "AC2c1bbdba04e5b5fca9d896cf9c372d64";
    public static final String AUTH_TOKEN = "010a6f2eff2f5952bae1f8426e3055d9";

    public static void SendWhatsapp(String msg) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:+919043778107"),
                new com.twilio.type.PhoneNumber("whatsapp:+14155238886"), msg).create();

        System.out.println(message.getSid());
    }
}