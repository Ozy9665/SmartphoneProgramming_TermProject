package sdgp2026.TundraSurvivors

import android.graphics.Rect
import android.media.MediaPlayer
import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.Sprite

class TitleBackground(gctx: GameContext) : Sprite(gctx, R.mipmap.title) {
    init {
        this.x = gctx.metrics.width / 2f
        this.y = gctx.metrics.height / 2f
        this.width = gctx.metrics.width
        this.height = gctx.metrics.height

        this.srcRect = Rect(0, 0, this.bitmap!!.width, this.bitmap!!.height)
        syncDstRect()
    }
}
private var isStarting = false
private var startTimer = 0
class TitleScene(gctx: GameContext) : Scene(gctx) {

    override val world = World(Layer.values())

    private val titleSprite = TitleBackground(gctx)
    private var bgm: MediaPlayer? = null

    init {
        world.add(titleSprite, Layer.BG)

        bgm = MediaPlayer.create(MainActivity.mContext, R.raw.bgm_title)
        bgm?.isLooping = true
    }
    override fun update(gctx: GameContext) {
        super.update(gctx)
        startTimer++

        if (startTimer == 30) {
            bgm?.start()
        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP && !isStarting) {
            isStarting = true
            bgm?.stop()
            bgm?.release()
            bgm = null
            gctx.sceneStack.push(MainScene(gctx))
        }
        return true
    }
}