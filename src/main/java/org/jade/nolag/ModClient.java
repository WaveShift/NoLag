package org.jade.nolag;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import org.jetbrains.annotations.Nullable;

public class ModClient implements ClientModInitializer {
	@Nullable
	public static ModConfig config;

	@Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		var holder = AutoConfig.getConfigHolder(ModConfig.class);

		// Block visibility is baked into chunk geometry when a section is meshed, so flipping
		// renderNetherPortalBlocks does nothing to chunks that are already built - the portal stays
		// drawn until something happens to rebuild that section. That made the option look broken when
		// it was in fact working, just not retroactively. Rebuilding everything on save is heavy-handed
		// but it is the same thing F3+A does, it only fires when the user actually changes a setting,
		// and it means the toggle does what a toggle should.
		holder.registerSaveListener((configHolder, newConfig) -> {
			Minecraft client = Minecraft.getInstance();
			if (client != null && client.levelRenderer != null) {
				client.execute(() -> client.levelRenderer.allChanged());
			}

			return InteractionResult.SUCCESS;
		});

		config = holder.getConfig();
	}
}
