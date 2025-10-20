package org.mbg.common.base.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.mbg.common.util.RandomGenerator;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Data
@Component
@ConfigurationProperties(prefix = "validation")
public class ValidationProperties {
    private List<Pattern> identificationPatterns;

    private Pattern usernamePattern;

    private Pattern emailPattern;

    private Pattern phoneNumberPattern;

    private Pattern passwordPattern;

    private Pattern coordinatePattern;

    private Pattern otpPattern;

    private Pattern emailMbPattern;

    private Pattern userFullnameNonAccentPattern;

    private Pattern nameNonAccentPattern;

    private Pattern nameLaoPattern;

    private String usernameRegex;

    private String passwordRegex;

    private String emailRegex;

    private String phoneNumberRegex;

    private String coordinateRegex;

    private String otpRegex;

    private String userFullnameNonAccentRegex;

    private String emailMbRegex;

    private String nameNonAccentRegex;

    private PasswordRandom passwordRandom;

    private String passwordRandomPool;

    private String nameLaoRegex;

    private int phoneNumberMinLength;

    private int phoneNumberMaxLength;

    private int fullnameMinLength;

    private int fullnameMaxLength;

    private int usernameMinLength;

    private int usernameMaxLength;

    private int passwordMinLength;

    private int passwordMaxLength;

    private int emailMinLength;

    private int emailMaxLength;

    private int codeMinLength;

    private int codeMaxLength;

    private int addressMaxLength;

    private int nameMaxLength;

    private int descriptionMaxLength;

    private int urlMaxLength;

    private int intNumberMaxLength;

    private int smallTextMaxLength;

    private int mediumTextMaxLength;

    private int longTextMaxLength;

    private int superTextMaxLength;

    private int maxFileUpload;

    private int maxFileIdCardUpload;

    private int standardFileIdCardUpload;

    private int maxBankIconUpload;

    private int maxOrderNumber;

    private String[] identificationRegex;

    @Getter
    @Setter
    public static class PasswordRandom {
        private int uppercaseLength;

        private int lowercaseLength;

        private int specialLength;

        private int numberLength;
    }

    public String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(4);

        sb.append(RandomGenerator.generateRandomAlphabet(this.passwordRandom.getUppercaseLength(), false));
        sb.append(RandomGenerator.generateRandomAlphabet(this.passwordRandom.getLowercaseLength(), true));
        sb.append(RandomGenerator.generateRandomSpecialCharacters(this.passwordRandom.getSpecialLength()));
        sb.append(RandomGenerator.generateRandomNumbers(this.passwordRandom.getNumberLength()));

        return sb.toString();
    }
}
