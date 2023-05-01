
public class Person {
	
	private String _Name;
	private String _eMail;
	
	public Person(){
		this._Name = "";
		this._eMail = "";
	}

	public String get_Name() {
		return _Name;
	}


	public void set_Name(String Name) {
		this._Name = Name;
	}

	public String get_eMail() {
		return _eMail;
	}

	public void set_eMail(String _eMail) {
		this._eMail = _eMail;
	}
}
