/* ###
 * IP: GHIDRA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package first;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.json.*;
import docking.ActionContext;
import docking.ComponentProvider;
import docking.action.DockingAction;
import docking.action.ToolBarData;
import first.PopulateFunctions.modelFunction;
import ghidra.app.ExamplesPluginPackage;
import ghidra.app.plugin.PluginCategoryNames;
import ghidra.app.plugin.ProgramPlugin;
import ghidra.framework.plugintool.*;
import ghidra.framework.plugintool.util.PluginStatus;
import ghidra.util.HelpLocation;
import ghidra.util.Msg;
import resources.Icons;

/**
 * TODO: Provide class-level documentation that describes what this plugin does.
 */
//@formatter:off
@PluginInfo(
	status = PluginStatus.STABLE,
	packageName = ExamplesPluginPackage.NAME,
	category = PluginCategoryNames.EXAMPLES,
	shortDescription = "The objectif of this Ghidra Plugin to link Ghidra (From NSA) with a First Server Database (From Thalos Intelligence).",
	description = "The objectif of this Ghidra Plugin to link Ghidra (From NSA) with a First Server Database (From Thalos Intelligence)."
)
//@formatter:on
public class FirstPlugin extends ProgramPlugin  {

	MyProvider provider;
	static ArrayList<modelFunction > aList =    new ArrayList<modelFunction >(); 
	static String url =new String();
	static String apiKey = new String();            
	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	static Object[][] metadataList ;
	//  httpService myService = new httpService("https://louishusson.com/api/", "BFBFC6FC-4C84-4299-B2F6-7335C479810D");
 
	/**
	 * Plugin constructor.
	 * 
	 * @param tool The plugin tool that this plugin is added to.
	 */
	public FirstPlugin(PluginTool tool) {
		super(tool, true, true);

		// TODO: Customize provider (or remove if a provider is not desired)
		String pluginName = getName();
		provider = new MyProvider(this, pluginName);
		// TODO: Customize help (or remove if help is not desired)
		String topicName = this.getClass().getPackage().getName();
		String anchorName = "HelpAnchor";
		provider.setHelpLocation(new HelpLocation(topicName, anchorName));		
	}

	@Override
	public void init() {
		super.init();

		// TODO: Acquire services if necessary
	}

	// TODO: If provider is desired, it is recommended to move it to its own file
	private static class MyProvider extends ComponentProvider  implements ActionListener{

		private JPanel panel;
		private DockingAction action;
		private JButton loadFunctions;
	    private JTable table;
	    private JTable tableMetadata;
	    
		private DefaultTableModel tableModel;
		private DefaultTableModel tableMetadataModel;
		private JFrame frame;
		private JFrame popup;
		private JButton LoginButton;
	    private JButton CancelButton;
	    private JLabel Label1;
	    private JLabel Label2;
	    private JLabel LabelNameMetadata;

	    
	    private JTextField Label1Field;
	    private JTextField Label2Field; 
	    private JTextField NameMetadataField; 
	    private JPopupMenu popupMenu;
	    private JMenuItem menuItemAdd;
	    private JMenuItem menuItemManage;

	    private JMenuItem menuItemUnApply;
	    private JMenuItem menuItemSimilarFunction;
	    private JMenuItem menuItemColor;
	    private JMenuItem menuItemRemove;
	    private JMenuItem menuItemRemoveAll;
	    private JButton buttonAddMetadata;
	    
	    private JPopupMenu popupMenuMetadata;
	    private JMenuItem menuItemApplyMetadata;
	    private JMenuItem menuItemUnApplyMetadata;
	    private JMenuItem menuItemRemoveMetadata;
	    
		public MyProvider(Plugin plugin, String owner) {
			super(plugin.getTool(), owner, owner);
			buildPanel();
			createActions();		
		}	
		
		private void buildForm() {

	    	frame = new JFrame ("Connect to First Server");	    	
	        LoginButton = new JButton ("Connect");	        
	        CancelButton = new JButton ("Cancel");
	        Label1 = new JLabel ("Url ");
	        Label2 = new JLabel ("ApiKey");
	        Label1Field = new JTextField (11);
	        Label2Field = new JTextField (11);     
	        frame.setPreferredSize(new Dimension (624, 334));
	        frame.setLayout (null);
	        //add components
	        frame.add(LoginButton);
	        frame.add(CancelButton);
	        frame.add(Label1);
	        frame.add(Label2);
	        frame.add(Label1Field);
	        frame.add(Label2Field);
	        //set component bounds (only needed by Absolute Positioning)
	        LoginButton.setBounds (185, 155, 115, 30);
	        CancelButton.setBounds (360, 155, 105, 30);
	        Label1.setBounds (60, 60, 100, 25);
	        Label2.setBounds (50, 110, 100, 25);
	        Label1Field.setBounds (120, 60, 405, 30);
	        Label2Field.setBounds (120, 105, 405, 30);	      
	        frame.pack();
	        frame.setVisible (true);
	        LoginButton.addActionListener(this) ;				
	        CancelButton.addActionListener(this); 
			
		}
		private void builPopup(String namePopUp, String nameLabel , String nameButton)
		{
			popup = new JFrame(namePopUp);
			
			LabelNameMetadata = new JLabel (nameLabel);
			buttonAddMetadata = new JButton(nameButton);			
			NameMetadataField = new JTextField (12);
			
			popup.setPreferredSize(new Dimension (624, 334));		
			popup.setLayout (null);	
			
			popup.add(buttonAddMetadata);
			popup.add(NameMetadataField);
			popup.add(LabelNameMetadata);
			
			buttonAddMetadata.setBounds (160, 155, 115, 30);
			NameMetadataField.setBounds (160, 60, 405, 30);
			LabelNameMetadata.setBounds (60, 60, 100, 25);
			popup.pack();
			popup.setVisible(true);
			buttonAddMetadata.addActionListener(this);
			
			
		}
		private void closePopup()
		{
			popup.setVisible(false);
		}
		 private void metadataTable(String opCode) 
		 { 
			 httpService myService = new httpService(url, apiKey);
			 myService.noSSLVerification();
			 JSONObject metdataJson =myService.getMetadataCreated(-1);
			JSONArray metadataListResult =   (JSONArray) metdataJson.get("results");
			ArrayList<MetadataModel> metadataModel = new ArrayList<MetadataModel>();
			for(int i =0; i<metadataListResult.length();i++)
			{
				metadataModel.add(new MetadataModel(metadataListResult.getJSONObject(i)));
				
			}
			
			metadataList = new Object[metadataModel.size()][6];
			
	  			for (int i = 0; i < metadataModel.size(); i++) {			
	  				metadataList [i][0] = metadataModel.get(i).jsonObject.get("creator");
	  				metadataList [i][1] = metadataModel.get(i).jsonObject.get("name");
	  				metadataList [i][2] = metadataModel.get(i).jsonObject.get("prototype");
	  				metadataList [i][3] = metadataModel.get(i).jsonObject.get("comment");
	  				metadataList [i][4] = metadataModel.get(i).jsonObject.get("id");	
	  				metadataList [i][5] = opCode;
	  			}		
	  			
			 JFrame frameMetadata = new JFrame(); 
			 frameMetadata.setTitle("Metadata First"); 	
			 
			 String[] columnNames = { "Creator", "Name", "Prototype","Comment" };  
			 tableMetadataModel =new DefaultTableModel(metadataList, columnNames); 
			 tableMetadata = new JTable(tableMetadataModel);
			// JTable j = new JTable(metadataList, columnNames); 
			 tableMetadata.setBounds(30, 40, 200, 300); 	
			 JScrollPane sp = new JScrollPane(tableMetadata); 
			 popupMenuMetadata = new JPopupMenu();			
			 menuItemApplyMetadata = new JMenuItem("Apply Metadata");
			 menuItemUnApplyMetadata = new JMenuItem("UnApply Metadata");
			 menuItemRemoveMetadata = new JMenuItem("Remove Metadata");			 
			 menuItemApplyMetadata.addActionListener(this);
			 menuItemUnApplyMetadata.addActionListener(this);
			 menuItemRemoveMetadata.addActionListener(this);
			 popupMenuMetadata.add(menuItemApplyMetadata);
			 popupMenuMetadata.add(menuItemUnApplyMetadata);
			 popupMenuMetadata.add(menuItemRemoveMetadata);
			 tableMetadata.setComponentPopupMenu(popupMenuMetadata);
			 tableMetadata.addMouseListener(new TableMouseListener(tableMetadata));			 
			 frameMetadata.add(sp);
			 frameMetadata.setSize(500, 200); 		
			 frameMetadata.setVisible(true); 				 
		 } 
		 
		// Customize GUI
		private void buildPanel() {
			 panel = new JPanel(new BorderLayout());
			 popupMenu = new JPopupMenu();
			 menuItemAdd = new JMenuItem("Add Function Metadata");
			 menuItemManage =new JMenuItem("Manage Metadata");
			
			 menuItemUnApply = new JMenuItem("UnApply Metadata");
			 menuItemSimilarFunction = new JMenuItem("Scan For Similar Function");
			 menuItemColor = new JMenuItem("Color in Ghidra");
			// menuItemColor.setEnabled(false);
			 menuItemRemove = new JMenuItem("Remove");
			 menuItemRemoveAll = new JMenuItem("Remove All");			 
	        menuItemAdd.addActionListener(this);
	        menuItemManage.addActionListener(this);
	     
	        menuItemUnApply.addActionListener(this);
	        menuItemSimilarFunction.addActionListener(this);
	        menuItemColor.addActionListener(this);
	        menuItemRemove.addActionListener(this);
	        menuItemRemoveAll.addActionListener(this);	        
			popupMenu.add(menuItemAdd);
			popupMenu.add(menuItemManage);
			
			popupMenu.add(menuItemUnApply);
			popupMenu.add(menuItemSimilarFunction);
			popupMenu.add(menuItemColor);
			popupMenu.add(menuItemRemove);
			popupMenu.add(menuItemRemoveAll);		
			panel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Functions List", TitledBorder.CENTER, TitledBorder.TOP));
			loadFunctions = new JButton("Load Functions");
			loadFunctions.setBounds(50,100,95,30);  
			loadFunctions.addActionListener(this);	
			
			populateTable();
			String[] header = { "Id", "Name", "Opcode","Prototype","Comment"};	
			Object[][] fileList = populateTable();
			tableModel = new DefaultTableModel(fileList, header); 
			table = new JTable(tableModel);
			
			JTableHeader headerlabel = table.getTableHeader();
			headerlabel.setBackground(Color.BLACK);
			headerlabel.setForeground(Color.BLUE);	
			table.setComponentPopupMenu(popupMenu);
			table.addMouseListener(new TableMouseListener(table));
			table.setModel(tableModel);
			tableModel.fireTableDataChanged();
			panel.add(new JScrollPane(table));	
			panel.add(loadFunctions, BorderLayout.NORTH);
			setVisible(true);
		}		
	
	
		// TODO: Customize actions
		private void createActions() {
			action = new DockingAction("Connect to First API ", getName()) {
				@Override
				public void actionPerformed(ActionContext context) {
					buildForm();
				}
			};
			action.setToolBarData(new ToolBarData(Icons.NAVIGATE_ON_OUTGOING_EVENT_ICON, null));
			action.setEnabled(true);
			action.markHelpUnnecessary();
			dockingTool.addLocalAction(this, action);
		}

		@Override
		public JComponent getComponent() {
			return panel;
		}
		
	    public void actionPerformed(ActionEvent event) {	    	
	       
	    	Object  object =  event.getSource();
	        if (object ==loadFunctions)
	        {
	        	PopulateFunctions pf =new PopulateFunctions();
	        	try {
	        		pf.run();	
	        		aList= pf.getFuntions();
	        		buildPanel();
	        		} 
	        	catch (Exception ex) {	        
	        		ex.printStackTrace();
	        		}		
	        	}
	        
	        
	        else if (object == LoginButton)
	        {
	        	testConnexion();
	        }
	        else  if (object == CancelButton)
	        {
	        	frame.setVisible(false);
	        }
	        
	        else  if (object == menuItemAdd) {
	        	builPopup("Add Metadata","Metadata Name","Add Metadata");	 
	        } 
	        
	        else if (object == buttonAddMetadata) {
	        	String metadata = new String (NameMetadataField.getText());
	        	try {
					addFunctionMetadata(metadata);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	        	
        	} 
	        else if (object == menuItemManage) {
	        	  int selectedRow = table.getSelectedRow();
	        	  String opCode= aList.get(selectedRow).opCode;
        		metadataTable(opCode);        
        		
        	} 
        	else if (object == menuItemApplyMetadata) {
        	   	ApplyMetadata();     
        	}
        	else if (object == menuItemUnApplyMetadata) {
        		UnApplyMetadata();
        	}
        	else if (object == menuItemRemoveMetadata) {
        		RemoveMetadata();   
        		
        	}        	
        	else if (object == menuItemUnApply) {
        		
        	}
	        else if (object == menuItemSimilarFunction) {
	        	Msg.showInfo(getClass(), panel, "Action", "Scan For Similar Function!");
	        } 
	        else if (object == menuItemColor) {
	        	try {
					checkFunctionFirst();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}      
	        }
	        else if (object == menuItemRemove) {
	        	removeCurrentRow();
	        }
	        else if (object == menuItemRemoveAll) {
	        	removeAllRows(); 
	        }        
	        
	    }	   
	    
	    private void testConnexion() {	    	
	    	url = new String (Label1Field.getText());
	        apiKey = new String( Label2Field.getText());
	        httpService myService = new httpService(url, apiKey);
	        myService.noSSLVerification();
	        try {
	        	
	        	JSONObject connectionStatus = myService.testConnection();
	        	String statusValue =connectionStatus.get("status").toString();
	        	if (statusValue.equalsIgnoreCase("CONNECTED"))
	        	{
	        		Msg.showInfo(getClass(), panel, "Connection to First API", "Connection : " + statusValue);
	        		frame.setVisible(false);
	        	}
	        	else 
	        		Msg.showError(getClass(), panel, "Connection to First API", "Connection : " + statusValue);		        	
	        	
			} 
	        catch (Exception exception)
	        {
				exception.printStackTrace();				
	        }  		        
	    }
	    
	    public Object[][]  populateTable()
	  		{
	  			
	  			Object[][] fileList = new Object[aList.size()][6];
	  			for (int i = 0; i < aList.size(); i++) {			
	  			    fileList [i][0] = aList.get(i).idfunction;
	  			    fileList [i][1] = aList.get(i).namefunction;
	  			    fileList [i][2] = aList.get(i).opCode;
	  			    fileList [i][3] = aList.get(i).prototypefunction;
	  			    fileList [i][4] = aList.get(i).comment; 
	  			    }		
	  			 return fileList;
	  		}
	    private void checkFunctionFirst() throws Exception {
	    	int selectedRow = table.getSelectedRow();
	    	String opCodeFunction =tableModel.getValueAt(selectedRow, 2).toString();		    	
	    	String md5Function = calculMd5(opCodeFunction);
	    	System.out.println(md5Function); 
	    	String crc32 = calculCrc32(opCodeFunction);	    	
	        httpService myService = new httpService(url, apiKey);
	        JSONObject result = myService.checkInSample(md5Function,crc32);
			if(result.getBoolean("failed")==false) { 
				Msg.showInfo(getClass(), panel, "Check First", " Request : Success \n Checking : " +result.get("checkin"));				
				}
			else
				
			Msg.showError(getClass(), panel,  "Check First", "Request : Failed");
	    }
	    
	    private String calculMd5(String opCodeFunction)
	    {
	    	MessageDigest messageDigest;
			try {
				messageDigest = MessageDigest.getInstance("MD5");
				messageDigest.update(opCodeFunction.getBytes());
		    	byte[] digiest = messageDigest.digest();
		    	char[] hexChars = new char[digiest.length * 2];
		    	for (int j = 0; j < digiest.length; j++) {
		    		int v = digiest[j] & 0xFF;
		    		hexChars[j * 2] = HEX_ARRAY[v >>> 4];
		    		hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];		
		    	}	    	
		    	String md5Function = String.valueOf(hexChars);
		    	return md5Function;
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	    
	    }
	    
	    private String calculCrc32(String opcodeFunction)
	    {
	    	byte[] bytes = opcodeFunction.getBytes();
	        Checksum checksum = new CRC32(); 
	        checksum.update(bytes, 0, bytes.length);
	        String crc32 = String.valueOf(checksum.getValue()); 
	        return crc32;
	        
	    }
	    
	    // FIXME  		  
	    private void addFunctionMetadata(String nameMetadata) throws Exception {
	    	
	    	 int selectedRow = table.getSelectedRow();
	    	 httpService myService = new httpService(url, apiKey);
	    	 modelFunction rowFunction =  aList.get(selectedRow);
	    	 String md5Opcode = calculMd5(rowFunction.opCode);
	    	 String crc32Opcode = calculCrc32(rowFunction.opCode);
	    	 myService.checkInSample(md5Opcode, crc32Opcode);	    	 
	    	 List<String> apis = null;
	    	 functionMetadata fm = new functionMetadata(rowFunction.opCode,rowFunction.architecture.toString()
	    			,nameMetadata,rowFunction.prototypefunction,"comment test", apis);
       		   JSONObject  jsonResponse = myService.addFunctionMetadata(md5Opcode, crc32Opcode, fm);
       		
       		   if((boolean) jsonResponse.get("failed"))
       		   {
       			
       			  Msg.showInfo(getClass(), panel, "Custom Action", "Add the metadata to the function failed ");  
       		   }
       		   else
       			
       			   Msg.showInfo(getClass(), panel, "Custom Action", "Add the metadata to the function succeeded");  
       		closePopup();
		    }
	    
	    private void ApplyMetadata() {
	    	
	    	int selectedRow = tableMetadata.getSelectedRow();
	    	httpService myService = new httpService(url, apiKey);
	    	Object[] rowMetadata = metadataList[selectedRow];
	    	String opcode = (String) rowMetadata[5];
	    	String md5Opcode = calculMd5(opcode);
	    	String crc32OpCode = calculCrc32(opcode);
	    	String id = (String) rowMetadata[4];
	    	JSONObject  jsonResponse = myService.applyMetadata(md5Opcode, crc32OpCode,id );
	    	Msg.showInfo(getClass(), panel, "Custom Action", "Applying metadata to the function : " + jsonResponse.get("results").toString());  
	    	
	         	
	    }
	    private void UnApplyMetadata() {

	    	int selectedRow = tableMetadata.getSelectedRow();
	    	httpService myService = new httpService(url, apiKey);
	    	Object[] rowMetadata = metadataList[selectedRow];
	    	String opcode = (String) rowMetadata[5];
	    	String md5Opcode = calculMd5(opcode);
	    	String crc32OpCode = calculCrc32(opcode);
	    	String id = (String) rowMetadata[4];
	    	JSONObject  jsonResponse = myService.unapplyMetadata(md5Opcode, crc32OpCode,id );
	    	Msg.showInfo(getClass(), panel, "Custom Action", "Unapplying metadata to the function : " + jsonResponse.get("results").toString());  
	    }
	    	
	    private void RemoveMetadata() {
	    	int selectedRow = tableMetadata.getSelectedRow();
	    	httpService myService = new httpService(url, apiKey);
	    	Object[] rowMetadata = metadataList[selectedRow];	    	
	    	String id = (String) rowMetadata[4];
	    	JSONObject  jsonResponse = myService.deleteMetadata(id);
	    
	    	Msg.showInfo(getClass(), panel, "Custom Action", "Removing metadata from First : " +  jsonResponse.get("deleted").toString());
	    	
	    }
	    
	    private void removeCurrentRow() {
		        int selectedRow = table.getSelectedRow();
		        tableModel.removeRow(selectedRow);
		    }
	    
	    private void removeAllRows() {
		        int rowCount = tableModel.getRowCount();
		        for (int i = 0; i < rowCount; i++) {
		            tableModel.removeRow(0);
		        }
	    }
		    
	}
}
