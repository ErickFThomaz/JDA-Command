package com.github.nigthcrawlerx1.jdacommands.commands;

import com.github.nigthcrawlerx1.jdacommands.builders.CommandBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
                this.args = Arrays.copyOfRange(argsWithoutPrefix, 1, argsWithoutPrefix.length);
                this.joinedArgs = String.join(" ", this.args);
                this.rawArgs = raw.replaceFirst(prefix + this.label + "\\s+", "");
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

        /**
         * Returns the arguments as a single String.
         * @param fromIndex from which argument index the Strings will be joined.
         * @return the arguments joined with a space
         * @throws IllegalArgumentException if the given index is invalid (higher than the argument length or lower than 0.
         * @deprecated This method was deprecated and replaced by CommandEvent.Command#joinArgs.
         */
        @Deprecated
        public String getJoinedArgs(int fromIndex) {
            if (fromIndex >= args.length || fromIndex < 0)
                throw new IllegalArgumentException("índice inválido! A matriz de argumentos possui apenas um comprimento total de " + args.length);
            return String.join(" ", Arrays.asList(args).subList(fromIndex, args.length));
        }

        /**
         * Joins the command arguments from a specific index on.
         * @param fromIndex a start index which may not be out of bounds.
         * @return the arguments, joined with a space from a specific index on
         * @throws IllegalArgumentException if a parameter does not apply to the requirements.
         */
        public String joinArgs(int fromIndex) {
            return joinArgs(fromIndex, args.length);
        }

        /**
         * Joins the command arguments from and to a specific index.
         * @param fromIndex a start index which may not be out of bounds.
         * @param toIndex an end index which may not be smaller than fromIndex nor out of bounds.
         * @return the arguments, joined with a space within a specific range
         * @throws IllegalArgumentException if a parameter does not apply to the requirements.
         */
        public String joinArgs(int fromIndex, int toIndex) {
            return joinArgs(" ", fromIndex, toIndex);
        }

        /**
         * Joins the command arguments with a specific delimiter from and to a specific index.
         * @param delimiter the delimiter that is supposed to join the arguments.
         * @param fromIndex a start index which may not be out of bounds.
         * @param toIndex an end index which may not be smaller than fromIndex nor out of bounds.
         * @return the arguments, joined with the given delimiter within a specific range
         * @throws IllegalArgumentException if a parameter does not apply to the requirements.
         */
        public String joinArgs(@Nonnull CharSequence delimiter, int fromIndex, int toIndex) {
            if (fromIndex >= args.length || fromIndex < 0 || toIndex < fromIndex || toIndex > args.length)
                throw new IllegalArgumentException("Índice inválido! Os índices estão fora dos limites ou toIndex é menor que fromIndex.");
            return String.join(delimiter, Arrays.copyOfRange(args, fromIndex, toIndex));
        }
    }
}
