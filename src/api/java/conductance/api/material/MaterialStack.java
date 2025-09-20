package conductance.api.material;

import conductance.api.NCMaterials;

public record MaterialStack(Material material, int count) {

	public static final MaterialStack EMPTY = new MaterialStack(NCMaterials.AIR, 0);

	boolean isEmpty() {
		return this.material == NCMaterials.AIR || this.count <= 0;
	}
}
