package com.disdaccbot.commands;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Say implements CommandsInterface{

	@Override
	public String getName() {
		return "say";
	}

	@Override
	public String getDesc() {
		return "Get the bot to send a message";
	}

	@Override
	public List<OptionData> getOptions() {
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.STRING, "message", "Get the bot to send a message", true));
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		
		//Get the required option
		OptionMapping messageOption = event.getOption("message");
		String message = messageOption.getAsString();
		
		//get optional option
		MessageChannel channel;
		OptionMapping channelOption = event.getOption("channel");
		if(channelOption != null) {
			channel = channelOption.getAsChannel().asGuildMessageChannel();
		}
		else {
			channel = event.getMessageChannel();
		}
		
		channel.sendMessage(message).queue();
		event.reply("The bot has passed your mesage!").setEphemeral(true).queue();
		
	}
	
}
