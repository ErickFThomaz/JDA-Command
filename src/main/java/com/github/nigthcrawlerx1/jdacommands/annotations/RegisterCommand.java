package com.github.nigthcrawlerx1.jdacommands.annotations;

/*
 *   Copyright 2020 Erick (NightCrawlerX1 / NightCrawlerX)
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

import java.lang.annotation.*;

/**
 * Uma anotação aplicada para {@link java.lang.reflect.Method Method} isso fara com que
 * em {@link com.github.nigthcrawlerx1.jdacommands.command.manager.CommandManager CommandManager} registre
 * os comandos que usam está classe.
 *
 *<p>Para registrar o comando é obrigatorio colocar está classe</p>
 * <br>Siga o exemplo:<br/>
 * <pre><code>
 *     pubic class ExampleCommand{
 *         {@link com.github.nigthcrawlerx1.jdacommands.annotations.RegisterCommand @RegisterCommand}
 *         public static {@link com.github.nigthcrawlerx1.jdacommands.command.ICommand ICommand} example(){
 *             retrun new {@link com.github.nigthcrawlerx1.jdacommands.command.ICommand.Builder ICommand.Builder()}
 *             .setAction(CommandEvent ->{
 *                 CommandEvent.sendMessage("Olá");
 *             })
 *             .build();
 *         }
 *     }
 * </code></pre>
 *
 * @see com.github.nigthcrawlerx1.jdacommands.annotations.RegisterCommand
 * @see com.github.nigthcrawlerx1.jdacommands.command.ICommand ICommand
 *
 * @since 0.0.1
 * @author Erick
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface RegisterCommand {
}
