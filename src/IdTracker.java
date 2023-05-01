public class IdTracker {
	private String _lastUniqueCustomerID;
	private String _lastUniqueEmployeeID;
	private String _lastUniqueOrderID;
	
	public IdTracker(){
		_lastUniqueCustomerID = "PCID00000";
		_lastUniqueEmployeeID = "PEID000";
		_lastUniqueOrderID = "OID000000";
	}
	
	public String genID(String idType) {
		if (idType.equals("Employee")) {
			return genUniqueNumber(_lastUniqueEmployeeID, 4, 3);
		} else if (idType.equals("Order")) {
			return genUniqueNumber(_lastUniqueOrderID, 3, 6);
		} else {
			System.out.println("no ID was changed");
			return "";
		}
	}
	
	private String genUniqueNumber(String ID, int idxSplit, int idWidth) {
		String idPrefix = ID.substring(0,idxSplit);
		int id = Integer.parseInt(ID.substring(idxSplit));
		id++;
		return String.format("%s%0" + idWidth + "d", idPrefix, id);
	}

	public String get_lastUniqueCustomerID() {
		return _lastUniqueCustomerID;
	}

	public void set_lastUniqueCustomerID(String _lastUniqueCustomerID) {
		this._lastUniqueCustomerID = _lastUniqueCustomerID;
	}

	public String get_lastUniqueEmployeeID() {
		return _lastUniqueEmployeeID;
	}

	public void set_lastUniqueEmployeeID(String _lastUniqueEmployeeID) {
		this._lastUniqueEmployeeID = _lastUniqueEmployeeID;
	}

	public String get_lastUniqueOrderID() {
		return _lastUniqueOrderID;
	}

	public void set_lastUniqueOrderID(String _lastUniqueOrderID) {
		this._lastUniqueOrderID = _lastUniqueOrderID;
	}
}
