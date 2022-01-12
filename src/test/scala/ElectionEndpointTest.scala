package com.political.speeches

import org.http4s._
import org.scalatest.OptionValues
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec
import zio.Task

class ElectionEndpointTest extends AnyWordSpec with Matchers with OptionValues with TestRuntime {

  "ElectionEndpoint" when {

    "handling Evaluation route " should {

      "respond with 200 - Ok" when {

        "endpoint was able to parse URL and process CSV" in {
          val request = Request[Task](
            method = Method.GET,
            uri = Uri.unsafeFromString("evaluation?url1=https://people.sc.fsu.edu/~jburkardt/data/csv/addresses.csv")
          )

          runRequest(new ElectionEndpoint())(request).status mustBe Status.Ok
        }
      }

      "respond with 400 - BadRequest" when {

        "endpoint was not received query params" when {

          "result indicates failure" in {
            val request = Request[Task](method = Method.GET, uri = Uri.unsafeFromString("evaluation"))
            runRequest(new ElectionEndpoint())(request).status mustBe Status.BadRequest
          }
        }

        "query param bad format" when {
          "result indicates failure" in {
            val request = Request[Task](method = Method.GET, uri = Uri.unsafeFromString("evaluation?url=wrongUrl"))
            runRequest(new ElectionEndpoint())(request).status mustBe Status.BadRequest
          }
        }

      }
    }
  }

  private def runRequest(endpoint: ElectionEndpoint)(
    request: Request[Task]
  ): Response[Task] =
    unsafeRun(endpoint.route.run(request))

}
