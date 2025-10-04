package conductance.core.machine;

import java.util.Objects;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBlockFactory;
import conductance.api.machine.event.MachineBlockItemFactory;
import conductance.api.machine.event.MachineBuilder;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.tier.Tier;

@RequiredArgsConstructor
@Accessors(fluent = true, chain = true)
final class MachineBuilderImpl<T extends MachineBlockEntity<T>> implements MachineBuilder<T> {

	private final ResourceLocation registryKey;
	private final MachineBlockEntityFactory<T> blockEntityFactory;
	private @Setter MachineBlockFactory<T> blockFactory = MachineBlock::new;
	private @Setter MachineBlockItemFactory<T> itemFactory = MachineBlockItem::new;
	private MachineRecipeType[] recipeTypes = new MachineRecipeType[0];
	private @Nullable GuiSetup guiSetup = new GuiSetup();
	private BlockRotationType rotationType = BlockRotationType.HORIZONTAL;
	private ModelType modelType = ModelType.DEFAULT;
	private @Nullable Object modelTypeData = null;
	private Function<String, MutableComponent> nameFactory = Component::translatable;

	@Override
	public MachineBuilder<T> recipeType(final MachineRecipeType recipeType, final MachineRecipeType... additionalRecipeTypes) {
		this.recipeTypes = new MachineRecipeType[1 + additionalRecipeTypes.length];
		this.recipeTypes[0] = recipeType;
		System.arraycopy(additionalRecipeTypes, 0, this.recipeTypes, 1, additionalRecipeTypes.length);
		return this;
	}

	@Override
	public MachineBuilder<T> guiSetup(@Nullable final GuiSetup guiSetup) {
		this.guiSetup = guiSetup;
		return this;
	}

	@Override
	public MachineBuilder<T> rotationType(final BlockRotationType type) {
		this.rotationType = type;
		return this;
	}

	@Override
	public MachineBuilder<T> simpleModel(final ResourceLocation casingTexture) {
		this.modelType = ModelType.SIMPLE;
		this.modelTypeData = Objects.requireNonNull(casingTexture, "Casing texture cannot be null");
		return this;
	}

	@Override
	public MachineBuilder<T> tieredModel(final String machineModelKey, final Tier tier) {
		this.modelType = ModelType.TIERED;
		this.modelTypeData = new Object[] {
			Objects.requireNonNull(machineModelKey, "Machine key cannot be null"),
			Objects.requireNonNull(tier, "Tier cannot be null"),
		};
		return this;
	}

	@Override
	public MachineBuilder<T> customModel() {
		this.modelType = ModelType.CUSTOM;
		return this;
	}

	@Override
	public MachineBuilder<T> customName(final Function<String, MutableComponent> nameFactory) {
		this.nameFactory = nameFactory;
		return this;
	}

	public MachineTypeImpl<T> build(final ResourceLocation registryKey) {
		final String descriptionId = Util.makeDescriptionId("machine", registryKey);
		final MachineTypeImpl<T> result = Util.make(new MachineTypeImpl<>(descriptionId, this.nameFactory.apply(descriptionId)), type -> {
			type.setBlock(MachineCore.createBlock(this.registryKey.getPath(), type, this.blockFactory));
			type.setItem(MachineCore.createItem(this.registryKey.getPath(), type.getDescriptionId(), type.getBlock(), this.itemFactory));
			type.setBlockEntityType(MachineCore.createBlockEntityType(this.registryKey.getPath(), type, type.getBlock(), this.blockEntityFactory));
			type.setRecipeTypes(this.recipeTypes);
			type.setGuiSetup(this.guiSetup);
			type.setRotationType(this.rotationType);
		});
		switch (this.modelType) {
			case DEFAULT -> MachineModelHandler.addDefault(result);
			case SIMPLE -> MachineModelHandler.addSimple(result, (ResourceLocation) this.modelTypeData);
			case TIERED -> MachineModelHandler.addTiered(result, (String) ((Object[]) this.modelTypeData)[0], (Tier) ((Object[]) this.modelTypeData)[1]);
		}
		return result;
	}
}
