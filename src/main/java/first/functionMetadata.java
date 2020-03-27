package first;

import java.util.ArrayList;
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
     * @throws JSONException
     */
    public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis, String id) throws JSONException {
        this.opcodes=opcodes;
        this.architecture=architecture;
        this.name=name;
        this.prototype=prototype;
        this.comment=comment;
        this.apis=apis;
        this.id=id;
        if(apis ==null)
        {
        	this.apis =  new ArrayList<String>();
        	this.apis.add("test"); 
        }
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
     * @throws JSONException
     */
    public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis) throws JSONException {
        this(opcodes, architecture, name, prototype, comment, apis, "");
    }

    /**
     * Class Builder without the optionals name, prototype, comment, id
     * @param opcodes
     * @param architecture
     * @param apis
     * @throws JSONException
     */
    public functionMetadata(String opcodes, String architecture, List<String> apis) throws JSONException {
        this(opcodes, architecture, "", "", "", apis, "");
    }

    /**
     * jsonBuilder create the needed JSON to simplify AddFunctionMetadata
     * @throws JSONException
     */
    private void jsonBuilder() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("opcodes", this.opcodes);
        jsonObject.put("architecture", this.architecture);
        if (this.name != "") { jsonObject.put("name", this.name);}
        if (this.prototype!= "") { jsonObject.put("prototype", this.prototype);}
        if (this.comment != "") { jsonObject.put("comment", this.comment);}        
        jsonObject.put("apis", this.apis);
        if (this.id != "") {jsonObject.put("id", this.id);}

        this.jsonObject = new JSONObject();
        this.jsonObject.put("client_id", jsonObject);
    }
}
