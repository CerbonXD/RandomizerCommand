package com.cerbon.randomizercommand.config;

import com.cerbon.randomizercommand.RandomizerCommand;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

@Config(name = RandomizerCommand.MOD_ID)
public class RDCConfig implements ConfigData {

    public List<Permission> permissions = new ArrayList<>();

    public static class Permission {
        public String permission;
    }
}
