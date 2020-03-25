package first;

import java.util.List;
import org.json.*;

public class functionMetadata {
	
	String opcodes;
	String architecture;
	String name;
	String prototype;
	String comment;
	List<String> apis;
	String id;
	JSONObject jsonObject;
	
	public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis, String id) {
		this.opcodes=opcodes;
		this.architecture=architecture;
		this.name=name;
		this.prototype=prototype;
		this.comment=comment;
		this.apis=apis;
		this.id=id;

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("opcodes", opcodes);
		jsonObject.put("architecture", architecture);
		jsonObject.put("name", name);
		jsonObject.put("prototype", prototype);
		jsonObject.put("comment", comment);
		jsonObject.put("apis", apis);
		jsonObject.put("id", id);

		this.jsonObject = new JSONObject();
		this.jsonObject.put("client_id", jsonObject);
	}
}
