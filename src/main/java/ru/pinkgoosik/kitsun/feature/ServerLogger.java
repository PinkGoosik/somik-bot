package ru.pinkgoosik.kitsun.feature;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildMessageChannel;
import org.jetbrains.annotations.Nullable;
import ru.pinkgoosik.kitsun.Bot;
import ru.pinkgoosik.kitsun.event.DiscordEventsListener;
import ru.pinkgoosik.kitsun.util.KitsunColors;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

public class ServerLogger {
	public String server;
	public boolean enabled = false;
	public String channel = "";

	public ServerLogger(String serverID) {
		this.server = serverID;
	}

	public void enable(String channelID) {
		this.enabled = true;
		this.channel = channelID;
	}

	public void disable() {
		this.enabled = false;
	}

	public void ifEnabled(Consumer<ServerLogger> consumer) {
		if(enabled) consumer.accept(this);
	}

	public void onMemberJoin(Member member) {
		var embed = new EmbedBuilder();
		embed.setAuthor(member.getUser().getAsTag() + " joined", null, member.getUser().getEffectiveAvatarUrl());
		embed.setColor(KitsunColors.getGreen());
		embed.setTimestamp(Instant.now());
		log(embed.build());
	}

	public void onMemberLeave(User user) {
		var embed = new EmbedBuilder();
		embed.setAuthor(user.getAsTag() + " left", null, user.getEffectiveAvatarUrl());
		embed.setColor(KitsunColors.getRed());
		embed.setTimestamp(Instant.now());
		log(embed.build());
	}

	public void onMessageUpdate(DiscordEventsListener.CachedMessage old, Message message) {
		if(!old.contentRaw().equals(message.getContentRaw())) {
			var embed = new EmbedBuilder();
			embed.setTitle("Message Updated", "https://discord.com/channels/" + message.getGuild().getId() + "/" + message.getChannel().getId() + "/" + message.getId());
			embed.addField(new MessageEmbed.Field("Channel", "<#" + message.getChannel().getId() + ">", true));
			embed.addField(new MessageEmbed.Field("Member", "<@" + message.getAuthor().getId() + ">", true));
			embed.addField(new MessageEmbed.Field("Before", old.contentRaw(),false));
			embed.addField(new MessageEmbed.Field("After", message.getContentRaw(),false));
			embed.setColor(KitsunColors.getBlue());
			embed.setTimestamp(Instant.now());
			log(embed.build());
		}
	}

	public void onMessageDelete(DiscordEventsListener.CachedMessage message) {
		var embed = new EmbedBuilder();
		embed.setTitle("Message Deleted");
		embed.addField(new MessageEmbed.Field("Channel", "<#" + message.channelId() + ">", true));
		embed.addField(new MessageEmbed.Field("Member", "<@" + message.memberId() + ">", true));
		embed.addField(new MessageEmbed.Field("Message", message.contentRaw(), false));
		embed.setColor(KitsunColors.getRed());
		embed.setTimestamp(Instant.now());
		log(embed.build());
	}

	public void onVoiceChannelNameUpdate(String old, String current) {
		var embed = new EmbedBuilder();
		embed.setTitle("Voice Channel Updated");
		embed.addField(new MessageEmbed.Field("Before", old, false));
		embed.addField(new MessageEmbed.Field("After", current, false));
		embed.setColor(KitsunColors.getBlue());
		embed.setTimestamp(Instant.now());
		log(embed.build());
	}

	public void onVoiceChannelDelete(AutoChannelsManager.Session session, @Nullable Member owner, Channel voiceChannel) {
		var embed = new EmbedBuilder();
		embed.setTitle("Voice Channel Deleted");
		embed.addField(new MessageEmbed.Field("Channel", voiceChannel.getName(), true));
		if(owner != null) {
			embed.addField(new MessageEmbed.Field("Owner", "<@" + owner.getId() + ">", true));
		}

		if(session != null) {
			Instant created = Instant.parse(session.created);
			Instant now = Instant.now();
			embed.addField(new MessageEmbed.Field("Lasted", "**" + (int) ChronoUnit.MINUTES.between(created, now) + "** min", false));
		}

		embed.setColor(KitsunColors.getRed());
		embed.setTimestamp(Instant.now());
		log(embed.build());
	}

	public void onAutoChannelCreate(Member member, Channel voiceChannel) {
		var embed = new EmbedBuilder();
		embed.setTitle("Voice Channel Created");
		embed.addField(new MessageEmbed.Field("Channel", voiceChannel.getName(), true));
		embed.addField(new MessageEmbed.Field("Member", "<@" + member.getId() + ">", true));
		embed.setColor(KitsunColors.getGreen().getRGB());
		embed.setTimestamp(Instant.now());
		log(embed.build());
	}

	private void log(MessageEmbed embed) {
		if(Bot.jda.getGuildChannelById(ChannelType.TEXT, channel) instanceof GuildMessageChannel chan) {
			chan.sendMessageEmbeds(embed).queue();
		}
	}
}
