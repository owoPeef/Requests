package ru.owopeef.oworequests;

import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import ru.owopeef.oworequests.vk.groups;
import ru.owopeef.oworequests.vk.longpoll.thread;

import java.io.File;

public class Main extends JavaPlugin
{
    thread th = new thread();
    public static String token;
    public static Integer group_id;
    @Override
    public void onEnable()
    {
        File currentFile = new File(System.getProperty("user.dir") + "\\plugins\\owoRequests\\config.yml");
        if (!currentFile.exists())
        {
            this.getConfig().options().copyDefaults(true);
            this.saveConfig();
        }
        else
        {
            token = this.getConfig().getString("token");
            group_id = this.getConfig().getInt("group_id");
            JSONObject response = groups.getLongPollServer();
            try {
                assert response != null;
                for(int i = 0; i<response.names().length(); i++){
                    JSONObject jsonObject = new JSONObject(response.get(response.names().getString(i)).toString());
                    thread.server = jsonObject.getString("server");
                    thread.key = jsonObject.getString("key");
                    thread.ts = jsonObject.getInt("ts");
                }
                th.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDisable()
    {
        th.stop();
    }
}
