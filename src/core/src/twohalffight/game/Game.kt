package twohalffight.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

class Game : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch

    internal lateinit var player: Texture

    internal var x: Float = 0f

    internal var y: Float = 100f

    internal val top: Float = 300f

    internal val bottom: Float = 100f

    internal val moveSpeed: Float = 3f

    internal var jumpSpeed: Float = 7f

    override fun create() {
        batch = SpriteBatch()
        player = Texture("player.png")
    }

    override fun render() {
        // left - 666666
        // right - e6e6e6

        x = if (x > 1440f) {
            0f
        }
        else {
            x + moveSpeed
        }

        if (y > top || y < bottom) {
            jumpSpeed = -jumpSpeed
        }
        y += jumpSpeed
        
        val color = 0.259f
        Gdx.gl.glClearColor(color, color, color, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        batch.draw(player, x, y)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        player.dispose()
    }
}


