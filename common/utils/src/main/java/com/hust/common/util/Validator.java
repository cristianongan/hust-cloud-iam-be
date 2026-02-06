package com.hust.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 *
 * @author os_linhlh2
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {
	private static final int CHAR_BEGIN = 65;

	private static final int CHAR_END = 122;

	private static final int DIGIT_BEGIN = 48;

	private static final int DIGIT_END = 57;

	private static final char[] _EMAIL_ADDRESS_SPECIAL_CHAR = new char[] { '.', '!', '#', '$', '%', '&', '\'', '*', '+',
			'-', '/', '=', '?', '^', '_', '`', '{', '|', '}', '~' };

	private static final String LOCALHOST = "localhost";

	private static final Pattern codeStringPattern = Pattern.compile(Constants.Regex.CODE);

	private static final Pattern emailAddressPattern = Pattern.compile(Constants.Regex.EMAIL, Pattern.CASE_INSENSITIVE);

	private static final Pattern ipV4AddressPattern = Pattern.compile(Constants.Regex.IPV4);
	
	private static final Pattern ipV6AddressPattern = Pattern.compile(Constants.Regex.IPV6);

	private static final Pattern numberPattern = Pattern.compile(Constants.Regex.NUMBER);
	
	private static final Pattern vnPhoneNumberPattern = Pattern.compile(Constants.Regex.PHONE_REGEX);

	private static final Pattern variableNamePattern = Pattern.compile(Constants.Regex.USERNAME);
	
	private static final String VARIABLE_TERM_BEGIN = "[$";

	private static final String VARIABLE_TERM_END = "$]";
	
	private static final String XML_BEGIN = "<?xml";

	private static final String XML_EMPTY = "<root />";

	private static final List<SimpleDateFormat> dateFormats = new ArrayList<>() {

		@Serial
		private static final long serialVersionUID = 1081800986306230549L;

		{
			add(new SimpleDateFormat("MM/dd/yyyy"));
			add(new SimpleDateFormat("dd.MM.yyyy"));
			add(new SimpleDateFormat("dd/MM/yyyy"));
			add(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"));
			add(new SimpleDateFormat("dd.MM.yyyy hh:mm:ss a"));
			add(new SimpleDateFormat("dd.MMM.yyyy"));
			add(new SimpleDateFormat("dd-MMM-yyyy"));
		}
	};



	/**
	 * Returns <code>true</code> if the short integers are equal.
	 *
	 * @param date1 the first Date
	 * @param date2 the second Date
	 * @return <code>true</code> if the dates are equal; <code>false</code>
	 *         otherwise
	 */
    public static boolean equals(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return false;
        }

        return date1.compareTo(date2) == 0;
    }

	/**
	 * Returns <code>true</code> if the objects are either equal, the same instance,
	 * or both <code>null</code>.
	 *
	 * @param obj1 the first object
	 * @param obj2 the second object
	 * @return <code>true</code> if the objects are either equal, the same instance,
	 *         or both <code>null</code>; <code>false</code> otherwise
	 */
	public static boolean equals(Object obj1, Object obj2) {
		if ((obj1 == null) && (obj2 == null)) {
			return true;
		} else if ((obj1 == null) || (obj2 == null)) {
			return false;
		} else {
			return obj1.equals(obj2);
		}
	}

	public static String getNotNullString(Object object) {
		return object != null ? object.toString() : StringPool.BLANK;
	}

	public static String getSafeFileName(String input) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != '/' && c != '\\' && c != 0) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Returns <code>true</code> if the string is an email address. The only
	 * requirements are that the string consist of two parts separated by an
	 * @ symbol, and that it contain no whitespace.
	 *
	 * @param address the string to check
	 * @return <code>true</code> if the string is an email address;
	 *         <code>false</code> otherwise
	 */
	public static boolean isAddress(String address) {
		if (isNull(address)) {
			return false;
		}

		String[] tokens = address.split(StringPool.AT);

		if (tokens.length != 2) {
			return false;
		}

		for (String token : tokens) {
			for (char c : token.toCharArray()) {
				if (Character.isWhitespace(c)) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the string is an alphanumeric name, meaning it
	 * contains nothing but English letters, numbers, and spaces.
	 *
	 * @param name the string to check
	 * @return <code>true</code> if the string is an Alphanumeric name;
	 *         <code>false</code> otherwise
	 */
	public static boolean isAlphanumericName(String name) {
		if (isNull(name)) {
			return false;
		}

		for (char c : name.trim().toCharArray()) {
			if (!isChar(c) && !isDigit(c) && !Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the character is in the ASCII character set.
	 * This includes characters with integer values between 32 and 126 (inclusive).
	 *
	 * @param c the character to check
	 * @return <code>true</code> if the character is in the ASCII character set;
	 *         <code>false</code> otherwise
	 */
	public static boolean isAscii(char c) {
		return ((int) c >= 32) && ((int) c <= 126);
	}

	/**
	 * Returns <code>true</code> if the character is an upper or lower case English
	 * letter.
	 *
	 * @param c the character to check
	 * @return <code>true</code> if the character is an upper or lower case English
	 *         letter; <code>false</code> otherwise
	 */
	public static boolean isChar(char c) {
		return ((int) c >= CHAR_BEGIN) && ((int) c <= CHAR_END);
	}

	/**
	 * Returns <code>true</code> if string consists only of upper and lower case
	 * English letters.
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string consists only of upper and lower case
	 *         English letters
	 */
	public static boolean isChar(String s) {
		if (isNull(s)) {
			return false;
		}

		for (char c : s.toCharArray()) {
			if (!isChar(c)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isCodeString(String codeString) {
		if (isNull(codeString)) {
			return false;
		}

		return codeStringPattern.matcher(codeString).matches();
	}

	/**
	 * Returns <code>true</code> if the date is valid in the Gregorian calendar.
	 *
	 * @param month the month to check
	 * @param day   the day to check
	 * @return <code>true</code> if the date is valid in the Gregorian calendar;
	 *         <code>false</code> otherwise
	 */
	public static boolean isDate(int month, int day, int year) {
		return isGregorianDate(month, day, year);
	}

	/**
	 * 
	 * @param input the input string
	 * @return <code>true</code> if the date is valid in the Gregorian
	 */
	public static boolean isDate(String input) {
		if (input == null || input.trim().isEmpty()) {
			return false;
		}

		return dateFormats.stream()
				.anyMatch(format -> {
					try {
						format.setLenient(false);
						format.parse(input);

						return true;
					} catch (ParseException e) {
						return false;
					}
				});
	}

	/**
	 * Returns <code>true</code> if the character is a digit between 0 and 9
	 * (inclusive).
	 *
	 * @param c the character to check
	 * @return <code>true</code> if the character is a digit between 0 and 9
	 *         (inclusive); <code>false</code> otherwise
	 */
	public static boolean isDigit(char c) {
		return ((int) c >= DIGIT_BEGIN) && ((int) c <= DIGIT_END);
	}

	/**
	 * Returns <code>true</code> if the string consists of only digits between 0 and
	 * 9 (inclusive).
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string consists of only digits between 0 and
	 *         9 (inclusive); <code>false</code> otherwise
	 */
	public static boolean isDigit(String s) {
		if (isNull(s)) {
			return false;
		}

		for (char c : s.toCharArray()) {
			if (!isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the string is a valid domain name. See RFC-1034
	 * (section 3), RFC-1123 (section 2.1), and RFC-952 (section B. Lexical
	 * grammar).
	 *
	 * @param domainName the string to check
	 * @return <code>true</code> if the string is a valid domain name;
	 *         <code>false</code> otherwise
	 */
	public static boolean isDomain(String domainName) {

		// See RFC-1034 (section 3), RFC-1123 (section 2.1), and RFC-952
		// (section B. Lexical grammar)

		if (isNull(domainName) 
				|| domainName.length() > 255
				|| domainName.startsWith(StringPool.PERIOD) 
				|| domainName.endsWith(StringPool.PERIOD)
				|| (!domainName.contains(StringPool.PERIOD) && !domainName.equals(LOCALHOST))) {
			return false;
		}

		String[] domainNameArray = StringUtil.split(domainName, CharPool.PERIOD);

		for (String domainNamePart : domainNameArray) {
			char[] domainNamePartCharArray = domainNamePart.toCharArray();

			for (int i = 0; i < domainNamePartCharArray.length; i++) {
				char c = domainNamePartCharArray[i];

				if (((i == 0) && (c == CharPool.DASH))
						|| ((i == (domainNamePartCharArray.length - 1)) && (c == CharPool.DASH))
						|| ((!isChar(c)) && (!isDigit(c)) && (c != CharPool.DASH))) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the string is a valid email address.
	 *
	 * @param emailAddress the string to check
	 * @return <code>true</code> if the string is a valid email address;
	 *         <code>false</code> otherwise
	 */
	public static boolean isEmailAddress(String emailAddress) {
		return emailAddressPattern.matcher(emailAddress).matches();
	}

	/**
	 * Returns <code>true</code> if the character is a special character in an email
	 * address.
	 *
	 * @param c the character to check
	 * @return <code>true</code> if the character is a special character in an email
	 *         address; <code>false</code> otherwise
	 */
	public static boolean isEmailAddressSpecialChar(char c) {
		for (char value : _EMAIL_ADDRESS_SPECIAL_CHAR) {
			if (c == value) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns <code>true</code> if the date is valid in the Gregorian calendar.
	 *
	 * @param month the month (0-based, meaning 0 for January)
	 * @param day   the day of the month
	 * @param year  the year
	 * @return <code>true</code> if the date is valid; <code>false</code> otherwise
	 */
	public static boolean isGregorianDate(int month, int day, int year) {
		if ((month < 0) || (month > 11)) {
			return false;
		}

		int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (month == 1) {
			int febMax = 28;

			if (((year % 4) == 0) && ((year % 100) != 0) || ((year % 400) == 0)) {

				febMax = 29;
			}

			return (day >= 1) && (day <= febMax);
		} else return (day >= 1) && (day <= months[month]);
	}

	/**
	 * Returns <code>true</code> if the string is a hexadecimal number. At present
	 * the only requirement is that the string is not <code>null</code>; it does not
	 * actually check the format of the string.
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string is a hexadecimal number;
	 *         <code>false</code> otherwise
	 * @see #isNull(String)
	 */
	public static boolean isHex(String s) {
		return !isNull(s);
	}

	public static boolean isHour(String input) {
		return input != null && input.matches(Constants.Regex.HOUR);
	}

	/**
	 * Returns <code>true</code> if the string is an HTML document. The only
	 * requirement is that it contain the opening and closing html tags.
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string is an HTML document;
	 *         <code>false</code> otherwise
	 */
	public static boolean isHTML(String s) {
		if (isNull(s)) {
			return false;
		}

		return ((s.contains("<html>")) || (s.contains("<HTML>")))
				&& ((s.contains("</html>")) || (s.contains("</HTML>")));
	}

	/**
	 * Returns <code>true</code> if the string is a valid IPv4 IP address.
	 *
	 * @param ipAddress the string to check
	 * @return <code>true</code> if the string is an IPv4 IP address;
	 *         <code>false</code> otherwise
	 */
	public static boolean isIPV4Address(String ipAddress) {
		return ipV4AddressPattern.matcher(ipAddress).matches();
	}

	public static boolean isIPV6Address(String ipAddress) {
		return ipV6AddressPattern.matcher(ipAddress).matches();
	}

	public static boolean isIPAddress(String ipAddress) {
		return isIPV4Address(ipAddress) || isIPV6Address(ipAddress);
	}
	/**
	 * Returns <code>true</code> if the date is valid in the Julian calendar.
	 *
	 * @param month the month (0-based, meaning 0 for January)
	 * @param day   the day of the month
	 * @param year  the year
	 * @return <code>true</code> if the date is valid; <code>false</code> otherwise
	 */
	public static boolean isJulianDate(int month, int day, int year) {
		if ((month < 0) || (month > 11)) {
			return false;
		}

		int[] months = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (month == 1) {
			int febMax = 28;

			if ((year % 4) == 0) {
				febMax = 29;
			}

			return (day >= 1) && (day <= febMax);
		} else return (day >= 1) && (day <= months[month]);
	}

	/**
	 * Returns <code>true</code> if the string contains a valid number according to
	 * the Luhn algorithm, commonly used to validate credit card numbers.
	 *
	 * @param number the string to check
	 * @return <code>true</code> if the string contains a valid number according to
	 *         the Luhn algorithm; <code>false</code> otherwise
	 */
	public static boolean isLUHN(String number) {
		if (number == null) {
			return false;
		}

		number = StringUtil.reverse(number);

		int total = 0;

		for (int i = 0; i < number.length(); i++) {
			int x;

			int i1 = Integer.parseInt(number.substring(i, i + 1));

			if (((i + 1) % 2) == 0) {
				x = i1 * 2;

				if (x >= 10) {
					String s = String.valueOf(x);

					x = Integer.parseInt(s.substring(0, 1)) + Integer.parseInt(s.substring(1, 2));
				}
			} else {
				x = i1;
			}

			total = total + x;
		}

		return (total % 10) == 0;
	}

	public static boolean isMinute(String input) {
		return input != null && input.matches(Constants.Regex.MINUTE);
	}

	/**
	 * Returns <code>true</code> if the string is a name, meaning it contains
	 * nothing but English letters and spaces.
	 *
	 * @param name the string to check
	 * @return <code>true</code> if the string is a name; <code>false</code>
	 *         otherwise
	 */
	public static boolean isName(String name) {
		if (isNull(name)) {
			return false;
		}

		for (char c : name.trim().toCharArray()) {
			if (!isChar(c) && !Character.isWhitespace(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the collection is not
	 *
	 * @param collection the collection to check
	 * @return <code>true</code> if the collection is not <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNotNull(Collection<?> collection) {
		return !isNull(collection);
	}

	/**
	 * Returns <code>true</code> if the long number object is not <code>null</code>,
	 * meaning it is neither a <code>null</code> reference or zero.
	 *
	 * @param l the long number object to check
	 * @return <code>true</code> if the long number object is not <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNotNull(Long l) {
		return !isNull(l);
	}

	/**
	 * Returns <code>true</code> if the object is not <code>null</code>, using the
	 * rules from {@link #isNotNull(Long)} or {@link #isNotNull(String)} if the
	 * object is one of these types.
	 *
	 * @param obj the object to check
	 * @return <code>true</code> if the object is not <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNotNull(Object obj) {
		return !isNull(obj);
	}

	/**
	 * Returns <code>true</code> if the array is not <code>null</code>, meaning it
	 * is neither a <code>null</code> reference or empty.
	 *
	 * @param array the array to check
	 * @return <code>true</code> if the array is not <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNotNull(Object[] array) {
		return !isNull(array);
	}

	/**
	 * Returns <code>true</code> if the string is not <code>null</code>, meaning it
	 * is not a <code>null</code> reference, nothing but spaces, or the string "
	 * <code>null</code>".
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string is not <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNotNull(String s) {
		return !isNull(s);
	}

	/**
	 * Returns <code>true</code> if the collection is <code>null</code>, meaning it
	 * is either a <code>null</code> reference or empty.
	 *
	 * @param collection the collection to check
	 * @return <code>true</code> if the collection is <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNull(Collection<?> collection) {
		return (collection == null) || (collection.isEmpty());
	}

	/**
	 * Returns <code>true</code> if the long number object is <code>null</code>,
	 * meaning it is either a <code>null</code> reference or zero.
	 *
	 * @param l the long number object to check
	 * @return <code>true</code> if the long number object is <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNull(Long l) {
		return (l == null) || (l < 0);
	}

	/**
	 * Returns <code>true</code> if the object is <code>null</code>, using the rules
	 * from {@link #isNull(Long)} or {@link #isNull(String)} if the object is one of
	 * these types.
	 *
	 * @param obj the object to check
	 * @return <code>true</code> if the object is <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNull(Object obj) {
		if (obj instanceof Long) {
			return isNull((Long) obj);
		} else if (obj instanceof String) {
			return isNull((String) obj);
		} else return obj == null;
	}

	/**
	 * Check if all member of an array object is null
	 * @param array the array to check
	 * @return true if all member of an array object
	 */
    public static boolean isAllNull(Object[] array) {
		if (array != null) {
			for (Object ob : array) {
				if (Objects.nonNull(ob)) {
					return false;
				}
			}
		}

		return true;
	}
	
	/**
	 * Returns <code>true</code> if the array is <code>null</code>, meaning it is
	 * either a <code>null</code> reference or empty.
	 *
	 * @param array the array to check
	 * @return <code>true</code> if the array is <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNull(Object[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * Returns <code>true</code> if the string is <code>null</code>, meaning it is a
	 * <code>null</code> reference, nothing but spaces, or the string "
	 * <code>null</code>".
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string is <code>null</code>;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNull(String s) {
		if (s == null) {
			return true;
		}

		int counter = 0;

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == CharPool.SPACE) {
				continue;
			} else if (counter > 3) {
				return false;
			}

			if ((counter == 0 && c != CharPool.LOWER_CASE_N)
					|| (counter == 1 && c != CharPool.LOWER_CASE_U)
					|| ((counter == 2 || counter == 3) && c != CharPool.LOWER_CASE_L)) {
					return false;
					
			} 

			counter++;
		}

		return (counter == 0) || (counter == 4);
	}

	/**
	 * Returns <code>true</code> if the string is a decimal integer number, meaning
	 * it contains nothing but decimal digits.
	 *
	 * @param number the string to check
	 * @return <code>true</code> if the string is a decimal integer number;
	 *         <code>false</code> otherwise
	 */
	public static boolean isNumber(String number) {
		if (isNull(number)) {
			return false;
		}

		for (char c : number.toCharArray()) {
			if (!isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	public static boolean isNumberic(String number) {
		if (isNull(number)) {
			return false;
		}

		return numberPattern.matcher(number).matches();
	}

	/**
	 * Returns <code>true</code> if the string is a valid password, meaning it is at
	 * least four characters long and contains only letters and decimal digits.
	 *
	 * @return <code>true</code> if the string is a valid password;
	 *         <code>false</code> otherwise
	 */
	public static boolean isPassword(String password) {
		if (isNull(password)) {
			return false;
		}

		if (password.length() < 4) {
			return false;
		}

		for (char c : password.toCharArray()) {
			if (!isChar(c) && !isDigit(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns <code>true</code> if the string is a valid phone number. The only
	 * requirement is that there are decimal digits in the string; length and format
	 * are not checked.
	 *
	 * @param phoneNumber the string to check
	 * @return <code>true</code> if the string is a valid phone number;
	 *         <code>false</code> otherwise
	 */
	public static boolean isPhoneNumber(String phoneNumber) {
		return isNumber(phoneNumber);
	}

	public static boolean isVNPhoneNumber(String phoneNumber) {
		return vnPhoneNumberPattern.matcher(phoneNumber).matches();
	}
	
	public static boolean isPort(String input) {
		return input != null && input.matches(Constants.Regex.PORT);
	}

	/**
	 * Returns <code>true</code> if the string is a valid URL based on the rules in
	 * {@link URL}.
	 *
	 * @param url the string to check
	 * @return <code>true</code> if the string is a valid URL; <code>false</code>
	 *         otherwise
	 */
	public static boolean isUrl(String url) {
		if (Validator.isNull(url)) {
			return false;
		}

		try {
			URI uri = URI.create(url);
			return uri.getScheme() != null
					&& (uri.getScheme().startsWith("http")
					|| uri.getScheme().startsWith("https")
					|| uri.getScheme().startsWith("ftp"));
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/**
	 * Returns <code>true</code> if the string is a valid variable name in Java.
	 *
	 * @param variableName the string to check
	 * @return <code>true</code> if the string is a valid variable name in Java;
	 *         <code>false</code> otherwise
	 */
	public static boolean isVariableName(String variableName) {
		if (isNull(variableName)) {
			return false;
		}

		return variableNamePattern.matcher(variableName).matches();
	}

	/**
	 * Returns <code>true</code> if the string is a valid variable term, meaning it
	 * begins with "[$" and ends with "$]".
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string is a valid variable term;
	 *         <code>false</code> otherwise
	 */
	public static boolean isVariableTerm(String s) {
		return s.startsWith(VARIABLE_TERM_BEGIN) && s.endsWith(VARIABLE_TERM_END);
	}

	public static boolean isVersion(String input) {
		return input != null && input.matches(Constants.Regex.VERSION);
	}

	/**
	 * Returns <code>true</code> if the character is whitespace, meaning it is
	 * either the <code>null</code> character '0' or whitespace according to
	 * {@link Character#isWhitespace(char)}.
	 *
	 * @param c the character to check
	 * @return <code>true</code> if the character is whitespace; <code>false</code>
	 *         otherwise
	 */
	public static boolean isWhitespace(char c) {
		return ((int) c == 0) || Character.isWhitespace(c);
	}

	/**
	 * Returns <code>true</code> if the string is an XML document. The only
	 * requirement is that it contain either the xml start tag "<?xml" or the empty
	 * document tag "<root />".
	 *
	 * @param s the string to check
	 * @return <code>true</code> if the string is an XML document;
	 *         <code>false</code> otherwise
	 */
	public static boolean isXml(String s) {
		return s.startsWith(XML_BEGIN) || s.startsWith(XML_EMPTY);
	}
}
