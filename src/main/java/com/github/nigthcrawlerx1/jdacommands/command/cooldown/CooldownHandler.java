package com.github.nigthcrawlerx1.jdacommands.command.cooldown;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CooldownHandler {

    private static ArrayList<Cooldown> cooldowns;

    public CooldownHandler(){
        Logger logger = LoggerFactory.getLogger(CooldownHandler.class);
        logger.debug("Inicando sistema de cooldown");
        cooldowns = new ArrayList<>();
    }


    public boolean hasCooldown(String guild, String user, String command){
        for(Cooldown cooldown : cooldowns){
            if(cooldown.getGuildId().equals(guild)){
                if(cooldown.getGuildId().equals(user)){
                    if (cooldown.getCommand().equals(command)){
                        if (cooldown.getSeconds() >= (System.currentTimeMillis() / 1000)){
                            return true;
                        }else {
                            cooldowns.remove(cooldown);
                            break;
                        }
                    }
                }
            }
        }
        return false;
    }
    public void setCooldown(String guild, String user, String command, Long time){
        if(!hasCooldown(guild, user, command)){
            cooldowns.add(new Cooldown(guild, user, (System.currentTimeMillis() / 1000) + time, command));
        }
    }

    public long getCooldown(String guild, String user, String command){
        for(Cooldown cooldown : cooldowns){
            if(cooldown.getGuildId().equals(guild)){
                if(cooldown.getUserId().equals(user)){
                    if (cooldown.getCommand().equals(command)){
                        if (cooldown.getSeconds() >= (System.currentTimeMillis() / 1000)){
                            return cooldown.getSeconds() - (System.currentTimeMillis() / 1000);
                        }else {
                            cooldowns.remove(cooldown);
                            break;
                        }
                    }
                }
            }
        }
        return 0;
    }
}
