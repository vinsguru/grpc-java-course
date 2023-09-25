package org.udemy.practice.protobuf;

import org.udemy.models.Credentials;
import org.udemy.models.EmailCredentials;
import org.udemy.models.PhoneOtp;

public class OneOfDemo {

    public static void main(String[] args) {
        Credentials credentials = Credentials.newBuilder().setEmailMode(EmailCredentials.newBuilder().setEmail("abc@gmail.com").setPassword("12344")).setPhoneOtp(PhoneOtp.newBuilder().setNumber(123345).setCode(112).build()).build();
        login(credentials);
    }

    private static void login(Credentials credentials){
        switch (credentials.getModeCase()) {
            case EMAILMODE:
                System.out.println(credentials.getEmailMode());
                break;
            case PHONEOTP:
                System.out.println(credentials.getPhoneOtp());
                break;
            case MODE_NOT_SET:
                System.out.println("Mode not set");
        }
    }
}
