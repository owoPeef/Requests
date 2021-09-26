package ru.owopeef.oworequests.vk;

import org.json.JSONObject;
import ru.owopeef.oworequests.Main;
import ru.owopeef.oworequests.URL.Requests;

import java.util.Objects;

public class groups {
    public static JSONObject getLongPollServer()
    {
        try
        {
            return new JSONObject(Objects.requireNonNull(Requests.url_get("groups.getLongPollServer", "&group_id=" + Main.group_id)));
        }
        catch (Exception ignored)
        {
            return null;
        }
    }
}
