package com.github.nigthcrawlerx1.jdacommands.command.manager;

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
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Aqui os comandos são registrados conforme o {@link org.reflections.Reflections Reflections}
 * encontra os comandos usando {@link org.reflections.scanners.MethodAnnotationsScanner MethodAnnotationsScanner}
 * para encontrar as classes apartir do seu packege que você definiu em {@link com.github.nigthcrawlerx1.jdacommands.CommandBuilder CommandBuilder}
 * para ele encontrar os comandos que estão usando a classe de registro de comandos {@link com.github.nigthcrawlerx1.jdacommands.annotations.RegisterCommand RegisterCommand}
 * e adicionar em {@link java.util.List List} todos os comandos encontrados.
 *
 * @author Erick
 * @see com.github.nigthcrawlerx1.jdacommands.CommandBuilder#setPackegeCommands(String)
 * @see com.github.nigthcrawlerx1.jdacommands.annotations.RegisterCommand RegisterCommand
 * @see com.github.nigthcrawlerx1.jdacommands.command.ICommand ICommand
 * @since 0.0.1
 */
public class CommandManager {

    private final HashMap<String, ICommand> commandMap = new HashMap();
    private final List<ICommand> commands;
    private final Logger log = LoggerFactory.getLogger(CommandManager.class);
    private final CommandBuilder builder;

    /**
     *
     * @param builder
     *        O {@link com.github.nigthcrawlerx1.jdacommands.CommandBuilder} não pode ser nulo aqui para que ele posso iniciar corretamente o registro de comandos.
     */
    public CommandManager(@Nonnull CommandBuilder builder){
        this.builder = builder;
        long start = System.currentTimeMillis();
        log.info("Inicializando os comandos.");
        commands = new ArrayList<>(new Reflections(builder.getPackege(), new MethodAnnotationsScanner())
                .getMethodsAnnotatedWith(RegisterCommand.class).stream()
                .map(method -> {
                    if (method.getReturnType() != ICommand.class)
                        return null;
                    try {
                        return (ICommand) method.invoke(null);
                    } catch (Exception e) {
                        log.warn("Failed to load ICommand from the method [" + method.getName() + "] at " + method.getDeclaringClass().getName());
                        e.printStackTrace();
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList()));
        for (ICommand iCommand : commands) {
            for (String alias : iCommand.getAliases()) {
                if (commandMap.containsKey(alias)){
                    log.warn("It seems more than one ICommand with the alias [" + alias + "] has been registered! Only the last one will be executed!");
                }
                commandMap.put(alias, iCommand);
            }
        }
        long end = System.currentTimeMillis();
        long count = (end - start);
        log.info("Foram registrados {} comandos em {} segundos." , commands.size() , (count / 1000));
    }

    /**
     *
     * @param category
     * @return Todos os comandos da {@link ICategory} especifica.
     */
    public List<ICommand> getCommands(ICategory category) {
        return commands.stream().filter(command -> category.equals(command.getCategory())).collect(Collectors.toList());
    }

    public void invoke(CommandEvent event) {
        ICommand cmd = event.getCommand();
        if(cmd.isOwnerOnly() && builder.getOwners().isEmpty()){
            event.sendMessage("Desculpe mas a lista de donos está vazia e não posso deixar você executar isso.");
            return;
        }
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

    public ICommand getCommand(String aliase){
        return commandMap.get(aliase);
    }

    public HashMap<String, ICommand> getCommandMap() {
        return commandMap;
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
