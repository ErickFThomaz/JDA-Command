package com.github.nigthcrawlerx1.jdacommands.builders;

import com.github.nigthcrawlerx1.jdacommands.commands.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;
import java.util.Map;


class CommandListener extends ListenerAdapter {

    private CommandBuilder settings;
    private Map<Long, Long> cooldowns; // Long: User id, Long: last timestamp

    public CommandListener(CommandBuilder settings) {
        this.settings = settings;
        this.cooldowns = new HashMap<>();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        if (!settings.getBlacklistedChannels().contains(channel.getIdLong()) && (!event.getAuthor().isBot() || settings.botsMayExecute())) {
            String raw = event.getMessage().getContentRaw();
            String prefix = settings.getPrefix(event.getGuild().getIdLong());
            if (raw.startsWith(prefix)) {
                long timestamp = System.currentTimeMillis();
                long userId = event.getAuthor().getIdLong();
                if (cooldowns.containsKey(userId) && (timestamp - cooldowns.get(userId)) < settings.getCooldown()) {
                    if (settings.isResetCooldown())
                        cooldowns.put(userId, timestamp);
                    return;
                }
                cooldowns.put(userId, timestamp);
                CommandEvent.Command cmd = CommandEvent.parseCommand(raw, prefix, settings);
                if (cmd.getExecutor() != null) {
                    try {
                        cmd.getExecutor().onCommand(new CommandEvent(event.getJDA(), event.getResponseNumber(), event.getMessage(), cmd, settings),
                                 cmd.getArgs());
                    } catch (Throwable t) {
                        CommandBuilder.LOGGER.warn("Um dos comandos teve uma exceção não capturada:", t);
                    }
                } else {
                    Message unknownCommand = settings.getUnknownCommandMessage();
                    if (unknownCommand != null && event.getGuild().getSelfMember().hasPermission(channel, Permission.MESSAGE_WRITE, Permission.MESSAGE_EMBED_LINKS))
                        channel.sendMessage(unknownCommand).queue();
                }
            }
        }
    }
}
