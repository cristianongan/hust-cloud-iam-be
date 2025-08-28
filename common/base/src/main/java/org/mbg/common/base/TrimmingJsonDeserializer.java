package org.mbg.common.base;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.mbg.common.util.GetterUtil;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * @author LinhLH Trim tất cả tham số String đầu vào
 */
@JsonComponent
public class TrimmingJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
		return p.hasToken(JsonToken.VALUE_STRING) ? GetterUtil.getString(p.getText(), null) : p.getValueAsString();
	}
}
