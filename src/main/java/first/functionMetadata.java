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

	public String getAPIS() {
        	String answer = "[";
        	for (int i = 0; i < this.apis.size(); i++) {
            		answer += "\"" + this.apis.get(i) + "\",";
        	}
        	return answer.substring(0, answer.length() - 1) + "]";
    	}
	

}
