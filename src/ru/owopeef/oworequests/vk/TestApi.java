package ru.owopeef.oworequests.vk;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TestApi
{
    public static String messages_send(Integer user_id, String message)
    {
        try {
            URL url = new URL("https://api.vk.com/method/messages.send?v=5.131&user_id=" + user_id + "&random_id=0&message=" + message + "&access_token=5d81f1be4884d49259c6a05b604b66f94851649b5d4bf74a21000119c77863801a92b0e2061dc019d87ba");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
