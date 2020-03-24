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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.json.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
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
import ghidra.program.model.listing.Function;
import ghidra.program.model.listing.FunctionIterator;
import ghidra.program.model.listing.FunctionManager;
import ghidra.program.model.listing.Listing;
import ghidra.program.model.listing.Program;
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
	shortDescription = "Plugin short description goes here.",
	description = "Plugin long description goes here."
)
//@formatter:on
public class FirstPlugin extends ProgramPlugin  {

	MyProvider provider;
	
	  static ArrayList<modelFunction > aList =  
              new ArrayList<modelFunction >(); 

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
		private DefaultTableModel tableModel;
	    private JPopupMenu popupMenu;
	    private JMenuItem menuItemAdd;
	    private JMenuItem menuItemCheck;
	    private JMenuItem menuItemShowDetails;
	    private JMenuItem menuItemColor;
	    private JMenuItem menuItemRemove;
	    private JMenuItem menuItemRemoveAll;
	    
		public MyProvider(Plugin plugin, String owner) {
			super(plugin.getTool(), owner, owner);
			buildPanel();
			createActions();
		
		}
		

		
		// Customize GUI
		private void buildPanel() {
			panel = new JPanel(new BorderLayout());
			
		//	JTextArea textArea = new JTextArea(5, 25);
			//textArea.setEditable(false);
		//	panel.add(new JScrollPane(textArea));
			
			 popupMenu = new JPopupMenu();
			 menuItemAdd = new JMenuItem("Add to First");
			 menuItemCheck = new JMenuItem("Check From First");
			 menuItemShowDetails = new JMenuItem("Show Details");
			 menuItemColor = new JMenuItem("Color");
			 menuItemRemove = new JMenuItem("Remove");
			 menuItemRemoveAll = new JMenuItem("Remove All");

			 
	        menuItemAdd.addActionListener((ActionListener) this);
	        menuItemCheck.addActionListener((ActionListener) this);
	        menuItemShowDetails.addActionListener((ActionListener) this);
	        menuItemColor.addActionListener((ActionListener) this);
	        menuItemRemove.addActionListener((ActionListener) this);
	        menuItemRemoveAll.addActionListener((ActionListener) this);
	        
			popupMenu.add(menuItemAdd);
			popupMenu.add(menuItemCheck);
			popupMenu.add(menuItemShowDetails);
			popupMenu.add(menuItemColor);
			popupMenu.add(menuItemRemove);
			popupMenu.add(menuItemRemoveAll);
			
			
			panel.setBorder(BorderFactory.createTitledBorder( BorderFactory.createEtchedBorder(), "Functions List", TitledBorder.CENTER, TitledBorder.TOP));
			loadFunctions = new JButton("Load Functions");
			loadFunctions.setBounds(50,100,95,30);  
			
			loadFunctions.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PopulateFunctions pf =new PopulateFunctions();	
			        	try {
							pf.run();		
							
							 aList= pf.getFuntions();  
							// removeAllRows();
							 refresh();
							 tableModel.fireTableDataChanged();
					    	 table.repaint();
					    	// provider = new MyProvider(this, pluginName);
							 buildPanel();
						} catch (Exception ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
						
					}
				});
		
			String[] header = { "Id", "Prototype", "HashCode" };
			//PopulateFunctionsFromGhidra(aList);	
			

        	//tableModel= (DefaultTableModel) table.getModel();
			Object[][] fileList = new Object[aList.size()][3];

			for (int i = 0; i < aList.size(); i++) {
			    fileList [i][0] = aList.get(i).id;
			    fileList [i][1] = aList.get(i).name;
			    fileList [i][2] = aList.get(i).hashCode;
			    
			    
			}		
			 
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
			action = new DockingAction("My Action", getName()) {
				@Override
				public void actionPerformed(ActionContext context) {
					//Msg.showInfo(getClass(), panel, "Custom Action", "Hello!");      	
				}
			};
			action.setToolBarData(new ToolBarData(Icons.ADD_ICON, null));
			action.setEnabled(true);
			action.markHelpUnnecessary();
			dockingTool.addLocalAction(this, action);
		}

		@Override
		public JComponent getComponent() {
			return panel;
		}
		
		
	    public void actionPerformed(ActionEvent event) {
	    	
	        JMenuItem menu = (JMenuItem) event.getSource();
	        if (menu == menuItemAdd) {
	           try {
				addNewRow();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        	//Msg.showInfo(getClass(), panel, "Custom Action", "menuItemAdd!");
	        } else if (menu == menuItemCheck) {
	        	try {
					checkFunctionFirst();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}        

	        } else if (menu == menuItemShowDetails) {
	        	Msg.showInfo(getClass(), panel, "Custom Action", "menuItemShowDetails!");
	        } else if (menu == menuItemColor) {
	        	Msg.showInfo(getClass(), panel, "Custom Action", "menuItemColor!");
	        }
	        else if (menu == menuItemRemove) {
	        	removeCurrentRow();
	        }
	        else if (menu == menuItemRemoveAll) {
	        	removeAllRows(); 
	        }        
	        
	    }

	    private void refresh() {
	    	tableModel.fireTableDataChanged();
	    	 table.repaint();
	    }
	    private void checkFunctionFirst() throws Exception {
	    	int selectedRow = table.getSelectedRow();
	    	var name =tableModel.getValueAt(selectedRow, 1);
	    	httpService myService = new httpService("https://louishusson.com/api/", "BFBFC6FC-4C84-4299-B2F6-7335C479810D");
			JSONObject result = myService.checkInFirst("1b3105ada011ed1053739d9c6028b3cc","1272189608");
			if(result.getBoolean("failed")==false) {
				System.out.println("Request Success");
				System.out.println("checkin = " + result.get("checkin"));
			}
			else
				System.out.println("Request failed");
	    }
	    private void addNewRow() throws Exception {
	       // tableModel.addRow(new String[0]);	    	
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
