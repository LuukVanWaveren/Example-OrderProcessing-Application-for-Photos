
import java.time.Duration;
import java.time.LocalDateTime;

public class Product{
	
	private int _Product_ID;
	private int _Photo_ID;
	private String _Photo_type;
	private double _price;
	private int _amount;
	private Duration _prodTime;
	
	private DataManager _dm = DataManager.getInstance();
	
	public Product(){
		this(0);
	}
	
	Product(int product_ID) {
		_amount = 1;
		set_Product_ID(product_ID);
	}
	
	@Override
	public String toString() {
		return _Photo_type;
	}
	
	private void retrieveProdData() {
		String prodTimeStr = _dm.getProducts().get(_Product_ID)[3];
		_prodTime = Duration.ofSeconds(0);
		_prodTime = _prodTime.plusHours(Integer.parseInt(prodTimeStr.substring(0, 2)));
		_prodTime = _prodTime.plusMinutes(Integer.parseInt(prodTimeStr.substring(3, 5)));
		
		_Photo_ID = Integer.parseInt(_dm.getProducts().get(_Product_ID)[0]);
		_Photo_type = _dm.getProducts().get(_Product_ID)[1];
		_price = Double.parseDouble(_dm.getProducts().get(_Product_ID)[2]);
	}
	
	public int get_amount() {
		return _amount;
	}

	public void set_amount(int _amount) {
		this._amount = _amount;
	}
	
	public double get_price() {
		return _price;
	}

	public void set_price(double price) {
		_price = price;
	}

	public Duration get_ProdTime() {
		return _prodTime;
	}
	
	public String get_ProdTimeFormatted() {
		return String.format("%02d:%02d", _prodTime.getSeconds()/3600, _prodTime.getSeconds()%3600/60);
	}
	
	public static LocalDateTime addTime(LocalDateTime newTime, Duration diffPeriod) {
		return newTime.plusSeconds(diffPeriod.getSeconds());
	}

	public int get_Product_ID() {
		return _Product_ID;
	}

	public void set_Product_ID(int product_ID) {
		_Product_ID = product_ID;
		retrieveProdData();
	}

	public int get_Photo_ID() {
		return _Photo_ID;
	}

	public void set_Photo_ID(int photo_ID) {
		_Photo_ID = photo_ID;
	}

	public String get_Photo_type() {
		return _Photo_type;
	}

	public void set_Photo_type(String photo_type) {
		_Photo_type = photo_type;
	}
}
