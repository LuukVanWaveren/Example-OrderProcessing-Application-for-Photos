import java.awt.Color;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EmployeePanel extends SubPanel {
	private static final long serialVersionUID = 1L;
	
	JComponent[][] employeeComponents;
	JPanel plFiller;
	JLabel header;
	
	public EmployeePanel(GuiMain gm) {
		super(gm);
		header = createHeader("EMPLOYEE", 18);
		
		CreateEmployeeFields();
		
		//flexible space filler in Employee sub panel
		plFiller = new JPanel();	
		plFiller.setOpaque(false);

		AddGuiComponentsToPanel();
	}
	
	private void CreateEmployeeFields(){
		String[] employeeValueNames = {"Employee ID", "Name", "E-mail adress"};		//employee field names for JLabels.text
		employeeComponents = new JComponent[3][2];
		for (int i = 0; i < employeeComponents.length; i++) {
			employeeComponents[i] = CreateEmployeeField(employeeValueNames[i]);
		}
		
		//non universal JLabel employee settings
		employeeComponents[0][1].setOpaque(false);
		((JTextField) employeeComponents[0][1]).setEditable(false);
	}
	
	private JComponent[] CreateEmployeeField(String employeeValueName){
		//configure JLabels for employee field names
		JLabel employeeLabel = new JLabel(employeeValueName);
		employeeLabel.setForeground(Color.WHITE);
		employeeLabel.setFont(new Font("Courier", Font.PLAIN,14));
		
		//configure JLabels for employee field values
		JTextField employeeValue = new JTextField("");
		employeeValue.setBorder(null);
		employeeValue.setForeground(Color.CYAN);
		employeeValue.setBackground(gm.colorButton);
		employeeValue.setFont(new Font("Courier", Font.PLAIN,14));
		
		return new JComponent[] {employeeLabel, employeeValue};
	}
	
	private void AddGuiComponentsToPanel() {
		this.add(employeeComponents[0][0], gbcDefaultAssets.EditGbc(0, 1));
		this.add(employeeComponents[0][1], gbcDefaultAssets.EditGbc(1, 1));
		this.add(employeeComponents[1][0], gbcDefaultAssets.EditGbc(0, 2));
		this.add(employeeComponents[1][1], gbcDefaultAssets.EditGbc(1, 2));
		this.add(employeeComponents[2][0], gbcDefaultAssets.EditGbc(0, 3));
		this.add(employeeComponents[2][1], gbcDefaultAssets.EditGbc(1, 3));
		this.add(plFiller,gbcDefaultAssetsFiller.EditGbc(0, 4));
		this.add(header, gbcHeader.EditGbc2(0, 0, 2, 1));
	}
	
	/**
	* clear all gui field values from the employee sub panels
	* <p>
	* This method has no input or return values,
	* but sets all employee JLabel texts to an empty string.
	*/
	public void guiClear() {
		for (int i = 0; i < employeeComponents.length; i++) {
			((JTextField) employeeComponents[i][1]).setText("");
		}
	}
	
	/**
	* Get all employee and employee fields filled in on the gui
	* and load them into the active order
	* <p>
	* This method has not input or return values,
	* but retrieves and alters the employee and employee instance
	* from the active order instead
	*/
	public void writeFieldsToOrder() {
		Employee employee = dm.get_ActiveOrder().get_employee();

		employee.set_Name(((JTextField)employeeComponents[1][1]).getText());
		employee.set_eMail(((JTextField)employeeComponents[2][1]).getText());
	}
	
	public void guiLoadFromOrder() {
		Employee employee = dm.get_ActiveOrder().get_employee();
		((JTextField)employeeComponents[0][1]).setText(employee.get_employeeID().toString());
		((JTextField)employeeComponents[1][1]).setText(employee.get_Name());
		((JTextField)employeeComponents[2][1]).setText(employee.get_eMail());
	}
}