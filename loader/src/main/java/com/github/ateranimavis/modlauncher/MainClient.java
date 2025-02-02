package com.github.ateranimavis.modlauncher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.github.ateranimavis.util.ArgumentList;
import cpw.mods.modlauncher.Launcher;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.PathConverter;
import joptsimple.util.PathProperties;

public class MainClient {

    public static void main(final String[] args) throws IOException {
        final OptionParser parser = new OptionParser();
        final ArgumentAcceptingOptionSpec<Path> gameDir = parser.accepts("gameDir", "Alternative game directory").withRequiredArg().withValuesConvertedBy(new PathConverter(PathProperties.DIRECTORY_EXISTING)).defaultsTo(Paths.get("."));
        parser.allowsUnrecognizedOptions();
        final OptionSet optionSet = parser.parse(args);

        final Path gameDirectory = optionSet.valueOf(gameDir);
        final Path modsDirectory = modsDirectory(gameDirectory);

        final ArgumentList lst = ArgumentList.from(args);
        lst.putLazy("gameDir", ".");
        lst.putLazy("launchTarget", "client_dev");

        // TODO: List
        lst.put("mixin.config", "launcher.mixins.json");

        Launcher.main(lst.getArguments());
    }

    private static Path modsDirectory(Path gameDirectory) throws IOException {
        final Path modsDirectory = gameDirectory.resolve("mods");

        if (Files.notExists(modsDirectory)) {
            Files.createDirectories(modsDirectory);
        }

        return modsDirectory;
    }
}
