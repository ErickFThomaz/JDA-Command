package com.github.nigthcrawlerx1.jdacommands.command;

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

    long getCooldown();

    ICategory getCategory();

    boolean isOwnerOnly();

    Permission[] perms();

    class Builder {
        private Consumer<CommandEvent> action;
        private String name, description, usage , roleName;
        private String[] aliases;
        private boolean guildOwner, ownerOnly , requireRole;
        private Permission[] perms;
        private ICategory category;
        private long cooldown;

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

        public Builder setPermissions(Permission... perms) {
            this.perms = perms;
            return this;
        }

        public Builder setCooldown(long cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public ICommand build(){
            if (aliases.length == 0) throw new IllegalArgumentException("At least one Aliase is needed!");
            if (name == null) name = aliases[0];
            if (action == null) throw new IllegalArgumentException("A command NEED an ACTION!");
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

                @Override
                public long getCooldown() {
                    return cooldown;
                }
            };
        }
    }
}
