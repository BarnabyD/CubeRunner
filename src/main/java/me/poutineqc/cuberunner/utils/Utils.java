package me.poutineqc.cuberunner.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import java.time.Duration;

public class Utils {

	/**
	 * Send a title to a player using the modern Paper API.
	 * This replaces the deprecated NMS reflection-based sendTitle method.
	 * @param p The player to send the title to
	 * @param title The main title text
	 * @param subtitle The subtitle text
	 * @param fadeIn Time for the title to fade in (in ticks)
	 * @param stay Time for the title to stay (in ticks)
	 * @param fadeOut Time for the title to fade out (in ticks)
	 */
	public static void sendTitle(Player p, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
		Component titleComponent = Component.text(color(title));
		Component subtitleComponent = Component.text(color(subtitle));
		Title.Times times = Title.Times.times(
			Duration.ofMillis(fadeIn * 50L),
			Duration.ofMillis(stay * 50L),
			Duration.ofMillis(fadeOut * 50L)
		);
		Title titleObject = Title.title(titleComponent, subtitleComponent, times);
		p.showTitle(titleObject);
	}
	
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}

	public static boolean isEqualOnColorStrip(String l2, String l1) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', l1))
				.equalsIgnoreCase(ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', l2)));
	}

	public static String strip(String string) {
		return ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', string));
	}

}
