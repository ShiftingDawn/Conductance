package conductance.api.material;

public interface MaterialDataMap {

	int getMaterialColorRGB();

	int getMaterialColorARGB();

	MaterialTextureSet getTextureSet();

	long getProtons();

	long getNeutrons();

	long getMass();
}
