package com.github.nigthcrawlerx1.jdacommands.command.manager;

import com.github.nigthcrawlerx1.jdacommands.CommandBuilder;
import com.github.nigthcrawlerx1.jdacommands.annotations.RegisterCommand;
import com.github.nigthcrawlerx1.jdacommands.command.CommandEvent;
import com.github.nigthcrawlerx1.jdacommands.command.ICommand;
import com.github.nigthcrawlerx1.jdacommands.command.category.ICategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CommandManager {

    private final List<ICommand> commands;
    private final Logger log = LoggerFactory.getLogger(CommandManager.class);
    private final CommandBuilder builder;

    public CommandManager(@Nonnull String packege, @Nonnull CommandBuilder builder){
        this.builder = builder;
        long start = System.currentTimeMillis();
        log.info("Inicializando os comandos.");
        commands = new ArrayList<>(new Reflections(packege, new MethodAnnotationsScanner())
                .getMethodsAnnotatedWith(RegisterCommand.class).stream()
                .map(method -> {
                    if (method.getReturnType() != ICommand.class)
                        return null;
                    try {
                        return (ICommand) method.invoke(null);
                    } catch (Exception e) {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList()));
        long end = System.currentTimeMillis();
        long count = (end - start);
        log.info("Foram registrados {} comandos em {} segundos." , commands.size() , (count / 1000));
    }

    public List<ICommand> getCommands(ICategory category) {
        return commands.stream().filter(command -> category.equals(command.getCategory())).collect(Collectors.toList());
    }

    public void invoke(CommandEvent event) {
        ICommand cmd = event.getCommand();
        if (cmd.isOwnerOnly() && !builder.isOwner(event.getAuthor())) {
            event.sendMessage("Este comando só pode ser usado pelos donos.");
            return;
        }
        try {
            cmd.invoke(event);
        }catch (ArrayIndexOutOfBoundsException ex){
            log.info("Houve um erro ao tentar executar o comando {} pelo usuario {}.\n Erro:{}:{}" , cmd.getName() , event.getAuthor().getName() , ex.getMessage() , ex.getCause().getLocalizedMessage());
            event.sendMessage(getHelp(cmd , event.getMember()));
        }
    }

    public List<ICommand> getCommands() {
        return commands;
    }

    public MessageEmbed getHelp(ICommand cmd, Member member) {
        User user = member.getUser();
        EmbedBuilder embedBuilder = new EmbedBuilder(); embedBuilder.setTitle(" Ajuda para " + cmd.getName(), null);
        embedBuilder.setFooter("Requerido por " + user.getName(), user.getEffectiveAvatarUrl());
        embedBuilder.setTimestamp(Instant.now());
        if (cmd.isOwnerOnly() && !builder.isOwner(user)) {
            embedBuilder.setDescription("Este comando é apenas para desenvolvedores, você não deve estar olhando para isso.");
            return embedBuilder.build();
        }
        embedBuilder.appendDescription("**" + cmd.getCategory().getEmoji() + " | " + cmd.getCategory().getName() + "**\n\n");
        if (cmd.getDescription() != null) {
            embedBuilder.appendDescription("" + cmd.getDescription() + "\n\n");
        }
        if (cmd.getUsageInstruction() != null) {
            embedBuilder.appendDescription("**Instruções de uso:**\n" + Arrays.stream(cmd.getUsageInstruction().split("\n")).map(s -> builder.getPrefix() + s).collect(Collectors.joining("\n")) + "");
        } else
            embedBuilder.appendDescription("Sem instruções de uso.");
        if (cmd.getAliases().length > 1)
            embedBuilder.appendDescription("\n\n**Aliases:** " + String.join(", ", cmd.getAliases()));
        embedBuilder.setThumbnail("https://cdn.discordapp.com/icons/490957752693162005/ec53e89c7d9f5865ed818f3045aafbdd.png");
        return embedBuilder.build();
    }
}
