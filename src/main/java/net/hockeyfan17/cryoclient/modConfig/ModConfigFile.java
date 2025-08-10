package net.hockeyfan17.cryoclient.modConfig;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModConfigFile {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    public static final List<String> trackList = new ArrayList<>();
    private static final Path TRACK_FILE = Path.of("config/trackfile.json");
    private static final Gson GSON = new Gson();

    public static void loadTrackList() {
        if (!Files.exists(TRACK_FILE)) {
            client.inGameHud.getChatHud().addMessage(Text.literal("File does not exist"));
            return;
        }
        try (Reader reader = new FileReader(TRACK_FILE.toFile())) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            List<String> loaded = GSON.fromJson(reader, listType);
            trackList.clear();
            if (loaded != null) trackList.addAll(loaded);
        } catch (IOException e) {
            client.inGameHud.getChatHud().addMessage(Text.literal("Friend list could not be loaded"));
        }
    }

    public static void saveTrackList() {
        try {
            Files.createDirectories(TRACK_FILE.getParent());
            try (Writer writer = new FileWriter(TRACK_FILE.toFile())) {
                GSON.toJson(trackList, writer);
            }
        } catch (IOException e) {
            client.inGameHud.getChatHud().addMessage(Text.literal("File could not be saved"));
        }
    }
}
