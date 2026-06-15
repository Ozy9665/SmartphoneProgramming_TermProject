package sdgp2026.TundraSurvivors

import android.graphics.Canvas
import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.SheetSprite // [수정!] Sprite 대신 SheetSprite를 가져옵니다.
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

// [핵심 수정!] 부모를 SheetSprite로 바꾸고, ice_golem을 장착! 뒤에 5f는 1초에 5번 프레임을 바꾸는 속도입니다.
class Enemy(
    gctx: GameContext,
    val player: Player,
    spawnX: Float,
    spawnY: Float
) : SheetSprite(gctx, R.mipmap.ice_golem, 5f) {

    private val moveSpeed = 4f
    private val playerSpeed = 15f

    // 💡 [가위질 좌표] 그림판을 켜고 골렘 1번(왼쪽), 2번(오른쪽)의 네 모서리 좌표를 따서 넣어주세요!
    // 아래 숫자는 예시입니다. 그림에 맞게 수정하셔야 완벽하게 나옵니다.
    private val golemFrames = listOf(
        Rect(190, 270, 670, 715), // 프레임 1: 왼쪽 골렘 자르기 (Left, Top, Right, Bottom)
        Rect(815, 270, 1300, 715)  // 프레임 2: 오른쪽 골렘 자르기
    )

    init {
        this.x = spawnX
        this.y = spawnY
        this.width = 400f
        this.height = 400f

        // 방금 자른 2프레임 애니메이션 세트를 몬스터에게 적용!
        this.frameRects = golemFrames
    }

    override fun update(gctx: GameContext) {
        super.update(gctx) // 여기서 프레임(그림) 넘기는 애니메이션이 자동으로 계산됩니다!

        // 1. 월드 밀림 상쇄 (런닝머신 원리)
        val angle = player.joystick.angle
        val power = player.joystick.power
        if (power > 0f) {
            val dx = cos(angle) * power
            val dy = sin(angle) * power
            this.x -= dx * playerSpeed
            this.y -= dy * playerSpeed
        }

        // 2. 플레이어 추적 이동
        val dirX = player.x - this.x
        val dirY = player.y - this.y
        val distance = sqrt(dirX * dirX + dirY * dirY)

        if (distance > 0f) {
            this.x += (dirX / distance) * moveSpeed
            this.y += (dirY / distance) * moveSpeed
        }

        syncDstRect()
    }

    override fun draw(canvas: Canvas) {
        val saveCount = canvas.save()

        // 플레이어 위치에 따라 골렘 이미지를 좌우로 플립(Flip)
        if (this.x > player.x) {
            canvas.scale(-1f, 1f, this.x, this.y)
        }

        // 통통 튀는 코드는 지우고, 깔끔하게 엔진의 프레임 애니메이션 그리기만 호출!
        super.draw(canvas)

        canvas.restoreToCount(saveCount)
    }
}