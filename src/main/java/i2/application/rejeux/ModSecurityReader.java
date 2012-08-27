package i2.application.rejeux;

import static i2.application.rejeux.model.builders.BuildersHelper.aRequestHeader;
import static i2.application.rejeux.model.builders.BuildersHelper.anAuditLogHeader;
import i2.application.rejeux.model.AuditLogHeader;
import i2.application.rejeux.model.Header;
import i2.application.rejeux.model.HeaderMap;
import i2.application.rejeux.model.ModSecurityLog;
import i2.application.rejeux.model.RequestBody;
import i2.application.rejeux.model.RequestHeader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModSecurityReader {

	private static final Logger LOG = LoggerFactory.getLogger(ModSecurityReader.class);

	private Scanner scanner;

	public ModSecurityReader() {
	}
	
	public ModSecurityReader(InputStream input) {
		super();
		scanner = new Scanner(input);
	}
	
	public ModSecurityReader(String input) {
		super();
		scanner = new Scanner(input);
	}
	
	public void close() {
		if (scanner != null) {
			scanner.close();
		}
	}

	public void printSections() {
		scanner.useDelimiter("--[\\-\\@0-9A-Za-z]*-*--"); 
		while (scanner.hasNext()) {
			LOG.debug(scanner.next());
		}
	}
	
	public List<ModSecurityLog> readSections(String input) {
		LOG.info("Lecture du fichier de trace...");
		scanner = new Scanner(input);
		return readSections();
	}
	
	
	public List<ModSecurityLog> readSections() {
		
		List<ModSecurityLog> logs = new ArrayList<ModSecurityLog>();
		ModSecurityLog log = null;
		while (scanner.hasNext("--[\\-\\@0-9A-Za-z]*-*--")) {
			
			String delimiter = scanner.next("--[\\-\\@0-9A-Za-z]*-*--");
			LOG.trace("Delimiter : {}",delimiter);
			scanner.useDelimiter("--[\\-\\@0-9A-Za-z]*-*--");
			
			LOG.trace("Pattern : {}",scanner.delimiter());
			if (delimiter.endsWith("-A--")) {
				LOG.debug("##################  Début d'un message ########################");
				String id = readId(delimiter);
				log = new ModSecurityLog(id);
				String sectionA = scanner.next();
				LOG.trace("Section A : {}",sectionA);
				AuditLogHeader auditLogHeader = readAuditLogHeader(sectionA);
				log.setAuditLogHeader(auditLogHeader);
			}
			if (delimiter.endsWith("-B--")) {
				String sectionB = scanner.next();
				LOG.trace("Section B : {}",sectionB);
				RequestHeader requestHeader = readRequestHeader(sectionB);
				log.setRequestHeader(requestHeader);
			}
			if (delimiter.endsWith("-C--")) {
				String sectionC = scanner.next();
				LOG.trace("Section C : {}",sectionC);
				RequestBody requestBody = readRequestBody(sectionC);
				log.setRequestBody(requestBody);
			}
			if (delimiter.endsWith("-F--")) {
				LOG.trace("Section F : {}",scanner.next());
			}
			if (delimiter.endsWith("-H--")) {
				LOG.trace("Section H : {}",scanner.next());
			}
			if (delimiter.endsWith("-Z--")) {
				LOG.trace("Section Z : Fin du message");
				logs.add(log);
				LOG.debug("##################  Fin d'un message ########################");
			}
			scanner.reset();
			
		}
		LOG.info("{} messages de trace lus", logs.size());
		return logs;
	}

	public RequestBody readRequestBody(String sectionC) {
		return new RequestBody(sectionC.trim());
	}

	public RequestHeader readRequestHeader(String sectionB) {
		String[] sectionBTokens = sectionB.trim().split("\\r?\\n");
		for (int i=0;i<sectionBTokens.length;i++) {
			LOG.trace("sectionBTokens : {}",sectionBTokens[i]);
		}
		String firstLine = sectionBTokens[0];
		LOG.trace("firstLine : {}",firstLine);
		String[] firstLineTokens = firstLine.split(" ");
		for (int i=0;i<firstLineTokens.length;i++) {
			LOG.trace("firstLineTokens : {}",firstLineTokens[i]);
		}
		String method = firstLineTokens[0];
		LOG.debug("method : {}",method);
		String url = firstLineTokens[1];
		LOG.debug("url : {}",url);
		String protocol = firstLineTokens[2];
		LOG.debug("protocol : {}",protocol);
		
		HeaderMap headers = new HeaderMap();
		for (int i=1;i<sectionBTokens.length;i++) {
			String line = sectionBTokens[i];
			String[] headerTokens = line.split(":");
			String headerName = headerTokens[0].trim();
			String headerValue = headerTokens[1].trim();
			Header header = new Header(headerName, headerValue);
			LOG.debug("Ajout d'un header : {}",header);
			headers.add(header);
		}
		
		RequestHeader requestHeader = aRequestHeader().method(method)
		  										.url(url)
		  										.protocol(protocol)
		  										.headers(headers)
		  										.build();
		return requestHeader;
	}
	
	public String readId(String delimiter) {
		LOG.trace(delimiter);
		// on récupère l'id
		String id = delimiter.replaceFirst("--", "").replaceAll("-.--", "");
		LOG.debug("Id : {}",id);
		return id;
	}
	
	/**
	 * read Audit Log Header (Section A)
	 */
	public AuditLogHeader readAuditLogHeader(String sectionA) {
		String timestamp = sectionA.substring(sectionA.indexOf("["), sectionA.indexOf("]")+1);
		LOG.debug("timestamp : {}", timestamp);
		
		String[] sectionATokens = sectionA.replace(timestamp, "").trim().split(" ");
		
		String transactionId = sectionATokens[0];
		LOG.debug("transactionId : {}",transactionId);
		String sourceIP = sectionATokens[1];
		LOG.debug("sourceIP : {}",sourceIP);
		String sourcePort = sectionATokens[2];
		LOG.debug("sourcePort : {}",sourcePort);
		String destinationIP = sectionATokens[3];
		LOG.debug("destinationIP : {}",destinationIP);
		String destinationPort = sectionATokens[4];
		LOG.debug("destinationPort : {}",destinationPort);
		
		AuditLogHeader header = anAuditLogHeader().timestamp(timestamp)
												  .transactionId(transactionId)
												  .sourceIP(sourceIP)
												  .sourcePort(sourcePort)
												  .destinationIP(destinationIP)
												  .destinationPort(destinationPort)
												  .build();
		
		return header;
	}

	public void readBody(String log) {
		String line = scanner.next("--[\\-\\@0-9A-Za-z]*-B--");
		LOG.trace(line);
	}
}
