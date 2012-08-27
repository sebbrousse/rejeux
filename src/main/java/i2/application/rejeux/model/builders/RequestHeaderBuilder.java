package i2.application.rejeux.model.builders;

import i2.application.rejeux.model.HeaderMap;
import i2.application.rejeux.model.RequestHeader;

public class RequestHeaderBuilder {

	private String method;
	private String url;
	private String protocol;
	private HeaderMap headers = new HeaderMap();
	
	public RequestHeaderBuilder method(String method) {
		this.method = method;
		return this;
	}
	
	public RequestHeaderBuilder url(String url) {
		this.url = url;
		return this;
	}
	
	public RequestHeaderBuilder protocol(String protocol) {
		this.protocol = protocol;
		return this;
	}
	
	public RequestHeaderBuilder headers(HeaderMap headers) {
		this.headers = headers;
		return this;
	}
	
	public RequestHeader build() {
		return new RequestHeader(method, url, protocol, headers);
	}

	
	
	
}
