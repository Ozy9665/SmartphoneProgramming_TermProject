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

        this.srcRect = Rect(0, 0, this.bitmap!!.width, this.bitmap!!.height)
    }

    override fun update(gctx: GameContext) {
        super.update(gctx)
    }

    override fun draw(canvas: Canvas) {
        val tileW = 2000f
        val tileH = 2000f

        val leftOffset = ((this.x % tileW) + tileW) % tileW - tileW
        val topOffset = ((this.y % tileH) + tileH) % tileH - tileH

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