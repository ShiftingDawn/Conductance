package nl.appelgebakje22.xdata.adapter;

public interface BooleanAdapter extends BaseAdapter {

	void setBoolean(boolean value);

	boolean getBoolean();

	boolean getBoolean(boolean fallback);
}
