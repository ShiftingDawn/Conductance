package nl.appelgebakje22.xdata.adapter;

public interface CharAdapter extends BaseAdapter {

	void setChar(char value);

	char getChar();

	char getChar(char fallback);
}
