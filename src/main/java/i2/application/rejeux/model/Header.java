package i2.application.rejeux.model;

public class Header {

	private String name;
	private String value;
	
	public Header(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	
	public Header(String name) {
		super();
		this.name = name;
	}


	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}


	@Override
	public String toString() {
		return "Header [name=" + name + ", value=" + value + "]";
	}
	
}
