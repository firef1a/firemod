package dev.fire.config;

import com.google.gson.*;
import dev.fire.FileManager;
import dev.fire.Mod;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Config {
    private static Config instance;
    public boolean TestVar = DefaultConfig.TestVar;

    public static Config getConfig() {
        if (instance == null) {
            try {
                instance = new Config();

                //Gson gson = new GsonBuilder().setPrettyPrinting().create();
                JsonObject object = new JsonParser().parse(FileManager.readConfig(FileManager.getConfigFile())).getAsJsonObject();

                instance.TestVar = object.get("TestVar").getAsBoolean();

            } catch (Exception exception) {
                Mod.LOGGER.info("Config didn't load: " + exception);
                Mod.LOGGER.info("Making a new one.");
                instance = new Config();
                instance.save();
            }
        }
        return instance;
    }

    public static void clear() {
        instance = null;
    }

    private void save() {
        try {
            JsonObject object = new JsonObject();

            object.addProperty("TestVar", Config.getConfig().TestVar);

            FileManager.writeConfig(FileManager.getConfigFile(), object.toString());
        } catch (Exception e) {
            Mod.LOGGER.info("Couldn't save config: " + e);
        }
    }

    public YetAnotherConfigLib getLibConfig() {
        YetAnotherConfigLib.Builder yacl =
                YetAnotherConfigLib.createBuilder()
                        .title(Text.literal("Used for narration. Could be used to render a title in the future."))
                        .category(displayCategory().build());

        return yacl.save(this::save).build();
    }


    private ConfigCategory.Builder displayCategory() {
        ConfigCategory.Builder configBuilder = ConfigCategory.createBuilder()
                .name(Text.literal("HUD Displays"))
                .tooltip(Text.literal("Customize your Site-03 HUD!"));

        // define group builders

        // player list obj group
        OptionGroup playerList = OptionGroup.createBuilder()
                .name(Text.literal("test"))
                .description(OptionDescription.of(Text.literal("moment of inertia")))
                .option(Option.createBuilder(boolean.class)
                        .name(Text.literal("placeholder bool"))
                        .description(OptionDescription.createBuilder()
                                .text(Text.literal("realism"))
                                .build())
                        .binding(
                                DefaultConfig.TestVar,
                                () -> TestVar,
                                opt -> TestVar = opt
                        )
                        .controller(TickBoxControllerBuilder::create)
                        .build())
                .build();


        configBuilder.group(playerList);


        // return built category
        return configBuilder;
    }



}
