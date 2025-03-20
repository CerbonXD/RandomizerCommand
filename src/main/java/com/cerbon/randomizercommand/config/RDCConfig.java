package com.cerbon.randomizercommand.config;

import com.cerbon.randomizercommand.RandomizerCommand;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

@Config(name = RandomizerCommand.MOD_ID)
public class RDCConfig implements ConfigData {

    @ConfigEntry.BoundedDiscrete(min = 1, max = 4)
    public int commandLevel = 4;
    public List<String> permissions = new ArrayList<>();
}
