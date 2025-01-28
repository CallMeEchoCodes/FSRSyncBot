package dev.callmeecho.fsrsbot.listener;

import dev.callmeecho.fsrsbot.FSRSBot;
import dev.callmeecho.fsrsbot.FSRSConfig;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberJoinListener;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

import java.util.Optional;

public final class MemberJoinListener implements ServerMemberJoinListener {
	@Override
	public void onServerMemberJoin(ServerMemberJoinEvent event) {
		Server server = event.getServer();
		if (server.getId() != FSRSConfig.INSTANCE.syncGuildId()) return;

		Optional<User> user = FSRSBot.getServer().getMemberById(event.getUser().getId());
		if (user.isEmpty()) return;

		if (user.get().getRoles(FSRSBot.getServer()).stream()
			.noneMatch(role -> role.getId() == FSRSConfig.INSTANCE.guildRoleId())) return;

		event.getUser().addRole(FSRSBot.getSyncRole())
			.exceptionally(throwable -> {
				FSRSBot.logError("Error syncing role", throwable);
				return null;
			});
	}
}
