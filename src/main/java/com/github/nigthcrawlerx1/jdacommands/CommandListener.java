package com.github.nigthcrawlerx1.jdacommands;

import com.github.nigthcrawlerx1.jdacommands.command.CommandEvent;
import com.github.nigthcrawlerx1.jdacommands.command.ICommand;
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

        if (msg.startsWith(prefix = builder.getPrefix()) || msg.startsWith(prefix = builder.getAlternativePrefix()) ||
                msg.startsWith(prefix = event.getJDA().getSelfUser().getAsMention() + " ")) {
            String fprefix = prefix, alias = msg.substring(fprefix.length()).split(" ")[0];
            ICommand cmd = builder.getCommandManager().getCommands().stream().filter(c -> Arrays.asList(c.getAliases()).contains(alias)).findFirst().orElse(null);

            if(cmd == null)
                return;
            new Thread(() -> {
                CommandEvent commandEvent = new CommandEvent(cmd , event , event.getMessage().getContentRaw().substring(fprefix.length() + alias.length()).trim());
                try {
                    cmd.invoke(commandEvent);
                }catch (Exception ex){
                    log.error("Houve um erro ao tentar executar o comando {} . Error {}:{}" , cmd.getName() , ex.getMessage() , ex.getLocalizedMessage());
                }
            }).start();
        }
    }
}
