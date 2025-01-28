package dev.callmeecho.fsrsbot.listener;

import dev.callmeecho.fsrsbot.registry.Registries;
import org.javacord.api.listener.GloballyAttachableListener;

public final class Listeners {
	public static final CommandListener COMMAND = register("command", new CommandListener());

	public static final RoleSyncAddListener ROLE_SYNC_ADD = register("role_sync_add", new RoleSyncAddListener());

	public static final RoleSyncRemoveListener ROLE_SYNC_REMOVE = register("role_sync_remove", new RoleSyncRemoveListener());


	private static <T extends GloballyAttachableListener> T register(String id, T entry) {
		Registries.LISTENER.register(id, entry);
		return entry;
	}

	public static void init() {
		// NO-OP
	}
}
