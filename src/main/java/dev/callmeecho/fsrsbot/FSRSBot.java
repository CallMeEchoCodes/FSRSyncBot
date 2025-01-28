package dev.callmeecho.fsrsbot;

import dev.callmeecho.fsrsbot.command.Commands;
import dev.callmeecho.fsrsbot.listener.Listeners;
import dev.callmeecho.fsrsbot.registry.Registries;
import dev.callmeecho.fsrsbot.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.activity.ActivityType;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.util.logging.FallbackLoggerConfiguration;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public final class FSRSBot {
	private static final Logger LOGGER = LogManager.getLogger(FSRSBot.class);
	private static Server syncServer;
	private static Server server;

	private static Role syncRole;
	private static DiscordApi api;

	public static void main(String[] args) {
		DiscordApiBuilder apiBuilder = new DiscordApiBuilder();
		apiBuilder
			.setToken(FSRSConfig.INSTANCE.token())
			.addIntents(Intent.GUILDS, Intent.GUILD_MEMBERS);

		Listeners.init();
		Registries.LISTENER.freeze();
		Registries.LISTENER.forEach(apiBuilder::addListener);

		Commands.init();
		Registries.COMMAND.freeze();

		api = apiBuilder.login().join();

		Registries.COMMAND.sendCommands(api);

		if (FSRSConfig.INSTANCE.debug()) {
			FallbackLoggerConfiguration.setDebug(true);
			api.updateActivity(ActivityType.WATCHING, "Echo struggle");
			LOGGER.debug("Debug mode enabled");
		}

		syncServer = api.getServerById(FSRSConfig.INSTANCE.syncGuildId()).orElseThrow();
		server = api.getServerById(FSRSConfig.INSTANCE.guildId()).orElseThrow();

		syncRole = syncServer.getRoleById(FSRSConfig.INSTANCE.syncGuildRoleId()).orElseThrow();

		LOGGER.info("Logged in as {}", api.getYourself().getDiscriminatedName());
	}

	public static <T extends Runnable> void tryRun(T runnable, String message) {
		try {
			runnable.run();
		} catch (Throwable t) {
			logError(message, t);
		}
	}

	public static void logError(String message, Throwable t) {
		if (!FSRSConfig.INSTANCE.debug() || FSRSConfig.INSTANCE.debugChannelId() <= 0) return;

		StringWriter stackTrace = new StringWriter();
		PrintWriter writer = new PrintWriter(stackTrace);
		t.printStackTrace(writer);

		api.getTextChannelById(FSRSConfig.INSTANCE.debugChannelId())
			.ifPresent(channel -> {
				EmbedBuilder embed = new EmbedBuilder()
					.setTitle(message)
					.setDescription("```lisp\n%s```"
						.formatted(StringUtil.truncate(stackTrace.toString(), 2048)))
					.addField("Full message", t.getMessage(), false)
					.setColor(Color.RED);

				new MessageBuilder()
					.setEmbed(embed)
					.send(channel);
			});
	}

	public static DiscordApi getApi() {
		return api;
	}

	public static Server getSyncServer() {
		return syncServer;
	}

	public static Server getServer() {
		return server;
	}

	public static Role getSyncRole() {
		return syncRole;
	}
}
