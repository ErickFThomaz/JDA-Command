package com.github.nigthcrawlerx1.jdacommands;

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

import com.github.nigthcrawlerx1.jdacommands.command.cooldown.CooldownHandler;
import com.github.nigthcrawlerx1.jdacommands.command.manager.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class CommandBuilder {

    private final ShardManager shardManager;
    private final JDA jda;
    private List<String> owners;
    private String prefix , alternativePrefix;
    private String src;
    private CommandManager commandManager;
    private CooldownHandler cooldownHandler;

    private final Logger log = LoggerFactory.getLogger(CommandBuilder.class);



    public CommandBuilder(@Nonnull ShardManager shardManager){
        this.shardManager = shardManager;
        this.jda = null;

    }

    public CommandBuilder(@Nonnull JDA jda){
        this.shardManager = null;

        this.jda = jda;

    }

    public CommandBuilder setPrefix(@Nonnull String prefix) {
        this.prefix = prefix;
        return this;
    }

    public CommandBuilder setAlternativePrefix(@Nonnull String alternativePrefix) {
        this.alternativePrefix = alternativePrefix;
        return this;
    }

    public CommandBuilder setOwners(@Nonnull List<String> owners) {
        this.owners = owners;
        return this;
    }

    public CommandBuilder setOwners(@Nonnull String... owners) {
        this.owners = Arrays.asList(owners);
        return this;
    }

    public CommandBuilder setPackegeCommands(String src) {
        this.src = src;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getAlternativePrefix() {
        return alternativePrefix;
    }

    public String getPackege(){
        return src;
    }

    public List<String> getOwners() {
        return owners;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Deprecated
    public CooldownHandler getCooldownHandler() {
        return cooldownHandler;
    }

    @Deprecated
    private void addListener() throws Exception {
        if(jda != null){
            jda.addEventListener(new CommandListener(this));
        }
        if(shardManager != null){
            shardManager.addEventListener(new CommandListener(this));
        }
        if(shardManager == null && jda == null){
            log.info("A instancia do JDA ou ShardManager não foi iniciada");
            throw new Exception("A instancia do JDA ou ShardManager não foi iniciada");
        }
    }

    public void build() throws Exception {
        if(src.isEmpty()){
            throw new Exception("Não foi possivel inicar o sistema de comandos. Tente verificar se você setou uma src");
        }
        commandManager = new CommandManager(this);
        addListener();
    }

    public boolean isOwner(@Nonnull Member member){
        return owners.contains(member.getId());
    }

    public boolean isOwner(@Nonnull User user){
        return owners.contains(user.getId());
    }

    public boolean isOwner(@Nonnull String id){
        return owners.contains(id);
    }
}
