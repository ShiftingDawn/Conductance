package conductance.api.resource.event;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import com.google.gson.JsonElement;
import conductance.api.plugin.IConductancePluginEvent;
import conductance.api.resource.BlockStateBuilder;
import conductance.api.resource.ItemsModelBuilder;
import conductance.api.resource.ModelBuilder;

public interface AddRuntimeModelEvent extends IConductancePluginEvent {

	void addBlockState(ResourceLocation location, Consumer<BlockStateBuilder> builder);

	default void addBlockState(final Block block, final Consumer<BlockStateBuilder> builder) {
		this.addBlockState(BuiltInRegistries.BLOCK.getKey(block), builder);
	}

	void addBlockModel(ResourceLocation location, Consumer<ModelBuilder> builder);

	default void addBlockModel(final Block block, final Consumer<ModelBuilder> builder) {
		this.addBlockModel(BuiltInRegistries.BLOCK.getKey(block), builder);
	}

	void addItemsModel(ResourceLocation location, Consumer<ItemsModelBuilder> builder);

	default void addItemsModel(final Item item, final Consumer<ItemsModelBuilder> builder) {
		this.addItemsModel(BuiltInRegistries.ITEM.getKey(item), builder);
	}

	void addItemModel(ResourceLocation location, Consumer<ModelBuilder> builder);

	default void addItemModel(final Item item, final Consumer<ModelBuilder> builder) {
		this.addItemModel(BuiltInRegistries.ITEM.getKey(item), builder);
	}

	default void addItemModelDelegate(final Block block) {
		this.addItemsModel(block.asItem(), builder -> builder.simple(ModelLocationUtils.getModelLocation(block)));
	}

	void insertBlockState(ResourceLocation location, JsonElement data);

	default void insertBlockState(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertBlockState(location, provider.get());
	}

	void insertBlockModel(ResourceLocation location, JsonElement data);

	default void insertBlockModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertBlockModel(location, provider.get());
	}

	void insertItemsModel(ResourceLocation location, JsonElement data);

	default void insertItemsModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertItemsModel(location, provider.get());
	}

	void insertItemModel(ResourceLocation location, JsonElement data);

	default void insertItemModel(final ResourceLocation location, final Supplier<JsonElement> provider) {
		this.insertItemModel(location, provider.get());
	}
}
