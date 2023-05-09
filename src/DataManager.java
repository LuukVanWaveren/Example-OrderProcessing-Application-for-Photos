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
	private Employee _ActiveEmployee;
	private IdTracker _idTracker;
	private OrderManager _orderManager;
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
		
		_orderManager = new OrderManager(true);
		load_orderManager();
		
		_ActiveEmployee = new Employee(false);
	}

	public OrderManager get_orderManager() {
		return _orderManager;
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
	
	public void saveOrderComplete() throws Exception {
		saveOrder();
		saveIdTracker();
		saveOrderManager();
	}
	
	public void saveOrder() throws Exception {
		try {
			if (_ActiveEmployee.get_employeeID().get_idNumber() > 0) {
				_xmlReadWrite.genXmlFromClass(_ActiveOrder, "\\" + _ActiveOrder.get_orderID() + ".xml");
			} else {
				throw new Exception("No valid Employee active");
			}
			
			//_idTracker.set_lastUniqueEmployeeID(_ActiveOrder.get_employee().get_employeeID());
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
	
	public void saveOrderManager() {
		try {
			_xmlReadWrite.genXmlFromClass(_orderManager, "\\orderManager.xml");
		} catch (NullPointerException npe) {
			System.out.println(npe.getMessage());
		}
	}
	
	public void saveEmployeeComplete() {
		saveEmployee();
		saveIdTracker();
	}
	
	public void saveEmployee() {
		try {
			_xmlReadWrite.genXmlFromClass(get_ActiveEmployee(), "\\" + get_ActiveEmployee().get_employeeID() + ".xml");
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

	public Employee get_ActiveEmployee() {
		return _ActiveEmployee;
	}

	public void set_ActiveEmployee(Employee _ActiveEmployee) {
		this._ActiveEmployee = _ActiveEmployee;
	}
	
	public void readOrder(String orderID) {
		try {
			_ActiveOrder = (Order) _xmlReadWrite.genClassFromXml("\\" + orderID + ".xml");
			_ActiveOrder.updateFields();
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}
	}
	
	public void readEmployee(String employeeID) {
		try {
			_ActiveEmployee = (Employee) _xmlReadWrite.genClassFromXml("\\" + employeeID + ".xml");
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
	
	public void readOrderManager() {
		try {
			this._orderManager = (OrderManager) _xmlReadWrite.genClassFromXml("\\orderManager.xml");
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
	
	private void load_orderManager() {
		if (!_xmlReadWrite.chkDir("\\saveFiles\\orderManager.xml")) {
			saveOrderManager();
		} else {
			readOrderManager();
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
