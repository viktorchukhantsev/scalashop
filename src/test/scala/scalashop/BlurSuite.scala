package scalashop

import java.util.concurrent.*
import scala.collection.*

class BlurSuite extends munit.FunSuite:

  val testVerticalImageResults = List(
    (0, 0, 4),
    (1, 0, 5),
    (2, 0, 5),
    (3, 0, 6),
    (0, 1, 4),
    (1, 1, 5),
    (2, 1, 5),
    (3, 1, 6),
    (0, 2, 4),
    (1, 2, 5),
    (2, 2, 5),
    (3, 2, 6),
  )

  val testHorizontalImageResults = List(
    (0, 0, 2),
    (1, 0, 2),
    (2, 0, 3),
    (0, 1, 3),
    (1, 1, 4),
    (2, 1, 4),
    (0, 2, 0),
    (1, 2, 0),
    (2, 2, 0),
  )

  test("boxBlurKernel zero radius edge case") {
    val src = Img(5, 5)
    for {
      x <- 0 until 5
      y <- 0 until 5
    } yield src.update(x, y, rgba(x, y, x + y, math.abs(x - y)))

    for {
      x <- 0 until 5
      y <- 0 until 5
    } assert(
      boxBlurKernel(src, x, y, 0) == rgba(x, y, x + y, math.abs(x - y)),
      "boxBlurKernel(_,_,0) should be identity.",
    )
  }

  test("boxBlurKernel return correct value of image with radius 1") {
    val src = new Img(3, 4)
    src(0, 0) = 0; src(1, 0) = 1; src(2, 0) = 2
    src(0, 1) = 3; src(1, 1) = 4; src(2, 1) = 5
    src(0, 2) = 6; src(1, 2) = 7; src(2, 2) = 8
    src(0, 3) = 100; src(1, 3) = 23; src(2, 3) = 18

    assert(boxBlurKernel(src, 1, 2, 1) == 19, "boxBlurKernel(1, 2, 1) should be 12")
  }

  test("VerticalBoxBlur.blur radius 2") {
    val w = 4
    val h = 3
    val src = new Img(w, h)
    val dst = new Img(w, h)
    src(0, 0) = 0; src(1, 0) = 1; src(2, 0) = 2; src(3, 0) = 9
    src(0, 1) = 3; src(1, 1) = 4; src(2, 1) = 5; src(3, 1) = 10
    src(0, 2) = 6; src(1, 2) = 7; src(2, 2) = 8; src(3, 2) = 11

    VerticalBoxBlur.blur(src, dst, 0, 4, 2)

    testVerticalImageResults.foreach { case (x, y, expected) =>
      assert(dst(x, y) == expected, s"${dst(x, y)} != ${expected}")
    }
  }

  test("VerticalBoxBlur.parBlur radius 2") {
    val w = 4
    val h = 3
    val src = new Img(w, h)
    val dst = new Img(w, h)
    src(0, 0) = 0; src(1, 0) = 1; src(2, 0) = 2; src(3, 0) = 9
    src(0, 1) = 3; src(1, 1) = 4; src(2, 1) = 5; src(3, 1) = 10
    src(0, 2) = 6; src(1, 2) = 7; src(2, 2) = 8; src(3, 2) = 11

    VerticalBoxBlur.parBlur(src, dst, 2, 2)

    testVerticalImageResults.foreach { case (x, y, expected) =>
      assert(dst(x, y) == expected, s"${dst(x, y)} != ${expected}")
    }
  }

  test("HorizontalBoxBlur.blur radius 1") {
    val w = 3
    val h = 3
    val src = new Img(w, h)
    val dst = new Img(w, h)
    src(0, 0) = 0; src(1, 0) = 1; src(2, 0) = 2
    src(0, 1) = 3; src(1, 1) = 4; src(2, 1) = 5
    src(0, 2) = 6; src(1, 2) = 7; src(2, 2) = 8

    HorizontalBoxBlur.blur(src, dst, 0, 2, 1)

    testHorizontalImageResults.foreach { case (x, y, expected) =>
      assert(dst(x, y) == expected, s"x=${x} y=${y} ${dst(x, y)} != ${expected}")
    }
  }

  test("HorizontalBoxBlur.parBlur radius 1".ignore){
    val w = 3
    val h = 3
    val src = new Img(w, h)
    val dst = new Img(w, h)
    src(0, 0) = 0; src(1, 0) = 1; src(2, 0) = 2
    src(0, 1) = 3; src(1, 1) = 4; src(2, 1) = 5
    src(0, 2) = 6; src(1, 2) = 7; src(2, 2) = 8

    HorizontalBoxBlur.parBlur(src, dst, 2, 1)

    testHorizontalImageResults.foreach { case (x, y, expected) =>
      assert(dst(x, y) == expected, s"x=${x} y=${y} ${dst(x, y)} != ${expected}")
    }
  }
