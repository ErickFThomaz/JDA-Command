package com.github.nigthcrawlerx1.jdacommands.commands.help;

import com.github.nigthcrawlerx1.jdacommands.builders.CommandBuilder;
import com.github.nigthcrawlerx1.jdacommands.commands.CommandEvent;
import com.github.nigthcrawlerx1.jdacommands.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class DefaultHelpCommand extends AbstractHelpCommand {

    private final Message info = new MessageBuilder().setContent("Command info:\n" +"Mostra todos os comandos disponíveis ou fornece ajuda para um comando específico.").build();

    @Override
    public void provideGeneralHelp(CommandEvent event, String prefix, Map<String, ICommand> commands) {
        Member selfMember = event.getGuild().getSelfMember();
        if (event.checkBotPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
            return;

        CommandBuilder settings = event.getCommandSettings();
        EmbedBuilder embed = new EmbedBuilder().setColor(settings.getHelpColor() != null ? settings.getHelpColor() : selfMember.getColor());
        String helpLabels = "[" + String.join("|", settings.getLabels(this)) + "]";
        embed.appendDescription("Para saber mais sobre um comando específico, basta ligar para `").appendDescription(prefix)
                .appendDescription(helpLabels).appendDescription(" <label>`.\nOs seguintes comandos estão disponíveis no momento:\n");
        String commandsList = commands.keySet().stream().map((label) -> prefix + label).collect(Collectors.joining(", "));
        if (commandsList.length() < 1010)
            embed.addField("Comandos", "```\n" + commandsList + "```", false);
        else
            embed.addField("Atenção", "Muitos comandos para mostrar.", false);
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public void provideSpecificHelp(CommandEvent event, String prefix, ICommand command, Set<String> labels) {
        if (!event.checkBotPermissions(Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
            return;

        event.getChannel().sendMessage(command.info(event.getMember(), prefix, labels)).queue();
    }

    @Override
    public Message info(Member member, String prefix, Set<String> labels) {
        return info;
    }
}
