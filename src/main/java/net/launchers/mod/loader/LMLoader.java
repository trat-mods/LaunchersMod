package net.launchers.mod.loader;

import net.fabricmc.api.ModInitializer;
import net.launchers.mod.initializer.*;

public class LMLoader implements ModInitializer
{
    public static final String MOD_ID = "launchersmod";
    
    @Override public void onInitialize()
    {
        LMBlock.initialize();
        LMEntities.initialize();
        LMItem.initialize();
        LMSounds.initialize();
        LMCommands.initialize();
    }
}
