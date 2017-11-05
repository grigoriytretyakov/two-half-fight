package twohalffight.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer

class Game : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch

    internal lateinit var player: Texture

    internal lateinit var shapeRenderer: ShapeRenderer

    internal val top: Float = 300f

    internal val bottom: Float = 100f

    internal var x: Float = 0f

    internal var y: Float = top

    internal var moveSpeed: Float = 0f

    internal val jumpMaxSpeed: Float = 20f
    
    internal val jumpSlowdown: Float = 1f
    
    internal var jumpSpeed: Float = 0f

    internal var right: Float = 1280f

    override fun create() {
        batch = SpriteBatch()
        player = Texture("player.png")
        shapeRenderer = ShapeRenderer()
        right = right - player.getWidth().toFloat()
    }

    override fun render() {
        // left - 666666
        // right - e6e6e6

        val a: Boolean = Gdx.input.isKeyPressed(Input.Keys.A);
        val d: Boolean = Gdx.input.isKeyPressed(Input.Keys.D);
        val s: Boolean = Gdx.input.isKeyPressed(Input.Keys.S);

        if (s) {
            moveSpeed = 0f
        }
        else if (a) {
            moveSpeed = -4f
        }
        else if(d) {
            moveSpeed = 4f
        }

        val w: Boolean = Gdx.input.isKeyPressed(Input.Keys.W);
        if (w && y == bottom) {
            jumpSpeed = jumpMaxSpeed
        }

        x = x + moveSpeed
        x = if (x > right) {
            right
        }
        else if (x < 0f) {
            0f
        }
        else {
            x
        }

        if (y < bottom) {
            jumpSpeed = 0f
            y = bottom
        }
        else {
            jumpSpeed = Math.min(jumpMaxSpeed, jumpSpeed - jumpSlowdown)
            y += jumpSpeed
        }

        // left - #666666 (0.259f)
        val left = 0.259f
        Gdx.gl.glClearColor(left, left, left, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // right - #e6e6e6 (0.902)
        val rc = 0.902f
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(rc, rc, rc, 1f);
        shapeRenderer.rect(640f, 0f, 640f, 720f);
        shapeRenderer.end();

        batch.begin()
        batch.draw(player, x, y)
        batch.draw(player, right, y)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        player.dispose()
    }
}


