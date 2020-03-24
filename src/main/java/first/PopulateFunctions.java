package first;
import ghidra.pcode.*;

import ghidra.app.script.GhidraScript;
import ghidra.feature.fid.service.FidService;
import ghidra.framework.model.DomainFile;
import ghidra.framework.model.DomainFolder;
import ghidra.program.database.ProgramContentHandler;
import ghidra.program.model.listing.*;
import ghidra.util.Msg;
import ghidra.util.exception.CancelledException;
import ghidra.util.exception.VersionException;

import java.io.IOException;
import java.util.ArrayList;

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
		findFunction(programs, name);
	}

	@SuppressWarnings("null")
	private  void findFunction(ArrayList<DomainFile> programs, String name) {
		for (DomainFile domainFile : programs) {
			
			Program program = null;
		
		
			try {
				program = (Program) domainFile.getDomainObject(this, false, false, monitor);
				FunctionManager functionManager = program.getFunctionManager();
				FunctionIterator functions = functionManager.getFunctions(true);
			if(domainFile.getName().toLowerCase().endsWith(name.toLowerCase()))
			{

				for (Function function : functions) {					
					if (function!=null) {
						//test= test + function.getName();
						//println("found " + function.getName() + " in " + domainFile.getPathname());						
						modelFunction a1 = new modelFunction(function.getName(),function.getID(),function.hashCode()); 
					        				      
					        aList.add(a1);
					        var test = function.getBody();
					      
						 }		
					  int zz =2;
				}
				
			}
			
				//println("found " + test + " in " + domainFile.getPathname());
				
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
		
		public String name;
		public int hashCode;
		public long  id;
		
		public modelFunction(String Fname,long Fid, int HashCode) {
		
			this.name = Fname;
			this.hashCode =HashCode;
			this.id =Fid;
			
			
		}
		}
		
	
}
