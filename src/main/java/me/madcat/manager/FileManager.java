package me.madcat.manager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.madcat.MadCat;
import me.madcat.features.Feature;
import me.madcat.features.modules.Module;

public class FileManager
extends Feature {
    private final Path base = this.getMkDirectory(this.getRoot(), "MadCat");

    private String[] expandPath(String string) {
        return string.split(":?\\\\\\\\|\\/");
    }

    private static String[] getBaseResolve0(int n) {
        return new String[n];
    }

    public Path getConfig() {
        return this.getBasePath().resolve("config");
    }

    public Path getMkBaseDirectory(String ... stringArray) {
        return this.getMkDirectory(this.getBasePath(), this.expandPaths(stringArray).collect(Collectors.joining(File.separator)));
    }

    public static List<String> readTextFileAllLines(String string) {
        try {
            Path path = Paths.get(string);
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        }
        catch (IOException iOException) {
            System.out.println("WARNING: Unable to read file, creating new file: " + string);
            FileManager.appendTextFile("", string);
            return Collections.emptyList();
        }
    }

    public Path getMkBaseResolve(String ... stringArray) {
        Path path = this.getBaseResolve(stringArray);
        this.createDirectory(path.getParent());
        return path;
    }

    public Path getBaseResolve(String ... stringArray) {
        String[] stringArray2 = this.expandPaths(stringArray).toArray(FileManager::getBaseResolve0);
        if (stringArray2.length < 1) {
            throw new IllegalArgumentException("missing path");
        }
        return this.lookupPath(this.getBasePath(), stringArray2);
    }

    private void createDirectory(Path path) {
        block3: {
            try {
                if (Files.isDirectory(path)) break block3;
                if (Files.exists(path)) {
                    Files.delete(path);
                }
                Files.createDirectories(path);
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    public FileManager() {
        this.getMkDirectory(this.base, "pvp");
        for (Module.Category category : MadCat.moduleManager.getCategories()) {
            Path path = this.getMkDirectory(this.base, "config");
            this.getMkDirectory(path, category.getName());
        }
    }

    public Path getCache() {
        return this.getBasePath().resolve("cache");
    }

    public Path getMkConfigDirectory(String ... stringArray) {
        return this.getMkDirectory(this.getConfig(), this.expandPaths(stringArray).collect(Collectors.joining(File.separator)));
    }

    private Stream<String> expandPaths(String ... stringArray) {
        return Arrays.stream(stringArray).map(this::expandPath).flatMap(Arrays::stream);
    }

    public Path getBasePath() {
        return this.base;
    }

    public static void appendTextFile(String string, String string2) {
        try {
            Path path = Paths.get(string2);
            OpenOption[] openOptionArray = new OpenOption[1];
            openOptionArray[0] = Files.exists(path) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE;
            Files.write(path, Collections.singletonList(string), StandardCharsets.UTF_8, openOptionArray);
        }
        catch (IOException iOException) {
            System.out.println("WARNING: Unable to write file: " + string2);
        }
    }

    private Path getRoot() {
        return Paths.get("");
    }

    private Path getMkDirectory(Path path, String ... stringArray) {
        if (stringArray.length < 1) {
            return path;
        }
        Path path2 = this.lookupPath(path, stringArray);
        this.createDirectory(path2);
        return path2;
    }

    private Path lookupPath(Path path, String ... stringArray) {
        return Paths.get(path.toString(), stringArray);
    }
}

