package controllers

import javax.inject.Inject

import com.dsf.example.play.models.pgsql.{PostalCodeDAO, SampleTableDAO}
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by chaehb on 8/4/16.
  */
class HelloController @Inject()(postalCodeDAO: PostalCodeDAO,sampleTableDAO: SampleTableDAO)(implicit ec: ExecutionContext) extends Controller {
  def hello(name:String) = Action.async {
    Future {
      //
      Ok("Hello, " + name)
    }
  }
}

// Synchronize - blocking
// Asynchronize - unblocking

// Thread -

// Future