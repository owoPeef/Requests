package ru.owopeef.oworequests.vk.longpoll;

import org.json.JSONObject;
import ru.owopeef.oworequests.URL.Requests;
import ru.owopeef.oworequests.vk.groups;

public class thread extends Thread {
    public static String server;
    public static String key;
    public static int ts;

    @Override
    public void run() {
        while (true) {
            Requests.url_lp();
            try {
                Thread.sleep(3 * 1002);
            }
            catch (InterruptedException ignored) {}
        }
    }

    public static void reloadLongPollServer() {
        try {
            JSONObject c = groups.getLongPollServer();
            assert c != null;
            for(int i = 0; i<c.names().length(); i++){
                JSONObject jsonObject = new JSONObject(c.get(c.names().getString(i)).toString());
                server = jsonObject.getString("server");
                key = jsonObject.getString("key");
                ts = jsonObject.getInt("ts");
            }
        }
        catch (Exception ignored) {}
    }
}
