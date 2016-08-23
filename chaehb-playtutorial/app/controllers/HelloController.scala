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
  def find= Action.async { implicit request =>
    println(request.rawQueryString)
    println(request.headers.keys)
    Future {
      val result = Await.result(postalCodeDAO.findmanageNumberOfBuilding("3611010500105120002000009"), Duration.Inf)
      result.foreach(println)
      Ok(Json.toJson(result)) // find?managecode=3611010500105120002000009
      //Ok
    }
  }
  def get(county:String = "") = Action.async{ implicit request =>
      postalCodeDAO.findbycounty(county).map(result=>{
        if(result.isEmpty){
          Ok
        }else{
          val list = result.map(ps =>{
            (PostalCode.apply _).tupled(ps.postalCode, ps.streetNumberAddress, ps.additionalAddress)
          })
          Ok(Json.toJson(list))
        }
      })
  }

def list(offset:Long=0, length:Int=25) = Action.async{implicit request =>
  postalCodeDAO.list(offset,length).map(results =>{
    if(results.isEmpty){
      Ok
    }else{
      val list = results.map(ps =>{
        (PostalCode.apply _).tupled(ps.postalCode, ps.streetNumberAddress, ps.additionalAddress )
      })

      Ok(Json.toJson(list))
    }
  })
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