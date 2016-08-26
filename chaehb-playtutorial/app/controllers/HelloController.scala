package controllers

import javax.inject.Inject

import com.dsf.example.play.models.entities.{DataTables_ResponData, PostalCode}
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
  def find = Action.async { implicit request =>
    println(request.body.asFormUrlEncoded.get.get("search[value]").get.head)
    //println(request.queryString.get("draw"))
    //println(request.body.asFormUrlEncoded.get.toString())
    //println(request.getQueryString("draw"))
    //println(RquestBody())
     // println(request.body)
    val draw               = request.body.asFormUrlEncoded.get.get("draw").get.head.toInt
    val data               = request.body.asFormUrlEncoded.get.get("search[value]").get.head
    val length             = request.body.asFormUrlEncoded.get.get("length").get.head.toInt
    val search_list_length = postalCodeDAO.filterCount(data).toString match {
      case i => i.toInt
    }
    //  val recordsTotal = request.getQueryString("recordsTotal").get.toInt
    //  val recordsFiltered = request.getQueryString("recordsFiltered").get.toInt

    postalCodeDAO.PostalCodes_search(data, length).map(result =>{
          if(result.isEmpty){
            Ok(Json.toJson(DataTables_ResponData(draw)))
          }else{
            val list = result.map(ps =>{
              (PostalCode.apply _).tupled(ps.postalCode, ps.streetNumberAddress, ps.additionalAddress)
            })
            Ok(Json.toJson(DataTables_ResponData(draw, 6198481, search_list_length, list)))
          }
        })
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