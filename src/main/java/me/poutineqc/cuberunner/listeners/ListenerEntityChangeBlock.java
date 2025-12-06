package me.poutineqc.cuberunner.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.persistence.PersistentDataType;

import me.poutineqc.cuberunner.CubeRunner;

public class ListenerEntityChangeBlock implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityChangeBlock(EntityChangeBlockEvent event) {
		if ((event.getEntity() instanceof FallingBlock)) {
			FallingBlock fallingBlock = (FallingBlock) event.getEntity();
			// Check if block is a wool variant (modern materials use individual color constants)
			String materialName = fallingBlock.getBlockData().getMaterial().toString();
			if (materialName.endsWith("_WOOL") && (event.getBlock().getType() == Material.AIR)) {
				// Check if this is a CubeRunner falling block using persistent data
				NamespacedKey key = new NamespacedKey(CubeRunner.get(), "player-uuid");
				if (fallingBlock.getPersistentDataContainer().has(key, PersistentDataType.STRING))
					event.setCancelled(false);
			}
		}
	}

}
