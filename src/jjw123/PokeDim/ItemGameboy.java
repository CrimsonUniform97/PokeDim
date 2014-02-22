package jjw123.PokeDim;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ItemGameboy extends Item {

	public ItemGameboy(int par1) {
		super(par1);
	}
	
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if(par3EntityPlayer instanceof EntityPlayerMP) {
        	((EntityPlayerMP)par3EntityPlayer).mcServer.getConfigurationManager().transferPlayerToDimension((EntityPlayerMP) par3EntityPlayer, par3EntityPlayer.dimension == 0 ? PokeDim.INSTANCE.dimID : 0, new TeleporterPokemon((WorldServer) par2World));
        }
        return par1ItemStack;
    }
}
