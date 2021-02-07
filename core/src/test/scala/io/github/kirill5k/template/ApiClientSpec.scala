package io.github.kirill5k.template

import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j.Slf4jLogger
import io.github.kirill5k.template.CatsSpec
import sttp.client3
import sttp.model.{Header, Method}

import scala.io.Source

trait ApiClientSpec extends CatsSpec {

  def backendStub: SttpBackendStub[IO, Any] =
    AsyncHttpClientCatsBackend.stub[IO]
  
  def json(path: String): String = Source.fromResource(path).getLines().toList.mkString
}

object RequestOps {

  implicit final class RequestSyntax(private val req: client3.Request[_, _]) extends AnyVal {
    def isPost: Boolean =
      req.method == Method.POST

    def isGet: Boolean =
      req.method == Method.GET

    def isPut: Boolean =
      req.method == Method.PUT

    def hasBearerToken(token: String): Boolean =
      req.headers.contains(new Header("Authorization", s"Bearer $token"))

    def hasBody(json: String): Boolean =
      req.body.toString.contains(json)

    def hasHost(host: String): Boolean =
      req.uri.host.contains(host)

    def hasPath(path: String): Boolean =
      req.uri.path == path.split("/").filter(_.nonEmpty).toList

    def isGoingTo(url: String): Boolean = {
      val urlParts = url.split("/")
      hasHost(urlParts.head) && req.uri.path.startsWith(urlParts.tail.filter(_.nonEmpty).toList)
    }

    def bodyContains(body: String): Boolean =
      req.body.toString.contains(body)
  }
}
