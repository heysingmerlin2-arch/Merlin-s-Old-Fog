# Merlin's Old Fog
Client-side Fabric mod for Minecraft 26.2 that restores the classic 1.8.9 fog. Works with Sodium.

## Config
`config/merlins_old_fog.json` (created on first start, hot-reloaded while the game runs)

| Option | Default | Meaning |
|---|---|---|
| enabled | true | false = vanilla fog |
| overworldStart / overworldEnd | 0.75 / 1.0 | fog range as fraction of render distance |
| applyInNether | true | change Nether fog too |
| netherStart / netherEnd | 0.05 / 0.5 | Nether fraction (end applies to min(far, netherMaxDistance)) |
| netherMaxDistance | 192 | blocks |
| disableEnvironmentalFog | true | remove modern extra haze layer |
| matchSkyFog | true | sky fog ends where terrain fog ends |

## Build
`./gradlew build` (JDK 25) -> `build/libs/merlins-old-fog-1.0.0.jar`

## GitHub Codespaces
1. Push this folder to a GitHub repo -> Code -> Codespaces -> Create. The devcontainer installs JDK 25 and builds automatically.
2. Download the jar from `build/libs/` (right-click -> Download) or from the Actions artifact.
3. Optional test in the cloud: `./gradlew runClient`, then open port 6080 (noVNC, password `vscode`). Slow without a GPU; mainly for checking that it starts.
