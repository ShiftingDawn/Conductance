package nl.appelgebakje22.xdata.adapter;

public interface StringAdapter extends BaseAdapter {

	void setString(String value);

	String getString();

	String getString(String fallback);
}
