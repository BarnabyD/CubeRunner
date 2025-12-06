package me.poutineqc.cuberunner.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import me.poutineqc.cuberunner.ArenaData;
import me.poutineqc.cuberunner.CubeRunner;
import me.poutineqc.cuberunner.MySQL;
import me.poutineqc.cuberunner.utils.ItemStackManager;

public class ColorManager {

	private long colorIndice;
	private List<ItemStackManager> allBlocks;
	private List<ItemStackManager> onlyChoosenBlocks;
	private MySQL mysql;
	private Arena arena;
	private ArenaData arenaData;

	public ColorManager(Long colorIndice, CubeRunner plugin, Arena arena) {
		this.colorIndice = colorIndice;
		this.mysql = plugin.getMySQL();
		this.arenaData = plugin.getArenaData();
		this.arena = arena;
		updateLists();
	}

	public void setColorIndice(long colorIndice) {
		this.colorIndice = colorIndice;
		updateLists();

		if (mysql.hasConnection()) {
			mysql.update("UPDATE " + CubeRunner.get().getConfiguration().tablePrefix + "ARENAS SET colorIndice="
					+ colorIndice + " WHERE name='" + arena.getName() + "';");
		} else {
			arenaData.getData().set("arenas." + arena.getName() + ".colorIndice", colorIndice);
			arenaData.saveArenaData();
		}
	}

	private static final Material[] WOOL_COLORS = {
		Material.WHITE_WOOL, Material.ORANGE_WOOL, Material.MAGENTA_WOOL, Material.LIGHT_BLUE_WOOL,
		Material.YELLOW_WOOL, Material.LIME_WOOL, Material.PINK_WOOL, Material.GRAY_WOOL,
		Material.LIGHT_GRAY_WOOL, Material.CYAN_WOOL, Material.PURPLE_WOOL, Material.BLUE_WOOL,
		Material.BROWN_WOOL, Material.GREEN_WOOL, Material.RED_WOOL, Material.BLACK_WOOL
	};

	private static final Material[] TERRACOTTA_COLORS = {
		Material.WHITE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.MAGENTA_TERRACOTTA, Material.LIGHT_BLUE_TERRACOTTA,
		Material.YELLOW_TERRACOTTA, Material.LIME_TERRACOTTA, Material.PINK_TERRACOTTA, Material.GRAY_TERRACOTTA,
		Material.LIGHT_GRAY_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.PURPLE_TERRACOTTA, Material.BLUE_TERRACOTTA,
		Material.BROWN_TERRACOTTA, Material.GREEN_TERRACOTTA, Material.RED_TERRACOTTA, Material.BLACK_TERRACOTTA
	};

	public void updateLists() {
		allBlocks = new ArrayList<ItemStackManager>();
		onlyChoosenBlocks = new ArrayList<ItemStackManager>();
		long tempColorIndice = colorIndice;

		for (int i = 31; i >= 0; i--) {
			ItemStackManager icon;
			if (i >= 16) {
				// Use terracotta colors for upper 16 colors
				icon = new ItemStackManager(TERRACOTTA_COLORS[i % 16]);
			} else {
				// Use wool colors for lower 16 colors
				icon = new ItemStackManager(WOOL_COLORS[i % 16]);
			}

			int value = (int) Math.pow(2, i);
			if (value <= tempColorIndice) {
				icon.addEnchantement(Enchantment.UNBREAKING, 1);
				tempColorIndice -= value;
				onlyChoosenBlocks.add(0, icon);
			}

			allBlocks.add(0, icon);
		}

		if (onlyChoosenBlocks.size() == 0)
			onlyChoosenBlocks = allBlocks;
	}

	public ItemStackManager getRandomAvailableBlock() {
		return onlyChoosenBlocks.get((int) Math.floor(Math.random() * onlyChoosenBlocks.size()));
	}

	public List<ItemStackManager> getAllBlocks() {
		return allBlocks;
	}

	public List<ItemStackManager> getOnlyChoosenBlocks() {
		return onlyChoosenBlocks;
	}

	public long getColorIndice() {
		return colorIndice;
	}

}
