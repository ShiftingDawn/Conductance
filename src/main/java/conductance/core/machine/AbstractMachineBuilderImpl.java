package conductance.core.machine;

import java.util.Objects;
import java.util.function.Function;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import lombok.AccessLevel;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.block.BlockRotationType;
import conductance.api.machine.MachineBlock;
import conductance.api.machine.MachineBlockEntity;
import conductance.api.machine.MachineBlockItem;
import conductance.api.machine.event.AbstractMachineBuilder;
import conductance.api.machine.event.MachineBlockEntityFactory;
import conductance.api.machine.event.MachineBlockFactory;
import conductance.api.machine.event.MachineBlockItemFactory;
import conductance.api.machine.gui.GuiSetup;
import conductance.api.recipe.MachineRecipeType;
import conductance.api.tier.Tier;

@Setter(AccessLevel.PACKAGE)
abstract class AbstractMachineBuilderImpl<T extends MachineBlockEntity<T>, BUILDER extends AbstractMachineBuilder<T, BUILDER>> implements AbstractMachineBuilder<T, BUILDER> {

	private final ResourceLocation registryKey;
	private final MachineBlockEntityFactory<T> blockEntityFactory;
	private MachineBlockFactory<T> blockFactory = MachineBlock::new;
	private MachineBlockItemFactory<T> itemFactory = MachineBlockItem::new;
	private MachineRecipeType[] recipeTypes = new MachineRecipeType[0];
	private @Nullable GuiSetup guiSetup = new GuiSetup();
	private BlockRotationType rotationType = BlockRotationType.HORIZONTAL;
	private ModelType modelType = ModelType.DEFAULT;
	private @Nullable Object modelTypeData = null;
	private Function<String, MutableComponent> nameFactory = Component::translatable;

	protected AbstractMachineBuilderImpl(final ResourceLocation registryKey, final MachineBlockEntityFactory<T> blockEntityFactory) {
		this.registryKey = registryKey;
		this.blockEntityFactory = blockEntityFactory;
	}

	@Override
	public BUILDER blockFactory(final MachineBlockFactory<T> blockFactory) {
		this.blockFactory = blockFactory;
		return this.self();
	}

	@Override
	public BUILDER itemFactory(final MachineBlockItemFactory<T> itemFactory) {
		this.itemFactory = itemFactory;
		return this.self();
	}

	@Override
	public BUILDER recipeType(final MachineRecipeType recipeType, final MachineRecipeType... additionalRecipeTypes) {
		this.recipeTypes = new MachineRecipeType[1 + additionalRecipeTypes.length];
		this.recipeTypes[0] = recipeType;
		System.arraycopy(additionalRecipeTypes, 0, this.recipeTypes, 1, additionalRecipeTypes.length);
		return this.self();
	}

	@Override
	public BUILDER guiSetup(@Nullable final GuiSetup guiSetup) {
		this.guiSetup = guiSetup;
		return this.self();
	}

	@Override
	public BUILDER rotationType(final BlockRotationType type) {
		this.rotationType = type;
		return this.self();
	}

	@Override
	public BUILDER simpleModel(final ResourceLocation casingTexture) {
		this.modelType = ModelType.SIMPLE;
		this.modelTypeData = Objects.requireNonNull(casingTexture, "Casing texture cannot be null");
		return this.self();
	}

	@Override
	public BUILDER bronzeMachineModel() {
		this.modelType = ModelType.BRONZE_MACHINE;
		return this.self();
	}

	@Override
	public BUILDER customModel() {
		this.modelType = ModelType.CUSTOM;
		return this.self();
	}

	@Override
	public BUILDER customName(final Function<String, MutableComponent> nameFactory) {
		this.nameFactory = nameFactory;
		return this.self();
	}

	protected abstract MachineTypeImpl<T> makeMachineType(ResourceLocation registryKey, String descriptionId, MutableComponent name);

	public MachineTypeImpl<T> build(final ResourceLocation registryKey) {
		final String descriptionId = Util.makeDescriptionId("machine", registryKey);
		final MachineTypeImpl<T> result = Util.make(this.makeMachineType(registryKey, descriptionId, this.nameFactory.apply(descriptionId)), type -> {
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
			case BRONZE_MACHINE -> MachineModelHandler.addBronze(result);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	protected final BUILDER self() {
		return (BUILDER) this;
	}
}
