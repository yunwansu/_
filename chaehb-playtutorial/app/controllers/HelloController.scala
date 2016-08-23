package controllers

import javax.inject.Inject

import com.dsf.example.play.models.pgsql.PostalCodeDAO
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by chaehb on 8/4/16.
  */
class HelloController @Inject()(postalCodeDAO: PostalCodeDAO)(implicit ec: ExecutionContext) extends Controller {
  def hello(name:String) = Action.async {
    Future {
      //
      Ok("Hello, " + name)
    }
  }
  def find(managecode :String = "")= Action.async {
    Future {
      val result = Await.result(postalCodeDAO.findmanageNumberOfBuilding(managecode),Duration.Inf)
      result.foreach(println)
      Ok(Json.toJson(result)) // find?managecode=3611010500105120002000009
      //Ok
    }
  }
}

// Synchronize - blocking
// Asynchronize - unblocking

// Thread -

// Future