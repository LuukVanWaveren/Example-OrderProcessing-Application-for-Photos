
public class Customer extends Person {
	private String _adress;
	private String _postal;
	private String _place;
	private String _mobileNumber;
	
	public Customer(){
		super();
	}
	
	public String get_adress() {
		return _adress;
	}

	public void set_adress(String _adress) {
		this._adress = _adress;
	}

	public String get_postal() {
		return _postal;
	}

	public void set_postal(String _postal) {
		this._postal = _postal;
	}

	public String get_place() {
		return _place;
	}

	public void set_place(String _place) {
		this._place = _place;
	}

	public String get_mobileNumber() {
		return _mobileNumber;
	}

	public void set_mobileNumber(String _mobileNumber) {
		this._mobileNumber = _mobileNumber;
	}
}
