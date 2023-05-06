public class Employee extends Person {

	private IdEmployee _employeeID;
	
	public Employee(){
		super();
	}
	
	public Employee(boolean genId){
		this();
		if (genId) {
			_employeeID = new IdEmployee(true);
		}
	}

	public IdEmployee get_employeeID() {
		return _employeeID;
	}

	public void set_employeeID(IdEmployee _employeeID) {
		this._employeeID = _employeeID;
	}
}
