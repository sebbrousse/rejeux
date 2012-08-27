package i2.application.rejeux.model;

import java.util.Map;

public class ModSecurityLog {

	private String id;
	private AuditLogHeader auditLogHeader;
	private RequestHeader requestHeader;
	private RequestBody requestBody;
	
	public ModSecurityLog(String id) {
		super();
		this.id = id;
	}
	
	public void setAuditLogHeader(AuditLogHeader auditLogHeader) {
		this.auditLogHeader = auditLogHeader;
	}
	
	public void setRequestHeader(RequestHeader requestHeader) {
		this.requestHeader = requestHeader;
	}
	
	public void setRequestBody(RequestBody requestBody) {
		this.requestBody = requestBody;
	}
	
	public Map<String, String> getRequestHeaders() {
		return requestHeader.headers().asMap();
	}

	public String getUrl() {
		return requestHeader.getUrl();
	}

	public String getRequestBody() {
		return requestBody==null?"":requestBody.getBody();
	}

	public String getId() {
		return id;
	}
	
}
