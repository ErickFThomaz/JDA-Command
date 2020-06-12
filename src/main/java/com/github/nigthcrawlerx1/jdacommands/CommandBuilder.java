package com.github.nigthcrawlerx1.jdacommands;

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

    public CommandBuilder setOwners(List<String> owners) {
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
        commandManager = new CommandManager(src , this);
        addListener();
    }

    public boolean isOwner(@Nonnull Member member){
        return owners.contains(member.getId());
    }

    public boolean isOwner(@Nonnull User user){
        return owners.contains(user.getId());
    }
}
