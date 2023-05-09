import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OpenHoursPanel extends SubPanel {

	private static final long serialVersionUID = 1L;
	
	JLabel header;
	float[] columnWidthPercentage = {0.4f, 0.15f, 0.15f};
	
	public OpenHoursPanel(GuiMain gm) {
		super(gm);
		header = createHeader("OPENING HOURS", 18);
		
		this.add(header, gbcHeader);
		
		this.setMinimumSize(new Dimension(320,300));
		this.setPreferredSize(new Dimension(320,300));
		
	    String[] columnNames2 = {"Day","Open", "Closed"}; //column names for tableOpenHours
	    
	    // construct table model with default table settings for tableOpenHours
	    DefaultTableModel model = new DefaultTableModel(dm.get_openHours(), columnNames2) {
		    /**
			 * make all table lines un-editable
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		
		//create table that shows opening hours of store
		JTable tableOpenHours = new JTable(model);
		tableOpenHours.setFocusable(false);
		tableOpenHours.setRowSelectionAllowed(false);
		tableOpenHours.setForeground(Color.GRAY);
		tableOpenHours.setOpaque(false);
	    tableOpenHours.setShowGrid(false);
	    tableOpenHours.setShowHorizontalLines(true);
	    tableOpenHours.setRowHeight(32);
	    tableOpenHours.setFont(new Font("Courier", Font.PLAIN,14));
	    ((DefaultTableCellRenderer)tableOpenHours.getDefaultRenderer(Object.class)).setOpaque(false);
	    
	    this.add(tableOpenHours, gbcDefaultAssets.EditGbc(0, 1));
		
		
		// Settings affecting multiple assets
	    addComponentListener(new ComponentAdapter() {
	        @Override
	        public void componentResized(ComponentEvent e) {
	        	resizeColumns(tableOpenHours, columnWidthPercentage);
	        }
	    });
	}

}
