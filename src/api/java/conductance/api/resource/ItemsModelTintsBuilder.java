package conductance.api.resource;

public interface ItemsModelTintsBuilder {

	ItemsModelTintsBuilder constant(int color);

	ItemsModelTintsBuilder constant(float r, float g, float b);

	ItemsModelTintsBuilder dye(int color);

	ItemsModelTintsBuilder dye(float r, float g, float b);

	ItemsModelTintsBuilder firework(int color);

	ItemsModelTintsBuilder firework(float r, float g, float b);

	ItemsModelTintsBuilder grass(float temperature, float downfall);

	ItemsModelTintsBuilder mapColor(int color);

	ItemsModelTintsBuilder mapColor(float r, float g, float b);

	ItemsModelTintsBuilder potion(int color);

	ItemsModelTintsBuilder potion(float r, float g, float b);

	ItemsModelTintsBuilder team(int color);

	ItemsModelTintsBuilder team(float r, float g, float b);

	ItemsModelTintsBuilder customModelData(int index, int color);

	default ItemsModelTintsBuilder customModelData(final int color) {
		return this.customModelData(0, color);
	}

	ItemsModelTintsBuilder customModelData(int index, float r, float g, float b);

	default ItemsModelTintsBuilder customModelData(final float r, final float g, final float b) {
		return this.customModelData(0, r, g, b);
	}
}
