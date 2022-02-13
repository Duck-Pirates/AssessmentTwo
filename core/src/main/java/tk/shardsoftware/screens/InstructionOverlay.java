package tk.shardsoftware.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;

import static tk.shardsoftware.util.ResourceUtil.font;

public class InstructionOverlay implements Disposable {

	private SpriteBatch batch;
	private ShapeRenderer shapeRenderer;

	public boolean shouldDisplay = true;

	private GlyphLayout instructions;

	public InstructionOverlay(SpriteBatch batch) {
		this.batch = batch;
		this.shapeRenderer = new ShapeRenderer();
		instructions = new GlyphLayout();
		instructions.setText(font,
				"Destroy all colleges but your own to win!\n\nPress Esc to toggle this display.");
	}

	public void render() {
		Gdx.gl.glEnable(GL30.GL_BLEND);
		Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		batch.begin();
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.setColor(0.1f, 0.1f, 0.1f, 0.8f);
		shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		shapeRenderer.setColor(Color.TAN);
		shapeRenderer.circle(50 + 90, Gdx.graphics.getHeight() - 50, 35); // top
		shapeRenderer.circle(50, Gdx.graphics.getHeight() - 50 - 90, 35); // left
		shapeRenderer.circle(50 + 90 + 90, Gdx.graphics.getHeight() - 50 - 90, 35); // right
		shapeRenderer.circle(50 + 90, Gdx.graphics.getHeight() - 50 - 90 - 90, 35); // bottom

		// shapeRenderer.circle(50, Gdx.graphics.getHeight() - 420, 35); // fire
		shapeRenderer.rect(15, Gdx.graphics.getHeight() - 420 - 30, 100, 50);

		shapeRenderer.circle(50, Gdx.graphics.getHeight() - 420 - 90, 35); // map

		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.rectLine(50 + 50, Gdx.graphics.getHeight() - 50 - 90, 50 + 40 + 90,
				Gdx.graphics.getHeight() - 50 - 90, 3);
		shapeRenderer.rectLine(50 + 90, Gdx.graphics.getHeight() - 50 - 50, 50 + 90,
				Gdx.graphics.getHeight() - 50 - 90 - 40, 3);

		shapeRenderer.end();
		batch.end();

		batch.begin();
		font.draw(batch, "W", 50 + 78, Gdx.graphics.getHeight() - 35); // top
		font.draw(batch, "S", 50 + 80, Gdx.graphics.getHeight() - 35 - 90 - 90); // bottom
		font.draw(batch, "A", 40, Gdx.graphics.getHeight() - 35 - 90); // left
		font.draw(batch, "D", 40 + 90 + 90, Gdx.graphics.getHeight() - 35 - 90); // right

		font.draw(batch, "Space", 30, Gdx.graphics.getHeight() - 410); // fire
		font.draw(batch, "Fire Cannons", 50 + 80, Gdx.graphics.getHeight() - 410);

		font.draw(batch, "M", 40, Gdx.graphics.getHeight() - 410 - 90); // map
		font.draw(batch, "Toggle Map", 40 + 70, Gdx.graphics.getHeight() - 410 - 90);

		font.draw(batch, "Move Controls", 40, Gdx.graphics.getHeight() - 50 - 90 - 90 - 70);

		// Render instructions
		font.draw(batch, instructions, Gdx.graphics.getWidth() - instructions.width - 50,
				Gdx.graphics.getHeight() - 50);

		batch.end();
		Gdx.gl.glDisable(GL30.GL_BLEND);
	}

	@Override
	public void dispose() {
		shapeRenderer.dispose();
	}

}
