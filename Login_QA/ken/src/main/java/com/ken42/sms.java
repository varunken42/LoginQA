
package com.ken42;

import java.io.FileReader;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import io.jsonwebtoken.io.IOException;

public class sms {

    // Find your Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "AC2c1bbdba04e5b5fca9d896cf9c372d64";
    public static final String AUTH_TOKEN = "010a6f2eff2f5952bae1f8426e3055d9";

    public static void SendSMS(String msg) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(new PhoneNumber("+916379194140"), // to
                new PhoneNumber("+15167154154"), msg).create();

        System.out.println(message.getSid());
    }
}