package me.poutineqc.cuberunner.utils;

import org.bukkit.Material;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemHeadManager extends ItemStackManager {

	private String playerName;

	public ItemHeadManager(String playerName) {
		super(Material.PLAYER_HEAD);
		this.playerName = playerName;
	}

	public ItemHeadManager(ItemStack itemStack) {
		super(itemStack);

		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		this.playerName = meta != null && meta.getOwningPlayer() != null ? meta.getOwningPlayer().getName() : null;
	}

	public ItemHeadManager() {
		super(Material.PLAYER_HEAD);
	}

	@Override
	public ItemStack getItem() {
		ItemStack itemStack = super.getItem();
		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		if (playerName != null && meta != null) {
			meta.setOwningPlayer(Bukkit.getOfflinePlayer(playerName));
			itemStack.setItemMeta(meta);
		}
		return itemStack;
	}

	@Override
	public boolean isSame(ItemStack itemStack) {
		if (!super.isSame(itemStack))
			return false;

		SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
		if (meta != null && meta.hasOwner()) {
			if (playerName == null)
				return false;
			else if (meta.getOwningPlayer() != null && !meta.getOwningPlayer().getName().equalsIgnoreCase(playerName))
				return false;
		} else if (playerName != null)
			return false;

		return true;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerName() {
		return playerName;
	}
}