package ru.owopeef.oworequests.URL;

import org.bukkit.plugin.Plugin;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.owopeef.oworequests.Main;
import ru.owopeef.oworequests.vk.longpoll.thread;
import ru.owopeef.oworequests.vk.messages;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

public class Requests {
    private static final Plugin plugin = Main.getPlugin(Main.class);
    public static String url_get(String method, String params)
    {
        try {
            String request_url = "https://api.vk.com/method/"+method+"?v=5.131&access_token=" + Main.token + params;
            URL url = new URL(request_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
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
    public static void url_lp()
    {
        try {
            String request_url = thread.server + "?act=a_check&key=" + thread.key + "&ts=" + thread.ts + "&wait=0&mode=128&version=3";
            URL url = new URL(request_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("failed"))
            {
                int failedCode = jsonResponse.getInt("failed");
                if (failedCode == 1) {
                    for(int i = 0; i<jsonResponse.names().length(); i++){
                        JSONObject jsonObject = new JSONObject(jsonResponse.get(jsonResponse.names().getString(i)).toString());
                        thread.ts = jsonObject.getInt("ts");
                    }
                } else {
                    thread.reloadLongPollServer();
                }
            }
            thread.ts = jsonResponse.getInt("ts");
            JSONArray updates = jsonResponse.getJSONArray("updates");
            int d = 0;
            while (updates.length() != d)
            {
                Object update = updates.get(d);
                JSONObject typeJson = (JSONObject) update;
                String type = typeJson.getString("type");
                JSONObject jsonObject = ((JSONObject) update).getJSONObject("object");
                if (Objects.equals(type, "message_new"))
                {
                    JSONObject jsonMessage = jsonObject.getJSONObject("message");
                    String messageText = jsonMessage.getString("text");
                    int from_id = jsonMessage.getInt("from_id");
                    List<String> messageTextSplit = List.of(messageText.split("\n"));
                    List<String> configText = plugin.getConfig().getStringList("text");
                    if (messageTextSplit.size() == configText.size())
                    {
                        String nick = null;
                        String activity = null;
                        String playtime = null;
                        int w = 0;
                        while (messageTextSplit.size() != w)
                        {
                            String message = messageTextSplit.get(w);
                            String configLine = configText.get(w);
                            List<String> msgSplit = List.of(message.split(" "));
                            List<String> configLineSplit = List.of(configLine.split(" "));
                            int k = 0;
                            while (k != configLineSplit.size())
                            {
                                if (Objects.equals(configLineSplit.get(k), "{nick}"))
                                {
                                    nick = msgSplit.get(k);
                                }
                                else if (Objects.equals(configLineSplit.get(k), "{activity}"))
                                {
                                    int msgLength = message.length();
                                    int cfgLineLength = configLine.replace("{activity}", "").length();
                                    if (message.endsWith(" "))
                                    {
                                        activity = message.substring(cfgLineLength, msgLength-1);
                                    }
                                    else
                                    {
                                        activity = message.substring(cfgLineLength, msgLength);
                                    }
                                }
                                else if (Objects.equals(configLineSplit.get(k), "{playtime}"))
                                {
                                    int msgLength = message.length();
                                    int cfgLineLength = configLine.replace("{playtime}", "").length();
                                    if (message.endsWith(" "))
                                    {
                                        playtime = message.substring(cfgLineLength, msgLength-1);
                                    }
                                    else
                                    {
                                        playtime = message.substring(cfgLineLength, msgLength);
                                    }
                                }
                                k++;
                            }
                            w++;
                        }
                        messages.send(from_id, "Ваша заявка была отправлена, ожидайте ответа!");
                        String new_request = "[REQUESTS] НОВАЯ ЗАЯВКА ("+nick+") | («"+activity+"») | (" + playtime + ")";
                        try {
                            System.out.println(new_request);
                            Objects.requireNonNull(plugin.getServer().getPlayer("owoPeef")).sendMessage(new_request);
                        } catch (NullPointerException e) {
                            plugin.getLogger().info("Players not found");
                        }
                    }
                    int sender = jsonMessage.getInt("from_id");
                    FileWriter fw = new FileWriter("messages.txt", true);
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write("[" + sender + "] «" + messageText + "»");
                    bw.newLine();
                    bw.close();
                }
                d++;
            }
        } catch (Exception e) {
            System.out.println("\n");
            e.printStackTrace();
            System.out.println("\n");
        }
    }
}
