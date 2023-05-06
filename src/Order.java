import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Order {
	
	private DataManager _dm = DataManager.getInstance();
	
	private IdOrder _orderID;
	private Customer _customer;
	private Employee _employee;
	private ArrayList<Product> _products;
	private double _totalCost;
	
	private Timestamp _curTime;
	//private LocalDateTime _delivTime;
	private Duration _maxProdTime;
	private DateTimeFormatter _timeDTF = DateTimeFormatter.ofPattern("dd-M-yyyy HH:mm");
	
	private Object[][] _openHours = _dm.get_openHours();
	private OrderSummary _orderSummary;

	public Order() {
	}
	
	Order(boolean genId) {
		if (genId) {
			_orderID = new IdOrder(true);
		}
		_employee = new Employee(true);
		_customer = new Customer();
		
		_products = new ArrayList<Product>();
		_curTime = new Timestamp(System.currentTimeMillis());
		_maxProdTime = Duration.ZERO;
		_orderSummary = new OrderSummary(_orderID, _curTime, _maxProdTime);
		updateFields();
	}
	
	public OrderSummary get_orderSummary() {
		return _orderSummary;
	}

	public void set_orderSummary(OrderSummary _orderSummary) {
		this._orderSummary = _orderSummary;
	}

	private void calcMaxProdTime() {
		_maxProdTime = Duration.ZERO;
		//Duration prodTime;
		for (int i = 0; i < _products.size(); i++) {
			_maxProdTime = _maxProdTime.plus(_products.get(i).get_ProdTime().multipliedBy(_products.get(i).get_amount()));
		}
	}
	
	private LocalDateTime calcDelivTime() throws StuckInLoopException {
		Duration prodTimeRemaining = _dm.get_orderManager().calcProdTimeTillOrder(_orderSummary);
		//Duration prodTimeRemaining = Duration.ZERO;
		OpenHoursOnDay openHoursOnDay = new OpenHoursOnDay(_openHours, _curTime.toLocalDateTime());
		Duration timeAvailableOnDay =  openHoursOnDay.getRemainingTimeOnDay();
		
		int j=0;
		while (!prodTimeRemaining.isNegative()) {
			if(!timeAvailableOnDay.isNegative()) {
				/* if the production time remaining is not at least 1 hour more than the duration
				 * available on a day, the delivery day postpones to the next day so customer has
				 * at least 1 hour to pick up the order before closing */
				if (timeAvailableOnDay.minusHours(1).compareTo(prodTimeRemaining) < 0) {
					prodTimeRemaining = prodTimeRemaining.minus(timeAvailableOnDay);
				} else {
					openHoursOnDay.addDurationToLdt(prodTimeRemaining);
					break;
				}
			}
			openHoursOnDay.setLtdToOpenTimeNextDay();
			j++;
			if (j > 100) {
				throw new StuckInLoopException("Program is stuck in loop after too many tries. " + j + " iterations executed");
			}
		}
		return openHoursOnDay.getLdt();
	}
	
	public double get_totalCost() {
		return _totalCost;
	}

	public void set_totalCost(double _totalCost) {
		this._totalCost = _totalCost;
	}
	
	public String get_delivTimeFormatted() {
		try {
			LocalDateTime delivTime = calcDelivTime();
			
			if (delivTime != null) {
				return delivTime.format(_timeDTF);
			}
		} catch (StuckInLoopException e) {
			e.printStackTrace();
			JLabel msg = new JLabel("Delivery time was not able to compute, check if opening hours are valid");
        	JOptionPane.showMessageDialog(null, msg, "Invalid opening hours", JOptionPane.DEFAULT_OPTION);
		}
		return "";
	}
	
	public String get_maxProdTimeFormatted() {
		if (_maxProdTime != null) {
			long s = _maxProdTime.getSeconds();
			return String.format("%d:%02d", s / 3600, (s % 3600) / 60);
		}
		return "";
	}

	private void calcTotalPrice() {
		_totalCost=0;
		for (int i = 0; i < _products.size(); i++) {
			_totalCost += _products.get(i).get_price() * _products.get(i).get_amount();
		}
	}
	
	public String get_curTimeFormatted() {
		return new SimpleDateFormat("dd-M-yyyy HH:mm").format(_curTime);
	}

	public void addProdToOrder(Product newProduct) {
		_products.add(newProduct);
		updateFields();
	}
	
	public void removeProdFromOrder(int OrderLine_ID) {
		_products.remove(OrderLine_ID);
		updateFields();
	}
	
	public void updateFields() {
		calcMaxProdTime();
		calcTotalPrice();
		updateOrderSummary();
	}
	
	private void updateOrderSummary() {
		_orderSummary.set_maxProdTime(_maxProdTime.getSeconds());
		_dm.get_orderManager().updateOrderSummary(_orderSummary);
	}
	
	public Timestamp get_curTime() {
		return _curTime;
	}

	public void set_curTime(Timestamp _curTime) {
		this._curTime = _curTime;
	}

	public IdOrder get_orderID() {
		return _orderID;
	}

	public void set_orderID(IdOrder _orderID) {
		this._orderID = _orderID;
	}

	public ArrayList<Product> getProducts() {
		return _products;
	}

	public void setProducts(ArrayList<Product> products) {
		_products = products;
	}

	public Customer get_customer() {
		return _customer;
	}

	public void set_customer(Customer customer) {
		this._customer = customer;
	}

	public Employee get_employee() {
		return _employee;
	}

	public void set_employee(Employee employee) {
		this._employee = employee;
	}
}

class StuckInLoopException extends Exception {
	private static final long serialVersionUID = 1L;

	public StuckInLoopException(String errorMessage) {
        super(errorMessage);
    }
}

class OpenHoursOnDay {
	private LocalDateTime _ldt;
	private LocalTime _open;
	private LocalTime _closed;
	private int _idxDay;
	private Object[][] _openHours;
	private Duration _remainingTimeOnDay;
	
	OpenHoursOnDay(Object[][] openHours, LocalDateTime ldt) throws StuckInLoopException{
		this._openHours = openHours;
		this._ldt = ldt;
		this.updateDayIndex();
		this.updateOpenTimes();
		
		this.updateLtd();
		this.updateDayIndex();
		this.updateOpenTimes();
		this.updateRemainingTimeOnDay();
	}
	
	private void updateRemainingTimeOnDay() {
		this._remainingTimeOnDay = Duration.between(this._ldt.toLocalTime(), this._closed);
	}
	
	private void updateLtd() throws StuckInLoopException {
		if (this._ldt.toLocalTime().isBefore(this._open)) {
			this.setLtdToOpenTime();
		} else if (this._ldt.toLocalTime().isAfter(this._closed)) {
			this.setLtdToOpenTimeNextDay();
		}
	}
	
	private void updateOpenTimes(){
		this._open = (LocalTime) _openHours[this._idxDay][1];
		this._closed = (LocalTime) _openHours[this._idxDay][2];
	}
	
	public void updateDayIndex() {
		for (int i = 0; i < _openHours.length; i++) {
			if ((DayOfWeek) _openHours[i][0] == _ldt.getDayOfWeek()) {
				this._idxDay = i;
				return;
			}
		}
		this._idxDay = -1;
	}
	
	public void setLtdToOpenTime() {
		this._ldt = this._ldt.toLocalDate().atTime(this._open.getHour(), this._open.getMinute());
		this.updateRemainingTimeOnDay();
	}
	
	public void setLtdToOpenTimeNextDay() throws StuckInLoopException {
		int k=0;
	
		do {
			this._ldt = this._ldt.plusDays(1);
			this.updateDayIndex();
			this.updateOpenTimes();
			k++;
			
			if (k > 7) {
				throw new StuckInLoopException("Program is stuck in loop after too many tries. " + k + " iterations executed");
			}
		} while(this._open == this._closed);
		this.setLtdToOpenTime();
	}
	
	public Duration getRemainingTimeOnDay() {
		return this._remainingTimeOnDay;
	}
	
	public void addDurationToLdt(Duration duration) throws StuckInLoopException {
		this._ldt = this._ldt.plus(duration);
		this.updateLtd();
		this.updateDayIndex();
		this.updateOpenTimes();
		this.updateRemainingTimeOnDay();
	}

	public LocalDateTime getLdt() {
		return this._ldt;
	}

	public void setLdt(LocalDateTime ldt) {
		this._ldt = ldt;
		this.updateOpenTimes();
	}

	public LocalTime getOpen() {
		return this._open;
	}

	public LocalTime getClosed() {
		return this._closed;
	}

	public int getIdxDay() {
		return this._idxDay;
	}

	public void setOpenHours(Object[][] openHours) {
		this._openHours = openHours;
	}
}
