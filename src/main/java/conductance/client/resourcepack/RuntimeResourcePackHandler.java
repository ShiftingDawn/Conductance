package conductance.client.resourcepack;

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
public class RuntimeResourcePackHandler implements RepositorySource {

	private static final String NAME = Conductance.MODID + ":runtime_resource_pack";

	public RuntimeResourcePackHandler() {
		RuntimeResourcePack.reset();
	}

	@Override
	public void loadPacks(final Consumer<Pack> consumer) {
		consumer.accept(Pack.readMetaAndCreate(new PackLocationInfo(RuntimeResourcePackHandler.NAME, Component.literal(RuntimeResourcePackHandler.NAME), PackSource.BUILT_IN, Optional.empty()), new Pack.ResourcesSupplier() {

			@Override
			public PackResources openPrimary(final PackLocationInfo packLocationInfo) {
				return new RuntimeResourcePack(packLocationInfo);
			}

			@Override
			public PackResources openFull(final PackLocationInfo packLocationInfo, final Pack.Metadata metadata) {
				return this.openPrimary(packLocationInfo);
			}
		}, PackType.CLIENT_RESOURCES, new PackSelectionConfig(true, Pack.Position.BOTTOM, true)));
	}

	@SubscribeEvent
	private static void onAddPackFinders(final AddPackFindersEvent event) {
		if (event.getPackType() == PackType.CLIENT_RESOURCES) {
			event.addRepositorySource(new RuntimeResourcePackHandler());
		}
	}
}
