package io.github.lmath.redirect

import cats.effect.Sync
import cats.implicits.catsSyntaxApplicativeId
import org.http4s.{Header, Headers, HttpRoutes, Response, Uri}
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher
import org.typelevel.ci.CIString

object RedirectRoutes {

  object ReprQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("repr")

  def redirectRoute[F[_]: Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    def response(path: Uri.Path, reprParam: Option[String]) = {
      val location = s"http://localhost:8080/api/${path.renderString}"
      val locationString = reprParam match {
        case Some(repr) => s"$location?repr=$repr"
        case None => location
      }
      Response[F]().withStatus(PermanentRedirect).withHeaders(Headers(Header.Raw(CIString("Location"), locationString)))
    }

    HttpRoutes.of[F] {
      case _ -> "api" /: rest :? ReprQueryParamMatcher(repr) => response(rest, repr).pure[F]
    }

  }
}