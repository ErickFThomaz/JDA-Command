package com.github.nigthcrawlerx1.jdacommands.command.argumento;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collections;
import java.util.List;

public class Argumento {

    public final static Argumento EMPTY_ARG = new Argumento("", null);
    private final Guild guild;
    private final String argumento;

    public Argumento(String argumento, Guild guild) {
        this.argumento = argumento;
        this.guild = guild;
    }

    public int length(){return this.argumento.length();}

    public boolean isEmpty(){
        return this.argumento.isEmpty();
    }

    public boolean equals(String anotherString){
        return this.argumento.equals(anotherString);
    }

    public boolean match(String... possibilities){
        return equalsIgnoreCase(possibilities);
    }

    public boolean equalsIgnoreCase(String anotherString){
        return argumento.equalsIgnoreCase(anotherString);
    }

    public boolean equalsIgnoreCase(String... possibilities){
        String lowerCaseArg = argumento.toLowerCase();
        for (String possibility : possibilities){
            if (possibility.toLowerCase().equals(lowerCaseArg)){
                return true;
            }
        }
        return false;
    }

    public String toLowerCase(){
        return this.argumento.toLowerCase();
    }

    public String toUpperCase(){
        return this.argumento.toUpperCase();
    }

    public Member getMemberFromID(){
        if (!argumento.isEmpty()) return null;
        Long user_id = getLong();
        return user_id == null ? null : guild.getMemberById(user_id);
    }

    public List<Member> getMembersFromName(){
        if (!argumento.isEmpty()) return Collections.EMPTY_LIST;
        return guild.getMembersByEffectiveName(argumento.startsWith("@") ? argumento.substring(1) : argumento, true);
    }

    public String asString() {
        return argumento;
    }

    public Integer getInteger(){
        try {
            return Integer.parseInt(argumento);
        }catch (Exception ignored){
            return null;
        }
    }

    public Float getFloat(){
        try {
            return Float.parseFloat(argumento);
        }catch (Exception ignored){
            return null;
        }
    }

    public Long getLong(){
        try {
            return Long.parseLong(argumento);
        }catch (Exception ignored){
            return null;
        }
    }

    public Double getDouble(){
        try {
            return Double.parseDouble(argumento);
        }catch (Exception ignored){
            return null;
        }
    }

    public Boolean getBoolean(){
        switch (argumento.toLowerCase()){
            case "on":
            case "true":
            case "verdadeiro":
            case "yes":
            case "sim":
            case "y":
            case "s":
                return true;
            case "off":
            case "false":
            case "falso":
            case "não":
            case "nao":
            case "n":
                return false;
        }
        return null;
    }

    public String replaceAll(String regex, String replacement){
        return this.argumento.replaceAll(regex, replacement);
    }

    public String replace(CharSequence target, CharSequence replacement){
        return this.argumento.replace(target, replacement);
    }


    @Override
    public String toString() {
        return argumento;
    }
}

