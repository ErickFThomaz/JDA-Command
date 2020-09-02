package com.github.nigthcrawlerx1.jdacommands.command.cooldown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class CooldownHandler {

    private HashMap<String, Cooldown> mapOfCooldowns = new HashMap<>();

    public CooldownHandler(){
        Logger logger = LoggerFactory.getLogger(CooldownHandler.class);
        logger.debug("Inicando sistema de cooldown");
    }

    public boolean hasCooldown(String guild, String user, String command){
        Cooldown cooldown = getTheCooldown(guild, user, command);
        return cooldown != null;
    }

    public void setCooldown(String guild, String user, String command, Long time){
        if(!hasCooldown(guild, user, command)){
            Cooldown cooldown = new Cooldown(guild, user, time, command);
            mapOfCooldowns.put(cooldown.getIdentifier(), cooldown);
        }
    }

    public Cooldown getTheCooldown(String guild, String user, String command){
        String IDENTIFIER = guild + "-" + user + "-" + command;
        Cooldown cooldown = mapOfCooldowns.get(IDENTIFIER);
        if (cooldown != null){
            if (cooldown.isInCooldown()){
                return cooldown;
            }else {
                mapOfCooldowns.remove(IDENTIFIER);
            }
        }
        return null;
    }

    public long getCooldown(String guild, String user, String command){
        Cooldown cooldown = getTheCooldown(guild, user, command);
        return cooldown != null ? cooldown.getTimeRemaining() : 0;
    }
}
