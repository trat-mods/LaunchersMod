package net.launchers.mod.loader;

import net.fabricmc.api.ModInitializer;
import net.launchers.mod.initializer.LMBlock;
import net.launchers.mod.initializer.LMEntities;
import net.launchers.mod.initializer.LMItem;
import net.launchers.mod.initializer.LMSounds;

public class LMLoader implements ModInitializer
{
    public static final String MOD_ID = "launchersmod";
    
    @Override public void onInitialize()
    {
        LMBlock.initialize();
        LMEntities.initialize();
        LMItem.initialize();
        LMSounds.initialize();
    }
}
