package dev.merlin.oldfog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JSON config: .minecraft/config/merlins_old_fog.json
 * The file is hot-reloaded: save it while the game is running and the
 * new values apply within about a second (no restart needed).
 */
public class OldFogConfig {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("merlins_old_fog.json");

	// ---- options (defaults = Minecraft 1.8.9) ----
	/** Master switch. false = vanilla fog. */
	public boolean enabled = true;

	/** Overworld-like dimensions: fog start/end as a fraction of the render distance. */
	public float overworldStart = 0.75F;
	public float overworldEnd = 1.0F;

	/** Dimensions with a ceiling (Nether). 1.8.9: start 0.05, end = min(far, 192) * 0.5 */
	public boolean applyInNether = true;
	public float netherStart = 0.05F;
	public float netherEnd = 0.5F;
	public float netherMaxDistance = 192.0F;

	/** Turn off the modern extra haze layer (recommended for the 1.8.9 look). */
	public boolean disableEnvironmentalFog = true;

	/** Sky fog follows the terrain fog end (1.8.9 behaviour). */
	public boolean matchSkyFog = true;

	// ---- loading ----
	private static OldFogConfig instance = new OldFogConfig();
	private static long lastModified = -1;
	private static long lastCheck = 0;

	public static OldFogConfig get() {
		long now = System.currentTimeMillis();
		if (now - lastCheck > 1000) {
			lastCheck = now;
			try {
				long mod = Files.exists(PATH) ? Files.getLastModifiedTime(PATH).toMillis() : -2;
				if (mod != lastModified) {
					load();
					lastModified = Files.exists(PATH) ? Files.getLastModifiedTime(PATH).toMillis() : -2;
				}
			} catch (IOException e) {
				MerlinsOldFog.LOGGER.warn("Could not check config file", e);
			}
		}
		return instance;
	}

	public static void load() {
		OldFogConfig cfg = new OldFogConfig();
		try {
			if (Files.exists(PATH)) {
				try (Reader r = Files.newBufferedReader(PATH)) {
					OldFogConfig read = GSON.fromJson(r, OldFogConfig.class);
					if (read != null) cfg = read;
				}
			}
			cfg.sanitize();
			try (Writer w = Files.newBufferedWriter(PATH)) { // rewrite -> adds new options, fixes values
				GSON.toJson(cfg, w);
			}
		} catch (Exception e) {
			MerlinsOldFog.LOGGER.error("Invalid config, using defaults", e);
			cfg = new OldFogConfig();
		}
		instance = cfg;
	}

	private void sanitize() {
		overworldStart = clamp(overworldStart, 0F, 10F);
		overworldEnd = Math.max(clamp(overworldEnd, 0.01F, 10F), overworldStart + 0.01F);
		netherStart = clamp(netherStart, 0F, 10F);
		netherEnd = Math.max(clamp(netherEnd, 0.01F, 10F), netherStart + 0.01F);
		netherMaxDistance = clamp(netherMaxDistance, 16F, 4096F);
	}

	private static float clamp(float v, float min, float max) {
		return Float.isNaN(v) ? min : Math.max(min, Math.min(max, v));
	}
}
