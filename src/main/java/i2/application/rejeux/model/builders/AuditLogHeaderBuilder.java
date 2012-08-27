package i2.application.rejeux.model.builders;

import i2.application.rejeux.model.AuditLogHeader;

public class AuditLogHeaderBuilder {

	private String timestamp;
	private String transactionId;
	private String sourceIP;
	private String sourcePort;
	private String destinationIP;
	private String destinationPort;
	
	public AuditLogHeaderBuilder timestamp(String timestamp) {
		this.timestamp = timestamp;
		return this;
	}
	
	public AuditLogHeaderBuilder transactionId(String transactionId) {
		this.transactionId = transactionId;
		return this;
	}
	
	public AuditLogHeaderBuilder sourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
		return this;
	}
	
	public AuditLogHeaderBuilder sourcePort(String sourcePort) {
		this.sourcePort = sourcePort;
		return this;
	}
	
	public AuditLogHeaderBuilder destinationIP(String destinationIP) {
		this.destinationIP = destinationIP;
		return this;
	}
	
	public AuditLogHeaderBuilder destinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
		return this;
	}
	
	public AuditLogHeader build() {
		return new AuditLogHeader(timestamp, transactionId, sourceIP, sourcePort, destinationIP, destinationPort);
	}
	
	
}
