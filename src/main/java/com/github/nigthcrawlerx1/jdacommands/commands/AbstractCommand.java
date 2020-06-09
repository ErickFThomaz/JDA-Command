package com.github.nigthcrawlerx1.jdacommands.commands;

import com.github.nigthcrawlerx1.jdacommands.builders.CommandBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCommand implements ICommand {

    private final Map<SubCommand, Method> subCommands;

    protected AbstractCommand() {
        // This has to be changed as soon as onCommand changes
        final Class<?>[] parameterTypes = {CommandEvent.class, Member.class, TextChannel.class, String[].class};
        this.subCommands = new HashMap<>();
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SubCommand.class)) {
                if (method.getReturnType().equals(Void.TYPE) && Modifier.isPublic(method.getModifiers()) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                    subCommands.put(method.getAnnotation(SubCommand.class), method);
                } else {
                    CommandBuilder.getLOGGER().warn("Você está usando uma assinatura de método inválida para a anotação SubCommand no método " + getClass().getName() + "#" + method.getName()
                            + ".\nExpected: void (com.github.johnnyjayjay.commandapi.CommandEvent, net.dv8tion.jda.api.entities.Member, net.dv8tion.jda.api.entities.TextChannel, java.lang.String[])\nFound: "
                            + method.getReturnType().getName() + " (" + Arrays.stream(method.getParameterTypes()).map(Class::getName).collect(Collectors.joining(", ")) + ")\nEste método será, portanto, ignorado.");
                }
            }
        }
    }

    @Override
    public void onCommand(CommandEvent event, String[] args) {
        CommandBuilder settings = event.getCommandSettings();
        Optional<SubCommand> matchesArgs = subCommands.keySet().stream()
                .filter((sub) -> !sub.isDefault())
                .filter((sub) -> sub.args().length == args.length || (sub.moreArgs() && args.length > sub.args().length))
                .filter((sub) -> {
                    String regex;
                    for (int i = 0; i < sub.args().length; i++) {
                        regex = settings.isLabelIgnoreCase() ? "(?i)" + sub.args()[i] : sub.args()[i];
                        if (!args[i].matches(regex))
                            return false;
                    }
                    return true;
                })
                .filter((sub) -> event.checkBotPermissions(sub.botPerms())).findFirst();
        if (matchesArgs.isPresent()) {
            this.invokeMethod(subCommands.get(matchesArgs.get()), event, args);
        } else {
            subCommands.keySet().stream().filter((sub) -> event.checkBotPermissions(sub.botPerms()))
                    .filter(SubCommand::isDefault).findFirst().map(subCommands::get)
                    .ifPresent((method) -> this.invokeMethod(method, event, args));
        }
    }
    private void invokeMethod(Method method, CommandEvent event, String[] args) {
        try {
            method.invoke(this, event, args);
        } catch (IllegalAccessException e) {
            CommandBuilder.getLOGGER().error("Ocorreu uma exceção ao tentar chamar o método de subcomando; Relate isso em um problema do github.", e);
        } catch (InvocationTargetException e) {
            CommandBuilder.getLOGGER().warn("Um dos comandos teve uma exceção não capturada:", e.getCause());
        }
    }
}
