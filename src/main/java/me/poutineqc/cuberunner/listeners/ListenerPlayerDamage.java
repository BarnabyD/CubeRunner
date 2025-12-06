package me.poutineqc.cuberunner.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.persistence.PersistentDataType;

import me.poutineqc.cuberunner.CRStats;
import me.poutineqc.cuberunner.CubeRunner;
import me.poutineqc.cuberunner.CRPlayer.PlayerStatsException;
import me.poutineqc.cuberunner.game.Arena;
import me.poutineqc.cuberunner.game.GameState;
import me.poutineqc.cuberunner.game.User;
import me.poutineqc.cuberunner.game.Arena.LeavingReason;

public class ListenerPlayerDamage implements Listener {

	@EventHandler
	public void onPlayerDamageByEntity(EntityDamageByEntityEvent event) throws PlayerStatsException {

		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		Arena arena = Arena.getArenaFromPlayer(player);

		if (arena == null)
			return;

		if (event.getCause() != DamageCause.FALLING_BLOCK) {
			event.setCancelled(true);
			return;
		}

		if (arena.getGameState() != GameState.ACTIVE) {
			event.setCancelled(true);
			return;
		}

		User user = arena.getUser(player);
		if (user.isEliminated()) {
			event.setCancelled(true);
			return;
		}

		event.setDamage(0);
		arena.eliminateUser(arena.getUser(player), LeavingReason.CRUSHED);

		// Get the damager UUID from persistent data instead of custom name
		if (event.getDamager() instanceof FallingBlock) {
			FallingBlock block = (FallingBlock) event.getDamager();
			NamespacedKey key = new NamespacedKey(CubeRunner.get(), "player-uuid");
			String dammagerUUID = block.getPersistentDataContainer().get(key, PersistentDataType.STRING);
			if (dammagerUUID != null && !dammagerUUID.equalsIgnoreCase(player.getUniqueId().toString())) {
				Player damager = Bukkit.getPlayer(UUID.fromString(dammagerUUID));
				if (damager != null) {
					CubeRunner.get().getCRPlayer(damager).increment(CRStats.KILLS, true);
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {

		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		Arena arena = Arena.getArenaFromPlayer(player);

		if (arena == null)
			return;

		if (event.getCause() != DamageCause.FALLING_BLOCK) {
			event.setCancelled(true);
			return;
		}

		User user = arena.getUser(player);
		if (user.isEliminated()) {
			event.setCancelled(true);
			return;
		}
	}

}
