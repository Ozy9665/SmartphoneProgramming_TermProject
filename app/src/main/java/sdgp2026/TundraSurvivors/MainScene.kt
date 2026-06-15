package sdgp2026.TundraSurvivors

import android.view.MotionEvent
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.Scene
import kr.ac.tukorea.ge.spgp2026.a2dg.scene.World
import kr.ac.tukorea.ge.spgp2026.a2dg.view.GameContext
import kr.ac.tukorea.ge.spgp2026.a2dg.objects.JoyStick

enum class Layer {
    BG, PLAYER, UI
}

class MainScene(gctx: GameContext) : Scene(gctx) {

    override val world = World(Layer.values())

    private val background = Background(gctx)

    // [수정] 진짜 조이스틱 이미지(joystick_bg, joystick_thumb)를 연결합니다!
    // 가로 모드 화면 구조에 맞춰서 크기(반지름 130, 45)와 배치 좌표(250f, 750f)를 최적화했습니다.
    val joystick = JoyStick(
        gctx,
        R.mipmap.joystick_bg,
        R.mipmap.joystick_thumb,
        250f, 750f, 130f, 45f
    )

    private val player = Player(gctx, joystick, background)

    init {
        world.add(background, Layer.BG)
        world.add(player, Layer.PLAYER)
        world.add(joystick, Layer.UI)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (joystick.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }
}