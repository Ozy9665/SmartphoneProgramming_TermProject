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

        // 👾 플레이어(1000, 500)를 포위하도록 사방 우주 외곽에 몬스터 4마리 스폰!
        world.add(Enemy(gctx, player, 200f, 200f), Layer.PLAYER)   // 좌측 상단
        world.add(Enemy(gctx, player, 1800f, 200f), Layer.PLAYER)  // 우측 상단
        world.add(Enemy(gctx, player, 200f, 900f), Layer.PLAYER)   // 좌측 하단
        world.add(Enemy(gctx, player, 1800f, 900f), Layer.PLAYER)  // 우측 하단
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (joystick.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }
}