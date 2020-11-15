package com.github.nigthcrawlerx1.jdacommands.command.argumento;

import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MultiArgumentos {

    private final List<String> stringArgs = new ArrayList<String>();
    private final List<FlagedArgumento> flags = new ArrayList<FlagedArgumento>();
    private final List<Argumento> argumentos = new ArrayList<Argumento>();
    private final Guild guild;

    private boolean flagfied = false;

    public MultiArgumentos(String[] args, Guild guild) {
        for (int i = 0; i < args.length; i++) {
            String theArg = args[i];
            stringArgs.add(theArg);
            argumentos.add(new Argumento(theArg, guild));
        }
        this.guild = guild;
    }

    public void flagify(){
        if (!flagfied){
            flagfied = true;
            for (int i = stringArgs.size() - 1; i >= 0; i--) {
                String theArg = stringArgs.get(i);
                if (theArg.charAt(0) == '-' && !(theArg.length() > 1 && Character.isDigit(theArg.charAt(1)))){
                    stringArgs.remove(i);
                    argumentos.remove(i);
                    flags.add(0, new FlagedArgumento(theArg, guild));
                }
            }
        }
    }

    public void forEach(Consumer<Argumento> action){
        argumentos.forEach(action);
    }

    public Stream<Argumento> stream(){
        return argumentos.stream();
    }

    public List<Argumento> getArgs(){
        return this.argumentos;
    }

    public List<FlagedArgumento> getFlags(){
        flagify();
        return this.flags;
    }

    public Argumento get(int index){
        return index < this.argumentos.size() ? this.argumentos.get(index) : Argumento.EMPTY_ARG;
    }

    public FlagedArgumento getFlag(String flagName){
        flagify();
        if (flags.size() > 0){
            flagName = flagName.toLowerCase();
            for (FlagedArgumento flag : flags) {
                if (flag.getFlagName().equals(flagName)){
                    return flag;
                }
            }
        }
        return FlagedArgumento.EMPTY_ARG;
    }

    public boolean emptyArgs(int... numbers){
        if (numbers != null && numbers.length > 0){
            for (int argNumber : numbers) {
                if (get(argNumber).isEmpty()){
                    return true;
                }
            }
        }
        return false;
    }

    @Deprecated
    public boolean anyEmptyArgs(int... numbers){
        return emptyArgs(numbers);
    }

    // -----------------------------------------------------------------------------------------------------------------------------//
    // Plain String Part
    // -----------------------------------------------------------------------------------------------------------------------------//

    public List<String> getStringArgs(){
        return this.stringArgs;
    }

    public String getStringArg(int index){
        return index < this.stringArgs.size() ? this.stringArgs.get(index) : "";
    }

    public String joinStringArgs(){
        return String.join(" ", this.stringArgs);
    }

    public String joinStringArgs(int indexStart, int indexEndExclusive){
        return String.join(" ", stringArgs.subList(indexStart, indexEndExclusive));
    }

    public String joinStringArgs(int indexStart){
        return String.join(" ", stringArgs.subList(indexStart,stringArgs.size()));
    }

}
