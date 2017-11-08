package twohalffight.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer


class AI {

}


class SquareJumper(x: Float, y: Float, texture: Texture, batch: SpriteBatch, flip: Boolean, left: Float, right: Float) {
    internal lateinit var batch: SpriteBatch

    internal lateinit var player: Texture

    internal var x: Float = 0f

    internal var y: Float = 0f

    internal var bottom: Float = 0f

    internal var left: Float = 0f

    internal var right: Float = 0f

    internal var flip: Boolean = false

    internal var moveSpeed: Float = -4f

    internal val jumpMaxSpeed: Float = 33f
    
    internal val jumpSlowdown: Float = 1.5f
    
    internal var jumpSpeed: Float = 0f


    init {
        this.batch = batch
        this.player = texture
        this.x = x
        this.y = y
        this.bottom = y
        this.left = left
        this.right = right - player.getWidth().toFloat()
        this.flip = flip

        if (flip) {
            moveSpeed = -moveSpeed
        }
    }

    fun jump() {
        if (y == bottom) {
            jumpSpeed = jumpMaxSpeed
        }
    }

    fun update() {
        x = x + moveSpeed
        x = if (x >= right) {
            right
        }
        else if (x <= left) {
            left
        }
        else {
            x
        }

        if (y <= bottom && jumpSpeed != jumpMaxSpeed) {
            jumpSpeed = 0f
            y = bottom
            if (flip) {
                moveSpeed = 4f
            }
            else {
                moveSpeed = -4f
            }
        }
        else {
            jumpSpeed = Math.min(jumpMaxSpeed, jumpSpeed - jumpSlowdown)
            y += jumpSpeed
        }
    }

    fun left() {
        moveSpeed = -4f
    }

    fun right() {
        moveSpeed = 4f
    }

    fun draw() {
        // May be it is wrong, but it's good enough for "hello world"
        batch.draw(
            player,
            x,
            y,
            player.getWidth().toFloat(),
            player.getHeight().toFloat(),
            0,
            0,
            player.getWidth(),
            player.getHeight(),
            flip,
            false
        )
    }
}


class Game : ApplicationAdapter() {
    internal lateinit var batch: SpriteBatch

    internal lateinit var texture: Texture

    internal lateinit var human: SquareJumper

    internal lateinit var ai: SquareJumper

    internal lateinit var shapeRenderer: ShapeRenderer

    internal val floor: Float = 50f

    internal var width: Float = 1280f


    override fun create() {
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()

        texture = Texture("player.png")
        human = SquareJumper(width / 4f - texture.getWidth().toFloat() / 2f, floor, texture, batch, false, 0f, width / 2f)
        ai = SquareJumper(width / 4f * 3f - texture.getWidth().toFloat() / 2f, floor, texture, batch, true, width / 2f, width)
    }

    override fun render() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            human.left()
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            human.right()
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            human.jump()
        }

        // This is should be in method which will be calling by timer, not render
        human.update()
        ai.update()

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
        human.draw()
        ai.draw()
        batch.end()

    }

    override fun dispose() {
        batch.dispose()
        texture.dispose()
    }
}


