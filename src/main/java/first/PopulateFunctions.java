package first;

import java.io.IOException;
import java.util.ArrayList;
import ghidra.app.script.GhidraScript;
import ghidra.feature.fid.service.FidService;
import ghidra.framework.model.DomainFile;
import ghidra.framework.model.DomainFolder;
import ghidra.framework.Architecture;
import ghidra.framework.Platform;
import ghidra.program.database.ProgramContentHandler;
import ghidra.program.model.address.Address;
import ghidra.program.model.address.AddressIterator;
import ghidra.program.model.address.AddressSetView;
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.FunctionManager;
import ghidra.program.model.listing.Program;
import ghidra.program.model.mem.Memory;
import ghidra.util.Msg;
import ghidra.util.exception.CancelledException;
import ghidra.util.exception.VersionException;

public class PopulateFunctions extends GhidraScript {

	FidService service;
	public  ArrayList<modelFunction > getFuntions()
	{
		return aList;
	}	  
    // Here aList is an ArrayList of ArrayLists 
    ArrayList<modelFunction > aList =  
              new ArrayList<modelFunction>(); 	 
           
              
	@Override
	protected void run() throws Exception {
		service = new FidService();

		DomainFolder folder =
			askProjectFolder("Please select a project folder to RECURSIVELY look for a named function:");
		
		String name =
			askString("Please enter file name",
				"Please enter the file name you're looking for:");

	
		ArrayList<DomainFile> programs = new ArrayList<DomainFile>();
		findPrograms(programs, folder);
		findFunctions(programs, name);
	}

	@SuppressWarnings("null")
	private  void findFunctions(ArrayList<DomainFile> programs, String name) {
		  Architecture architecture=  Platform.CURRENT_PLATFORM.getArchitecture(); 
		for (DomainFile domainFile : programs) {
			
			Program program = null;
			Memory memory;
			
			try {
				program = (Program) domainFile.getDomainObject(this, false, false, monitor);
				FunctionManager functionManager = program.getFunctionManager();
				FunctionIterator functions = functionManager.getFunctions(true);
				memory = program.getMemory();
			if(domainFile.getName().toLowerCase().contains(name.toLowerCase()))
			{

				for (Function function : functions) {					
					if (function!=null) {
						function.getPrototypeString(true, true);
						
					    AddressIterator ari = function.getBody().getAddresses(true);
					    String opcode = "";
                        for (Address a : ari) {                        	
                            opcode += String.format("%02X",  memory.getByte(a));
                        }
                       Address test = function.getSymbol().getAddress();
                       opcode+= String.format("%02X",  memory.getByte(test));          
						modelFunction a1 = new modelFunction(String.valueOf(function.getID()),function.getName(),function.getBody(),function.getPrototypeString(true, true),function.getComment(),architecture,opcode); 
						aList.add(a1);		
						}		
					}
				}
			}
			catch (Exception e) {
				Msg.warn(this, "problem looking at " + domainFile.getName(), e);
			}
			finally {
				if (program != null) {
					program.release(this);
				}
			}
		}
	}

	private void findPrograms(ArrayList<DomainFile> programs, DomainFolder folder)
			throws VersionException, CancelledException, IOException {
		DomainFile[] files = folder.getFiles();
		for (DomainFile domainFile : files) {
			
			if (domainFile.getContentType().equals(ProgramContentHandler.PROGRAM_CONTENT_TYPE)) {
				programs.add(domainFile);
			}
		}
		DomainFolder[] folders = folder.getFolders();
		for (DomainFolder domainFolder : folders) {
			
			findPrograms(programs, domainFolder);
		}
	}
	
	public class modelFunction{

		public String  idfunction;
		public String namefunction;	
		public AddressSetView bodyfunction;
		public String prototypefunction;	
		public String comment;	
		public Architecture  architecture;

		public String opCode;
		
		public modelFunction(String Fid, String functionName, AddressSetView bodyFunction, String prototypeFunction, String commentFunction, Architecture architecture, String opcode){
			this.idfunction =Fid;
			this.namefunction = functionName;
			this.bodyfunction =bodyFunction;
			this.prototypefunction =prototypeFunction;
			this.comment =commentFunction;
			this.architecture = architecture;
			this.opCode = opcode;
			
		}
	}
	
}
