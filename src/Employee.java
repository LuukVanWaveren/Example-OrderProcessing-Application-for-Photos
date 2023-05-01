public class Employee extends Person {

	private String _employeeID;
	
	private DataManager _dm = DataManager.getInstance();
	
	public Employee(){
	}
	
	public Employee(boolean genID){
		super();
		if (genID) {
			_employeeID = _dm.get_idTracker().genID("Employee");
		}
	}

	public String get_employeeID() {
		return _employeeID;
	}

	public void set_employeeID(String _employeeID) {
		this._employeeID = _employeeID;
	}

}
