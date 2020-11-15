package com.github.nigthcrawlerx1.jdacommands.command;

/*
 *   Copyright 2020 Erick (NightCrawlerX1 / NightCrawlerX)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import com.github.nigthcrawlerx1.jdacommands.command.argumento.MultiArgumentos;
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
    private final MultiArgumentos multiArgumentos;

    public CommandEvent(ICommand command, GuildMessageReceivedEvent event, String arguments){
        this.command = command;
        this.event = event;
        this.arguments = arguments;
        this.multiArgumentos = new MultiArgumentos(arguments.split(" "), event.getGuild());
    }

    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    public String getArguments() {
        return arguments;
    }

    public MultiArgumentos getArgumentos(){
        return multiArgumentos;
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


    //Normals
    public void sendMessage(Message message) {
        getChannel().sendMessage(message).queue();
    }

    public void sendMessage(String message) {
        getChannel().sendMessage(message).queue();
    }

    public void sendMessageFormat(String message , Object... objects) {
        getChannel().sendMessageFormat(message , objects).queue();
    }

    public void sendMessage(MessageEmbed embed) {
        getChannel().sendMessage(embed).queue();
    }

    //Consumers <Message>

    public void sendMessage(Message message , Consumer<? super Message> consumer) {
        getChannel().sendMessage(message).queue(consumer);
    }

    public void sendMessage(MessageEmbed message , Consumer<? super Message> consumer) {
        getChannel().sendMessage(message).queue(consumer);
    }
    public void sendMessage(String message ,  Consumer<? super Message> consumer) {
        getChannel().sendMessage(message).queue(consumer);
    }

    //Consumers<Message , Throwable>
    public void sendMessage(String message , Consumer<? super Message> consumer , Consumer<? super Throwable> throwable) {
        getChannel().sendMessage(message).queue(consumer , throwable);
    }

    public void sendMessage(Message message , Consumer<? super Message> consumer , Consumer<? super Throwable> throwable) {
        getChannel().sendMessage(message).queue(consumer , throwable);
    }

    public void sendMessage(MessageEmbed message , Consumer<? super Message> consumer , Consumer<? super Throwable> throwable) {
        getChannel().sendMessage(message).queue(consumer , throwable);
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
