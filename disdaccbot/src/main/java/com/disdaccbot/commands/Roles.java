package com.disdaccbot.commands;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class Roles implements CommandsInterface{

	@Override
	public String getName() {
		return "roles";
	}

	@Override
	public String getDesc() {
		return "Display existing roles in the server";
	}

	@Override
	public List<OptionData> getOptions() {
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.STRING, "roles", "Display existing roles in the server", true));
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {
		event.deferReply().setEphemeral(true).queue();
		String response = "";
		
		for (Role role : event.getGuild().getRoles()) {
			response+= role.getAsMention()+"\n";
		}
		event.getHook().sendMessage(response).queue();
		
	}

}
