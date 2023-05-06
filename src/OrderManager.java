
import java.time.Duration;
import java.util.ArrayList;


public class OrderManager {

	private ArrayList<OrderSummary> _orderPriority;
	
	public ArrayList<OrderSummary> getOrderPriority() {
		return _orderPriority;
	}

	public void setOrderPriority(ArrayList<OrderSummary> orderPriority) {
		this._orderPriority = orderPriority;
	}

	public OrderManager() {
	}
	
	public OrderManager(boolean genNew) {
		_orderPriority = new ArrayList<OrderSummary>();
	}

	public void addOrderSummary(OrderSummary orderSummary) {
		_orderPriority.add(orderSummary);
		sortOrderPriority();
	}
	
	public void removeOrderSummary(OrderSummary orderSummary) {
		_orderPriority.remove(getIdxOfOrderSummary(orderSummary));
		sortOrderPriority();
	}
	
	public void updateOrderSummary(OrderSummary orderSummary) {
		int idx = getIdxOfOrderSummary(orderSummary);
		if (idx < 0) {
			addOrderSummary(orderSummary);
		} else {
			_orderPriority.set(idx, orderSummary);
			//sortOrderPriority();
		}	
	}
	
	public void sortOrderPriority() {
		//Collections.sort(_orderPriority, new OrderSummary.CurTimeComparator());
	}
	
	private int getIdxOfOrderSummary(OrderSummary orderSummary) {
		for ( int i = 0 ; i < _orderPriority.size(); i++) {
			if (_orderPriority.get(i).get_orderID().toString().equals( orderSummary.get_orderID().toString())) {
				return i;
			}
		}
		return -1;
	}
	
	private void showOrderPriority() {
		for ( int i = 0 ; i < _orderPriority.size(); i++) {
			System.out.format(_orderPriority.get(i).get_orderID() + ", %d\n", _orderPriority.get(i).get_maxProdTime());
		}

	}
	
	public Duration calcProdTimeTillOrder(OrderSummary orderSummary) {
		Duration ProdTime = Duration.ZERO;
		int idx = getIdxOfOrderSummary(orderSummary);
		for (int i = 0; i <= idx; i++) {
			ProdTime = ProdTime.plus(Duration.ofSeconds( _orderPriority.get(i).get_maxProdTime()));
		}
		return ProdTime;
	}
}