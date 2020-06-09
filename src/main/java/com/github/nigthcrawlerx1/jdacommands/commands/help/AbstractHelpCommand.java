package com.github.nigthcrawlerx1.jdacommands.commands.help;

import com.github.nigthcrawlerx1.jdacommands.builders.CommandBuilder;
import com.github.nigthcrawlerx1.jdacommands.commands.CommandEvent;
import com.github.nigthcrawlerx1.jdacommands.commands.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class AbstractHelpCommand implements ICommand {
    @Override
    public final void onCommand(CommandEvent event, String[] args) {
        CommandBuilder settings = event.getCommandSettings();
        String prefix = settings.getPrefix(event.getGuild().getIdLong());
        Map<String, ICommand> unmodifiableCommands = Collections.unmodifiableMap(settings.getCommands());
        if (args.length == 1) {
            String label = settings.isLabelIgnoreCase() ? args[0].toLowerCase() : args[0];
            if (settings.getLabelSet().contains(label)) {
                ICommand command = settings.getCommands().get(label);
                this.provideSpecificHelp(event, prefix, command, settings.getLabels(command));
            } else {
                this.provideGeneralHelp(event, prefix, unmodifiableCommands);
            }
        } else {
            this.provideGeneralHelp(event, prefix, unmodifiableCommands);
        }
    }

    public abstract void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands);

    public abstract void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels);
}
