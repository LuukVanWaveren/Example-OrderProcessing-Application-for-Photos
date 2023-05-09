import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OrderPanel extends SubPanel {

	private static final long serialVersionUID = 1L;

	float[] columnWidthPercentage = {0.05f, 0.55f, 0.1f, 0.1f, 0.03f};
	
	Product[] items;
	JLabel header;
	

	
	DefaultTableModel model;											//default table model for tableOrder
	JTable tableOrder;													//gui table with all items of order
	JComboBox<Product> cbxOrder;	
	
	public OrderPanel(GuiMain gm) {
		super(gm);
		
		header = createHeader("ORDER", 18);

		
		items = retrieveItems();
		
		//configure the order combobox
		cbxOrder = new JComboBox<Product>(items);
		cbxOrder.setFont(new Font("Courier", Font.PLAIN,16));
		
		Dimension dimBtnOrder = new Dimension(90, 35); //button size for all buttons in Order sub panel
		
		// create button for adding products to active order
		JButton btnOrderAdd = createButton("Add", dimBtnOrder);
		btnOrderAdd.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  addComboProdToOrder();
				  gm.resultPanel.guiLoadResultFields();
			  } 
			} );
		
		// create button for removing products from active order
		JButton btnOrderRemove = createButton("Remove", dimBtnOrder);
		btnOrderRemove.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  removeProdFromOrder();
				  gm.resultPanel.guiLoadResultFields();
			  }
			} );
		
		// create button for editing the amount of selected line in active order
		JButton btnOrderEdit = createButton("Edit", dimBtnOrder);
		btnOrderEdit.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
				  popUpEditAmount();
				  gm.resultPanel.guiLoadResultFields();
			  }
			} );
		
		// construct table model with default table settings for tableOrder
		model = new DefaultTableModel() {
		    /**
			 * make all table lines un-editable
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		//create table that shows all items in active order
		String[] columnNames = {"Photo ID","Photo type","Production time", "Price", "Amount"};
		for (int i = 0; i < columnNames.length; i++) {model.addColumn(columnNames[i]);};
		tableOrder = new JTable(model);
		tableOrder.setFocusable(false);
		tableOrder.setForeground(Color.GRAY);
		tableOrder.setOpaque(false);
	    tableOrder.setShowGrid(false);
	    tableOrder.setShowHorizontalLines(true);
	    tableOrder.setRowHeight(32);
	    tableOrder.setSelectionForeground(Color.CYAN);
	    tableOrder.setFont(new Font("Courier", Font.PLAIN,14));
	    ((DefaultTableCellRenderer)tableOrder.getDefaultRenderer(Object.class)).setOpaque(false);
	    
		this.add(header, gbcHeader.EditGbc2(0, 0, 4, 1));
		this.add(cbxOrder, gbcDefaultAssets.EditGbc(0, 1, 0.9, 0));
		this.add(btnOrderAdd, gbcDefaultAssets.EditGbc(1, 1));
		this.add(btnOrderRemove, gbcDefaultAssets.EditGbc(2, 1));
		this.add(btnOrderEdit, gbcDefaultAssets.EditGbc(3, 1));
		this.add(tableOrder, gbcDefaultAssets.EditGbc(0, 2, 0.5, 0.7, 4, 1));

		
		// Settings affecting multiple assets
	    addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	        	resizeColumns(tableOrder, columnWidthPercentage);
	        }
	    });
	}

	
	private Product[] retrieveItems() {
		ArrayList<Product> itemsAl = dm.getAvailProd(); //get all available products for the order combobox
		//convert itemsAl to Array "items"
		Product[] items = new Product[itemsAl.size()];
		items = itemsAl.toArray(items);
		return items;
	}
	
	/**
	* sets the gui order table to have all products that are part of the active order
	* <p>
	* This method has no input or return values,
	* but adds all products that are inside the active order to the gui order table one-by-one
	*/
	protected void guiLoadTable() {
		ArrayList<Product> orderProdAl = dm.get_ActiveOrder().getProducts();
		for (int i = 0; i < orderProdAl.size(); i++) {
			Product product = orderProdAl.get(i);
			addRowToTable(product);
		}
	}
	
	/**
	* clear the gui table in the Order sub panels
	* <p>
	* This method has no input or return values,
	* but sets the gui order table to be empty without rows.
	*/
	protected void guiClearTable() {
		((DefaultTableModel)tableOrder.getModel()).setNumRows(0);
	}
	
	/**
	* Show a popup window asking for input to change the amount of the seleceted order line
	* <p>
	* This method has not input or return values,
	* but retrieves and alters the products inside the active order.
	* The altered product is also loaded to the gui table
	*/
	private void popUpEditAmount() {
	    String input=null;
	    int inputInt = 0;
	    int idx=tableOrder.getSelectedRow();
	    
	    boolean invalidInput;
	    do{
	    	invalidInput=false;
	        try{
	        	Product product = dm.get_ActiveOrder().getProducts().get(idx);
	            input = JOptionPane.showInputDialog("Enter new amount (1-99)");
	            if (input != null && (inputInt = Integer.parseInt(input)) < 100 && inputInt > 0) {
				    product.set_amount(inputInt);
				    dm.get_ActiveOrder().updateFields();
				    model.setValueAt(product.get_amount(), idx, 4);
	            }
	        }catch(NumberFormatException err){
	        	err.printStackTrace();
	        	JLabel msg = new JLabel("Input was invalid, please enter an integer from 1 to 99");
	        	JOptionPane.showMessageDialog(null, msg, "Invalid input", JOptionPane.DEFAULT_OPTION);
	        	invalidInput=true;
	        }catch(IndexOutOfBoundsException err) {
	        	err.printStackTrace();
	        	JLabel msg = new JLabel("No product in the order table was selected");
	        	JOptionPane.showMessageDialog(null, msg, "No selection", JOptionPane.DEFAULT_OPTION);
	        }
	    }while(invalidInput);
	}
	    
	/**
	* add a selected product to the order
	* <p>
	* This method has no input or return values,
	* but adds the selected product from the combobox to the active order and the gui table.
	*/
	private void addComboProdToOrder() {
		Product product = (Product) cbxOrder.getSelectedItem();
		product = new Product(product.get_Product_ID());
		dm.get_ActiveOrder().addProdToOrder(product);
		addRowToTable(product);
	}
	
	/**
	* add a product to the gui table
	* <p>
	* This method has no input or return values,
	* but adds a product to the end of the gui table.
	* @param product a product of type Product that needs to be added to the gui table
	*/
	private void addRowToTable(Product product) {
		model.addRow(new Object[] {product.get_Photo_ID(), product.get_Photo_type(),
				product.get_ProdTimeFormatted(), product.get_price(), product.get_amount()});
	}
	
	/**
	* Removes the product, selected in the gui table, from the active order
	* <p>
	* This method has not input or return values,
	* but removes a product from the active order and also from the gui table
	*/
	private void removeProdFromOrder() {
		int idxRemove = tableOrder.getSelectedRow();
		try {
			dm.get_ActiveOrder().removeProdFromOrder(idxRemove);
			model.removeRow(idxRemove);
	    }catch(IndexOutOfBoundsException err) {
	    	err.printStackTrace();
	    	JLabel msg = new JLabel("No product in the order table was selected");
	    	JOptionPane.showMessageDialog(null, msg, "No selection", JOptionPane.DEFAULT_OPTION);
	    }
	}
}
