package i2.application.rejeux.model.builders;

public class BuildersHelper {

	
	public static AuditLogHeaderBuilder anAuditLogHeader() {
		return new AuditLogHeaderBuilder();
	}
	
	public static RequestHeaderBuilder aRequestHeader() {
		return new RequestHeaderBuilder();
	}
}
