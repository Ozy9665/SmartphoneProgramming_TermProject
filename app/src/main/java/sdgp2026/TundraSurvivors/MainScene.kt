package sdgp2026.TundraSurvivors

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

    // [수정됨] XML 아이콘 대신, 안전한 player_sprite 를 조이스틱 이미지로 씁니다!
    // (모양은 웃기겠지만 절대 튕기지 않습니다!)
    val joystick = JoyStick(
        gctx,
        R.mipmap.player_sprite, // 조이스틱 배경
        R.mipmap.player_sprite, // 조이스틱 손잡이
        200f, -200f, 150f, 50f
    )

    private val player = Player(gctx, joystick)

    init {
        world.add(background, Layer.BG)
        world.add(player, Layer.PLAYER)
        world.add(joystick, Layer.UI)
    }
}