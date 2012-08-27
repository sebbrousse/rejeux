package i2.application.rejeux.model;


public class RequestHeader {

	
	private String method;
	private String url;
	private String protocol;
	private HeaderMap headers;
	
	public RequestHeader(String method, String url, String protocol, HeaderMap headers) {
		super();
		this.method = method;
		this.url = url;
		this.protocol = protocol;
		this.headers = new HeaderMap(headers);
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}
	
	public String getPrococol() {
		return protocol;
	}
	
	public int getNumberOfHeader() {
		return headers.size();
	}

	public Header getHeader(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean containsHeader(String headerName) {
		return headers.contains(headerName);
	}

	public String getValueFromHeader(String headerName) {
		return headers.getValueFromHeaderName(headerName);
	}

	public HeaderMap headers() {
		return headers;
	}

	@Override
	public String toString() {
		return "RequestHeader [method=" + method + ", url=" + url
				+ ", protocol=" + protocol + ", headers=" + headers + "]";
	}
}
