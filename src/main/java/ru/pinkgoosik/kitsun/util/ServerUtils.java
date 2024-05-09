package ru.pinkgoosik.kitsun.util;

import ru.pinkgoosik.kitsun.Bot;
import ru.pinkgoosik.kitsun.cache.ServerData;

public class ServerUtils {

	public static void forEach(ServerRunnable runnable) {
		Bot.jda.getGuilds().forEach(guild -> {
			String serverId = guild.getId();
			runnable.run(ServerData.get(serverId));
		});
	}

	public static void runFor(String serverId, ServerRunnable runnable) {
		Bot.jda.getGuilds().forEach(guild -> {
			if(guild.getId().equals(serverId)) {
				runnable.run(ServerData.get(serverId));
			}
		});
	}

	public static boolean exist(String serverId) {
		for(var guild : Bot.jda.getGuilds()) {
			if(guild.getId().equals(serverId)) return true;
		}
		return false;
	}

	@FunctionalInterface
	public interface ServerRunnable {
		void run(ServerData serverData);
	}
}
