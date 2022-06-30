package io.github.lmath.redirect

import cats.effect.{Async, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger

object RedirectServer {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    for {
      client <- Stream.resource(EmberClientBuilder.default[F].build)
      _ = client.toString
      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract segments not checked
      // in the underlying routes.
      httpApp = (
        RedirectRoutes.redirectRoute[F]()
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8081")
          .withHttpApp(finalHttpApp)
          .build >>
        Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}