package com.github.nigthcrawlerx1.jdacommands.command;

import com.github.nigthcrawlerx1.jdacommands.command.category.ICategory;
import net.dv8tion.jda.api.Permission;

import java.util.function.Consumer;

public interface ICommand {

    void invoke(CommandEvent event);

    String getName();

    String getDescription();

    boolean requireRole();

    String roleName();

    boolean  guildOwner();

    String getUsageInstruction();

    String[] getAliases();

    ICategory getCategory();

    boolean isOwnerOnly();

    Permission[] perms();

    class Builder {
        private Consumer<CommandEvent> action;
        private String name, description, usage , roleName;
        private String[] aliases;
        private boolean guildOwner, ownerOnly , requireRole;
        private Permission[] perms;
        ICategory category;

        public Builder setAction(Consumer<CommandEvent> action) {
            this.action = action;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder setCategory(ICategory category) {
            this.category = category;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setOwnerOnly(boolean ownerOnly) {
            this.ownerOnly = ownerOnly;
            return this;
        }

        public Builder setRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Builder setGuildOwner(boolean guildOwner) {
            this.guildOwner = guildOwner;
            return this;
        }

        public Builder setRequireRole(boolean requireRole) {
            this.requireRole = requireRole;
            return this;
        }

        public Builder setUsage(String usage) {
            this.usage = usage;
            return this;
        }

        public void setPermissions(Permission... perms) {
            this.perms = perms;
        }

        public ICommand build(){
            return new ICommand() {
                @Override
                public void invoke(CommandEvent event) {
                    action.accept(event);
                }

                @Override
                public String getName() {
                    return name;
                }

                @Override
                public String getDescription() {
                    return description;
                }

                @Override
                public boolean requireRole() {
                    return requireRole;
                }

                @Override
                public String roleName() {
                    return roleName;
                }

                @Override
                public boolean guildOwner() {
                    return guildOwner;
                }

                @Override
                public String getUsageInstruction() {
                    return usage;
                }

                @Override
                public String[] getAliases() {
                    return aliases;
                }

                @Override
                public ICategory getCategory() {
                    return category;
                }

                @Override
                public boolean isOwnerOnly() {
                    return ownerOnly;
                }

                @Override
                public Permission[] perms() {
                    return perms;
                }
            };
        }
    }
}
