# redirect
just a server that always redirects to `localhost:8080`

based on the [http4s quickstart giter8 template](https://github.com/http4s/http4s.g8)

If you clone this repo and then run `sbt run` this will bring up a server on `http:localhost:8081` that does one thing and one thing only:

If you look [here](https://github.com/lmath/redirect/blob/250bef6f15b5d0b6baef86ff079fff1cb2b78974/src/main/scala/io/github/lmath/redirect/RedirectRoutes.scala) you will see that when the server is running, if it receives requests to `<localhost:8081>/api/<anything>` it redirects and passes that request along to <localhost:8081>/api/<anything>`

You might be wondering why it matches on `/api` / rest of url rather than just matching all requests and that is because I found this [`/: extractor`](https://http4s.org/v0.23/docs/dsl.html#matching-paths) handy for getting my test case going as quickly as possible.
