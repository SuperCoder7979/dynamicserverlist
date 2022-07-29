package supercoder79.dynamicserverlist;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("dynamicserverlist")
public class DynamicServerList {
    public static final List<SyntheticServerData> INJECT = new ArrayList<>();

    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public DynamicServerList() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        try {
            Path path = FMLPaths.CONFIGDIR.get().resolve("dynamicserverlist.json");
            File file = path.toFile();
            if (file.exists()) {
                JsonObject root = JsonParser.parseString(Files.readString(path)).getAsJsonObject();
                for (JsonElement el : root.get("servers").getAsJsonArray()) {
                    JsonObject obj = el.getAsJsonObject();
                    SyntheticServerData data = new SyntheticServerData(obj.get("name").getAsString(), obj.get("ip").getAsString(), false);
                    INJECT.add(data);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Loaded {} synthetic server entries", INJECT.size());
    }
}
