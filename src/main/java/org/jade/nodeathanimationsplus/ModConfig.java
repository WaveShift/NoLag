package org.jade.nodeathanimationsplus;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "nodeathanimationsplus")
public class ModConfig implements ConfigData {
	public boolean poofParticles = true;
	public boolean deathAnimation = true;
	public int deathFlipDegrees = 0;
	// true keeps vanilla behaviour, so installing the mod changes nothing until this is turned off.
	// Hides the model only - the piglin still exists, still ticks, and can still hit you.
	public boolean renderZombifiedPiglins = true;
	// the portal block itself. It still exists and still teleports you - it is simply not drawn.
	public boolean renderNetherPortalBlocks = true;
	// the ambient purple particles and idle noise the portal block emits
	public boolean netherPortalParticles = true;
}
