package conductance.core.apiimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.Nullable;
import conductance.api.CAPI;
import conductance.api.registry.TaggedSetBuilder;
import conductance.api.util.TextHelper;

@Accessors(chain = true, fluent = true)
public class TaggedSetBuilderImpl<TYPE> implements conductance.api.registry.TaggedSetBuilder<TYPE> {

	final List<TagHandler<TYPE>> tags = new ArrayList<>();

	@Getter
	private final String registryKey;
	@Getter
	private final Function<TYPE, String> objectSerializer;
	@Getter
	private final Function<TYPE, String> unlocalizedNameFactory;

	@Getter
	@Setter
	private boolean generateItems;
	@Getter
	@Setter
	private boolean generateBlocks;
	@Getter
	@Setter
	private boolean generateFluids;

	@Setter
	@Getter
	private long unitValue = -1;

	public TaggedSetBuilderImpl(String registryKey, final Function<TYPE, String> objectSerializer, final Function<TYPE, String> unlocalizedNameFactory) {
		this.registryKey = registryKey;
		this.objectSerializer = objectSerializer;
		this.unlocalizedNameFactory = unlocalizedNameFactory;
	}

	public TaggedSetBuilderImpl(final String name, final Function<TYPE, String> objectSerializer, final String unlocalizedNameFactory) {
		this(name, objectSerializer, ignored -> unlocalizedNameFactory);
	}

	public TaggedSetBuilderImpl(final String name, final Function<TYPE, String> objectSerializer) {
		this(name, objectSerializer, "%s_" + TextHelper.toLowerCaseUnderscore(name));
	}

	// region Formatted Tags
	@Override
	public TaggedSetBuilder<TYPE> addTagLoader(final String tagPathFactory) {
		this.tags.add(new TagHandler<>("neoforge", tagPathFactory, this.objectSerializer, false));
		return this;
	}

	@Override
	public TaggedSetBuilder<TYPE> addTagCommon(final String tagPathFactory) {
		this.tags.add(new TagHandler<>("c", tagPathFactory, this.objectSerializer, false));
		return this;
	}

	@Override
	public TaggedSetBuilder<TYPE> addTagMod(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(CAPI.MOD_ID, tagPathFactory, this.objectSerializer, false));
		return this;
	}

	@Override
	public TaggedSetBuilder<TYPE> addTagVanilla(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(ResourceLocation.DEFAULT_NAMESPACE, tagPathFactory, this.objectSerializer, false));
		return this;
	}
	// endregion

	// region Unformatted Tags
	@Override
	public TaggedSetBuilder<TYPE> addTagLoaderUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>("neoforge", tagPathFactory, ignored -> tagPathFactory, true));
		return this;
	}

	@Override
	public TaggedSetBuilder<TYPE> addTagCommonUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>("c", tagPathFactory, ignored -> tagPathFactory, true));
		return this;
	}

	@Override
	public TaggedSetBuilder<TYPE> addTagModUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(CAPI.MOD_ID, tagPathFactory, ignored -> tagPathFactory, true));
		return this;
	}

	@Override
	public TaggedSetBuilder<TYPE> addTagVanillaUnformatted(final String tagPathFactory) {
		this.tags.add(new TagHandler<>(ResourceLocation.DEFAULT_NAMESPACE, tagPathFactory, ignored -> tagPathFactory, true));
		return this;
	}
	// endregion

	static class TagHandler<T> {

		public final String namespace;
		public final String tagPathFactory;
		public final Function<T, String> objectSerializer;
		public final boolean isGlobalTag;

		public TagHandler(@Nullable String namespace, String tagPathFactory, Function<T, String> objectSerializer, boolean isGlobalTag) {
			this.namespace = namespace != null ? namespace : tagPathFactory.contains(":") ? tagPathFactory.split(":", 2)[0] : CAPI.MOD_ID;
			this.tagPathFactory = tagPathFactory.contains(":") ? tagPathFactory.split(":", 2)[1] : tagPathFactory;
			this.objectSerializer = objectSerializer;
			this.isGlobalTag = isGlobalTag;
		}

		public ResourceLocation make(T obj) {
			return ResourceLocation.fromNamespaceAndPath(this.namespace, this.tagPathFactory.formatted(this.objectSerializer.apply(obj)));
		}

		@Override
		public int hashCode() {
			return this.tagPathFactory.hashCode();
		}

		@Override
		public boolean equals(final Object obj) {
			return obj instanceof final TagHandler<?> other && other.tagPathFactory.equals(this.tagPathFactory);
		}

		@Override
		public String toString() {
			return this.tagPathFactory;
		}
	}
}
