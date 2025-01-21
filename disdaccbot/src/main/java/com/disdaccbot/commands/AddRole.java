package com.disdaccbot.commands;

import java.util.ArrayList;
import java.util.List;

import com.disdaccbot.CommandsInterface;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class AddRole implements CommandsInterface {

	@Override
	public String getName() {
		return "addrole";
	}

	@Override
	public String getDesc() {
		return "Adds a role to a user in the server.";
	}

	@Override
	public List<OptionData> getOptions() {
		List<OptionData> options = new ArrayList<>();
		options.add(new OptionData(OptionType.USER, "user","The user that will receive the role", true));
		options.add(new OptionData(OptionType.ROLE, "role","The role to be given to a user", true));
		return options;
	}

	@Override
	public void execute(SlashCommandInteractionEvent event) {

		Member user = event.getOption("user").getAsMember();
		Role role = event.getOption("role").getAsRole();
		
		event.getGuild().addRoleToMember(user, role).queue();
		event.reply("Successfully added "+ role.getAsMention()+ " for "+ user.getAsMention()+ ".").queue();
	}

}
