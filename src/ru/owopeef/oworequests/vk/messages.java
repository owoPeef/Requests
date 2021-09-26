package ru.owopeef.oworequests.vk;

import ru.owopeef.oworequests.Main;
import ru.owopeef.oworequests.URL.Requests;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class messages {
    public static void send(Integer user_id, String message)
    {
        try {
            message = URLEncoder.encode(message, StandardCharsets.UTF_8);
            Requests.url_get("messages.send", "&user_id=" + user_id + "&random_id=0&message=" + message + "&group_id=" + Main.group_id);
        } catch (Exception e) {
            System.out.println("\n");
            e.printStackTrace();
            System.out.println("\n");
        }
    }
}
