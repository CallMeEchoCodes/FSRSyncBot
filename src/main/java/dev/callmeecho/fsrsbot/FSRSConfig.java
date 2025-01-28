package dev.callmeecho.fsrsbot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.callmeecho.fsrsbot.util.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public record FSRSConfig(
	String token,
	long guildId,
	long syncGuildId,
	long guildRoleId,
	long syncGuildRoleId,
	long debugChannelId,
	boolean debug
) {
	public static final Codec<FSRSConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		Codec.STRING.fieldOf("token").forGetter(FSRSConfig::token),
		Codec.LONG.fieldOf("guild_id").forGetter(FSRSConfig::guildId),
		Codec.LONG.fieldOf("sync_guild_id").forGetter(FSRSConfig::syncGuildId),
		Codec.LONG.fieldOf("guild_role_id").forGetter(FSRSConfig::guildRoleId),
		Codec.LONG.fieldOf("sync_guild_role_id").forGetter(FSRSConfig::syncGuildRoleId),
		Codec.LONG.optionalFieldOf("debug_channel_id", 0L).forGetter(FSRSConfig::debugChannelId),
		Codec.BOOL.optionalFieldOf("debug", false).forGetter(FSRSConfig::debug)
	).apply(instance, FSRSConfig::new));

	public static final FSRSConfig INSTANCE = Util.make(() -> {
		Path path = Path.of("config.json");

		if (!Files.exists(path)) throw new IllegalArgumentException("Config file does not exist");

		List<String> lines;

		try {
			lines = Files.readAllLines(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		JsonElement element = new Gson().fromJson(String.join("\n", lines), JsonElement.class);
		DataResult<FSRSConfig> result = FSRSConfig.CODEC.parse(JsonOps.INSTANCE, element);

		if (result.error().isPresent())
			throw new IllegalStateException(result.error().toString());

		return result.getOrThrow();
	});
}
