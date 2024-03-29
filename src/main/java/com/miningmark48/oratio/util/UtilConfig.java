package com.miningmark48.oratio.util;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import com.miningmark48.oratio.reference.Reference;

import java.io.*;

public class UtilConfig {

    public static boolean getConfigs() {
        UtilLogger.STATUS.log("Getting configs...");

        try {
            String configFile = "config.json";
            File file = new File(configFile);

            if (!file.exists()) {
                Writer writer = new OutputStreamWriter(new FileOutputStream(configFile), "UTF-8");
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonWriter jw = gson.newJsonWriter(writer);

                jw.beginObject();

                jw.name("bot");
                jw.beginObject();
                jw.name("botname").value("Bot Name");
                jw.name("token").value("bot.token");
                jw.name("key").value("bot.key");
                jw.name("client id").value("bot.id");
                jw.endObject();

                jw.name("directories");
                jw.beginObject();
                jw.name("messages").value("messages");
                jw.endObject();

                jw.endObject();

                writer.close();

                UtilLogger.INFO.log("Config file was created as it was not found. Stopping bot.");
                return false;
            } else {

                try {

                    JsonParser jp = new JsonParser();
                    InputStream inputStream = new FileInputStream(configFile);
                    JsonElement root = jp.parse(new InputStreamReader(inputStream));
                    JsonObject jsonObject = root.getAsJsonObject();

                    if (jsonObject != null) {
                        JsonObject jsonObjectBot = jsonObject.getAsJsonObject("bot");
                        JsonObject jsonObjectDir = jsonObject.getAsJsonObject("directories");

                        Reference.botName = jsonObjectBot.get("botname").getAsString();
                        Reference.botToken = jsonObjectBot.get("token").getAsString();
                        Reference.botCommandKey = jsonObjectBot.get("key").getAsString();
                        Reference.botClientID = jsonObjectBot.get("client id").getAsString();

                        Reference.messageDir = jsonObjectDir.get("messages").getAsString();

                    } else {
                        throw new NullPointerException();
                    }

                } catch (NullPointerException e) {
                    UtilLogger.FATAL.log("Unable to load configs, stopping bot.");
                    e.printStackTrace();
                    return false;
                }

                UtilLogger.STATUS.log("Configs loaded, continuing...");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
