package com.github.nigthcrawlerx1.jdacommands.command;

import com.github.nigthcrawlerx1.jdacommands.utils.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.List;
import java.util.function.Consumer;

public class CommandEvent {

    private final GuildMessageReceivedEvent event;
    private final String arguments;
    private final ICommand command;

    public CommandEvent(ICommand command, GuildMessageReceivedEvent event, String arguments){
        this.command = command;
        this.event = event;
        this.arguments = arguments;
    }
    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    public String getArguments() {
        return arguments;
    }

    public List<User> getMentionedUsers() {
        return getEvent().getMessage().getMentionedUsers();
    }

    public List<Member> getMentionedMembers() {
        return getEvent().getMessage().getMentionedMembers();
    }

    public String[] getContents() {
        return StringUtils.advancedSplitArgs(arguments, 0);
    }

    public JDA getJDA(){
        return getEvent().getJDA();
    }

    public void sendMessage(Message message) {
        getChannel().sendMessage(message).queue();
    }

    public void sendMessage(String message) {
        getChannel().sendMessage(message).queue();
    }

    public void sendMessageFormat(String message , Object... objects) {
        getChannel().sendMessageFormat(message , objects).queue();
    }

    public void sendFormat(String message, Object... format) {
        getChannel().sendMessageFormat(message, format).queue();
    }

    public void sendMessage(Message message , Consumer<? super Message> consumer) {
        getChannel().sendMessage(message).queue(consumer);
    }

    public void sendMessage(Message message , Consumer<? super Message> consumer , Consumer<? super Throwable> throwable) {
        getChannel().sendMessage(message).queue(consumer , throwable);
    }

    public void sendMessage(String message , Consumer<? super Message> consumer , Consumer<? super Throwable> throwable) {
        getChannel().sendMessage(message).queue(consumer , throwable);
    }

    public void sendMessage(MessageEmbed message , Consumer<? super Message> consumer) {
        getChannel().sendMessage(message).queue(consumer);
    }
    public void sendMessage(String message ,  Consumer<? super Message> consumer) {
        getChannel().sendMessage(message).queue(consumer);
    }

    public void sendMessage(MessageEmbed embed) {
        getChannel().sendMessage(embed).queue();
    }

    public Member getMember() {
        return getEvent().getMember();
    }

    public User getUser() {
        return getEvent().getAuthor();
    }

    public User getAuthor() {
        return getUser();
    }

    public Guild getGuild() {
        return getEvent().getGuild();
    }

    public Message getMessage() {
        return getEvent().getMessage();
    }

    public SelfUser getSelfUser() {
        return getJDA().getSelfUser();
    }

    public Member getSelfMember() {
        return getGuild().getSelfMember();
    }

    public TextChannel getChannel() {
        return getEvent().getChannel();
    }

    public ICommand getCommand() {
        return command;
    }
}
