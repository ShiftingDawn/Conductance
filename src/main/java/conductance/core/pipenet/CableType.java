package conductance.core.pipenet;

import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import conductance.api.NCMaterialTaggedSets;
import conductance.api.NCTextureTypes;
import conductance.api.material.Material;
import conductance.api.material.MaterialTextureType;
import conductance.api.material.TaggedMaterialSet;
import conductance.api.util.SafeOptional;
import static conductance.api.NCTextureTypes.WIRE_BASE;

public enum CableType {

	WIRE_1X(NCMaterialTaggedSets.WIRE_1X, .125f, 1, 2, -1, WIRE_BASE),
	WIRE_2X(NCMaterialTaggedSets.WIRE_2X, .25f, 2, 2, -1, WIRE_BASE),
	WIRE_4X(NCMaterialTaggedSets.WIRE_4X, .375f, 4, 2, -1, WIRE_BASE),
	WIRE_8X(NCMaterialTaggedSets.WIRE_8X, .5f, 8, 3, -1, WIRE_BASE),
	WIRE_12X(NCMaterialTaggedSets.WIRE_12X, .625f, 12, 3, -1, WIRE_BASE),
	WIRE_16X(NCMaterialTaggedSets.WIRE_16X, .75f, 16, 3, -1, WIRE_BASE),

	CABLE_1X(NCMaterialTaggedSets.CABLE_1X, .25f, 1, 1, 0, NCTextureTypes.WIRE_INSULATION_0),
	CABLE_2X(NCMaterialTaggedSets.CABLE_2X, .375f, 2, 1, 1, NCTextureTypes.WIRE_INSULATION_1),
	CABLE_4X(NCMaterialTaggedSets.CABLE_4X, .5f, 4, 1, 2, NCTextureTypes.WIRE_INSULATION_2),
	CABLE_8X(NCMaterialTaggedSets.CABLE_8X, .625f, 8, 1, 3, NCTextureTypes.WIRE_INSULATION_3),
	CABLE_12X(NCMaterialTaggedSets.CABLE_12X, .75f, 12, 1, 4, NCTextureTypes.WIRE_INSULATION_4),
	CABLE_16X(NCMaterialTaggedSets.CABLE_16X, .875f, 16, 1, 5, NCTextureTypes.WIRE_INSULATION_5);

	@Getter
	private final TaggedMaterialSet materialTaggedSet;
	private final float thickness;
	@Getter
	private final int amperage;
	@Getter
	private final int lossMultiplier;
	@Getter
	private final boolean isCable;
	private final MaterialTextureType textureType;

	CableType(final TaggedMaterialSet taggedSet, final float thickness, final int amperage, final int lossMultiplier, final int insulated, final MaterialTextureType textureType) {
		this.materialTaggedSet = taggedSet;
		this.thickness = thickness;
		this.amperage = amperage;
		this.lossMultiplier = lossMultiplier;
		this.textureType = textureType;
		this.isCable = insulated >= 0;
	}

	public PipeModel createPipeModel(final Material material) {
		final SafeOptional<ResourceLocation> baseTexture = NCTextureTypes.WIRE_BASE.getBlockTexture(material.getTextureSet(), null, null);
		final SafeOptional<ResourceLocation> sideTexture = (this.isCable ? NCTextureTypes.WIRE_INSULATION : WIRE_BASE).getBlockTexture(material.getTextureSet(), null, null);
		final PipeModel model = new PipeModel(this.thickness, () -> sideTexture, () -> baseTexture, null, null);
		if (this.isCable) {
			model.setEndOverlayTexture(this.textureType.getBlockTexture(material.getTextureSet(), null, null));
		}
		return model;
	}

	public CableData getPhysicalProperties(final CableData baseProps) {
		final int lossPerBlock;
		if (!baseProps.superconductor() && baseProps.cableLoss() == 0) {
			lossPerBlock = (int) (0.75 * this.lossMultiplier);
		} else {
			lossPerBlock = baseProps.cableLoss() * this.lossMultiplier;
		}
		return new CableData(baseProps.voltage(), baseProps.amperage() * this.amperage, lossPerBlock, baseProps.superconductor());
	}
}
