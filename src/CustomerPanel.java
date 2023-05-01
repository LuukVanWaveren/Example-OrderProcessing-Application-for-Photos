import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CustomerPanel extends SubPanel {

	private static final long serialVersionUID = 1L;
	JComponent[][] customerComponents;
	JLabel header;
	
	public CustomerPanel(GuiMain gm) {
		super(gm);
		header = createHeader("CUSTOMER", 18);
		
		
		String[] customerFieldNames = {						//customer field names for JLabels.text
				"Name","Adress","Postal code", "Place",
				"E-mail adress", "Mobile number"};
		customerComponents= new JComponent[6][2];
		for (int i = 0; i < customerComponents.length; i++) {
			
			//configure JLabels for customer field names
			customerComponents[i][0] = new JLabel(customerFieldNames[i]);
			customerComponents[i][0].setForeground(Color.WHITE);
			customerComponents[i][0].setFont(new Font("Courier", Font.PLAIN,14));
			
			//configure JLabels for customer field values
			customerComponents[i][1] = new JTextField("");
			customerComponents[i][1].setBorder(null);
			customerComponents[i][1].setForeground(Color.CYAN);
			customerComponents[i][1].setBackground(gm.colorButton);
			customerComponents[i][1].setFont(new Font("Courier", Font.PLAIN,14));
		}
		
		JPanel plFiller = new JPanel();	//creates flexible space filler in Customer sub panel
		plFiller.setOpaque(false);
		
		
		/* Add assets to Customer panel */
		this.add(header, gbcHeader.EditGbc2(0, 0, 2, 1));
		this.add(customerComponents[0][0], gbcDefaultAssets.EditGbc(0, 1));
		this.add(customerComponents[0][1], gbcDefaultAssets.EditGbc(1, 1));
		this.add(customerComponents[1][0], gbcDefaultAssets.EditGbc(0, 2));
		this.add(customerComponents[1][1], gbcDefaultAssets.EditGbc(1, 2));
		this.add(customerComponents[2][0], gbcDefaultAssets.EditGbc(0, 3));
		this.add(customerComponents[2][1], gbcDefaultAssets.EditGbc(1, 3));
		this.add(customerComponents[3][0], gbcDefaultAssets.EditGbc(0, 4));
		this.add(customerComponents[3][1], gbcDefaultAssets.EditGbc(1, 4));
		this.add(customerComponents[4][0], gbcDefaultAssets.EditGbc(0, 5));
		this.add(customerComponents[4][1], gbcDefaultAssets.EditGbc(1, 5));
		this.add(customerComponents[5][0], gbcDefaultAssets.EditGbc(0, 6));
		this.add(customerComponents[5][1], gbcDefaultAssets.EditGbc(1, 6));
		this.add(plFiller,gbcDefaultAssetsFiller.EditGbc(0, 7));
	}

	/**
	* clear all gui field values from the customer sub panels
	* <p>
	* This method has no input or return values,
	* but sets all customer JLabel texts to an empty string.
	*/
	public void guiClear() {
		for (int i = 0; i < customerComponents.length; i++) {
			((JTextField) customerComponents[i][1]).setText("");
		}
	}
	
	/**
	* sets all person and customer gui field values according to customer and employee instances from the active order
	* <p>
	* This method has no input or return values,
	* but sets all customer and employee JLabel texts to the values available 
	* in the customer and employee values in the active order instance.
	*/
	public void guiLoadFromOrder() {
		Customer customer = dm.get_ActiveOrder().get_customer();
		((JTextField)customerComponents[0][1]).setText(customer.get_Name());
		((JTextField)customerComponents[1][1]).setText(customer.get_adress());
		((JTextField)customerComponents[2][1]).setText(customer.get_postal());
		((JTextField)customerComponents[3][1]).setText(customer.get_place());
		((JTextField)customerComponents[4][1]).setText(customer.get_eMail());
		((JTextField)customerComponents[5][1]).setText(customer.get_mobileNumber());
	}
	
	/**
	* Get all customer and employee fields filled in on the gui
	* and load them into the active order
	* <p>
	* This method has not input or return values,
	* but retrieves and alters the customer and employee instance
	* from the active order instead
	*/
	public void writeFieldsToOrder() {
		Customer customer = dm.get_ActiveOrder().get_customer();
		
		customer.set_Name(((JTextField)customerComponents[0][1]).getText());
		customer.set_adress(((JTextField)customerComponents[1][1]).getText());
		customer.set_postal(((JTextField)customerComponents[2][1]).getText());
		customer.set_place(((JTextField)customerComponents[3][1]).getText());
		customer.set_eMail(((JTextField)customerComponents[4][1]).getText());
		customer.set_mobileNumber(((JTextField)customerComponents[5][1]).getText());
	}
}
