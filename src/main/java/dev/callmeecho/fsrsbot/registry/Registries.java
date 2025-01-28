package dev.callmeecho.fsrsbot.registry;

import dev.callmeecho.fsrsbot.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.listener.GloballyAttachableListener;

public final class Registries {
	private static final Logger LOGGER = LogManager.getLogger(Registries.class);

	public static final CommandRegistry COMMAND = new CommandRegistry();

	public static final Registry<GloballyAttachableListener> LISTENER = new SimpleRegistry<>();

	private Registries() {
		Util.utilError();
	}
}
