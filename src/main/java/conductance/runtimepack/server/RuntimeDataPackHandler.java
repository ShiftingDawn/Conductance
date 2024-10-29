package conductance.runtimepack.server;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import conductance.Conductance;

@EventBusSubscriber(modid = Conductance.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RuntimeDataPackHandler implements RepositorySource {

	private static final String NAME = Conductance.MODID + ":runtime_data_pack";

	public RuntimeDataPackHandler() {
		RuntimeDataPack.reset();
	}

	@Override
	public void loadPacks(final Consumer<Pack> consumer) {
		final PackLocationInfo info = new PackLocationInfo(RuntimeDataPackHandler.NAME, Component.literal(RuntimeDataPackHandler.NAME), PackSource.BUILT_IN, Optional.empty());
		final Pack.ResourcesSupplier resourcesSupplier = new Pack.ResourcesSupplier() {

			@Override
			public PackResources openPrimary(final PackLocationInfo packLocationInfo) {
				return new RuntimeDataPack(packLocationInfo);
			}

			@Override
			public PackResources openFull(final PackLocationInfo packLocationInfo, final Pack.Metadata metadata) {
				return this.openPrimary(packLocationInfo);
			}
		};
		final PackType type = PackType.SERVER_DATA;
		final PackSelectionConfig config = new PackSelectionConfig(true, Pack.Position.BOTTOM, true);

		consumer.accept(Pack.readMetaAndCreate(info, resourcesSupplier, type, config));
	}

	@SubscribeEvent
	private static void onAddPackFinders(final AddPackFindersEvent event) {
		if (event.getPackType() == PackType.SERVER_DATA) {
			event.addRepositorySource(new RuntimeDataPackHandler());
		}
	}
}
