package com.digitalingressos.ingressos.util;

public class PhoneFormatter {

    private static final String PHONE_MASK = "(##) #####-####";

    public static String formatPhoneNumber(String phoneNumber) {
        StringBuilder formattedPhoneNumber = new StringBuilder();

        int index = 0;
        for (char c : PHONE_MASK.toCharArray()) {
            if (c == '#') {
                if (index < phoneNumber.length()) {
                    formattedPhoneNumber.append(phoneNumber.charAt(index));
                    index++;
                }
            } else {
                formattedPhoneNumber.append(c);
            }
        }

        return formattedPhoneNumber.toString();
    }
}
