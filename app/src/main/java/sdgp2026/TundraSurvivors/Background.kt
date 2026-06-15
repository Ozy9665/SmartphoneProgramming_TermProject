package sdgp2026.TundraSurvivors

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class Background(gctx: GameContext) : Sprite(gctx, R.mipmap.bg_tundra) {

    init {
        this.x = 0f
        this.y = 0f

        // 아까 해결한 안전한 Rect 생성 방식 유지!
        this.srcRect = Rect(0, 0, this.bitmap!!.width, this.bitmap!!.height)
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)
        // syncDstRect()는 직접 3x3으로 수동 제어하므로 여기서는 생략해도 안전합니다!
    }

    // [핵심 추가] 엔진의 기본 그리기를 넘어서서, 무한 타일링 그리기를 수행합니다!
    override fun draw(canvas: Canvas) {
        // 눈밭 한 칸이 렌더링될 거대한 월드 크기 정의
        val tileW = 2000f
        val tileH = 2000f

        // 아무리 좌표가 멀어져도 끊기지 않도록 음수 방지형 Modulo 오프셋 계산 계산기 작동!
        val leftOffset = ((this.x % tileW) + tileW) % tileW - tileW
        val topOffset = ((this.y % tileH) + tileH) % tileH - tileH

        // 내 화면을 중심으로 주변 3x3 영역에 눈밭 딱지를 빈틈없이 덧붙여 도배합니다.
        for (i in -1..2) {
            for (j in -1..2) {
                val drawX = leftOffset + (i * tileW)
                val drawY = topOffset + (j * tileH)

                val dstRect = RectF(drawX, drawY, drawX + tileW, drawY + tileH)
                canvas.drawBitmap(this.bitmap!!, srcRect, dstRect, null)
            }
        }
    }
}