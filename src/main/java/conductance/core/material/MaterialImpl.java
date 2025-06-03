package conductance.core.material;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.ToLongFunction;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.util.Lazy;
import it.unimi.dsi.fastutil.objects.Object2LongArrayMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import conductance.api.material.IMaterialTrait;
import conductance.api.material.Material;
import conductance.api.material.MaterialFlag;
import conductance.api.material.MaterialTraitKey;
import conductance.api.material.PeriodicElement;
import conductance.api.material.traits.MaterialTraitFluid;
import conductance.api.registry.RegistryObject;

@Setter(AccessLevel.PACKAGE)
class MaterialImpl extends RegistryObject<ResourceLocation> implements Material {

	@Getter
	private final String descriptionId = Util.makeDescriptionId("material", this.getRegistryKey());
	@Getter(AccessLevel.PACKAGE)
	private final Map<MaterialTraitKey<?>, IMaterialTrait<?>> traits = new ConcurrentHashMap<>();
	@Getter(AccessLevel.PACKAGE)
	private final Set<MaterialFlag> flags = new HashSet<>();
	@Nullable
	private PeriodicElement periodicElement;
	@Getter(AccessLevel.PACKAGE)
	private final Object2LongMap<Material> components = new Object2LongArrayMap<>();
	@Nullable
	private Integer color = -1;
	@Nullable
	private Integer colorArgb = null;
	@Getter
	private ResourceLocation textureSet;
	private final Lazy<Long> protons = Lazy.of(() -> this.calc(PeriodicElement::protons, Material::getProtons, 43));
	private final Lazy<Long> neutrons = Lazy.of(() -> this.calc(PeriodicElement::neutrons, Material::getNeutrons, 55));
	private final Lazy<Long> mass = Lazy.of(() -> this.calc(PeriodicElement::mass, Material::getMass, 43));
	@Nullable
	private MaterialTraitKey<? extends MaterialTraitFluid<?>> defaultFluid;
	@Getter
	private TagKey<Block> blockRequiredToolTag = BlockTags.NEEDS_STONE_TOOL;
	@Getter
	private int burnTime;
	@Getter
	private int blockLightLevel;

	public MaterialImpl(final ResourceLocation registryKey) {
		super(registryKey);
	}

	@Override
	public boolean has(final MaterialFlag flag) {
		return this.flags.contains(flag);
	}

	@Override
	public boolean has(final MaterialTraitKey<?> key) {
		return this.traits.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IMaterialTrait<T>> @Nullable T get(final MaterialTraitKey<T> key) {
		return (T) this.traits.get(key);
	}

	@Override
	public int getMaterialColorRGB() {
		if (this.color == null) {
			this.color = this.calcColor();
		}
		return this.color;
	}

	@Override
	public int getMaterialColorARGB() {
		if (this.colorArgb == null) {
			this.colorArgb = 0xFF_000000 | this.getMaterialColorRGB();
		}
		return this.colorArgb;
	}

	@Override
	public long getProtons() {
		return this.protons.get();
	}

	@Override
	public long getNeutrons() {
		return this.neutrons.get();
	}

	@Override
	public long getMass() {
		return this.mass.get();
	}

	@Override
	@Nullable
	public MaterialTraitKey<? extends MaterialTraitFluid<?>> getDefaultFluid() {
		return this.defaultFluid;
	}

	private int calcColor() {
		long calculatedColor = 0;
		int componentCount = 0;
		for (final Object2LongMap.Entry<Material> entry : this.components.object2LongEntrySet()) {
			calculatedColor += entry.getKey().getMaterialColorRGB();
			componentCount += (int) entry.getLongValue();
		}
		return (int) (calculatedColor / componentCount);
	}

	private long calc(final ToLongFunction<PeriodicElement> rootProvider, final ToLongFunction<Material> provider, final long fallback) {
		if (this.periodicElement != null) {
			return rootProvider.applyAsLong(this.periodicElement);
		} else if (this.components.isEmpty()) {
			return fallback; // Technetium
		} else {
			long total = 0;
			long amount = 0;
			for (final Object2LongMap.Entry<Material> entry : this.components.object2LongEntrySet()) {
				total += entry.getLongValue() * provider.applyAsLong(entry.getKey());
				amount += entry.getLongValue();
			}
			return total / amount;
		}
	}
}
