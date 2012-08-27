package i2.application.rejeux.model;

public class AuditLogHeader {

	
	private String timestamp;
	private String transactionId;
	private String sourceIP;
	private String sourcePort;
	private String destinationIP;
	private String destinationPort;
	
	public AuditLogHeader(String timestamp,
			String transactionId,
			String sourceIP,
			String sourcePort,
			String destinationIP,
			String destinationPort) {
		super();
		this.timestamp = timestamp;
		this.transactionId = transactionId;
		this.sourceIP = sourceIP;
		this.sourcePort = sourcePort;
		this.destinationIP = destinationIP;
		this.destinationPort = destinationPort;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public String getSourcePort() {
		return sourcePort;
	}

	public String getDestinationIP() {
		return destinationIP;
	}

	public String getDestinationPort() {
		return destinationPort;
	}
	
}
