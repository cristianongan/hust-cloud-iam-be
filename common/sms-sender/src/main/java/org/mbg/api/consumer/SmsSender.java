package org.mbg.api.consumer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.dom.DOMSource;

import org.mbg.common.base.configuration.ValidationProperties;
import org.mbg.common.label.LabelKey;
import org.mbg.common.label.Labels;
import org.mbg.common.security.RsaProvider;
import org.mbg.common.util.Constants;
import org.mbg.common.util.StringUtil;
import org.mbg.common.util.Validator;
import org.mbg.common.util.XMLUtil;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.mbg.api.request.SmsRequest;
import org.mbg.api.response.SmsResponse;
import org.mbg.api.response.SmsResponse.Result;
import org.mbg.configuration.SmsProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SmsSender {

    private final RestTemplate restTemplate;

    private final SmsProperties properties;
    
    private final ValidationProperties validation;
    
    private final RsaProvider rsaProvider;

    public SmsResponse send(SmsRequest request) {
        String phoneNumber = request.getTo();
        
        String content = request.getContent();
        
        if (Validator.isNull(phoneNumber)) {
            return new SmsResponse(Result.FAILURE, Labels.getLabels(LabelKey.ERROR_PHONE_NUMBER_IS_INVALID));
        }
        
        if (Validator.isNull(content)) {
            return new SmsResponse(Result.FAILURE, Labels.getLabels(LabelKey.ERROR_MESSAGE_IS_REQUIRED));
        }
        
        phoneNumber = StringUtil.formatLaPhoneNumber(phoneNumber);
        
        try {
            MessageFactory factory = MessageFactory.newInstance();
            
            SOAPMessage soapMsg = factory.createMessage();

            soapMsg.getSOAPPart().getEnvelope().removeNamespaceDeclaration("SOAP-ENV");
            soapMsg.getSOAPPart().getEnvelope().addNamespaceDeclaration(Constants.Soap.SOAP_ENV,
                            "http://schemas.xmlsoap.org/soap/envelope/");
            soapMsg.getSOAPPart().getEnvelope().setPrefix(Constants.Soap.SOAP_ENV);
            soapMsg.getSOAPHeader().setPrefix(Constants.Soap.SOAP_ENV);
            soapMsg.getSOAPBody().setPrefix(Constants.Soap.SOAP_ENV);

            SOAPPart part = soapMsg.getSOAPPart();
            SOAPEnvelope envelope = part.getEnvelope();
            
            envelope.setAttribute("xmlns:impl", this.properties.getSchema());
            
            // create soap body
            SOAPBody body = envelope.getBody();
            SOAPBodyElement element = body.addBodyElement(envelope.createQName("wsCpMt", "impl"));
            
            element.addChildElement("User").addTextNode(this.properties.getUser());
            element.addChildElement("Password").addTextNode(this.rsaProvider.decrypt(this.properties.getPassword()));
            element.addChildElement("CPCode").addTextNode(this.properties.getCpCode());
            element.addChildElement("ServiceID").addTextNode(this.properties.getServiceId());
            element.addChildElement("RequestID").addTextNode(this.properties.getRequestId());
            element.addChildElement("UserID").addTextNode(phoneNumber);
            element.addChildElement("ReceiverID").addTextNode(phoneNumber);
            element.addChildElement("CommandCode").addTextNode(this.properties.getCommandCode());
            element.addChildElement("Content").addTextNode(content);
            element.addChildElement("ContentType").addTextNode(this.properties.getContentType());
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            soapMsg.writeTo(out);
            
            String requestBody = new String(out.toByteArray());
            
            HttpHeaders headers = new HttpHeaders();
            
            headers.setContentType(MediaType.valueOf(MediaType.TEXT_XML_VALUE));
            
            ResponseEntity<String> exchange = restTemplate.exchange(request.getBaseUrl(), request.getMethod(),
                            new HttpEntity<>(requestBody, headers),
                            String.class);
            
            JAXBContext jc = JAXBContext.newInstance(SmsResponse.class);
            
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            
            JAXBElement<SmsResponse> je =
                            unmarshaller.unmarshal(this.getDOMSource(exchange.getBody()), SmsResponse.class);

            _log.info("SMS response body: {}", exchange.getBody());
            
            return je.getValue();
        } catch (Exception e) {
            _log.error(Labels.getLabels(LabelKey.ERROR_MESSAGE_IS_REQUIRED), e.getMessage());
            
            return new SmsResponse(Result.FAILURE, Labels.getLabels(LabelKey.ERROR_CANNOT_SEND_SMS));
        }
    }
    
    private DOMSource getDOMSource(String body) throws ParserConfigurationException, SAXException, IOException {
        
        DocumentBuilder db = XMLUtil.createDocumentBuilder(true);
        
        Document doc = db.parse(new InputSource(new StringReader(body)));
        
        Node resultNode = doc.getElementsByTagName("return").item(0);
        
        return new DOMSource(resultNode);
    }
}
