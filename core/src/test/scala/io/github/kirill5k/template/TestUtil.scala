package io.github.kirill5k.template

import scala.io.Source

object TestUtil {
  def readFile(path: String): String = Source.fromResource(path).getLines().toList.mkString
}
