package io.github.lmath.redirect

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    RedirectServer.stream[IO].compile.drain.as(ExitCode.Success)
}
