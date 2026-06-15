package sdgp2026.TundraSurvivors

import android.graphics.Canvas
import android.graphics.Rect
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kotlin.math.cos
import kotlin.math.sin

class Fireball(
    gctx: GameContext,
    spawnX: Float,
    spawnY: Float,
    val angle: Float
) : Sprite(gctx, R.mipmap.fireball) {

    private val moveSpeed = 30f // 골렘보다 훨씬 빠르게 설정
    private val screenW = gctx.metrics.width
    private val screenH = gctx.metrics.height

    init {
        this.x = spawnX
        this.y = spawnY
        this.width = 200f // 불덩어리 크기
        this.height = 200f

        this.srcRect = Rect(0, 0, this.bitmap!!.width, this.bitmap!!.height)
        syncDstRect()
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)

        this.x += cos(angle) * moveSpeed
        this.y += sin(angle) * moveSpeed

        syncDstRect()
    }

    override fun draw(canvas: Canvas) {
        val saveCount = canvas.save()

        val degrees = Math.toDegrees(angle.toDouble()).toFloat() + 180
        canvas.rotate(degrees, this.x, this.y)

        super.draw(canvas)

        canvas.restoreToCount(saveCount)
    }
}