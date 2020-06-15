package net.vivatcreative.plotmanager.managers;

import net.vivatcreative.core.exceptions.WorldNotFoundException;
import net.vivatcreative.core.utils.VivatWorld;

public class Config {
    private static final VivatWorld[] worlds = {VivatWorld.BRONZE, VivatWorld.SILVER, VivatWorld.GOLD, VivatWorld.DIAMOND, VivatWorld.EMERALD, VivatWorld.MASTER, VivatWorld.FREEBUILD};

    public static VivatWorld[] getRegisteredWorlds(){ return worlds; }

    public static boolean pluginAllowed(String w){
        VivatWorld world = null;
        try {
            world = VivatWorld.fromString(w);
        } catch (WorldNotFoundException e) {
            return false;
        }
        for(VivatWorld s: worlds)
            if(s == world)
                return true;
        return false;
    }
}
