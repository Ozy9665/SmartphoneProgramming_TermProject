package sdgp2026.TundraSurvivors

import android.os.Bundle
import kr.ac.tukorea.ge.spgp2026.a2dg.activity.BaseGameActivity


class MainActivity : BaseGameActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // MainScene 띄우기
    override fun getStartScene(): Scene {
        return MainScene(gameView.gameContext)
    }
}