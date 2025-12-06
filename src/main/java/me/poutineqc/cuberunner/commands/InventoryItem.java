package me.poutineqc.cuberunner.commands;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.poutineqc.cuberunner.utils.ItemBannerManager;
import me.poutineqc.cuberunner.utils.ItemHeadManager;
import me.poutineqc.cuberunner.utils.ItemStackManager;

public class InventoryItem {
	private int position;
	private ItemStackManager item;
	
	public InventoryItem(ItemStackManager item, int position) {
		this.item = item;
		this.position = position;
	}
	
	public InventoryItem(ItemStackManager item) {
		this.item = item;
	}
	
	public InventoryItem(ItemStack itemStack) {
		Material type = itemStack.getType();
		if (type == Material.PLAYER_HEAD) {
			this.item = new ItemHeadManager(itemStack);
		} else if (isBannerMaterial(type)) {
			this.item = new ItemBannerManager(itemStack);
		} else {
			this.item = new ItemStackManager(itemStack);
		}
	}
	
	private static boolean isBannerMaterial(Material material) {
		return material == Material.WHITE_BANNER || material == Material.BLACK_BANNER 
			|| material == Material.BLUE_BANNER || material == Material.BROWN_BANNER
			|| material == Material.CYAN_BANNER || material == Material.GRAY_BANNER
			|| material == Material.GREEN_BANNER || material == Material.LIGHT_BLUE_BANNER
			|| material == Material.LIGHT_GRAY_BANNER || material == Material.LIME_BANNER
			|| material == Material.MAGENTA_BANNER || material == Material.ORANGE_BANNER
			|| material == Material.PINK_BANNER || material == Material.PURPLE_BANNER
			|| material == Material.RED_BANNER || material == Material.YELLOW_BANNER;
	}

	public InventoryItem(Material material) {
		switch (material) {
		case PLAYER_HEAD:
			this.item = new ItemHeadManager();
			break;
		case WHITE_BANNER:
		case BLACK_BANNER:
		case BLUE_BANNER:
		case BROWN_BANNER:
		case CYAN_BANNER:
		case GRAY_BANNER:
		case GREEN_BANNER:
		case LIGHT_BLUE_BANNER:
		case LIGHT_GRAY_BANNER:
		case LIME_BANNER:
		case MAGENTA_BANNER:
		case ORANGE_BANNER:
		case PINK_BANNER:
		case PURPLE_BANNER:
		case RED_BANNER:
		case YELLOW_BANNER:
			this.item = new ItemBannerManager();
			break;
		default:
			this.item = new ItemStackManager(material);
		}
	}

	public InventoryItem(Material material, int position) {
		this(material);
		this.position = position;
	}

	public ItemStackManager getItem() {
		return item;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public void addToInventory(Inventory inventory) {
		inventory.setItem(position, item.getItem());
	}
}