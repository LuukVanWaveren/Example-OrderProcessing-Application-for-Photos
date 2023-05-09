
public class IdOrder extends BaseId {
	
	public IdOrder() {
		super();
		_idTag = "OID";
		_idNumberLength = 6;
	}
	
	public IdOrder(boolean genId) {
		this();
		if (genId) {
			genId();
		}
	}

	@Override
	protected void genId() {
		_idNumber = _it.get_lastUniqueOrderID();
		_idNumber++;
	}
	
	@Override
	protected void updateIdTracker() {
		if (_idNumber > _it.get_lastUniqueOrderID()) {
			_it.set_lastUniqueOrderID(_idNumber);
		}
	}
}
