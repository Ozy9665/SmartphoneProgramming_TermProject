package sdgp2026.TundraSurvivors

import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.SheetSprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kotlin.math.cos
import kotlin.math.sin

class Player(gctx: GameContext, val joystick: JoyStick) : SheetSprite(gctx, R.mipmap.player_sprite, 3f) {

    private var speed = 10f

    init {
        this.x = 500f
        this.y = 800f
        this.width = 300f
        this.height = 300f

        this.frameRects = listOf(
            Rect(45, 80, 130, 205),
            Rect(190, 80, 275, 205)
        )
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)

        val angle = joystick.angle
        val power = joystick.power

        // 조이스틱을 밀 때만 좌표를 이동!
        if (power > 0f) {
            val dx = cos(angle) * power
            val dy = sin(angle) * power

            this.x += dx * speed
            this.y += dy * speed
        }

        // [핵심!] 움직이든 안 움직이든 그리기 동기화는 무조건 실행되어야 합니다! (if문 밖으로 탈출)
        syncDstRect()
    }
}