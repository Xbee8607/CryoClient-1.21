package net.hockeyfan17.cryoclient;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.hockeyfan17.cryoclient.features.BoatYaw;
import net.hockeyfan17.cryoclient.features.DemocracyChat;
import net.hockeyfan17.cryoclient.features.HidePassengers;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.hockeyfan17.cryoclient.features.PitReminder;
import net.hockeyfan17.cryoclient.modConfig.ModConfigScreen;



public class Client implements ClientModInitializer {


    @Override
    public void onInitializeClient() {

        AutoConfig.register(ModConfigScreen.class, GsonConfigSerializer::new);
        BoatYaw.BoatYawHud();
        PitReminder.PitReminderHud();

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
            PitReminder.pitReminderFunction(rawMessage);
            return true;
        });
    }
}
