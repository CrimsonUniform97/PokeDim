package jjw123.PokeDim;

import net.minecraft.world.WorldProvider;

public class WorldProviderPokemon extends WorldProvider {
	
    public String getDimensionName() {
        return "Pokemon";
    }
    
    @Override
    public boolean shouldMapSpin(String entity, double x, double y, double z) {
        return false;
    }
}
