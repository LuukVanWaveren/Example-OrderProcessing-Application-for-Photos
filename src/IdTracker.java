public class IdTracker {
	private int _lastUniqueEmployeeID;
	private int _lastUniqueOrderID;
	
	public IdTracker(){
		_lastUniqueEmployeeID = 0;
		_lastUniqueOrderID = 0;
	}

	public int get_lastUniqueEmployeeID() {
		return _lastUniqueEmployeeID;
	}

	public void set_lastUniqueEmployeeID(int _lastUniqueEmployeeID) {
		this._lastUniqueEmployeeID = _lastUniqueEmployeeID;
	}

	public int get_lastUniqueOrderID() {
		return _lastUniqueOrderID;
	}

	public void set_lastUniqueOrderID(int _lastUniqueOrderID) {
		this._lastUniqueOrderID = _lastUniqueOrderID;
	}
}
