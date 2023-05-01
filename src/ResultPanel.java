import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ResultPanel extends SubPanel {

	JLabel header;
	JPanel plFiller;
	
	JComponent[][] resultComponents;
	
	public ResultPanel(GuiMain gm) {
		super(gm);
		
		this.setOpaque(false);
		
		header = createHeader("RESULTS", 22);
		
		
		String[] resultValues = {"Date","Order ID",	//result field names for JLabels.text
				"Production time", "Delivery date",
				"Total cost"};
		resultComponents= new JComponent[5][2];
		for (int i = 0; i < resultComponents.length; i++) {
			
			//configure JLabels for Results field names
			resultComponents[i][0] = new JLabel(resultValues[i]);
			resultComponents[i][0].setForeground(Color.WHITE);
			resultComponents[i][0].setFont(new Font("Courier", Font.PLAIN,14));
			
			//configure JLabels for Results field values
			resultComponents[i][1] = new JLabel("");
			resultComponents[i][1].setForeground(Color.GRAY);
			resultComponents[i][1].setFont(new Font("Courier", Font.PLAIN,14));
		}
		
		//flexible space filler in Results sub panel
		plFiller = new JPanel();
		plFiller.setOpaque(false);
		
		Dimension dimBtnresultComponents = new Dimension(120, 35);
		JButton btnGenPdf = createButton("Generate PDF", dimBtnresultComponents);
		btnGenPdf.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
					InvoiceGen IgGen = new InvoiceGen();
					try {
						IgGen.genPDF();
						JLabel msg = new JLabel("<html>Order was saved at:<br/>"+IgGen.get_pdfSaveLoc() + "<html/>");
			        	JOptionPane.showMessageDialog(null, msg, "Invoice pdf saved", JOptionPane.DEFAULT_OPTION);
					} catch (IOException err) {
			        	err.printStackTrace();
			        	JLabel msg = new JLabel("In order to generate an invoice the order should contain products");
			        	JOptionPane.showMessageDialog(null, msg, "Order contains no products", JOptionPane.DEFAULT_OPTION);
					}
			  }
			} );
		JButton btnSaveOrder = createButton("Save order", dimBtnresultComponents);
		btnSaveOrder.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  gm.customerPanel.writeFieldsToOrder();
				  gm.employeePanel.writeFieldsToOrder();
				  dm.saveOrder();
				  loadNewOrder();
			  }
			} );
		JButton btnLoadOrder = createButton("Load order", dimBtnresultComponents);
		btnLoadOrder.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  if (popUpReadOrder()) {
					  gm.customerPanel.guiLoadFromOrder();
					  gm.employeePanel.guiLoadFromOrder();
					  gm.orderPanel.guiClearTable();
					  gm.orderPanel.guiLoadTable();
					  guiLoadResultFields();
				  }
			  }
			} );
		
		this.add(header, gbcHeader.EditGbc2(0, 0, 3, 1));
		this.add(resultComponents[0][0], gbcDefaultAssets.EditGbc(0, 1));
		this.add(resultComponents[0][1], gbcDefaultAssets.EditGbc2(1, 1, 2, 1));
		this.add(resultComponents[1][0], gbcDefaultAssets.EditGbc(0, 2));
		this.add(resultComponents[1][1], gbcDefaultAssets.EditGbc2(1, 2, 2, 1));
		this.add(resultComponents[2][0], gbcDefaultAssets.EditGbc(0, 3));
		this.add(resultComponents[2][1], gbcDefaultAssets.EditGbc2(1, 3, 2, 1));
		this.add(resultComponents[3][0], gbcDefaultAssets.EditGbc(0, 4));
		this.add(resultComponents[3][1], gbcDefaultAssets.EditGbc2(1, 4, 2, 1));
		this.add(resultComponents[4][0], gbcDefaultAssets.EditGbc(0, 5));
		this.add(resultComponents[4][1], gbcDefaultAssets.EditGbc2(1, 5, 2, 1));
		this.add(plFiller, gbcDefaultAssetsFiller.EditGbc(0, 7));
		this.add(btnGenPdf, gbcDefaultAssets.EditGbc(0, 8));
		this.add(btnSaveOrder, gbcDefaultAssets.EditGbc(1, 8));
		this.add(btnLoadOrder, gbcDefaultAssets.EditGbc(2, 8));
	}

	/**
	* sets all result gui field values according the active order parameters
	* <p>
	* This method has no input or return values,
	* but sets all result JLabel texts to the parameter values available 
	* in the active order instance.
	*/
	public void guiLoadResultFields() {
		((JLabel)resultComponents[0][1]).setText(dm.get_ActiveOrder().get_curTimeFormatted());
		((JLabel)resultComponents[1][1]).setText(dm.get_ActiveOrder().get_OrderID());
		((JLabel)resultComponents[2][1]).setText(dm.get_ActiveOrder().get_maxProdTimeFormatted());
		((JLabel)resultComponents[3][1]).setText(dm.get_ActiveOrder().get_delivTimeFormatted());
		((JLabel)resultComponents[4][1]).setText(String.format("%.2f",dm.get_ActiveOrder().get_totalCost()));
	}
	
	/**
	* Creates a new Order and loads it into the DataManager active order
	* <p>
	* This method has not input or return values,
	* but replaces the active order with a newly created empty order.
	* Also, the gui is updated to the new order.
	*/
	protected void loadNewOrder() {
		dm.set_ActiveOrder(new Order(true));
		gm.employeePanel.guiClear();
		gm.customerPanel.guiClear();
		gm.orderPanel.guiClearTable();
		gm.employeePanel.guiLoadFromOrder();
		guiLoadResultFields();
	}
	
	/**
	* Show a popup window showing available orders from which the user can make a selection
	* <p>
	* This method has not input or return values,
	* but retrieves the selected order from its XML file and loads into the DataManager active order
	* The new active order is not yet loaded to the gui
	* @throws IOException when no XML orders are available in source folder
	*/
	private boolean popUpReadOrder() {
	      File folder = new File(DataManager.get_xmlReadWrite().getFolderLoc() + "\\saveFiles");
	      String[] listOfFilesNames = filterFiles(folder.listFiles(), "OID");
	      String[] options = {"Ok"};
	      try {
		      if (listOfFilesNames.length < 1) {
		    	  throw new IOException();
		      }
			      JComboBox<Object> comboBox = new JComboBox<Object>(listOfFilesNames);
			      comboBox.setSelectedIndex(comboBox.getItemCount()-1);
			      int selectedOption = JOptionPane.showOptionDialog(null, comboBox, "Files",
	    		  JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
			      if(selectedOption == 0) {
					  dm.readOrder((String)comboBox.getSelectedItem());
					  return true;
			      }
		      } catch (IOException err) {
		    	  err.printStackTrace();
		    	  JLabel msg = new JLabel("No order files are available");
		    	  JOptionPane.showMessageDialog(null, msg, "No orders", JOptionPane.DEFAULT_OPTION);
		      }
	      return false;
	}
	
	/**
	* Filters a list of Files so only the files are left which name starts with the specified tag
	* <p>
	* @param listOfFiles file list of type File	which need to be filtered
	* @param tag string that needs to be included in the file name
	* @return a list of strings with file names that need to be included based on tag
	*/
	private String[] filterFiles(File[] listOfFiles, String tag) {
		ArrayList<String> listOfFilesStr = new ArrayList<String>();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String fileName = listOfFiles[i].getName();
				if (fileName.substring(0, tag.length()).equals(tag)) {
					listOfFilesStr.add(fileName.substring(0,fileName.length()-4));
				}
			}
		}
		return (String[]) listOfFilesStr.toArray(new String[listOfFilesStr.size()]);
	}
}
