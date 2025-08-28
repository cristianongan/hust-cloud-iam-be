package org.mbg.common.util;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 
 * 29/09/2022 - LinhLH: Create new
 *
 * @author LinhLH
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class XMLUtil {

    private static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String EXTERNAL_GENERAL_ENTITIES = "http://xml.org/sax/features/external-general-entities";
    private static final String EXTERNAL_PARAMETER_ENTITIES = "http://xml.org/sax/features/external-parameter-entities";
    private static final String LOAD_EXTERNAL_DTD = "http://apache.org/xml/features/nonvalidating/load-external-dtd";

    public static DocumentBuilder createDocumentBuilder(boolean namespaceAware) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, StringPool.BLANK);
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, StringPool.BLANK);

        // OWASP
        // https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html
        dbf.setFeature(DISALLOW_DOCTYPE_DECL, true);
        dbf.setFeature(EXTERNAL_GENERAL_ENTITIES, false);
        dbf.setFeature(EXTERNAL_PARAMETER_ENTITIES, false);
        // Disable external DTDs as well
        dbf.setFeature(LOAD_EXTERNAL_DTD, false);
        // and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and Entity Attacks"
        dbf.setExpandEntityReferences(false);

        dbf.setNamespaceAware(namespaceAware);

        return dbf.newDocumentBuilder();

    }
}
