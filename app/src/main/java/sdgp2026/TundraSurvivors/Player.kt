package sdgp2026.TundraSurvivors

import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.SheetSprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kotlin.math.cos
import kotlin.math.sin

class Player(gctx: GameContext, val joystick: JoyStick, val bg: Background) : SheetSprite(gctx, R.mipmap.player_sprite, 5f) {

    private var speed = 15f
    var maxHp = 10
    var hp = 10
    private var invincibleTimer = 0 // 피격 후 무적시간

    private val walkDownFrames = listOf(
        Rect(1120, 77, 1200, 205),
        Rect(1430, 77, 1515, 205)
    )
    private val walkLeftFrames = listOf(
        Rect(1130, 535, 1205, 665),
        Rect(1290, 535, 1365, 665)
    )
    private val walkRightFrames = listOf(
        Rect(1117, 378, 1197, 509),
        Rect(1275, 378, 1355, 509)
    )
    private val walkUpFrames = listOf(
        Rect(1120, 225, 1200, 358),
        Rect(1275, 224, 1360, 358)
    )

    init {
        this.x = gctx.metrics.width / 2f
        this.y = gctx.metrics.height / 2f
        this.width = 250f
        this.height = 250f

        // 기본 상태는 앞모습(Down)으로 초기화
        this.frameRects = walkDownFrames
    }


    fun takeDamage(damage: Int) {
        if (invincibleTimer > 0) return // 무적 상태면 데미지 무시!

        this.hp -= damage
        this.invincibleTimer = 30
        if (this.hp < 0) this.hp = 0
    }


    override fun update(gctx: GameContext) {
        super.update(gctx)

        val angle = joystick.angle
        val power = joystick.power
        if (invincibleTimer > 0) invincibleTimer--
        if (power > 0f) {
            val dx = cos(angle) * power
            val dy = sin(angle) * power

            bg.x -= dx * speed
            bg.y -= dy * speed

            if (angle > -Math.PI / 4 && angle <= Math.PI / 4) {
                this.frameRects = walkRightFrames
            }
            else if (angle > Math.PI / 4 && angle <= 3 * Math.PI / 4) {
                this.frameRects = walkDownFrames
            }
            else if (angle > -3 * Math.PI / 4 && angle <= -Math.PI / 4) {
                this.frameRects = walkUpFrames
            }
            else {
                this.frameRects = walkLeftFrames
            }
        }

        syncDstRect()
    }
}