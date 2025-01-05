package com.faithfulolaleru.loan_service_interview.utils;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AppUtils {

    public static String generateRandomString(int n) {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789" + "abcdefghijklmnopqrstuvxyz" + "_";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public static String buildEmail(String username, String eventName) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<p>Hi " + username + ", </p>" +
                "<p>Gentle reminder for " + eventName + "event tomorrow</p>" +
                "</div>";
    }


}
