package me.poutineqc.cuberunner.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.kyori.adventure.text.Component;

public class ItemStackManager {

	protected Material material;
	protected int amount = 1;

	protected String name;
	protected List<String> lore = new ArrayList<String>();
	protected Map<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>();

	public ItemStackManager(Material material) {
		this.material = material;
	}

	public ItemStackManager(ItemStack itemStack) {
		this.material = itemStack.getType();
		this.amount = itemStack.getAmount();

		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			if (meta.hasDisplayName()) {
				// Convert Component display name back to string with color codes
				Component displayComponent = meta.displayName();
				if (displayComponent != null) {
					this.name = Utils.componentToColorString(displayComponent);
				}
			}
			this.enchantments = meta.hasEnchants() ? meta.getEnchants() : new HashMap<Enchantment, Integer>();
			if (meta.hasLore()) {
				// Convert Component lore back to strings with color codes
				List<Component> componentLore = meta.lore();
				if (componentLore != null) {
					this.lore = new ArrayList<>();
					for (Component component : componentLore) {
						this.lore.add(Utils.componentToColorString(component));
					}
				}
			}
		}
	}

	public ItemStack getItem() {
		ItemStack itemStack = new ItemStack(material, amount);

		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			if (name != null) {
				meta.displayName(Utils.coloredComponent(name));
			}
			if (lore != null && !lore.isEmpty()) {
				List<Component> componentLore = new ArrayList<>();
				for (String loreLine : lore) {
					componentLore.add(Utils.coloredComponent(loreLine));
				}
				meta.lore(componentLore);
			}
			for (Entry<Enchantment, Integer> enchantment : enchantments.entrySet())
				meta.addEnchant(enchantment.getKey(), enchantment.getValue(), true);

			itemStack.setItemMeta(meta);
		}
		return itemStack;
	}

	public boolean isSame(ItemStack itemStack) {
		if (material != itemStack.getType())
			return false;

		ItemMeta meta = itemStack.getItemMeta();
		if (meta == null)
			return name == null && enchantments.isEmpty();

		if (meta.hasDisplayName()) {
			if (name == null)
				return false;
			
			Component displayComponent = meta.displayName();
			if (displayComponent == null)
				return false;
			
			String metaDisplayName = Utils.componentToColorString(displayComponent);
			if (!Utils.isEqualOnColorStrip(metaDisplayName, name))
				return false;
			
		} else if (name != null)
			return false;

		if (meta.hasEnchants()) {
			for (Entry<Enchantment, Integer> enchantment : meta.getEnchants().entrySet())
				if (!hasEnchantement(enchantment.getKey(), enchantment.getValue()))
					return false;
		} else if (enchantments.size() > 0)
			return false;

		return true;
	}

	public void setDisplayName(String displayName) {
		this.name = Utils.color(displayName);
	}

	public void displayName(Component displayNameComponent) {
		this.name = Utils.componentToColorString(displayNameComponent);
	}

	public void addToLore(String loreLine) {
		lore.add(Utils.color(loreLine));
	}

	public void setLore(List<String> lore) {
		this.lore = lore;
	}

	public void clearLore() {
		lore = new ArrayList<String>();
	}

	public void addEnchantement(Enchantment enchantment, int level) {
		enchantments.put(enchantment, level);
	}

	public void setEnchantements(Map<Enchantment, Integer> enchantments) {
		this.enchantments = enchantments;
	}

	public void clearEnchantements() {
		enchantments.clear();
	}

	public int getMaxStackSize() {
		return material.getMaxStackSize();
	}

	public Material getMaterial() {
		return material;
	}

	public boolean hasEnchantement(Enchantment enchantement) {
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
			if (entry.getKey() == enchantement)
				return true;

		return false;
	}

	public boolean hasEnchantement(Enchantment enchantement, int value) {
		for (Entry<Enchantment, Integer> entry : enchantments.entrySet())
			if (entry.getKey() == enchantement)
				if (entry.getValue() == value)
					return true;
				else
					return false;

		return false;
	}

	public String getDisplayName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}
}
