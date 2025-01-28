package dev.callmeecho.fsrsbot.listener;

import dev.callmeecho.fsrsbot.FSRSBot;
import dev.callmeecho.fsrsbot.util.EmbedUtil;
import dev.callmeecho.fsrsbot.registry.Registries;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.listener.interaction.SlashCommandCreateListener;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class CommandListener implements SlashCommandCreateListener {
	private final ExecutorService executorService = Executors.newCachedThreadPool();

	@Override
	public void onSlashCommandCreate(SlashCommandCreateEvent event) {
		CompletableFuture.runAsync(
				Registries.COMMAND
					.get(event.getSlashCommandInteraction().getCommandId())
					.<Runnable>map(
						command -> () ->
							command.execute(event.getSlashCommandInteraction(), event.getApi())
					).orElse(() -> event.getSlashCommandInteraction().createImmediateResponder().setContent("Command not found").respond()), executorService)
			.whenComplete((unused, throwable) -> {
				if (throwable != null) {
					event.getSlashCommandInteraction().createImmediateResponder().addEmbed(EmbedUtil.error("An error occurred while executing the command")).respond();
					FSRSBot.logError("An error occurred while executing a command", throwable);
				}
			});
	}
}
