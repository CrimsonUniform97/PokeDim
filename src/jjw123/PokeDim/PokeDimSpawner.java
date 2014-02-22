package jjw123.PokeDim;

import java.util.EnumSet;

import net.minecraft.server.MinecraftServer;
import pixelmon.spawning.PixelmonSpawner;
import cpw.mods.fml.common.TickType;

public class PokeDimSpawner extends PixelmonSpawner {
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		findChunksForSpawning(MinecraftServer.getServer().worldServerForDimension(PokeDim.INSTANCE.dimID));
		doSpawning(MinecraftServer.getServer().worldServerForDimension(PokeDim.INSTANCE.dimID));
	}
}
