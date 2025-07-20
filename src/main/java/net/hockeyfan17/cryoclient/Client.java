package net.hockeyfan17.cryoclient;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.hockeyfan17.cryoclient.features.BoatYaw;
import net.hockeyfan17.cryoclient.features.DemocracyChat;
import net.hockeyfan17.cryoclient.features.HidePassengers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.hockeyfan17.cryoclient.features.PitReminder;
import net.minecraft.client.MinecraftClient;



public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {


        BoatYaw.BoatYawHud();
        PitReminder.PitReminderHud();

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            CryoConfig.INSTANCE.load();
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            CryoConfig.INSTANCE.save();
        });

        CryoConfig.INSTANCE.load();

        // BoatYaw Command //
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            BoatYaw.BoatYawCommand(dispatcher);
        });


        // RotationsNeeded Command //
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            BoatYaw.RotationsNeededCommand(dispatcher);
        });

        // Hide Passengers Command //
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            HidePassengers.HidePassengerCommand(dispatcher);
        });


        // Democracy Chat Command //
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            DemocracyChat.DemocracyChatCommand(dispatcher);
        });

        // Pit Reminder Command //
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            PitReminder.PitReminderCommand(dispatcher);
        });


        ClientReceiveMessageEvents.ALLOW_GAME.register((message, sender) -> {
            String rawMessage = message.getString();
            DemocracyChat.democracyChatFunction(rawMessage);
            if(CryoConfig.INSTANCE.pitReminderToggle) {PitReminder.pitReminderFunction(rawMessage);}
            return true;
        });
    }
}
