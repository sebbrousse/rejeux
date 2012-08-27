package i2.application.rejeux;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import i2.application.rejeux.model.AuditLogHeader;
import i2.application.rejeux.model.RequestHeader;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ModSecurityReaderTest {

	private ModSecurityReader reader;
	
	@Before
	public void setUp() {
		reader = new ModSecurityReader();
	}
	
	@Test
	public void readIdShouldReturnOnlyTheId() {
		String id = "cee0de2b";
		String delimiter = "--"+id+"-A--";
		
		String returnedId = reader.readId(delimiter);
		assertEquals(returnedId, id);
	}
	
	@Test
	public void readAuditLogHeaderShouldReturnACompleteAuditLogHeaderWithCorrectInput() {
		
		String timestamp = "[19/Jun/2011:07:26:07 +0200]";
		String txId = "Tf2Ib6wb5ckAAGyQAPUAAAAL";
		String sourceIp = "91.117.252.184";
		String sourcePort = "22182";
		String destinationIp = "172.27.229.212";
		String destinationPort = "443";
		
		
		String stringHeader = timestamp+" "+txId+" "+sourceIp+" "+sourcePort+" "+destinationIp+" "+destinationPort;
		AuditLogHeader header = reader.readAuditLogHeader(stringHeader);
		
		assertNotNull("l'objet retourné est null",header);
		assertEquals("les timestamp ne sont pas égaux",timestamp, header.getTimestamp());
		assertEquals("les identifiants de transaction ne sont pas égaux",txId, header.getTransactionId());
		assertEquals("les adresses IP Source ne sont pas égales",sourceIp, header.getSourceIP());
		assertEquals("les ports Source  ne sont pas égaux",sourcePort, header.getSourcePort());
		assertEquals("les adresses IP Destination ne sont pas égales",destinationIp, header.getDestinationIP());
		assertEquals("les ports Destination ne sont pas égaux",destinationPort, header.getDestinationPort());
		
	}
	
	@Test
	public void readRequestHeaderShouldReturnACompleteRequestHeaderWithCorrectInput() {
		String method = "GET";
		String url = "/input/?application=naf&method=send&message=%2F%2FSR%2F%2FAD%2FFRA%2F%2FTM%";
		String protocol = "HTTP/1.1";
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("User-Agent","Java/1.6.0_18");
		
		String stringHeader = method+" "+url+" "+protocol+"\n";
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			stringHeader += entry.getKey()+":"+entry.getValue()+"\n";
		}
		
		RequestHeader requestHeader = reader.readRequestHeader(stringHeader);
		
		assertNotNull("l'objet retourné est null",requestHeader);
		assertEquals("les méthodes ne sont pas égales",method, requestHeader.getMethod());
		assertEquals("les urls de transaction ne sont pas égales",url, requestHeader.getUrl());
		assertEquals("les protocoles ne sont pas égaux",protocol, requestHeader.getPrococol());
		assertEquals("les entêtes ne font pas la même taille",headers.size(), requestHeader.getNumberOfHeader());
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			assertTrue("les entêtes ne sont pas égales",requestHeader.containsHeader(entry.getKey()));
			assertEquals("les valeurs des entêtes ne sont pas égales",requestHeader.getValueFromHeader(entry.getKey()),entry.getValue());
		}
	}
	
	@Test
	public void readRequestHeaderShouldTrimTheEmptyLine() {
		
	}
	
	
}
