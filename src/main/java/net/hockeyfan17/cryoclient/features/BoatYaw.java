package net.hockeyfan17.cryoclient.features;

import com.mojang.brigadier.CommandDispatcher;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hockeyfan17.cryoclient.Main;
import net.hockeyfan17.cryoclient.modConfig.ModConfigScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class BoatYaw {
    static ModConfigScreen Config = AutoConfig.getConfigHolder(ModConfigScreen.class).getConfig();

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public static void BoatYawCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        if (client == null || client.player == null) return;

        List<String> CryoClient = List.of("cc", "CryoClient");

        for (String cc : CryoClient) {
            dispatcher.register(literal(cc)
                    .then(literal("BoatYaw")
                            .executes(context -> {
                                Config.boatYawToggle = !Config.boatYawToggle;
                                Text message = Main.CryoClientName.copy()
                                        .append("Boat Yaw ").formatted(Formatting.GRAY)
                                        .append(Text.literal(Config.boatYawToggle ? "Enabled" : "Disabled")
                                                .formatted(Config.boatYawToggle ? Formatting.GREEN : Formatting.RED));
                                    client.player.sendMessage(message);
                                return 1;
                            })
                    )
            );
        }
    }

    public static void RotationsNeededCommand(CommandDispatcher<FabricClientCommandSource> dispatcher) {

        List<String> CryoClient = List.of("cc", "CryoClient");

        for (String cc : CryoClient) {
            dispatcher.register(literal(cc)
                    .then(literal("RotationsNeeded")
                            .executes(context -> {
                                totalRotationNeeded();
                                return 1;
                            })
                    )
            );
        }
    }

    public static void BoatYawHud() {
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            if (Config.boatYawToggle && client.player != null && client.player.getVehicle() instanceof BoatEntity boat) {
                double yaw = getBoatYaw(boat);
                String displayText = String.format("%.4f", yaw);
                int screenWidth = client.getWindow().getScaledWidth();
                int screenHeight = client.getWindow().getScaledHeight();
                int anchorX = (screenWidth / 2) + 20; // center of the screen
                int textWidth = client.textRenderer.getWidth(displayText);
                float x = anchorX - textWidth; // centered horizontally
                float y = screenHeight - 85; // 20px above hotbar (hotbar is ~20px tall)

                drawContext.drawTextWithShadow(
                        client.textRenderer,
                        displayText,
                        (int) x,
                        (int) y,
                        0xFFAAAAAA
                );
            }
        });
    }

    static double boatAngle;

    public static double getBoatYaw(BoatEntity boat) {
        float rawYaw = boat.getYaw();
        boatAngle = Math.round(rawYaw * 10000.0) / 10000.0;
        return Math.round(rawYaw * 10000.0) / 10000.0;
    }

    static double totalRotation;

    public static void totalRotationNeeded() {
        var client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        double startAngle = boatAngle;
        double baseTarget = 90.0;
        double step = 90.9091;
        int maxSteps = 50 * 360 / (int) step;

        double minDifference = Double.MAX_VALUE;
        int bestSteps = 0;
        double bestAngle = startAngle;

        for (int steps = -maxSteps; steps <= maxSteps; steps++) {
            double rotatedAngle = startAngle + steps * step;

            double k = Math.round((rotatedAngle - baseTarget) / 360.0);
            double nearestTarget = baseTarget + k * 360;

            double difference = Math.abs(rotatedAngle - nearestTarget);

            if (difference < minDifference) {
                minDifference = difference;
                bestSteps = steps;
                bestAngle = rotatedAngle;
            }
        }

        totalRotation = bestSteps * step;

        Text message = Main.CryoClientName.copy()
                        .append(String.valueOf(bestAngle)).formatted(Formatting.GREEN);
        client.player.sendMessage(message);
    }
}