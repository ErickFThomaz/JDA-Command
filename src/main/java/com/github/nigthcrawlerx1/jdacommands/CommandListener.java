package com.github.nigthcrawlerx1.jdacommands;

import com.github.nigthcrawlerx1.jdacommands.command.CommandEvent;
import com.github.nigthcrawlerx1.jdacommands.command.ICommand;
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
        if (event.getAuthor().isBot() || event.getAuthor().isFake())
            return;

        String prefix, msg = event.getMessage().getContentRaw().toLowerCase();
        if (msg.startsWith(prefix = builder.getPrefix()) || msg.startsWith(prefix = builder.getAlternativePrefix()) || msg.startsWith(event.getJDA().getSelfUser().getAsMention() +" ")) {
            String fprefix = prefix, alias = msg.substring(fprefix.length()).split(" ")[0];
            ICommand cmd = builder.getCommandManager().getCommands().stream().filter(c -> Arrays.asList(c.getAliases()).contains(alias)).findFirst().orElse(null);
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
                try {
                    cmd.invoke(commandEvent);
                }catch (Exception ex){
                    log.error("Houve um erro ao tentar executar o comando {} . Error" , cmd.getName() , ex);
                }
        }
    }

    public static boolean haveRole(CommandEvent event){
        Role m = event.getGuild().getRolesByName(event.getCommand().roleName() , true).get(0);
        if(event.getGuild().getMember(event.getAuthor()).getRoles().contains(m) || event.getMember().isOwner()){
            return true;
        }
        return false;
    }
}
