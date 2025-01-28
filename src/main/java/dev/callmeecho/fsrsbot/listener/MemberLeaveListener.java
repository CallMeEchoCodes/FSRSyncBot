package dev.callmeecho.fsrsbot.listener;

import dev.callmeecho.fsrsbot.FSRSBot;
import dev.callmeecho.fsrsbot.FSRSConfig;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.javacord.api.listener.server.member.ServerMemberLeaveListener;

import java.util.Optional;

public final class MemberLeaveListener implements ServerMemberLeaveListener {
	@Override
	public void onServerMemberLeave(ServerMemberLeaveEvent event) {
		Server server = event.getServer();
		if (server.getId() != FSRSConfig.INSTANCE.guildId()) return;

		Optional<User> syncUser = FSRSBot.getSyncServer().getMemberById(event.getUser().getId());
		if (syncUser.isEmpty()) return;

		syncUser.get().removeRole(FSRSBot.getSyncRole())
			.exceptionally(throwable -> {
				// Just putting this here to make it explicit that we don't care about an error here.
				return null;
			});
	}
}
