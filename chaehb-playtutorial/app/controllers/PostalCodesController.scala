/*package controllers

import javax.inject.Inject

import com.dsf.example.play.models.entities.StreetNumberAddressPostalCode
import com.dsf.example.play.models.pgsql.PostalCodeDAO
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by chaehb on 22/08/2016.
  */
class PostalCodesController @Inject()(postalCodeDAO: PostalCodeDAO)(implicit ec: ExecutionContext) extends Controller {

  def streetNameAddresses(offset:Long = 0, length:Int = 25) = Action.async {implicit request =>
    postalCodeDAO.list(offset,length).map(results =>{
      if(results.isEmpty){
        Ok
      }else{
        val list = results.map(ps =>{
          (StreetNumberAddressPostalCode.apply _).tupled(ps)
        })

        Ok(Json.toJson(list))
      }
    })
  }
}
*/