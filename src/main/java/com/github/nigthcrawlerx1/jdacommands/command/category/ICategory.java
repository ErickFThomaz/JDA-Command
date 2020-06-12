package com.github.nigthcrawlerx1.jdacommands.command.category;

public class  ICategory {

    private final String name , emoji;

    public ICategory(String name, String emoji){
        this.name = name;
       this.emoji = emoji;
    }

    public ICategory(String name){
        this.name = name;
        this.emoji = null;
    }

    public String getName() {
        return name;
    }

    public String getEmoji() {
        return emoji;
    }
}
