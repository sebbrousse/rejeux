package i2.application.rejeux.model;

import java.util.HashMap;
import java.util.Map;

public class HeaderMap {

	private Map<String, String> headers;
	
	public HeaderMap() {
		super();
		this.headers = new HashMap<String, String>();
	}
	
	public HeaderMap(HeaderMap headerMap) {
		this();
		for (Map.Entry<String, String> header : headerMap.getHeaders().entrySet()) {
			headers.put(header.getKey(), header.getValue());
		}
	}
	
	public void add(Header header) {
		headers.put(header.getName(), header.getValue());
	}
	
	public boolean containsHeaderName(String name) {
		return headers.containsKey(name);
	}
	
	private Map<String, String> getHeaders() {
		return headers;
	}

	public boolean contains(String headerName) {
		return headers.containsKey(headerName);
	}
	
	public int size() {
		return headers.size();
	}

	public String getValueFromHeaderName(String headerName) {
		return headers.get(headerName);
	}
	
	public Map<String, String> asMap() {
		return headers;
	}

	@Override
	public String toString() {
		return "HeaderMap [headers=" + headers + "]";
	}
	
}
