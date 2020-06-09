package com.github.nigthcrawlerx1.jdacommands.commands;

import com.github.nigthcrawlerx1.jdacommands.builders.CommandBuilder;
import com.github.nigthcrawlerx1.jdacommands.utils.StringUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.internal.utils.Checks;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandEvent extends GuildMessageReceivedEvent {

    private final Command command;
    private final CommandBuilder settings;
    public CommandEvent(@Nonnull JDA api, long responseNumber, @Nonnull Message message, @Nonnull Command command , @Nonnull CommandBuilder settings) {
        super(api, responseNumber, message);

        this.command = command;
        this.settings = settings;
    }

    public void sendMessage(String message){
        Checks.check(getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE) , "Mandando mensagem no canal");
        getChannel().sendMessage(message).queue();
    }

    public void sendMessage(Message message){
        Checks.check(getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE) , "Mandando mensagem no canal");
        getChannel().sendMessage(message).queue();
    }

    public void sendMessage(MessageEmbed message){
        Checks.check(getGuild().getSelfMember().hasPermission(Permission.MESSAGE_WRITE) , "Mandando mensagem no canal");
        getChannel().sendMessage(message).queue();
    }

    public Command getCommand() {
        return command;
    }

    public List<User> getMentionedUsers() {
        return getMessage().getMentionedUsers();
    }

    public List<Member> getMentionedMembers() {
        return getMessage().getMentionedMembers();
    }

    public CommandBuilder getCommandSettings() {
        return settings;
    }

    public boolean checkBotPermissions(Permission... permissions) {
        return this.guild.getSelfMember().hasPermission(this.channel, permissions);
    }

    public static Command parseCommand(String raw, String prefix, CommandBuilder settings) {
        return new Command(raw, prefix, settings);
    }
    public static class Command {

        private final ICommand command;
        private final String joinedArgs;
        private final String rawArgs;
        private final String rawMessage;
        private final String label;
        private final String[] args;

        private Command(String raw, String prefix, CommandBuilder settings) {
            String[] argsWithoutPrefix = raw.replaceFirst(prefix, "").split("\\s+");
            this.label = settings.isLabelIgnoreCase() ? argsWithoutPrefix[0].toLowerCase() : argsWithoutPrefix[0];;
            if (!settings.getCommand().containsKey(this.label)) {
                this.command = null;
                this.joinedArgs = null;
                this.rawMessage = null;
                this.rawArgs = null;
                this.args = null;
            } else {
                this.command = settings.getCommand().get(this.label);
                this.rawMessage = raw;
                this.args = splitArgs(raw);
                this.joinedArgs = String.join(" ", this.args);
                this.rawArgs = raw.replaceAll(prefix + label , "");
            }
        }

        /**
         * Returns the label of the command.
         * @return The label of the called command, e.g. "foo" if someone calls the command "!foo" (if the prefix is "!")
         */
        public String getLabel() {
            return label;
        }

        /**
         * Returns the arguments of the command.
         * @return The command arguments. In most cases, this is not of importance, because you get these already explicitly in the onCommand-method of ICommand.
         */
        public String[] getArgs() {
            return args;
        }

        /**
         * Returns the {@code ICommand} instance that executed this command.
         * @return The object that calls the onCommand-method. Might be useful in some special cases.
         */
        public ICommand getExecutor() {
            return command;
        }

        /**
         * Returns the raw content of the event message.
         * @return The raw Message that can also be retrieved with CommandEvent#getMessage#getContentRaw
         */
        public String getRawMessage() {
            return rawMessage;
        }

        /**
         * Gets the unmodified arguments of this Command.
         * @return the raw message without the prefix and the label.
         */
        public String getRawArgs() {
            return rawArgs;
        }

        /**
         * Returns the command arguments not as an array, but as a List.
         * @return the arguments as an immutable List
         */
        public List<String> getArgsAsList() {
            return Collections.unmodifiableList(Arrays.asList(args));
        }

        /**
         * Returns the arguments as a single String.
         * @return the arguments joined with a space
         */
        public String getJoinedArgs() {
            return joinedArgs;
        }

        protected String[] splitArgs(String content) {
            return StringUtils.splitArgs(content , 0);
        }

        public ICommand getCommand() {
            return command;
        }
    }
}
