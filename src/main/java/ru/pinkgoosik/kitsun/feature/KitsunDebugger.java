package ru.pinkgoosik.kitsun.feature;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import ru.pinkgoosik.kitsun.Bot;

import java.util.ArrayList;

public class KitsunDebugger {
	private static String debugChannel = "967506328190877726";
	public static final ArrayList<String> CACHE = new ArrayList<>();

	public static void onConnect(ReadyEvent event) {
		if(event.getJDA().getSelfUser().getId().equals("935826731925913630")) debugChannel = "1081197875830206475";
	}

	public static void ping(String message) {
		Bot.LOGGER.error(message);
		sendMessage("<@287598520268095488>\n" + message);
	}

	public static void report(String message) {
		Bot.LOGGER.error(message);
		sendMessage(message);
	}

	public static void info(String text) {
		Bot.LOGGER.info(text);
		sendMessage(text);
	}

	private static void sendMessage(String text) {
//		for(String reported : CACHE) {
//			if(reported.equals(text)) return;
//		}
		try {
			if(Bot.jda.getGuildChannelById(debugChannel) instanceof TextChannel textChannel) {
				textChannel.sendMessage(text).queue();
			}
//			CACHE.add(text);
		}
		catch(Exception e) {
			Bot.LOGGER.error("Failed to send debug message due to an exception:\n" + e);
		}
	}

}
