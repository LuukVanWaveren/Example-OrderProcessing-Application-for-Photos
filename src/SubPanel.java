import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
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
}
