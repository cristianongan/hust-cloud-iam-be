package org.mbg.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * @author LinhLH
 *
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonUtil {
	
	public static boolean canJsonParse(HttpEntity<String> response) {
	    if (Objects.nonNull(response)
                && Objects.nonNull(response.getBody())) {
	        _log.info("api call response {}", response.getBody());
	    }
	    
        return Objects.nonNull(response)
                && Objects.nonNull(response.getBody())
                && response.getBody().startsWith(StringPool.OPEN_CURLY_BRACE)
                && response.getBody().endsWith(StringPool.CLOSE_CURLY_BRACE);
	}
	
	public static boolean canJsonParse(String text) {
		return Objects.nonNull(text)
                && text.startsWith(StringPool.OPEN_CURLY_BRACE)
                && text.endsWith(StringPool.CLOSE_CURLY_BRACE);
	}
	
	public static String extractErrorMessage(String original) {
		return StringUtils.substringBetween(original, "[", "]");
	}
	
	public static JsonObject parse(String text) {
		if (!canJsonParse(text)) {
			return null;
		}

		return JsonParser.parseString(text).getAsJsonObject();
	}
	
	public static int getInt(JsonObject ob, String elementName, int defaultValue) {
		JsonElement e = ob.get(elementName);

		return e != null && !e.isJsonNull() ? GetterUtil.getInteger(e.getAsString(), defaultValue)
				: defaultValue;
	}
}
