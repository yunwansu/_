package controllers

import javax.inject.Inject

import com.dsf.example.play.models.entities.PostalCode
import com.dsf.example.play.models.pgsql.PostalCodeDAO
import play.api.libs.json._
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
    println(request.body.asFormUrlEncoded.get.get("search[value]").get.head)
    //println(request.getQueryString("draw"))
    println(RquestBody())
      println(request.body)
      val draw = request.body.asFormUrlEncoded.get.get("draw").get.head
      val data = request.body.asFormUrlEncoded.get.get("search[value]").get.head
    //  val recordsTotal = request.getQueryString("recordsTotal").get.toInt
    //  val recordsFiltered = request.getQueryString("recordsFiltered").get.toInt
      val count = Await.result(postalCodeDAO.filterCount(s"$data"), Duration.Inf)
      //val result = Await.result(postalCodeDAO.findmanageNumberOfBuilding(s"$data"), Duration.Inf)
        postalCodeDAO.PostalCodes_search(s"$data").map(result =>{
          if(result.isEmpty){
            Ok("{ \"draw\":"+draw+", \"recordsTotal\":"+6198481+", \"recordsFiltered\":"+count+", \"aaData\":{}}").as("application/json charset='utf-8'")
          }else{
            val list = result.map(ps =>{
              (PostalCode.apply _).tupled(ps.postalCode, ps.streetNumberAddress, ps.additionalAddress)
            })
            val data = "{ \"draw\":"+draw+", \"recordsTotal\":"+6198481+", \"recordsFiltered\":"+count+", \"aaData\":"+Json.toJson(list)+"}"
            /*Result(
              header = ResponseHeader(200, Map(CONTENT_LENGTH -> data.toString.length.toString, CONTENT_TYPE -> "application/json charset='utf-8'")),
              body = data
            )*/
            Ok(data).as("application/json charset='utf-8'")
          }
        })
     // result.foreach(println)
     /* val json = Json.obj(
        "draw" -> JsNumber(draw),
        "recordsTotal"->JsNumber(6198481),
        "recordsFiltered"->JsNumber(1)
      )*/
      //println(json)
     // Ok("{ \"draw\":"+draw+", \"recordsTotal\":"+6198481+", \"recordsFiltered\":"+count+", \"aaData\":["+Json.toJson(result)+"]}").as("app366+lication/json charset='utf-8'") // find?managecode=3611010500105120002000009
      //Ok
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

      Ok("{\"aaData\":"+Json.toJson(list)+"}").as("application/json charset='utf-8'")
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