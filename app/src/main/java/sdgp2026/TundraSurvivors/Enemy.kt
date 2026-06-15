package sdgp2026.TundraSurvivors

import android.graphics.Canvas
import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.SheetSprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

enum class EnemyType {
    SLIME, GOLEM
}

class Enemy(
    gctx: GameContext,
    val player: Player,
    spawnX: Float,
    spawnY: Float,
    val type: EnemyType
) : SheetSprite(
    gctx,
    if (type == EnemyType.SLIME) R.mipmap.ice_slime else R.mipmap.ice_golem,
    5f
) {

    private val playerSpeed = 15f

    private val moveSpeed = if (type == EnemyType.SLIME) 6f else 3.5f
    var hp = if (type == EnemyType.SLIME) 1 else 3

    // 넉백 관련 변수
    private var knockbackX = 0f
    private var knockbackY = 0f
    private var knockbackDuration = 0 // 넉백이 유지되는 프레임 수

    private val slimeFrames = listOf(
        Rect(170, 370, 675, 660),
        Rect(805, 370, 1405, 660)
    )
    private val golemFrames = listOf(
        Rect(190, 270, 670, 715),
        Rect(815, 270, 1300, 715)
    )

    init {
        this.x = spawnX
        this.y = spawnY

        // 크기 차별화
        if (type == EnemyType.SLIME) {
            this.width = 160f
            this.height = 160f
            this.frameRects = slimeFrames
        } else {
            this.width = 500f
            this.height = 500f
            this.frameRects = golemFrames
        }
    }

    fun takeDamage(damage: Int, kx: Float, ky: Float) {
        this.hp -= damage

        this.knockbackX = kx
        this.knockbackY = ky
        this.knockbackDuration = 5
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

        if (knockbackDuration > 0) {
            this.x += knockbackX
            this.y += knockbackY
            knockbackDuration--
        } else {
            val dirX = player.x - this.x
            val dirY = player.y - this.y
            val distance = sqrt(dirX * dirX + dirY * dirY)

            if (distance > 0f) {
                this.x += (dirX / distance) * moveSpeed
                this.y += (dirY / distance) * moveSpeed
            }
        }

        syncDstRect()
    }

    override fun draw(canvas: Canvas) {
        val saveCount = canvas.save()
        if (this.x > player.x) {
            canvas.scale(-1f, 1f, this.x, this.y)
        }
        super.draw(canvas)
        canvas.restoreToCount(saveCount)
    }
}