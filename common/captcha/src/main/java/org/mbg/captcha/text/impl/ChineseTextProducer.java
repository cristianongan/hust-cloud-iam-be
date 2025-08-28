package org.mbg.captcha.text.impl;

import org.mbg.captcha.text.TextProducer;

import java.util.Random;

/**
 * {@code ChineseTextProducer} is responsible for producing Chinese text.
 * <p>
 * This implementation provides random text from a predefined list of simplified Chinese phrases.
 * <p>
 * The generated text can be used in CAPTCHA implementations or other text generation contexts.
 *
 * @author LinhLH
 * @since 19/04/2025
 */
public class ChineseTextProducer implements TextProducer {
    private final String[] simplifiedChineseTexts = new String[]{
            "包括焦点", "新道消点", "服分目搜", "索姓名電", "子郵件信", "主旨請回", "電子郵件", "給我所有", "討論區明", "發表新文", "章此討論", "區所有文", "章回主題",
            "樹瀏覽搜"
    };

    /**
     * @return random Chinese text
     */
    public String getText() {
        return simplifiedChineseTexts[new Random().nextInt(simplifiedChineseTexts.length)];
    }
}
