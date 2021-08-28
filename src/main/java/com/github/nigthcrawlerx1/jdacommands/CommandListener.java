package com.github.nigthcrawlerx1.jdacommands;

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


import com.github.nigthcrawlerx1.jdacommands.command.CommandEvent;
import com.github.nigthcrawlerx1.jdacommands.command.ICommand;
import com.github.nigthcrawlerx1.jdacommands.utils.TimeUtils;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;

public class CommandListener extends ListenerAdapter {

    private final Logger log = LoggerFactory.getLogger(CommandListener.class);
    private final CommandBuilder builder;

    public CommandListener(CommandBuilder builder){
        this.builder = builder;
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem() || event.isWebhookMessage())
            return;

        String prefix, msg = event.getMessage().getContentRaw().toLowerCase();
        if (msg.startsWith(prefix = builder.getPrefix()) || msg.startsWith(prefix = builder.getAlternativePrefix()) || msg.startsWith(event.getJDA().getSelfUser().getAsMention() +" ")) {
            String fprefix = prefix, alias = msg.substring(fprefix.length()).split(" ")[0];
            ICommand cmd = builder.getCommandManager().getCommand(alias);
            if(cmd == null)
                return;

                CommandEvent commandEvent = new CommandEvent(cmd , event , event.getMessage().getContentRaw().substring(fprefix.length() + alias.length()).trim());

                if(cmd.guildOwner() && !commandEvent.getMember().isOwner()){
                    commandEvent.sendMessage("Somente o dono desse canal pode usar este comando.");
                    return;
                }

                if(cmd.requireRole()){
                    if(cmd.roleName() != null) {
                        if (!haveRole(commandEvent)) {
                            commandEvent.sendMessageFormat("Você precisa ter o cargo `%s` para poder usar este comando.", cmd.roleName());
                            return;
                        }
                    }
                }
                if(cmd.perms() != null){
                    if(!commandEvent.getMember().hasPermission(cmd.perms())){
                        commandEvent.sendMessageFormat("Você precisa da permissão %s para poder usar este comando.", cmd.perms());
                        return;
                    }
                }
                if(builder.getCooldownHandler().getCooldown(event.getGuild().getId() , event.getMember().getId() , cmd.getName()) > 0){
                    event.getChannel().sendMessageFormat("Você poderá usar este comando novamente em `%s`" , TimeUtils.getTime((builder.getCooldownHandler().getCooldown(event.getGuild().getId() , event.getMember().getId() , cmd.getName()))* 1000)).queue();
                    return;
                }
                try {
                    cmd.invoke(commandEvent);
                }catch (Exception ex){
                    log.error("Houve um erro ao tentar executar o comando {} . Error" , cmd.getName() , ex);
                }
                builder.getCooldownHandler().setCooldown(event.getGuild().getId(), event.getMember().getId(), cmd.getName() , cmd.getCooldown());
        }
    }

    public static boolean haveRole(CommandEvent event){
        Role m = event.getGuild().getRolesByName(event.getCommand().roleName() , true).get(0);
        return Objects.requireNonNull(event.getGuild().getMember(event.getAuthor())).getRoles().contains(m) || event.getMember().isOwner();
    }
}
