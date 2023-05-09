import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ResultPanel extends SubPanel {

	private static final long serialVersionUID = 1L;
	
	JLabel header;
	JPanel plFiller;
	
	JComponent[][] resultComponents;
	
	public ResultPanel(GuiMain gm) {
		super(gm);
		
		this.setOpaque(false);
		
		header = createHeader("RESULTS", 22);
		
		
		String[] resultValues = {"Date","Order ID",	//result field names for JLabels.text
				"Employee ID", "Production time", "Delivery date","Total cost"};
		resultComponents= new JComponent[6][2];
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
		
		Dimension dimBtnresult = new Dimension(120, 35);
		JButton btnGenPdf = createButton("Generate PDF", dimBtnresult);
		btnGenPdf.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) { 
					InvoiceGen IgGen = new InvoiceGen();
					try {
						saveOrder();
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
		JButton btnSaveOrder = createButton("Save order", dimBtnresult);
		btnSaveOrder.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  saveOrder();
				  loadNewOrder();
			  }
			} );
		JButton btnLoadOrder = createButton("Load order", dimBtnresult);
		btnLoadOrder.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  if (popUpRead("OID")) {
					  gm.customerPanel.guiLoadFromOrder();
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
		this.add(resultComponents[5][0], gbcDefaultAssets.EditGbc(0, 6));
		this.add(resultComponents[5][1], gbcDefaultAssets.EditGbc2(1, 6, 2, 1));
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
		((JLabel)resultComponents[1][1]).setText(dm.get_ActiveOrder().get_orderID().toString());
		((JLabel)resultComponents[2][1]).setText(dm.get_ActiveOrder().get_employee().get_employeeID().toString());
		((JLabel)resultComponents[3][1]).setText(dm.get_ActiveOrder().get_maxProdTimeFormatted());
		((JLabel)resultComponents[4][1]).setText(dm.get_ActiveOrder().get_delivTimeFormatted());
		((JLabel)resultComponents[5][1]).setText(String.format("%.2f",dm.get_ActiveOrder().get_totalCost()));
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
		gm.customerPanel.guiClear();
		gm.orderPanel.guiClearTable();
		guiLoadResultFields();
	}
	
	private void saveOrder() {
		  gm.customerPanel.writeFieldsToOrder();
		  dm.get_ActiveOrder().set_employee(dm.get_ActiveEmployee());
		  dm.get_ActiveOrder().get_orderID().updateIdTracker();
		  //dm.get_ActiveOrder().set_employee(dm.get_ActiveEmployee());
		  try {
			dm.saveOrderComplete();
		} catch (Exception err) {
        	err.printStackTrace();
        	JLabel msg = new JLabel("No valid employee active, Select a valid employee");
        	JOptionPane.showMessageDialog(null, msg, "No valid employee active", JOptionPane.DEFAULT_OPTION);
		}
	}
}
