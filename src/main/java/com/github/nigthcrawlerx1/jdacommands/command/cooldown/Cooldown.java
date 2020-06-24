package com.github.nigthcrawlerx1.jdacommands.command.cooldown;

public class Cooldown {

    private final String guildId , userId;
    private final long seconds;
    private final String command;

    public Cooldown(String guildId, String userId, long seconds , String command){
        this.guildId = guildId;
        this.userId = userId;
        this.seconds = seconds;
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
}
