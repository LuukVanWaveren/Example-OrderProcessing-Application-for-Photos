import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class SubPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	CustomGbc gbcHeader = new CustomGbc(new Insets(5,4,15,0), 0.5, 0);
	CustomGbc gbcDefaultAssets = new CustomGbc(new Insets(7,4,7,4));
	CustomGbc gbcDefaultAssetsFiller = new CustomGbc(new Insets(0,0,0,0), 0, 0.5);
	
	protected GuiMain gm;
	protected DataManager dm = DataManager.getInstance();
	
	public SubPanel(GuiMain gm) {
		this.gm = gm;
		this.setBackground(gm.colorPanel);
		this.setLayout(new GridBagLayout());
		this.setBorder(new EmptyBorder(5, 20, 5, 20));
		

	}

	/**
	* creates a JLabel in a specific style meant for use as a sub panel header.
	* <p>
	* This method takes a few JLabel specifications as input and outputs a styled JLabel using those inputs.
	* @param name String of text that the JLabel displays
	* @param fontSize int that represents the font size used for the header
	*/
	public static JLabel createHeader(String name,int fontSize) {
		JLabel header = new JLabel(name);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("Courier", Font.BOLD,fontSize));
		return header;
	}
	
	/**
	* resizes all column widths of an inserted table based on a percentage list.
	* <p>
	* This method has no return values,
	* but resizes each column from the input table with a corresponding percentage.
	* @param table JTable of which the columns width will be adjusted
	* @param cWP list with float numbers which are percentages, where each 
	* percentage tells what part of the complete table width each column should be
	*/
	protected void resizeColumns(JTable table, float[] cWP) {
	    int tW = table.getWidth();
	    TableColumn column;
	    TableColumnModel jTableColumnModel = table.getColumnModel();
	    int cantCols = jTableColumnModel.getColumnCount();
	    for (int i = 0; i < cantCols; i++) {
	        column = jTableColumnModel.getColumn(i);
	        int pWidth = Math.round(cWP[i] * tW);
	        column.setPreferredWidth(pWidth);
	    }
	}
	
	/**
	* creates a JButton in a specific style.
	* <p>
	* This method takes a few JButton specifications as input and outputs a styled JButton using those inputs.
	* @param text String of text that is displayed on the button
	* @param dim Dimension consisting of width and height that defines the button size
	*/
	public JButton createButton(String text, Dimension dim) {
		JButton newButton = new JButton(text);
		newButton.setBorderPainted(false);
		newButton.setBackground(gm.colorButton);
		newButton.setFocusPainted(false);
		newButton.setForeground(Color.WHITE);
		newButton.setMinimumSize(dim);
		newButton.setPreferredSize(dim);
		return newButton;
	}
	
	/**
	* Show a popup window showing available orders from which the user can make a selection
	* <p>
	* This method has not input or return values,
	* but retrieves the selected order from its XML file and loads into the DataManager active order
	* The new active order is not yet loaded to the gui
	* @throws IOException when no XML orders are available in source folder
	*/
	protected boolean popUpRead(String tag) {
	      File folder = new File(DataManager.get_xmlReadWrite().getFolderLoc() + "\\saveFiles");
	      String[] listOfFilesNames = filterFiles(folder.listFiles(), tag);
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
		    	  if (tag == "OID") {
		    		  dm.readOrder((String)comboBox.getSelectedItem());
		    	  } else {
		    		  dm.readEmployee((String)comboBox.getSelectedItem());
		    	  }
				  
				  return true;
		      }
	      } catch (IOException err) {
	    	  err.printStackTrace();
	    	  JLabel msg = new JLabel("No " + tag + " files are available");
	    	  JOptionPane.showMessageDialog(null, msg, "No " + tag, JOptionPane.DEFAULT_OPTION);
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
