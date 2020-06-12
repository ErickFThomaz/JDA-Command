package com.github.nigthcrawlerx1.jdacommands.utils;
/*
 * @author Lucasmellof, Lucas de Mello Freitas created on 09/06/2020
 */

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class Messages {
    public static String DEFAULT_INFO = "Nenhuma informação, descrição ou ajuda definida para este comando.";
    public static String SENDING_MSG = "Mandando mensagem no canal";
    public static Message MENTION_MSG = new MessageBuilder("Meu prefixo é ").build();
}
