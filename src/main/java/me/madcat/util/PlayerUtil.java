package me.madcat.util;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.util.UUIDTypeAdapter;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import javax.net.ssl.HttpsURLConnection;
import me.madcat.features.command.Command;
import net.minecraft.client.network.NetworkPlayerInfo;

public class PlayerUtil
implements Wrapper {
    private static final JsonParser PARSER = new JsonParser();

    private static JsonElement getResources(URL uRL) throws Exception {
        return PlayerUtil.getResources(uRL, "GET");
    }

    public static UUID getUUIDFromName(String string) {
        try {
            lookUpUUID lookUpUUID2 = new lookUpUUID(string);
            Thread thread = new Thread(lookUpUUID2);
            thread.start();
            thread.join();
            return lookUpUUID2.getUUID();
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String getIdNoHyphens(UUID uUID) {
        return uUID.toString().replaceAll("-", "");
    }

    private static JsonElement getResources(URL uRL, String string) throws Exception {
        HttpsURLConnection httpsURLConnection = null;
        try {
            httpsURLConnection = (HttpsURLConnection)uRL.openConnection();
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setRequestMethod(string);
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            Scanner scanner = new Scanner(httpsURLConnection.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append('\n');
            }
            scanner.close();
            String string2 = String.valueOf(stringBuilder);
            return PARSER.parse(string2);
        }
        finally {
            if (httpsURLConnection != null) {
                httpsURLConnection.disconnect();
            }
        }
    }

    public static ArrayList getHistoryOfNames(UUID uUID) {
        try {
            JsonArray jsonArray = PlayerUtil.getResources(new URL("https://api.mojang.com/user/profiles/" + PlayerUtil.getIdNoHyphens(uUID) + "/names")).getAsJsonArray();
            ArrayList arrayList = Lists.newArrayList();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String string = jsonObject.get("name").getAsString();
                long l = jsonObject.has("changedToAt") ? jsonObject.get("changedToAt").getAsLong() : 0L;
                arrayList.add(string + "脗搂8" + new Date(l));
            }
            Collections.sort(arrayList);
            return arrayList;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String requestIDs(String string) {
        String string2 = "https://api.mojang.com/profiles/minecraft";
        try {
            URL uRL = new URL(string2);
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(string.getBytes(StandardCharsets.UTF_8));
            outputStream.close();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            String string3 = PlayerUtil.convertStreamToString(bufferedInputStream);
            ((InputStream)bufferedInputStream).close();
            httpURLConnection.disconnect();
            return string3;
        }
        catch (Exception exception) {
            return null;
        }
    }

    public static String convertStreamToString(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "/";
    }

    public static class lookUpUUID
    implements Runnable {
        private UUID uuid;
        private final String name;
        static final boolean $assertionsDisabled = !PlayerUtil.class.desiredAssertionStatus();

        public String getName() {
            return this.name;
        }

        @Override
        public void run() {
            NetworkPlayerInfo networkPlayerInfo;
            try {
                List<NetworkPlayerInfo> playerInfoList = new ArrayList<>(Objects.requireNonNull(Wrapper.mc.getConnection()).getPlayerInfoMap());
                networkPlayerInfo = playerInfoList.stream().filter(this::run0).findFirst().orElse(null);
                if (!$assertionsDisabled && networkPlayerInfo == null) {
                    throw new AssertionError();
                }
                this.uuid = networkPlayerInfo.getGameProfile().getId();
            } catch (Exception exception) {
                networkPlayerInfo = null;
            }
            if (networkPlayerInfo != null) {
                return;
            }
            Command.sendMessage("Player isn't online. Looking up UUID..");
            String object = PlayerUtil.requestIDs("[\"" + this.name + "\"]");
            if (object == null || object.isEmpty()) {
                Command.sendMessage("Couldn't find player ID. Are you connected to the internet? (0)");
                return;
            }
            JsonElement jsonElement = new JsonParser().parse(object);
            if (jsonElement.getAsJsonArray().size() == 0) {
                Command.sendMessage("Couldn't find player ID. (1)");
                return;
            }
            try {
                String string = jsonElement.getAsJsonArray().get(0).getAsJsonObject().get("id").getAsString();
                this.uuid = UUIDTypeAdapter.fromString(string);
            } catch (Exception exception) {
                exception.printStackTrace();
                Command.sendMessage("Couldn't find player ID. (2)");
            }
        }


        private boolean run0(NetworkPlayerInfo networkPlayerInfo) {
            return networkPlayerInfo.getGameProfile().getName().equalsIgnoreCase(this.name);
        }

        public lookUpUUID(String string) {
            this.name = string;
        }

        public UUID getUUID() {
            return this.uuid;
        }
    }
}

