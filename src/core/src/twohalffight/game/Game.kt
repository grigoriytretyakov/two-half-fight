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


class SquareJumper(
        private var x: Float,
        private var y: Float,
        private val texture: Texture,
        private val batch: SpriteBatch,
        private val flip: Boolean,
        private val left: Float,
        private val right: Float) {
// Можно в конструкторе сразу писать private val/var, это будет аналог инициализации в init, только меньше писать
//    internal lateinit var batch: SpriteBatch
//
//    internal lateinit var player: Texture
//
//    internal var x: Float = 0f
//
//    internal var y: Float = 0f
//
//    internal var bottom: Float = 0f
//
//    internal var left: Float = 0f
//
//    internal var right: Float = 0f
//
//    internal var flip: Boolean = false

    private var moveSpeed: Float = -4f
    private val jumpMaxSpeed: Float = 33f
    private val jumpSlowdown: Float = 1.5f
    private var jumpSpeed: Float = 0f

    init {
//        this.batch = batch
//        this.player = texture
//        this.x = x
//        this.y = y
//        this.bottom = y
//        this.left = left
//        this.right = right - player.getWidth().toFloat()
//        this.flip = flip

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
        x = when(x) {
            >= right -> right
            <= left -> left
            else -> x
        }
//      В котлине существуют идиомы типа этой, куча if-ов заменяется на when (умный switch)
//        if (x >= right) {
//            right
//        }
//        else if (x <= left) {
//            left
//        }
//        else {
//            x
//        }

        if (y <= bottom && jumpSpeed != jumpMaxSpeed) {
            jumpSpeed = 0f
            y = bottom
            moveSpeed = if (flip) 4f else -4f
//            if (flip) {
//                moveSpeed = 4f
//            } else {
//                moveSpeed = -4f
//            }
        } else {
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

    private lateinit var batch: SpriteBatch
    private lateinit var texture: Texture
    private lateinit var human: SquareJumper
    private lateinit var ai: SquareJumper
    private lateinit var shapeRenderer: ShapeRenderer
    private val floor: Float = 50f
    private var width: Float = 1280f

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
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
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