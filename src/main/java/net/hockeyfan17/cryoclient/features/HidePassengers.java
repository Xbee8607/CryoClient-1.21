package net.hockeyfan17.cryoclient.features;

import com.mojang.brigadier.CommandDispatcher;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.hockeyfan17.cryoclient.modConfig.ModConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List;
import net.hockeyfan17.cryoclient.Main;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class HidePassengers {

    private static final MinecraftClient client = MinecraftClient.getInstance();
    static ModConfigScreen Config = AutoConfig.getConfigHolder(ModConfigScreen.class).getConfig();

    public static void HidePassengerCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        assert client.player != null;
        List<String> CryoClient = List.of("cc", "CryoClient");

        for (String cc : CryoClient) {
            dispatcher.register(literal(cc)
                            .executes(context -> {
                                client.player.sendMessage(Text.literal("Missing Args").formatted(Formatting.RED));
                                return 1;
                            })
                    .then(literal("HidePassengers")
                            .executes(context -> {
                                Config.hidePassengersToggle = !Config.hidePassengersToggle;
                                Text message = Main.CryoClientName.copy()
                                        .append(Text.literal("Hide Passengers ").formatted(Formatting.GRAY))
                                        .append(Text.literal(Config.hidePassengersToggle ? "Enabled" : "Disabled")
                                                .formatted(Config.hidePassengersToggle ? Formatting.GREEN : Formatting.RED));
                                client.player.sendMessage(message);
                                return 1;
                            })
                    )
            );
        }
    }
}
