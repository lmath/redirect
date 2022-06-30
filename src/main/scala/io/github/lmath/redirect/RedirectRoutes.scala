package io.github.lmath.redirect

import cats.effect.Sync
import cats.implicits.catsSyntaxApplicativeId
import org.http4s.{Header, Headers, HttpRoutes, Response, Uri}
import org.http4s.dsl.Http4sDsl
import org.typelevel.ci.CIString

object RedirectRoutes {

  def redirectRoute[F[_]: Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    def response(path: Uri.Path) = {
      Response[F]().withStatus(PermanentRedirect).withHeaders(Headers(Header.Raw(CIString("Location"), s"http://localhost:8080/api/${path.renderString}")))
    }

    HttpRoutes.of[F] {
      case _ -> "api" /: rest =>
        response(rest).pure[F]
    }

  }
}