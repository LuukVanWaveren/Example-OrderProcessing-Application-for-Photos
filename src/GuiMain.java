import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GuiMain extends JFrame {

	CustomerPanel customerPanel;
	EmployeePanel employeePanel;
	OpenHoursPanel openHoursPanel;
	OrderPanel orderPanel;
	ResultPanel resultPanel;
	
	// Colors used for gui assets
	Color colorBack = new Color(27, 31, 56);
	Color colorPanel = new Color(44, 48, 74);
	Color colorButton = new Color(72, 78, 120);
	
	CustomGbc gbcPanelMain = new CustomGbc(new Insets(8,8,8,8), 0.5, 0);
	CustomGbc gbcPanelTop = new CustomGbc(new Insets(8,8,0,8));
	CustomGbc gbcPanelBotLeft = new CustomGbc(new Insets(8,8,8,8), 0.5, 1);
	CustomGbc gbcPanelBotRight = new CustomGbc(new Insets(8,8,8,8), 0, 1);
	
	GuiMain(){
		StartMainWindow();
		
		JPanel upperPanel = CreatePrimaryPanel();
		JPanel lowerPanel = CreatePrimaryPanel();
		
		customerPanel = new CustomerPanel(this);
		employeePanel = new EmployeePanel(this);
		openHoursPanel = new OpenHoursPanel(this);
		orderPanel = new OrderPanel(this);
		resultPanel = new ResultPanel(this);
		
		
		upperPanel.add(customerPanel, gbcPanelTop.EditGbc(0, 0, 0.5, 1));
		upperPanel.add(employeePanel, gbcPanelTop.EditGbc(1, 0, 0.5, 0));
		upperPanel.add(openHoursPanel, gbcPanelTop.EditGbc(2, 0));
		lowerPanel.add(orderPanel, gbcPanelBotLeft);
		lowerPanel.add(resultPanel, gbcPanelBotRight.EditGbc(1, 0));
		
		this.add(upperPanel, gbcPanelMain);
		this.add(lowerPanel, gbcPanelMain.EditGbc(0, 1, 0.5, 1));
		
	    /* Final settings for startup */

		resultPanel.loadNewOrder();
		
		validate();
		
		//resizeColumns(tableOrder, columnWidthPercentage);
		//resizeColumns(tableOpenHours, columnWidthPercentage2);
		
	}
	
	private void StartMainWindow() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setMinimumSize(new Dimension(1200, 900));
		this.getContentPane().setBackground(colorBack);
		this.setVisible(true);
	}
	
	private JPanel CreatePrimaryPanel() {
		JPanel plMain = new JPanel();
		plMain.setLayout(new GridBagLayout());
		plMain.setOpaque(false);
		return plMain;
	}

}