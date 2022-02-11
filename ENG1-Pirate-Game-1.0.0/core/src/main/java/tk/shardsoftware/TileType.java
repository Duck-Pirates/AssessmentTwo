package tk.shardsoftware;

import com.badlogic.gdx.graphics.Texture;

import tk.shardsoftware.util.ResourceUtil;

/** The different tiles that can be in the world */
public enum TileType {
	WATER_DEEP("noisy-waterdeep.png", false), WATER_SHALLOW("noisy-watershallow.png", false),
	SAND("noisy-sand.png", true), DIRT("noisy-rock.png", true), GRASS("noisy-grass.png", true);

	private Texture tex;
	private boolean solid;

	private TileType(String texStr, boolean solid) {
		this.tex = ResourceUtil.getTileTexture(texStr);
		this.solid = solid;
	}

	public Texture getTex() {
		return tex;
	}

	public boolean isSolid() {
		return solid;
	}
}