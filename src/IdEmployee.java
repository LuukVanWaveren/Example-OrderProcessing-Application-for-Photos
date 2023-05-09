
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
		} else {
			_idNumber = -1;
		}
	}

	@Override
	protected void genId() {
		_idNumber = _it.get_lastUniqueEmployeeID();
		_idNumber++;
	}
	
	@Override
	protected void updateIdTracker() {
		if (_idNumber > _it.get_lastUniqueEmployeeID()) {
			_it.set_lastUniqueEmployeeID(_idNumber);
		}
	}
}
