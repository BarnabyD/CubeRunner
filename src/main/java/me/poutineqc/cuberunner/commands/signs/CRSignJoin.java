package me.poutineqc.cuberunner.commands.signs;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;

import me.poutineqc.cuberunner.Language;
import me.poutineqc.cuberunner.Language.Messages;
import me.poutineqc.cuberunner.commands.CRCommand;
import me.poutineqc.cuberunner.game.Arena;
import me.poutineqc.cuberunner.utils.Permissions;
import me.poutineqc.cuberunner.utils.Utils;

public class CRSignJoin extends CRSignPlayers {

	public CRSignJoin(SignChangeEvent event, Arena arena) {
		super(event.getBlock().getLocation(), SignType.JOIN);
		this.arena = arena;

		Language local = Language.getDefault();

		event.line(0, Utils.coloredComponent(local.get(Messages.PREFIX_LONG)));
		event.line(1, Utils.coloredComponent(local.get(Messages.KEYWORD_SIGN_JOIN)));
		switch (arena.getGameState()) {
		case ACTIVE:
		case ENDING:
			event.line(3, Utils.coloredComponent(local.get(Messages.KEYWORD_GAMESTATE_ACTIVE)));
			break;
		case READY:
		case STARTUP:
			event.line(3, Utils.coloredComponent("&a" + Utils.strip(Utils.color(local.get(Messages.KEYWORD_SCOREBOARD_PLAYERS)))
					+ " : &d" + String.valueOf(arena.getAmountOfPlayerInGame()) + "/"
					+ String.valueOf(arena.getMaxPlayer())));
			break;
		case UNREADY:
			event.line(3, Utils.coloredComponent(local.get(Messages.KEYWORD_GAMESTATE_UNSET)));
			break;
		}

		signs.add(this);
		updateSigns(arena);

	}

	public CRSignJoin(UUID uuid, Location location) {
		super(uuid, location, SignType.JOIN);

		boolean delete = false;
		BlockState block = null;
		try {
			block = location.getBlock().getState();
			if (!(block instanceof Sign)) {
				delete = true;
			} else {
				Sign sign = (Sign) block;
				arena = Arena.getArena(sign.getLine(2));
				if (arena == null)
					delete = true;
			}
		} catch (NullPointerException e) {
			delete = true;
		}

		if (delete) {
			removeSign();
			return;
		}

		signs.add(this);
		updateSigns(arena);
	}

	@Override
	protected boolean updateSign(Language local, Sign sign) {
		sign.line(0, Utils.coloredComponent(local.get(Messages.PREFIX_LONG)));
		sign.line(1, Utils.coloredComponent(local.get(Messages.KEYWORD_SIGN_JOIN)));
		sign.update();

		if (arena == null)
			return false;

		updateDisplay(local, sign);
		return true;
	}

	@Override
	public void onInteract(Player player) {
		if (Permissions.hasPermission(CRCommand.JOIN.getPermission(), player, true))
			CRCommand.JOIN.execute(plugin, player, new String[]{"join", arena.getName()}, true);
	}

}
