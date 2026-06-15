package sdgp2026.TundraSurvivors

import android.graphics.Canvas
import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Enemy(
    gctx: GameContext,
    val player: Player,
    spawnX: Float,
    spawnY: Float
) : Sprite(gctx, R.mipmap.player_sprite) {

    private val moveSpeed = 4f
    private val playerSpeed = 15f

    private var timeCount = 0f

    init {
        this.x = spawnX
        this.y = spawnY

        this.width = 200f
        this.height = 200f

        this.srcRect = Rect(45, 80, 130, 205)
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)

        val angle = player.joystick.angle
        val power = player.joystick.power
        if (power > 0f) {
            val dx = cos(angle) * power
            val dy = sin(angle) * power

            this.x -= dx * playerSpeed
            this.y -= dy * playerSpeed
        }

        val targetX = player.x
        val targetY = player.y

        val dirX = targetX - this.x
        val dirY = targetY - this.y
        val distance = sqrt(dirX * dirX + dirY * dirY)

        if (distance > 0f) {
            this.x += (dirX / distance) * moveSpeed
            this.y += (dirY / distance) * moveSpeed
        }

        timeCount += 0.2f

        syncDstRect()
    }

    override fun draw(canvas: Canvas) {
        val saveCount = canvas.save()

        if (this.x > player.x) {
            canvas.scale(-1f, 1f, this.x, this.y)
        }

        val bounceY = sin(timeCount) * 15f
        this.y += bounceY

        super.draw(canvas)

        this.y -= bounceY
        canvas.restoreToCount(saveCount)
    }
}