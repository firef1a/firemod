package dev.fire;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileManager {
    /**
     * Verify the existence and get the mod data folder.
     *
     * @return
     */
    public static Path Path() {
        Path path = Mod.MC.runDirectory.toPath().resolve(Mod.MOD_ID);
        path.toFile().mkdir();
        return path;
    }

    public static Path writeFile(String fileName, String content) throws IOException {
        return writeFile(fileName, content, true);
    }

    public static File getConfigFile() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), Mod.MOD_ID + ".json");
    }


    public static void writeConfig(String content) throws IOException {
        boolean ignore;
        File file = getConfigFile();
        Files.deleteIfExists(file.toPath());
        Files.createFile(file.toPath());
        if (!file.exists()) ignore = file.createNewFile();
        Files.write(file.toPath(), content.getBytes(), StandardOpenOption.WRITE);
    }

    public static void writeConfig(File file, String content) throws IOException {
        boolean ignore;
        Files.deleteIfExists(file.toPath());
        Files.createFile(file.toPath());
        if (!file.exists()) ignore = file.createNewFile();
        Files.write(file.toPath(), content.getBytes(), StandardOpenOption.WRITE);
    }

    public static Path writeFile(String fileName, String content, boolean doCharSet) throws IOException {
        Path path = Path().resolve(fileName);
        Files.deleteIfExists(path);
        Files.createFile(path);
        Files.write(path, content.getBytes(), StandardOpenOption.WRITE);
        return path;
    }

    public static String readFile(String fileName, Charset charset) throws IOException {
        return Files.readString(Path().resolve(fileName), charset);
    }
    public static String readFile(String fileName) throws IOException {
        return Files.readString(Path().resolve(fileName));
    }

    public static String readConfig(File file) throws IOException {
        return Files.readString(file.toPath());
    }

    public static boolean exists(String fileName) {
        return Files.exists(Path().resolve(fileName));
    }

}