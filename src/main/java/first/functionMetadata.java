package first;

import java.util.List;
import org.json.*;

/**
 * functionMetadata
 * This class contain all metadata info possible of a function
 */
public class functionMetadata {
	
	public String opcodes;
	public String architecture;
	public String name;
	public String prototype;
	public String comment;
	public List<String> apis;
	public String id;
	public JSONObject jsonObject;

	/**
	 * Class Builder full
	 * @param opcodes
	 * @param architecture
	 * @param name
	 * @param prototype
	 * @param comment
	 * @param apis
	 * @param id
	 */
	public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis, String id) {
		this.opcodes=opcodes;
		this.architecture=architecture;
		this.name=name;
		this.prototype=prototype;
		this.comment=comment;
		this.apis=apis;
		this.id=id;

		this.jsonBuilder();
	}

	/**
	 * Class Builder without the optional id
	 * @param opcodes
	 * @param architecture
	 * @param name
	 * @param prototype
	 * @param comment
	 * @param apis
	 */
	public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis) {
	    this(opcodes, architecture, name, prototype, comment, apis, "");
	}

	/**
	 * jsonBuilder create the needed JSON to simplify AddFunctionMetadata
	 */
	private void jsonBuilder(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("opcodes", this.opcodes);
		jsonObject.put("architecture", this.architecture);
		jsonObject.put("name", this.name);
		jsonObject.put("prototype", this.prototype);
		jsonObject.put("comment", this.comment);
		jsonObject.put("apis", this.apis);
		jsonObject.put("id", this.id);

		this.jsonObject = new JSONObject();
		this.jsonObject.put("client_id", jsonObject);
	}
}
