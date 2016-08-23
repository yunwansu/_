package controllers

import java.io.File
import java.util.UUID
import javax.inject.Inject

import com.dsf.example.play.{ApplicationConfig, DBState}
import com.dsf.example.play.models.entities.{AdditionalAddress, PostalCode, StreetNumberAddress}
import com.dsf.example.play.models.pgsql.{PostalCodeDAO, SampleTableDAO}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success}

/**
  * Created by chaehb on 8/18/16.
  */
class DatabaseSetupController @Inject()(postalCodeDAO: PostalCodeDAO,sampleTableDAO: SampleTableDAO)(implicit ec: ExecutionContext) extends Controller {

  def setup = Action.async {implicit request =>
    val dbState = ApplicationConfig.DatabaseState

    ApplicationConfig.DatabaseState = DBState.Initializing

    dbState match {
      case DBState.NotInitialized =>
        initializeDatabase(dbState)
      case DBState.NoData =>
        Future(insertPostalData(dbState))
      case _ =>
        ApplicationConfig.DatabaseState = dbState
        Future(BadRequest("database already has been initialized."))
    }
  }

  def initializeDatabase(dbState: Int) = {
    postalCodeDAO.createTable.map { _ =>
      insertPostalData(dbState)
    }
  }

  def insertPostalData(dbState:Int) = {
    println("insert postal code data ....")

    val dataDirectory = new File("data/K_POSTAL")
    if (dataDirectory.exists()) {
      val revisions = dataDirectory.listFiles().filter(_.isDirectory).sortBy(_.getName)
      if (revisions.nonEmpty) {
        val dataFiles = revisions.last.listFiles().filter(f => f.getName.endsWith(".txt"))

        var itemCount:Long = 0
        dataFiles.foreach(f => {
          val len = Source.fromFile(f, "x-windows-949").getLines().length
          itemCount += len

          println(f.getName + " : " + len)
        })
        println("===============")
        println("total items : "+itemCount)
        println("===============")

        var totalRows = 0L
        dataFiles.foreach(f => {
          val codes = Source.fromFile(f, "x-windows-949")

          println(f.getName)

//          var postalCodeItems:List[PostalCode] = List.empty[PostalCode]

          var rowCount = 0L
          codes.getLines().filterNot(l => l.startsWith("새우편번호")).foreach(line => {
            val columns = line.split("\\|")
            val streetNumberAddress = StreetNumberAddress(
              Some(columns(1)), Some(columns(2)),
              Some(columns(3)), Some(columns(4)),
              Some(columns(5)), Some(columns(6)),
              Some(columns(7)), Some(columns(8)), Some(columns(9))
            )

            val additionalAddress = if (columns.length == 26) {
              println(line)

              AdditionalAddress(
                Some(columns(10)), Some(columns(20)),
                Some(columns(11)), Some(columns(12)), Some(columns(13)),
                Some(columns(14)),
                Some(columns(15)),
                Some(columns(16)), Some(columns(17)),
                Some(columns(18)),
                Some(columns(19)),
                Some(columns(21)), Some(columns(23)),
                Some(columns(22)),
                Some(columns(24)),
                Some(columns(25))
              )
            } else if (columns.length == 25) {
              println(line)

              AdditionalAddress(
                Some(columns(10)), Some(columns(20)),
                Some(columns(11)), Some(columns(12)), Some(columns(13)),
                Some(columns(14)),
                Some(columns(15)),
                Some(columns(16)), Some(columns(17)),
                Some(columns(18)),
                Some(columns(19)),
                Some(columns(21)), Some(columns(23)),
                Some(columns(22)),
                Some(columns(24)),
                Some("")
              )
            } else {
              AdditionalAddress(
                Some(columns(10)), Some(columns(20)),
                Some(columns(11)), Some(columns(12)), Some(columns(13)),
                Some(columns(14)),
                Some(columns(15)),
                Some(columns(16)), Some(columns(17)),
                Some(columns(18)),
                Some(columns(19)),
                Some(columns(21)), Some(columns(23)),
                Some(columns(22)),
                Some(""),
                Some("")
              )
            }

        postalCodeDAO.insertPostalCode(PostalCode(UUID.randomUUID(), Some(columns(0)), streetNumberAddress, additionalAddress))
              .onComplete{
                case Success(cnt) => if(cnt!=1) println("success : "+cnt)
                case Failure(err) => println("failure : "+err)
//                case _=>
              }

//            postalCodeItems :+= PostalCode(UUID.randomUUID(), Some(columns(0)), streetNumberAddress, additionalAddress)

            totalRows += 1L
            rowCount += 1L


//            if(postalCodeItems.length == 128) {
//              postalCodeDAO.insertPostalCodes(postalCodeItems)
//
//              postalCodeItems = List.empty[PostalCode]
//            }
          })
//          if(postalCodeItems.nonEmpty){
//            postalCodeDAO.insertPostalCodes(postalCodeItems)
//          }
          println(rowCount)
        })

        println("total rows : "+totalRows)
        println("end data input.")
        ApplicationConfig.DatabaseState = DBState.Ready
//        Ok
      }else{
        ApplicationConfig.DatabaseState = dbState
//        BadRequest("no input data")
      }
    } else {
      ApplicationConfig.DatabaseState = dbState
//      BadRequest("no input data")
    }

    Ok("completed")
  }
}
