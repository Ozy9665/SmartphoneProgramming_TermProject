package sdgp2026.TundraSurvivors

import android.content.Context
import android.os.Bundle
import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext

class MainActivity : BaseGameActivity() {
    companion object {
        lateinit var mContext: Context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mContext = this

        super.onCreate(savedInstanceState)
    }

    override fun createRootScene(gctx: GameContext): Scene {
        return TitleScene(gctx)
    }
}