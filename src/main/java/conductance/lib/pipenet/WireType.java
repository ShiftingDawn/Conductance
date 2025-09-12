package conductance.lib.pipenet;

import lombok.Getter;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.material.TaggedMaterialSet;

@Getter
public enum WireType {

	WIRE_1X(NCMaterialTaggedSets.WIRE_1X, 4, 1),
	WIRE_2X(NCMaterialTaggedSets.WIRE_2X, 6, 2),
	WIRE_4X(NCMaterialTaggedSets.WIRE_4X, 8, 4),
	WIRE_8X(NCMaterialTaggedSets.WIRE_8X, 10, 8),
	WIRE_12X(NCMaterialTaggedSets.WIRE_12X, 12, 12),
	WIRE_16X(NCMaterialTaggedSets.WIRE_16X, 14, 16);

	private final TaggedMaterialSet materialTaggedSet;
	private final int voxels;
	private final int amperage;

	WireType(final TaggedMaterialSet taggedSet, final int voxels, final int amperage) {
		this.materialTaggedSet = taggedSet;
		this.voxels = voxels;
		this.amperage = amperage;
	}

	public PipeModel createPipeModel(final Material material) {
		return new PipeModel(this.voxels / 16.0f,
				() -> NCTextureTypes.WIRE_SIDE.getTexture(material.getTextureSet(), null, null),
				() -> NCTextureTypes.WIRE_BASE.getTexture(material.getTextureSet(), null, null)
		);
	}

	public WireData getPhysicalProperties(final WireData baseProps) {
		return new WireData(baseProps.voltage(), baseProps.amperage() * this.amperage);
	}
}
