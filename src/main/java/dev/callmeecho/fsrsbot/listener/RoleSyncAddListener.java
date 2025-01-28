package dev.callmeecho.fsrsbot.listener;

import dev.callmeecho.fsrsbot.FSRSBot;
import dev.callmeecho.fsrsbot.FSRSConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.role.UserRoleAddEvent;
import org.javacord.api.listener.server.role.UserRoleAddListener;

import java.util.Optional;

public final class RoleSyncAddListener implements UserRoleAddListener {
	@Override
	public void onUserRoleAdd(UserRoleAddEvent event) {
		Server server = event.getServer();
		if (server.getId() != FSRSConfig.INSTANCE.guildId()) return;
		if (event.getRole().getId() != FSRSConfig.INSTANCE.guildRoleId()) return;

		Optional<User> syncUser = FSRSBot.getSyncServer().getMemberById(event.getUser().getId());
		if (syncUser.isEmpty()) return;

		syncUser.get().addRole(FSRSBot.getSyncRole())
			.exceptionally(throwable -> {
				FSRSBot.logError("Error syncing role", throwable);
				return null;
			});
	}
}
