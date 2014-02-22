package jjw123.PokeDim;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.PortalPosition;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterPokemon extends Teleporter {

	public TeleporterPokemon(WorldServer par1WorldServer) {
		super(par1WorldServer);
	}
	
	@Override public boolean placeInExistingPortal(Entity par1Entity, double par2, double par4, double par6, float par8) { return true; }
	@Override public boolean makePortal(Entity par1Entity) { return true; }
	@Override public void removeStalePortalLocations(long par1) {}
	@Override public void placeInPortal(Entity par1Entity, double par2, double par4, double par6, float par8) {}
}
