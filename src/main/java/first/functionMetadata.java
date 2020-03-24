package first;

import java.util.List;

public class functionMetadata {
	
	String opcodes;
	String architecture;
	String name;
	String prototype;
	String comment;
	List<String> apis;
	String id;
	
	public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis, String id) {
		this.opcodes=opcodes;
		this.architecture=architecture;
		this.name=name;
		this.prototype=prototype;
		this.comment=comment;
		this.apis=apis;
		this.id=id;
	}

	
	

}
