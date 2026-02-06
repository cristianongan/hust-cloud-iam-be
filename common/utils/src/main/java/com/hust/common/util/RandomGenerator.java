package com.hust.common.util;

import java.security.SecureRandom;
import org.apache.commons.text.RandomStringGenerator;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 
 * 27/09/2022 - LinhLH: Create new
 *
 * @author LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomGenerator {

	private static final SecureRandom random = new SecureRandom();

	public static String randomWithNDigits(int n) {
		n = Math.abs(n);
		
		return String.format("%s",
				(int) Math.pow(10.0D, n - 1) + random.nextInt(9 * (int) Math.pow(10.0D, n - 1)));
	}
	
    public static String generateRandomNumbers(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(48, 57)
            .get();
        
        return pwdGenerator.generate(length);
    }
    
    public static String generateRandomAlphabet(int length, boolean lowerCase) {
        int low = lowerCase ? 97 : 65;
        int hi = lowerCase ? 122 : 90;

        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(low, hi)
            .get();
        
        return pwdGenerator.generate(length);
    }
    
    public static String generateRandomSpecialCharacters(int length) {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(33, 45)
            .get();
        
        return pwdGenerator.generate(length);
    }
}
