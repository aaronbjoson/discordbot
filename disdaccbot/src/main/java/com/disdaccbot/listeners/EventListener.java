package com.disdaccbot.listeners;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jetbrains.annotations.NotNull;

import com.disdaccbot.Config;
import com.disdaccbot.databases.SQLiteDataSource;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {
	
	@Override
	public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
		//Azure Reacted to a message in #general channel
		User user = event.getUser();
		String emoji = event.getReaction().getEmoji().getAsReactionCode();
		String channelMention = event.getChannel().getAsMention();
		//String jumpLink = event.getJumpUrl();
		
		String message = user.getAsTag() + " reacted to a message with " + emoji + " in the "+ channelMention + " channel!";
		event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
	}
	
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event) {
		String message = event.getMessage().getContentRaw();
		User sentByUser = event.getMember().getUser();
		Message.suppressContentIntentWarning();
		
		if(message.contains("ping") && !sentByUser.isBot()){
			event.getChannel().sendMessage("pong").queue();;
		}
		
		//final long guildId = event.getGuild().getIdLong();
	}
	
	@Override
	public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
		
		User user = event.getUser();
		String message = "**"+user.getAsTag() +"** updated their online status to "+ event.getNewOnlineStatus() + "!";		
		event.getGuild().getDefaultChannel().asTextChannel().sendMessage(message).queue();
	}
	
	
	
	@SuppressWarnings("unused")
	private String getPrefix(long guildId) {
		try(final PreparedStatement preparedStatement = SQLiteDataSource.getConnection().prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")){
			
			preparedStatement.setString(1, String.valueOf(guildId));
			
			try (final ResultSet resultSet = preparedStatement.executeQuery()){
				if(resultSet.next()) {
					return resultSet.getString("prefix");
				}
			}
			
			try (final PreparedStatement insertStatement = SQLiteDataSource.getConnection().prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {
				insertStatement.setString(1, String.valueOf(guildId));
				
				insertStatement.execute();
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return Config.get("prefix");
	}
}
