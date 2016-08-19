package controllers

import java.io.File
import javax.inject.Inject

import com.dsf.example.play.ApplicationConfig
import com.dsf.example.play.models.entities.{AdditionalAddress, PostalCode, StreetNumberAddress}
import com.dsf.example.play.models.pgsql.PostalCodeDAO
import play.api.mvc.{Action, Controller}

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source

/**
  * Created by chaehb on 8/18/16.
  */
class DatabaseSetupController @Inject()(postalCodeDAO: PostalCodeDAO)(implicit ec: ExecutionContext) extends Controller {

  def setup = Action.async {implicit request =>
println("do setup")
      postalCodeDAO.createTable.onComplete(_ => {

        postalCodeDAO.count.map(count => {
          if(count == 0){
            println("insert postal code data ....")

            val dataDirectory = new File("data/K_POSTAL")
            if(dataDirectory.exists()){
              val revisions = dataDirectory.listFiles().filter(_.isDirectory).sortBy(_.getName)
              if(revisions.nonEmpty) {
                val dataFiles = revisions.last.listFiles().filter(f => f.getName.endsWith(".txt"))
                dataFiles.foreach(f => {
                  println(f.getName)
                  val codes = Source.fromFile(f,"UTF-8")

                  codes.getLines().filterNot(l => l.startsWith("새우편번호")).foreach(line =>{
                    val columns = line.split('|')
                    val streetNumberAddress = StreetNumberAddress(
                      Some(columns(1)),Some(columns(2)),
                      Some(columns(3)),Some(columns(4)),
                      Some(columns(5)),Some(columns(6)),
                      Some(columns(7)),Some(columns(8)),Some(columns(9))
                    )

                    val additionalAddress = if(columns.length == 26) {
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
                    }else if(columns.length == 25) {
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
                        Some(" ")
                      )
                    }else{
                      println(columns(23))
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
                        Some(" "),
                        Some(" ")
                      )
                    }

                    val postalCode = PostalCode(Some(columns(0)),streetNumberAddress,additionalAddress)

                    postalCodeDAO.insertPostalCode(postalCode)
                  })
                }) // end File.txt
                ApplicationConfig.DataBaseReady = true //Change DatabaseReady flag
              }
            }
          }else{
            println("error")
          }
        })
      })

    Future(Ok)
  }
}
