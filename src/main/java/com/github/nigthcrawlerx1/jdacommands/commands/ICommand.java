package com.github.nigthcrawlerx1.jdacommands.commands;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Set;

public interface ICommand {

    Message DEFAULT_INFO = new MessageBuilder().setContent("Nenhuma informação, descrição ou ajuda definida para este comando.").build();

    void onCommand(final CommandEvent event, final String[] args);


    default Message info(Member member, String prefix, Set<String> labels) {
        return DEFAULT_INFO;
    }
}
