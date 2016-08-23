package controllers

import javax.inject.Inject

import com.dsf.example.play.models.entities.PostalCode
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
  def get(county : String = "") = Action.async{ implicit request =>
    Future {
      val list = postalCodeDAO.listcounty(county).map( result =>{
        (PostalCode.apply _).tupled(result.postalCode, result.streetNumberAddress, result.additionalAddress)
      })

      Ok(Json.toJson(list.value.get))
    }
  }

  def all = Action.async {
    Future {
      val result = Await.result(postalCodeDAO.all, Duration.Inf)
      Ok(Json.toJson(result))
    }
  }
}

// Synchronize - blocking
// Asynchronize - unblocking

// Thread -

// Future