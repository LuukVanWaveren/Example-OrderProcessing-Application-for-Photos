
import java.sql.Timestamp;
import java.time.Duration;


public class OrderSummary {
	
	private IdOrder _orderID;
	private Timestamp _curTime;
	private Long _maxProdTime;

	public OrderSummary(){
	}
	
	OrderSummary(IdOrder orderID, Timestamp curTime, Duration maxProdTime){
		_orderID = orderID;
		_curTime = curTime;
		_maxProdTime = maxProdTime.getSeconds();
	}
	
	
    public IdOrder get_orderID() {
		return _orderID;
	}

	public void set_orderID(IdOrder orderID) {
		this._orderID = orderID;
	}

	public Timestamp get_curTime() {
		return _curTime;
	}

	public void set_curTime(Timestamp curTime) {
		this._curTime = curTime;
	}

	public Long get_maxProdTime() {
		return _maxProdTime;
	}

	public void set_maxProdTime(Long maxProdTime) {
		this._maxProdTime = maxProdTime;
	}
}
