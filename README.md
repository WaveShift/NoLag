# No Death Animations+ (1.21.4 port)

A client-side Fabric mod that hides selected vanilla visuals. This is a port of
[meowjade/NoDeathAnimationPlus](https://github.com/meowjade/NoDeathAnimationPlus) from 1.20.4 to
1.21.4, with a few extra toggles added.

All original code is by **meowjade**. This repository exists because the upstream project targets
1.20.4 and has not been updated; it is redistributed under the GPL-3.0 licence the original ships
with. See [Licence](#licence).

## Options

Every option defaults to `true` (or `0` for the angle), meaning **installing the mod changes nothing
until you turn something off**. Configure via Mod Menu, or edit `config/nodeathanimationsplus.toml`.

| Option | Default | Turning it off |
| --- | --- | --- |
| `poofParticles` | `true` | mobs vanish without the puff of smoke |
| `deathAnimation` | `true` | dead mobs stop rendering immediately |
| `deathFlipDegrees` | `0` | angle a dying mob tips over at; `0` keeps them upright, `90` is vanilla |
| `renderZombifiedPiglins` | `true` | zombified piglins are not drawn |
| `renderNetherPortalBlocks` | `true` | nether portal blocks are not drawn |
| `netherPortalParticles` | `true` | no portal particles or ambient portal noise |

Note `deathFlipDegrees` applies regardless of `deathAnimation`, so the default of `0` means mobs die
standing upright rather than toppling over.

### These are visual filters, not removals

Nothing here changes what the server sees. A hidden piglin still exists, still ticks, and can still
hit you. A hidden nether portal still teleports you when you walk into it. Nothing is culled from
simulation, so none of these options improve tick performance — only render cost.

## Requirements

- Minecraft 1.21.4
- Fabric Loader 0.16.10+
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Cloth Config](https://modrinth.com/mod/cloth-config)
- [Mod Menu](https://modrinth.com/mod/modmenu) (optional, for the settings screen)

## Building

```
./gradlew build
```

Output lands in `build/libs/`.

## What changed from upstream

**Port to 1.21.4**

- `LivingEntityRenderer.getFlipDegrees` lost its entity parameter in 1.21.2's entity-render-state
  refactor, and `LivingEntityRenderer` gained type parameters (`<T, S, M>` extending
  `EntityRenderer<T, S>`). `LivingEntityRendererMixin` was rewritten against the new no-arg signature
  and no longer declares a superclass at all — it only injects, so it needs neither the generics nor
  the inheritance, which also keeps it stable across future signature changes.
- The other two original mixin targets (`EntityRenderDispatcher.shouldRender`,
  `LivingEntity.makePoofParticles`) were unchanged in 1.21.4 and are untouched.
- Dependencies bumped: Loom 1.9, Java 21, Loader 0.16.10, Fabric API 0.119.4+1.21.4,
  Cloth Config 17.0.144, Mod Menu 13.0.4.
- Parchment mappings dropped — there is no 1.21.4 release, and it only layers parameter *names* over
  Mojang mappings, so no target this mod injects into is affected.

**New options**

- `renderZombifiedPiglins`, folded into the existing `shouldRender` hook rather than adding a mixin.
- `renderNetherPortalBlocks`, hooked at `BlockRenderDispatcher.renderBatched`. Chosen over the more
  usual `getRenderShape → INVISIBLE` because `BlockRenderDispatcher` is client-only by construction,
  so cancelling it cannot reach collision or teleport logic; `getRenderShape` lives on
  `BlockBehaviour`, which is shared with the server.
- `netherPortalParticles`, hooked at `NetherPortalBlock.animateTick` — the client's random display
  tick, which only spawns particles and plays the ambient sound.

**Defaults changed.** Upstream suppressed poof particles and death animations out of the box. Here
everything defaults to vanilla behaviour so the mod is opt-in.

## Licence

GPL-3.0, inherited from upstream — see [LICENSE](LICENSE).

Upstream's `fabric.mod.json` declared `All-Rights-Reserved` while shipping the full GPL-3.0 licence
text as its `LICENSE` file. The two contradict each other. The `LICENSE` file is the explicit grant
and a complete copy of the GPL is not something committed by accident, so it is treated as governing
and the manifest field has been corrected to match. If the original author intended otherwise and
would like this repository taken down, open an issue and it will be removed.
