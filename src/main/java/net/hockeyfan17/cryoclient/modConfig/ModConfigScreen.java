package net.hockeyfan17.cryoclient.modConfig;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;


@Config(name = "cryoclient")
public final class ModConfigScreen implements ConfigData {
    @ConfigEntry.Gui.Tooltip
    public boolean boatYawToggle;

    @ConfigEntry.Gui.Tooltip
    public boolean hidePassengersToggle;

    @ConfigEntry.Gui.Tooltip
    public boolean democracyChatToggle;

    @ConfigEntry.Gui.Tooltip
    public boolean pitReminderToggle;
}