
public class IdEmployee extends BaseId {

	public IdEmployee() {
		super();
		_idTag = "PEID";
		_idNumberLength = 3;
	}
	
	public IdEmployee(boolean genId) {
		this();
		if (genId) {
			genId();
		}
	}

	@Override
	protected void genId() {
		_idNumber = _it.get_lastUniqueEmployeeID();
		_idNumber++;
		_it.set_lastUniqueEmployeeID(_idNumber);
	}
}
