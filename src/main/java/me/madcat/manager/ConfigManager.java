package me.madcat.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.UUID;
import java.util.stream.Collectors;
import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;
import me.madcat.features.setting.Bind;
import me.madcat.features.setting.EnumConverter;
import me.madcat.features.setting.Setting;

public class ConfigManager {
    public final ArrayList<Feature> features = new ArrayList<>();
    public String config = "MadCat/config/";

    public void saveCurrentConfig() {
        block3: {
            File file = new File("MadCat/currentconfig.txt");
            try {
                if (file.exists()) {
                    FileWriter fileWriter = new FileWriter(file);
                    String string = this.config.replaceAll("/", "");
                    fileWriter.write(string.replaceAll("MadCat", ""));
                    fileWriter.close();
                    break block3;
                }
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(file);
                String string = this.config.replaceAll("/", "");
                fileWriter.write(string.replaceAll("MadCat", ""));
                fileWriter.close();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public JsonObject writeSettings(Feature feature) {
        JsonObject jsonObject = new JsonObject();
        JsonParser jsonParser = new JsonParser();
        for (Setting setting : feature.getSettings()) {
            Object object;
            if (setting.isEnumSetting()) {
                object = new EnumConverter(((Enum)setting.getValue()).getClass());
                jsonObject.add(setting.getName(), ((EnumConverter) object).doForward((Enum)setting.getValue()));
                continue;
            }
            if (setting.isStringSetting()) {
                object = setting.getValue();
                setting.setValue(((String)object).replace(" ", "_"));
            }
            try {
                jsonObject.add(setting.getName(), jsonParser.parse(setting.getValueAsString()));
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return jsonObject;
    }

    public void init() {
        this.features.addAll(ModuleManager.modules);
        this.features.add(MadCat.friendManager);
        String string = this.loadCurrentConfig();
        this.loadConfig(string);
        MadCat.LOGGER.info("Config loaded.");
    }

    public static void setValueFromJson(Feature feature, Setting setting, JsonElement jsonElement) {
        switch (setting.getType()) {
            case "Boolean": {
                setting.setValue(jsonElement.getAsBoolean());
                return;
            }
            case "Double": {
                setting.setValue(jsonElement.getAsDouble());
                return;
            }
            case "Float": {
                setting.setValue(jsonElement.getAsFloat());
                return;
            }
            case "Integer": {
                setting.setValue(jsonElement.getAsInt());
                return;
            }
            case "String": {
                String string = jsonElement.getAsString();
                setting.setValue(string.replace("_", " "));
                return;
            }
            case "Bind": {
                setting.setValue(new Bind.BindConverter().doBackward(jsonElement));
                return;
            }
            case "Enum": {
                try {
                    EnumConverter enumConverter = new EnumConverter(((Enum)setting.getValue()).getClass());
                    Enum enum_ = enumConverter.doBackward(jsonElement);
                    setting.setValue(enum_ == null ? setting.getDefaultValue() : enum_);
                }
                catch (Exception exception) {
                    // empty catch block
                }
                return;
            }
        }
        MadCat.LOGGER.error("Unknown Setting type for: " + feature.getName() + " : " + setting.getName());
    }

    public String getDirectory(Feature feature) {
        String string = "";
        if (feature instanceof Module) {
            string = string + ((Module) feature).getCategory().getName() + "/";
        }
        return string;
    }

    public void loadConfig(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("MadCat").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        if (files.contains(new File("MadCat/" + name + "/"))) {
            this.config = "MadCat/" + name + "/";
        } else {
            this.config = "Madcat/config/";
        }
        MadCat.friendManager.onLoad();
        for (Feature feature : this.features) {
            try {
                loadSettings(feature);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        saveCurrentConfig();
    }

    public String loadCurrentConfig() {
        String string;
        block3: {
            File file = new File("MadCat/currentconfig.txt");
            string = "config";
            try {
                if (!file.exists()) break block3;
                Scanner scanner = new Scanner(file);
                while (scanner.hasNextLine()) {
                    string = scanner.nextLine();
                }
                scanner.close();
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return string;
    }

    public void saveSettings(Feature feature) throws IOException {
        String string;
        Path path;
        JsonObject jsonObject = new JsonObject();
        File file = new File(this.config + this.getDirectory(feature));
        if (!file.exists()) {
            file.mkdir();
        }
        if (!Files.exists(path = Paths.get(string = this.config + this.getDirectory(feature) + feature.getName() + ".json"))) {
            Files.createFile(path);
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String string2 = gson.toJson(this.writeSettings(feature));
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path)));
        bufferedWriter.write(string2);
        bufferedWriter.close();
    }

    public void resetConfig(boolean bl, String string) {
        for (Feature feature : this.features) {
            feature.reset();
        }
        if (bl) {
            this.saveConfig(string);
        }
    }

    public boolean configExists(String name) {
        final List<File> files = Arrays.stream(Objects.requireNonNull(new File("Madcat").listFiles())).filter(File::isDirectory).collect(Collectors.toList());
        return files.contains(new File("Madcat/" + name + "/"));
    }

    private void loadPath(Path path, Feature feature) throws IOException {
        InputStream inputStream = Files.newInputStream(path);
        try {
            ConfigManager.loadFile(new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject(), feature);
        }
        catch (IllegalStateException illegalStateException) {
            MadCat.LOGGER.error("Bad Config File for: " + feature.getName() + ". Resetting...");
            ConfigManager.loadFile(new JsonObject(), feature);
        }
        inputStream.close();
    }

    private static void loadFile(JsonObject jsonObject, Feature feature) {
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String string = entry.getKey();
            JsonElement jsonElement = entry.getValue();
            if (feature instanceof FriendManager) {
                try {
                    MadCat.friendManager.addFriend(new FriendManager.Friend(jsonElement.getAsString(), UUID.fromString(string)));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                continue;
            }
            boolean bl = false;
            for (Setting setting : feature.getSettings()) {
                if (!string.equals(setting.getName())) continue;
                try {
                    ConfigManager.setValueFromJson(feature, setting, jsonElement);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public void saveConfig(String string) {
        this.config = "MadCat/" + string + "/";
        File file = new File(this.config);
        if (!file.exists()) {
            file.mkdir();
        }
        MadCat.friendManager.saveFriends();
        for (Feature feature : this.features) {
            try {
                this.saveSettings(feature);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        this.saveCurrentConfig();
    }

    private void loadSettings(Feature feature) throws IOException {
        String string = this.config + this.getDirectory(feature) + feature.getName() + ".json";
        Path path = Paths.get(string);
        if (!Files.exists(path)) {
            return;
        }
        this.loadPath(path, feature);
    }
}

