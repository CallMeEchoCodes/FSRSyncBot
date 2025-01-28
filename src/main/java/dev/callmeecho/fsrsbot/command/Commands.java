package dev.callmeecho.fsrsbot.command;

import dev.callmeecho.fsrsbot.command.tool.PingCommand;
import dev.callmeecho.fsrsbot.registry.Registries;

public final class Commands {
	public static final Command PING = register(new PingCommand());

	private static <T extends Command> T register(T entry) {
		Registries.COMMAND.register(entry.getName(), entry);
		return entry;
	}

	public static void init() {
		// NO-OP
	}
}
