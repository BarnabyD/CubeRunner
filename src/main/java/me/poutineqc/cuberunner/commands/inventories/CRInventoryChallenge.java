package me.poutineqc.cuberunner.commands.inventories;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import me.poutineqc.cuberunner.CRPlayer;
import me.poutineqc.cuberunner.CRStats;
import me.poutineqc.cuberunner.CRPlayer.PlayerStatsException;
import me.poutineqc.cuberunner.CubeRunner;
import me.poutineqc.cuberunner.Language;
import me.poutineqc.cuberunner.AchievementManager.Challenge;
import me.poutineqc.cuberunner.Language.Messages;
import me.poutineqc.cuberunner.commands.InventoryItem;
import me.poutineqc.cuberunner.utils.ItemStackManager;
import me.poutineqc.cuberunner.utils.Utils;

public class CRInventoryChallenge extends CRInventory {

	public CRInventoryChallenge(CRPlayer crPlayer) {
		super(crPlayer);

		Language local = crPlayer.getLanguage();
		this.title = local.get(Messages.STATS_CHALLENGES_TITLE);
		this.amountOfRows = 3;
		createInventory();

		try {
			fillInventory();
		} catch (PlayerStatsException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fillInventory() throws PlayerStatsException {
		Language local = crPlayer.getLanguage();
		InventoryItem icon;
		int location;

		/***************************************************
		 * Glass Spacer
		 ***************************************************/

		icon = new InventoryItem(new ItemStackManager(Material.CYAN_STAINED_GLASS_PANE));
		icon.getItem().setDisplayName("");

		for (int i = 0; i < inventory.getSize(); i++)
			switch (i) {
			case 9:
			case 10:
			case 11:
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
				icon.setPosition(i);
				icon.addToInventory(inventory);
			}

		/***************************************************
		 * Stats
		 ***************************************************/

		NumberFormat format2 = new DecimalFormat("#0.00");
		NumberFormat format3 = new DecimalFormat("#0.000");
		
		icon = new InventoryItem(new ItemStackManager(Material.PAPER), 4);
		icon.getItem().displayName(Utils.coloredComponent("&6&l" + local.get(Messages.STATS_GUI_TITLE) + " : &bCubeRunner"));

		icon.getItem().addToLore(Utils.color("&m&7----------------------------"));
		icon.getItem().addToLore(local.get(Messages.STATS_INFO_AVERAGE_SCORE) + " : &e"
				+ format2.format(crPlayer.getDouble(CRStats.AVERAGE_SCORE)));
		icon.getItem()
				.addToLore(local.get(Messages.STATS_INFO_DISTANCE_RAN) + " : &e"
						+ format3.format(crPlayer.getDouble(CRStats.TOTAL_DISTANCE) / 1000) + " &a"
						+ local.get(Messages.KEYWORD_GENERAL_DISTANCE));
		icon.getItem().addToLore(Utils.color("&m&7----------------------------"));
		icon.getItem().addToLore(
				local.get(Messages.STATS_INFO_GAMES) + " : &e" + String.valueOf(crPlayer.getInt(CRStats.GAMES_PLAYED)));
		icon.getItem().addToLore(local.get(Messages.STATS_INFO_TOTAL_SCORE) + " : &e"
				+ String.valueOf(crPlayer.getInt(CRStats.TOTAL_SCORE)));
		icon.getItem().addToLore(
				local.get(Messages.STATS_INFO_KILLS) + " : &e" + String.valueOf(crPlayer.getInt(CRStats.KILLS)));
		icon.getItem().addToLore(local.get(Messages.STATS_INFO_MULTIPLAYER_WON) + " : &e"
				+ String.valueOf(crPlayer.getInt(CRStats.MULTIPLAYER_WON)));
		icon.getItem().addToLore(Utils.color("&m&7----------------------------"));
		icon.getItem().addToLore(Utils.color("&d") + Utils.strip(local.get(Messages.STATS_INFO_TIME_PLAYED)) + ": "
				+ getTimePLayed(local, crPlayer.getInt(CRStats.TIME_PLAYED)));
		if (CubeRunner.get().isEconomyEnabled())
			icon.getItem()
					.addToLore(Utils.color("&d") + Utils.strip(local.get(Messages.STATS_INFO_MONEY)) + ": &e"
							+ format2.format(crPlayer.getDouble(CRStats.MONEY)) + " &a"
							+ CubeRunner.get().getEconomy().currencyNamePlural());

		icon.addToInventory(inventory);

		/***************************************************
		 * Challenges
		 ***************************************************/

		location = 19;
		for (Entry<Challenge, Double> challenge : CubeRunner.get().getAchievementManager().getChallenges().entrySet()) {
			if (location == 22)
				location++;
			
			boolean done = crPlayer.hasChallenge(challenge.getKey().getCrStats());
			icon = new InventoryItem(new ItemStackManager(done ? Material.LIME_DYE : Material.GRAY_DYE));
			icon.getItem().displayName(Utils.coloredComponent(
					(done ? "&a" : "&c") + Utils.strip(local.get(challenge.getKey().getMessage()))));
			icon.getItem().addToLore(Utils.color("&7----------------------------"));
			icon.getItem()
					.addToLore(Utils.color("&b") + Utils.strip(local.get(Messages.KEYWORD_STATS_PROGRESSION)) + ": "
							+ (done ? (Utils.color("&a") + Utils.strip(local.get(Messages.KEYWORD_STATS_COMPLETED)))
									: (Utils.color("&c") + Utils.strip(local.get(Messages.KEYWORD_STATS_NOT_COMPLETED)))));
			if (!done && CubeRunner.get().isEconomyEnabled() && CubeRunner.get().getConfiguration().achievementsRewards)
				icon.getItem().addToLore(Utils.color("&b")
						+ Utils.strip(Utils.color(
								local.get(Messages.KEYWORD_STATS_REWARD) + ": "))
						+ Utils.color("&e") + String.valueOf(challenge.getValue())
						+ CubeRunner.get().getEconomy().currencyNamePlural());

			icon.setPosition(location++);
			icon.addToInventory(inventory);
		}

		/***************************************************
		 * Arrow
		 ***************************************************/

		icon = new InventoryItem(new ItemStackManager(Material.ARROW));

		icon.getItem().displayName(Utils.coloredComponent(local.get(Messages.STATS_GUI_TITLE)));

		icon.setPosition(8);
		icon.addToInventory(inventory);

		/***************************************************
		 * Display
		 ***************************************************/

		openInventory();

	}

	private String getTimePLayed(Language local, int timePlayed) {
		long hours = 0;

		timePlayed /= 60000;
		while (timePlayed > 60) {
			timePlayed -= 60;
			hours++;
		}

		return Utils.color("&e") + String.valueOf(hours) + Utils.color("&a") + " "
				+ Utils.strip(local.get(Messages.KEYWORD_GENERAL_HOURS)) + Utils.color("&e") + " "
				+ String.valueOf(timePlayed) + Utils.color("&a") + " "
				+ Utils.strip(local.get(Messages.KEYWORD_GENERAL_MINUTES));
	}

	@Override
	public void update(ItemStack itemStack, InventoryAction action) {
		Language local = crPlayer.getLanguage();

		if (Utils.isEqualOnColorStrip(itemStack.getItemMeta().getDisplayName(), local.get(Messages.STATS_GUI_TITLE))) {
			new CRInventoryStats(crPlayer);
		}
	}

}
