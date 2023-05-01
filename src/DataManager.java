import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DataManager{

	private static DataManager _dM;
	private static FileReadWrite _fmProducts;
	private static FileReadWrite _fmOpenHours;
	private static FileReadWrite _xmlReadWrite;
	private Order _ActiveOrder;
	private IdTracker _idTracker;
	private ArrayList<Product> _availProd;
	private Object[][] _openHours;

	private DataManager() {
	}
	
	private void delayedConstructor() {
		_xmlReadWrite = new FileReadWrite("\\src");
		
		_fmProducts = new FileReadWrite("\\src");
		_fmProducts.readCSVFile("\\PhotoShop_PriceList.csv", ";");
		
		_availProd = availableProducts(_fmProducts.getReadCsvValues());
		
		_fmOpenHours = new FileReadWrite("\\src");
		_fmOpenHours.readCSVFile("\\PhotoShop_OpeningHours.csv", ";");
		
		_openHours = getOpenHours(_fmOpenHours.getReadCsvValues());
		
		_idTracker = new IdTracker();
		load_idTracker();
		
		_ActiveOrder = new Order(true);
	}


	public Object[][] get_openHours() {
		return _openHours;
	}

	public static FileReadWrite get_xmlReadWrite() {
		return _xmlReadWrite;
	}

	public static DataManager getInstance() {
		if (_dM == null) {
			_dM = new DataManager();
			_dM.delayedConstructor();
		}
		return _dM;
	}
	
	public ArrayList<String[]> getProducts() {
		return _fmProducts.getReadCsvValues();
	}
	
	public ArrayList<String[]> getOpenHours() {
		return _fmOpenHours.getReadCsvValues();
	}
	
	public void saveOrder() {
		try {
			_xmlReadWrite.genXmlFromClass(get_ActiveOrder(), "\\" + get_ActiveOrder().get_OrderID() + ".xml");
			_idTracker.set_lastUniqueOrderID(_ActiveOrder.get_OrderID());
			_idTracker.set_lastUniqueEmployeeID(_ActiveOrder.get_employee().get_employeeID());
			saveIdTracker();
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		}
	}
	
	public void saveIdTracker() {
		try {
			_xmlReadWrite.genXmlFromClass(_idTracker, "\\IdTracker.xml");
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		}
	}
	
	public Order get_ActiveOrder() {
		return _ActiveOrder;
	}

	public void set_ActiveOrder(Order ActiveOrder) {
		_ActiveOrder = ActiveOrder;
	}

	public void readOrder(String orderID) {
		try {
			_ActiveOrder = (Order) _xmlReadWrite.genClassFromXml("\\" + orderID + ".xml");
			_ActiveOrder.updateFields();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	public void readIdTracker() {
		try {
			this._idTracker = (IdTracker) _xmlReadWrite.genClassFromXml("\\IdTracker.xml");
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}

	public IdTracker get_idTracker() {
		return _idTracker;
	}

	private void load_idTracker() {
		if (!_xmlReadWrite.chkDir("\\saveFiles\\IdTracker.xml")) {
			saveIdTracker();
		} else {
			readIdTracker();
		}
	}
	
	private ArrayList<Product> availableProducts(ArrayList<String[]> readCsvValues) {
		ArrayList<Product> products = new ArrayList<Product>();
		for (int i = 0; i < readCsvValues.size(); i++) {
			products.add(new Product(i));
		}
		return products;
	}
	
	private Object[][] getOpenHours(ArrayList<String[]> readCsvValues){
		Object[][] openHours = new Object[7][3];
		int i = 1;
		DateTimeFormatter timeDTF = DateTimeFormatter.ofPattern("HH:mm");
		for (DayOfWeek day : DayOfWeek.values()) {
		    openHours[i][0] = day;
		    openHours[i][1] = LocalTime.parse(readCsvValues.get(i+1)[2], timeDTF);
		    openHours[i][2] = LocalTime.parse(readCsvValues.get(i+1)[3], timeDTF);
		    i++;
		    if (i == 7) {i=0;}
		}
		return openHours;
	}

	public ArrayList<Product> getAvailProd() {
		return _availProd;
	}
}
