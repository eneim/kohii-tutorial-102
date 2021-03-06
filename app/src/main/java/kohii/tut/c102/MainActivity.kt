package kohii.tut.c102

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import kohii.v1.core.Playback
import kohii.v1.core.Playback.Controller
import kohii.v1.exoplayer.Kohii

class MainActivity : AppCompatActivity() {

  companion object {
    const val videoUrl = "https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val container: View = findViewById(R.id.container)
    val playerView: PlayerView = findViewById(R.id.playerView)

    val kohii = Kohii[this]
    kohii.register(this).addBucket(container)

    kohii.setUp(videoUrl) {
      tag = videoUrl
      controller = object : Controller {
        override fun kohiiCanStart(): Boolean = true
        override fun kohiiCanPause(): Boolean = true

        override fun setupRenderer(
          playback: Playback,
          renderer: Any?
        ) {
          if (renderer is PlayerView) {
            renderer.useController = true
            renderer.setControlDispatcher(kohii.createControlDispatcher(playback))
          }
        }

        override fun teardownRenderer(
          playback: Playback,
          renderer: Any?
        ) {
          if (renderer is PlayerView) {
            renderer.useController = false
            renderer.setControlDispatcher(null)
          }
        }
      }
    }
      .bind(playerView)
  }
}
