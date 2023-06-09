import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EmployeePanel extends SubPanel {
	private static final long serialVersionUID = 1L;
	
	JComponent[][] employeeComponents;
	JPanel plFiller;
	JLabel header;
	JButton btnSelectEmployee;
	JButton btnNewEmployee;
	JButton btnSaveEmployee;
	JPanel buttonPanel;
	
	public EmployeePanel(GuiMain gm) {
		super(gm);
		header = createHeader("EMPLOYEE", 18);
		
		CreateEmployeeFields();
		
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 8, 1));
		buttonPanel.setOpaque(false);

		
		btnSelectEmployee = CreateEmployeeSelectButton();
		btnNewEmployee = CreateEmployeeNewButton();
		btnSaveEmployee = CreateEmployeeSaveButton();
		btnSaveEmployee.setEnabled(false);
		
		AddButtonsToPanel();
		
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
		employeeValue.setOpaque(false);
		employeeValue.setEditable(false);
		
		return new JComponent[] {employeeLabel, employeeValue};
	}
	
	private JButton CreateEmployeeSelectButton() {
		JButton btn = createButton("Select employee", new Dimension(130, 35));
		btn.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  employeeFieldsEnabled(false);
				  if (popUpRead("PEID")) {
				  	guiLoadFromEmployee();
				  }
			  }
			} );
		return btn;
	}
	
	private JButton CreateEmployeeNewButton() {
		JButton btn = createButton("New employee", new Dimension(130, 35));
		btn.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  toggleEmployeeSaveButton();
				  dm.set_ActiveEmployee(new Employee(true));
				  guiLoadFromEmployee();
			  }
			} );
		return btn;
	}
	
	private JButton CreateEmployeeSaveButton() {
		JButton btn = createButton("Save employee", new Dimension(130, 35));
		btn.addActionListener(new ActionListener() { 
			  public void actionPerformed(ActionEvent e) {
				  toggleEmployeeSaveButton();
				  try {
					saveEmployee();
				  } catch (Exception err) {
					err.printStackTrace();
					JLabel msg = new JLabel("Not all fields are filled in. Enter all fields before saving.");
					JOptionPane.showMessageDialog(null, msg, "Not all fields are filled in", JOptionPane.DEFAULT_OPTION);
				  }
				  guiLoadFromEmployee();
			  }
			} );
		return btn;
	}

	private void AddGuiComponentsToPanel() {
		this.add(header, gbcHeader.EditGbc2(0, 0, 2, 1));
		this.add(employeeComponents[0][0], gbcDefaultAssets.EditGbc(0, 1));
		this.add(employeeComponents[0][1], gbcDefaultAssets.EditGbc(1, 1));
		this.add(employeeComponents[1][0], gbcDefaultAssets.EditGbc(0, 2));
		this.add(employeeComponents[1][1], gbcDefaultAssets.EditGbc(1, 2));
		this.add(employeeComponents[2][0], gbcDefaultAssets.EditGbc(0, 3));
		this.add(employeeComponents[2][1], gbcDefaultAssets.EditGbc(1, 3));
		this.add(plFiller,gbcDefaultAssetsFiller.EditGbc(0, 4));
		this.add(buttonPanel, gbcDefaultAssets.EditGbc2(0, 5, 2, 1));
	}
	
	private void AddButtonsToPanel() {
		buttonPanel.add(btnSelectEmployee);
		buttonPanel.add(btnNewEmployee);
		buttonPanel.add(btnSaveEmployee);
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
	 * @throws Exception 
	*/
	public void saveEmployee() throws Exception {
		Employee employee = dm.get_ActiveEmployee();
		String name = ((JTextField)employeeComponents[1][1]).getText();
		String email = ((JTextField)employeeComponents[2][1]).getText();
		if (name.trim().isEmpty() || email.trim().isEmpty()) {
			throw new Exception("Not all fields are filled");
		}
		employee.set_Name(name);
		employee.set_eMail(email);
		employee.get_employeeID().updateIdTracker();
		dm.saveEmployeeComplete();
	}
	
	public void guiLoadFromEmployee() {
		Employee employee = dm.get_ActiveEmployee();
		((JTextField)employeeComponents[0][1]).setText(employee.get_employeeID().toString());
		((JTextField)employeeComponents[1][1]).setText(employee.get_Name());
		((JTextField)employeeComponents[2][1]).setText(employee.get_eMail());
	}
	
	private void toggleEmployeeSaveButton() {
		if (btnNewEmployee.isEnabled()) {
			employeeFieldsEnabled(true);
		} else {
			employeeFieldsEnabled(false);
		}
	}
	
	private void employeeFieldsEnabled(boolean enabled) {
		btnNewEmployee.setEnabled(!enabled);
		btnSaveEmployee.setEnabled(enabled);
		employeeComponents[1][1].setOpaque(enabled);
		((JTextField) employeeComponents[1][1]).setEditable(enabled);
		employeeComponents[2][1].setOpaque(enabled);
		((JTextField) employeeComponents[2][1]).setEditable(enabled);
		employeeComponents[1][1].repaint();
		employeeComponents[2][1].repaint();
	}
}