package com.github.nigthcrawlerx1.jdacommands.command.argumento;

import net.dv8tion.jda.api.entities.Guild;

import java.util.regex.Pattern;

public class FlagedArgumento extends Argumento {

    public final static FlagedArgumento EMPTY_ARG = new FlagedArgumento();
    private final String flagName;

    private FlagedArgumento() {
        super("false", null);
        flagName = "";
    }

    public FlagedArgumento(String argumento, Guild guild) {
        super(argumento.contains(Pattern.quote(":")) ? argumento.split(Pattern.quote(":"))[1] : "true", guild);
        flagName = argumento.split(Pattern.quote(":"))[0].substring(1).toLowerCase();
    }

    public boolean isSet(){
        return !flagName.isEmpty();
    }

    public String getFlagName() {
        return flagName;
    }

    @Override
    public String toString() {
        return "FlagedArgumento{" +
                "flagName='" + flagName + '\'' +
                ", argumento='" + super.toString() + '\'' +
                '}';
    }
}
