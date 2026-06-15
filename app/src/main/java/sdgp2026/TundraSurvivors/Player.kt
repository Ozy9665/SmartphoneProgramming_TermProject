package sdgp2026.TundraSurvivors

import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.SheetSprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick
import kotlin.math.cos
import kotlin.math.sin

class Player(gctx: GameContext, val joystick: JoyStick, val bg: Background) : SheetSprite(gctx, R.mipmap.player_sprite, 3f) {

    private var speed = 15f

    // 💡 [4방향 프레임 모음집] 주인공 스프라이트 시트의 실제 세로 행 위치에 맞게 Rect 값을 수정해 주세요!
    // 아래 가위질 좌표 중 walkDownFrames 외의 나머지는 예시 좌표이므로, 실제 그림 배치에 맞게 Y축(두번째, 네번째 숫자)을 고쳐주시면 됩니다!
    private val walkDownFrames = listOf(
        Rect(1120, 77, 1200, 205),
        Rect(1430, 77, 1515, 205)
    )
    private val walkLeftFrames = listOf(
        Rect(1130, 535, 1205, 665),  // 왼쪽 걷기 행 좌표 (예시)
        Rect(1290, 535, 1365, 665)
    )
    private val walkRightFrames = listOf(
        Rect(1117, 378, 1197, 509), // 오른쪽 걷기 행 좌표 (예시)
        Rect(1275, 378, 1355, 509)
    )
    private val walkUpFrames = listOf(
        Rect(1120, 225, 1200, 358),  // 뒷모습 걷기 행 좌표 (예시)
        Rect(1275, 224, 1360, 358)
    )

    init {
        this.x = 1000f
        this.y = 500f
        this.width = 300f
        this.height = 300f

        // 기본 상태는 앞모습(Down)으로 초기화
        this.frameRects = walkDownFrames
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)

        val angle = joystick.angle
        val power = joystick.power

        if (power > 0f) {
            val dx = cos(angle) * power
            val dy = sin(angle) * power

            // 롤 Y키 고정 카메라 방식 유지 (배경을 반대로 밀기!)
            bg.x -= dx * speed
            bg.y -= dy * speed

            // 🕹️ 조이스틱 각도 분석을 통한 4방향 걷기(WALK) 애니메이션 제어 공식
            // 1. 우측 이동 (각도 약 0도 부근)
            if (angle > -Math.PI / 4 && angle <= Math.PI / 4) {
                this.frameRects = walkRightFrames
            }
            // 2. 하단 이동 (각도 약 90도 부근)
            else if (angle > Math.PI / 4 && angle <= 3 * Math.PI / 4) {
                this.frameRects = walkDownFrames
            }
            // 3. 상단 이동 (각도 약 -90도 부근)
            else if (angle > -3 * Math.PI / 4 && angle <= -Math.PI / 4) {
                this.frameRects = walkUpFrames
            }
            // 4. 좌측 이동 (나머지 180도 및 -180도 부근)
            else {
                this.frameRects = walkLeftFrames
            }
        }

        syncDstRect()
    }
}