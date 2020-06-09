package com.github.nigthcrawlerx1.jdacommands.commands;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    boolean isDefault() default false;

    String[] args() default {};

    boolean moreArgs() default false;

    Permission[] botPerms() default {};
}
