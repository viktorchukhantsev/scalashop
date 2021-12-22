package scalashop

import java.util.concurrent.*
import scala.collection.*

class BlurSuite extends munit.FunSuite:
  test("boxBlurKernel") {
    val canvas = new PhotoCanvas
    canvas.reload()
    val image = canvas.image

    val obtained = boxBlurKernel(image, 2, 2, 2)
    val expected = -15856112
    assertEquals(obtained, expected)
  }
