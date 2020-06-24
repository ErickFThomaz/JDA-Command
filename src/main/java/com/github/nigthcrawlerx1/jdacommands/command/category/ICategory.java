package com.github.nigthcrawlerx1.jdacommands.command.category;

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

/**
 * É usado no {@link com.github.nigthcrawlerx1.jdacommands.command.ICommand.Builder ICommand.Builder()} para setar
 * as categorias de um comando da forma que quiser.
 *
 * <p>Pode ser usado emojis do discord ate emojis Unicode<p/>
 *
 * @author Erick
 */

public class  ICategory {

    private final String name , emoji;
    private final boolean hide;

    /**
     * Categoria de comando contendo nome.
     * @param name
     */
    public ICategory(String name){
        this.name = name;
        this.emoji = null;
        this.hide = false;
    }

    public ICategory(String name , boolean hide){
        this.name = name;
        this.emoji = null;
        this.hide = hide;
    }

    /**
     * Categoria de comando contendo nome e emoji.
     * @param name
     *        O nome da categoria
     * @param emoji
     *        O emoji da categoria.
     */
    public ICategory(String name, String emoji){
        this.name = name;
        this.emoji = emoji;
        this.hide = false;
    }

    public ICategory(String name, String emoji , boolean hide){
        this.name = name;
        this.emoji = emoji;
        this.hide = hide;
    }

    /**
     *@return O nome da categoria.
     */
    public String getName() {
        return name;
    }

    /**
     * @return O emoji da categoria.
     */
    public String getEmoji() {
        return emoji;
    }

    public boolean isHide() {
        return hide;
    }
}
