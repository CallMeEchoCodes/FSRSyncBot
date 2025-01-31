package dev.callmeecho.fsrsbot.command.tool;

import dev.callmeecho.fsrsbot.command.Command;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.awt.*;
import java.time.temporal.ChronoUnit;

public class PingCommand implements Command {
	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public SlashCommandBuilder createSlashCommand() {
		return SlashCommand.with(getName(), "See the bot's latency");
	}

	@Override
	public void execute(SlashCommandInteraction interaction, DiscordApi api) {
		interaction.getChannel().orElseThrow().sendMessage("Pinging...").thenCompose(message -> {
			long roundtripLatency = ChronoUnit.MILLIS.between(interaction.getCreationTimestamp(), message.getCreationTimestamp());
			long gatewayLatency = api.getLatestGatewayLatency().toMillis();

			Color color = roundtripLatency < 150 ? Color.GREEN : roundtripLatency < 250 ? Color.YELLOW : Color.RED;

			EmbedBuilder embed = new EmbedBuilder()
				.setTitle("Pong!")
				.addField("Roundtrip Latency", "%dms".formatted(roundtripLatency), true)
				.addField("Gateway Latency", "%dms".formatted(gatewayLatency), true)
				.setColor(color);

			return message.delete().thenCompose(
				ignored -> interaction.createImmediateResponder().addEmbed(embed).respond());
		});
	}
}
