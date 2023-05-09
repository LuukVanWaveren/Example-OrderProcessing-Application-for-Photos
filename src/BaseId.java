
public class BaseId {

	protected IdTracker _it;
	
	protected String _idTag;
	protected int _idNumber;
	protected int _idNumberLength;
	
	public BaseId() {
		_it = DataManager.getInstance().get_idTracker();
	}
	
	protected void genId() {
	}
	
	protected void updateIdTracker() {
	}

    @Override
    public String toString() {
    	if (_idNumber > 0) {
    		return String.format("%s%0" + _idNumberLength + "d", _idTag, _idNumber);
    	} else {
    		return String.format("%s%s", _idTag, "__");
    	}
    }

	public int get_idNumber() {
		return _idNumber;
	}

	public void set_idNumber(int _idNumber) {
		this._idNumber = _idNumber;
	}
}
