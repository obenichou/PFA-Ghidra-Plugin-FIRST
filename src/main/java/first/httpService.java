package first;

import java.util.ArrayList;
import java.util.List;
import org.json.*;

/**
 * functionMetadata
 * This class contain all metadata info possible of a function
 */
public class functionMetadata {

    /**
     * The Opcodes.
     */
    public String opcodes;
    /**
     * The Architecture.
     */
    public String architecture;
    /**
     * The Name.
     */
    public String name;
    /**
     * The Prototype.
     */
    public String prototype;
    /**
     * The Comment.
     */
    public String comment;
    /**
     * The Apis.
     */
    public List<String> apis;
    /**
     * The Id.
     */
    public String id;
    /**
     * The Json object.
     */
    public JSONObject jsonObject;

    /**
     * Class Builder full
     *
     * @param opcodes      OPCODE of the function
     * @param architecture Architecture where the function is running
     * @param name         name of the metadata
     * @param prototype    prototype of the function
     * @param comment      Comment to the metadata
     * @param apis         ?
     * @param id           id for the link
     * @throws JSONException the json exception
     */
    public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis, String id) throws JSONException {
        this.opcodes=opcodes;
        this.architecture=architecture;
        this.name=name;
        this.prototype=prototype;
        this.comment=comment;
        this.apis=apis;
        this.id=id;
        // FIXME
        if(apis ==null)
        {
        	this.apis =  new ArrayList<String>();
        	this.apis.add("test"); 
        }
        this.jsonBuilder();
        
    }

    /**
     * Class Builder without the optional id
     *
     * @param opcodes      OPCODE of the function
     * @param architecture Architecture where the function is running
     * @param name         name of the metadata
     * @param prototype    prototype of the function
     * @param comment      Comment to the metadata
     * @param apis         ?
     * @throws JSONException the json exception
     */
    public functionMetadata(String opcodes, String architecture, String name, String prototype, String comment, List<String> apis) throws JSONException {
        this(opcodes, architecture, name, prototype, comment, apis, "");
    }

    /**
     * Class Builder without the optionals name, prototype, comment, id
     *
     * @param opcodes      OPCODE of the function
     * @param architecture Architecture where the function is running
     * @param apis         ?
     * @throws JSONException the json exception
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
