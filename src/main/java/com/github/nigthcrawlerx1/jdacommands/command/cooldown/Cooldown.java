package com.github.nigthcrawlerx1.jdacommands.command.cooldown;

public class Cooldown {

    private final String guildId , userId;
    private final long seconds;
    private final String command;

    public Cooldown(String guildId, String userId, long seconds , String command){
        this.guildId = guildId;
        this.userId = userId;
        this.seconds = (System.currentTimeMillis() / 1000) + seconds;
        this.command = command;
    }

    public String getGuildId() {
        return guildId;
    }

    public long getSeconds() {
        return seconds;
    }

    public String getCommand() {
        return command;
    }

    public String getUserId() {
        return userId;
    }

    public String getIdentifier(){
        return guildId + "-" + userId + "-" + command;
    }

    public boolean isInCooldown(){
        return seconds >= (System.currentTimeMillis() / 1000);
    }

    public long getTimeRemaining(){
        return isInCooldown() ? seconds - (System.currentTimeMillis() / 1000) : 0;
    }
}
