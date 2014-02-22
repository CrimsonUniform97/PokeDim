package jjw123.PokeDim;

import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Locale.Category;

import pixelmon.client.gui.inventoryExtended.InventoryDetectionTickHandler;
import pixelmon.config.PixelmonConfig;
import pixelmon.entities.pixelmon.EntityPixelmon;
import pixelmon.spawning.PixelmonSpawner;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SingleIntervalHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.common.registry.TickRegistry.TickQueueElement;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = "PokeDim", dependencies = "required-after:pixelmon")
public class PokeDim {
	
	@Instance("PokeDim")
	public static PokeDim INSTANCE;
	public int dimID = -2, gameboyItemID = 6540;
	public ItemGameboy gb;
	
	PriorityQueue<TickQueueElement> serverTickHandlers;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration c = new Configuration(event.getSuggestedConfigurationFile());
		dimID = c.get(c.CATEGORY_GENERAL, "Pokemon Dimension", -2).getInt();
		gameboyItemID = c.getItem("Gameboy", 6540).getInt();
		
		MinecraftForge.EVENT_BUS.register(this);
		ReflectionHelper.setPrivateValue(PixelmonConfig.class, null, true, "allowNonPixelmonMobs");
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		DimensionManager.registerProviderType(dimID, WorldProviderPokemon.class, false);
		DimensionManager.registerDimension(dimID, dimID);
		
		gb = new ItemGameboy(gameboyItemID);
		LanguageRegistry.addName(gb, "Gameboy");
		GameRegistry.addRecipe(new ItemStack(gb), "X", 'X', Block.dirt);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		try {
			serverTickHandlers = (PriorityQueue<TickQueueElement>) ReflectionHelper.findField(TickRegistry.class, "serverTickHandlers").get(null);
			Iterator<TickQueueElement> iterator = serverTickHandlers.iterator();
			while(iterator.hasNext()) {
				TickQueueElement tqe = iterator.next();
				if(tqe.ticker instanceof SingleIntervalHandler) {
					SingleIntervalHandler sth = (SingleIntervalHandler) tqe.ticker;
					ITickHandler th = (ITickHandler) ReflectionHelper.findField(SingleIntervalHandler.class, "wrapped").get(sth);
					if(th instanceof PixelmonSpawner) {
						serverTickHandlers.remove(tqe);
						TickRegistry.registerTickHandler(new PokeDimSpawner(), Side.SERVER);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@ForgeSubscribe
	@SideOnly(Side.CLIENT)
	public void onOverlay(RenderGameOverlayEvent event) {
		
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int offsetX = sr.getScaledWidth() / 3;

		String s = "Dimension Id: " + mc.thePlayer.dimension;
		fr.drawString(s, sr.getScaledWidth() / 2 - fr.getStringWidth(s) / 2, 2, 0xffdddddd);
	}
	
	@ForgeSubscribe
	public void onSpawn(EntityJoinWorldEvent event) {
		if(!(event.entity instanceof EntityPixelmon) && !(event.entity instanceof EntityPlayer) && event.entity instanceof EntityLiving && event.entity.dimension == dimID) {
			((EntityLiving)event.entity).setHealth(0);
		}
	}
}
