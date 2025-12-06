package me.poutineqc.cuberunner.utils;

import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import java.time.Duration;

public class Utils {

	private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

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
		Component titleComponent = MINI_MESSAGE.deserialize(color(title));
		Component subtitleComponent = MINI_MESSAGE.deserialize(color(subtitle));
		Title.Times times = Title.Times.times(
			Duration.ofMillis(fadeIn * 50L),
			Duration.ofMillis(stay * 50L),
			Duration.ofMillis(fadeOut * 50L)
		);
		Title titleObject = Title.title(titleComponent, subtitleComponent, times);
		p.showTitle(titleObject);
	}
	
	/**
	 * Convert legacy & color codes to MiniMessage format and return as Component
	 * @param string The string with & color codes
	 * @return The Component ready to send to players
	 */
	public static Component coloredComponent(String string) {
		return MINI_MESSAGE.deserialize(convertLegacyColorCodesToMiniMessage(string));
	}

	/**
	 * Convert legacy color codes (&) to MiniMessage format
	 * @param string The string with & color codes
	 * @return The string converted to MiniMessage format
	 */
	public static String color(String string) {
		return convertLegacyColorCodesToMiniMessage(string);
	}

	/**
	 * Check if two strings are equal when color codes are stripped
	 * Handles both & codes and § codes
	 * @param l2 First string
	 * @param l1 Second string
	 * @return true if the strings are equal when stripped
	 */
	public static boolean isEqualOnColorStrip(String l2, String l1) {
		try {
			return strip(l1).equalsIgnoreCase(strip(l2));
		} catch (Exception e) {
			// If stripping fails, try direct comparison
			return stripLegacyCharacters(l1).equalsIgnoreCase(stripLegacyCharacters(l2));
		}
	}

	/**
	 * Strip legacy color characters without parsing (fast fallback)
	 * @param string The string to strip
	 * @return The string without color codes
	 */
	private static String stripLegacyCharacters(String string) {
		if (string == null) return "";
		// Remove both & and § color codes and formatting codes
		return string.replaceAll("[&§][0-9a-fk-or]", "");
	}

	/**
	 * Strip color codes from a string
	 * @param string The string to strip
	 * @return The string without color codes
	 */
	public static String strip(String string) {
		if (string == null) return "";
		
		// First convert legacy § codes to & codes so we can process them
		String normalized = string.replace('§', '&');
		
		// Then convert & codes to MiniMessage format for deserialization
		Component component = MINI_MESSAGE.deserialize(convertLegacyColorCodesToMiniMessage(normalized));
		return plainText(component);
	}

	/**
	 * Extract plain text from a Component (recursively)
	 */
	private static String plainText(Component component) {
		StringBuilder sb = new StringBuilder();
		// Get the text content of this component itself if it's a TextComponent
		if (component instanceof net.kyori.adventure.text.TextComponent) {
			sb.append(((net.kyori.adventure.text.TextComponent) component).content());
		}
		// Then append all children recursively
		component.children().forEach(child -> sb.append(plainText(child)));
		return sb.toString();
	}

	/**
	 * Convert a Component back to string with & color codes
	 * Reconstructs the original & code format from MiniMessage Components
	 * @param component The component to convert
	 * @return The string with & color codes
	 */
	public static String componentToColorString(Component component) {
		if (component == null) return "";
		
		// Get the serialized form and convert back to & codes
		String serialized = MiniMessage.miniMessage().serialize(component);
		
		// Convert MiniMessage tags back to & codes
		return serialized
				.replace("<black>", "&0")
				.replace("<dark_blue>", "&1")
				.replace("<dark_green>", "&2")
				.replace("<dark_aqua>", "&3")
				.replace("<dark_red>", "&4")
				.replace("<dark_purple>", "&5")
				.replace("<gold>", "&6")
				.replace("<gray>", "&7")
				.replace("<dark_gray>", "&8")
				.replace("<blue>", "&9")
				.replace("<green>", "&a")
				.replace("<aqua>", "&b")
				.replace("<red>", "&c")
				.replace("<light_purple>", "&d")
				.replace("<yellow>", "&e")
				.replace("<white>", "&f")
				.replace("<obf>", "&k")
				.replace("<b>", "&l")
				.replace("<st>", "&m")
				.replace("<u>", "&n")
				.replace("<i>", "&o")
				.replace("<reset>", "&r");
	}

	/**
	 * Convert legacy & color codes to MiniMessage format
	 * Legacy codes like &c become <red>, &a becomes <green>, etc.
	 * @param text The text with & color codes
	 * @return The text converted to MiniMessage format
	 */
	private static String convertLegacyColorCodesToMiniMessage(String text) {
		if (text == null) return "";
		
		// Map of legacy color codes to MiniMessage tags
		return text
				.replace("&0", "<black>")
				.replace("&1", "<dark_blue>")
				.replace("&2", "<dark_green>")
				.replace("&3", "<dark_aqua>")
				.replace("&4", "<dark_red>")
				.replace("&5", "<dark_purple>")
				.replace("&6", "<gold>")
				.replace("&7", "<gray>")
				.replace("&8", "<dark_gray>")
				.replace("&9", "<blue>")
				.replace("&a", "<green>")
				.replace("&b", "<aqua>")
				.replace("&c", "<red>")
				.replace("&d", "<light_purple>")
				.replace("&e", "<yellow>")
				.replace("&f", "<white>")
				.replace("&k", "<obf>")
				.replace("&l", "<b>")
				.replace("&m", "<st>")
				.replace("&n", "<u>")
				.replace("&o", "<i>")
				.replace("&r", "<reset>");
	}

}
