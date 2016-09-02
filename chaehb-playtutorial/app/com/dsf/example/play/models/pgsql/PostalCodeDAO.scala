package com.dsf.example.play.models.pgsql

//import java.util.UUID
import javax.inject.Inject

import akka.actor.Status.Success
import com.dsf.example.play.models.entities._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Created by chaehb on 11/08/2016.
  */

class PostalCodeDAO  @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import driver.api._

  private val tableQuery = TableQuery[PostalCodesTable]

  def createTable = db.run{
     tableQuery.schema.create
  }

  def count = db.run {
    tableQuery.length.result
  }

  def filterCount(data:String) = Await.result(db.run{
    tableQuery.filter(postalCode => postalCode.province === data || postalCode.provinceEn === data || postalCode.county === data || postalCode.countyEn === data ||
      postalCode.town === data || postalCode.townEn === data || postalCode.streetName === data || postalCode.streetNameEn === data).length.result

  }, Duration.Inf)

  def close = db.close()

  def insertPostalCode(row:PostalCode) = Await.result(db.run {
    tableQuery += row
  }, Duration.Inf)

  def insertPostalCodes(rows:List[PostalCode]) = Await.result(db.run {
    tableQuery ++= rows
  }, Duration.Inf)

  def all:Future[Seq[PostalCode]] = db.run(
    tableQuery.result
  )

  def findbycounty(county :String) = db.run {
    val dbio = (for {
      postalCode <- tableQuery if postalCode.county === county
    } yield postalCode).result

    dbio
  }

  def listcounty(county:String = ""):Future[PostalCode] = db.run{
    val dbio = (for{
      postalCode <- tableQuery if postalCode.county === county
    } yield postalCode).result.headOption.flatMap({
      case Some(postalCode) =>
        DBIO.successful(postalCode)
      case None =>
        DBIO.successful(null)
    })

    dbio
  }

  def PostalCodes_search(data:String, list_start:Int = 0, list_length:Int = 10) = db.run{
    val dbio = (for{
      postalCode <- tableQuery if (postalCode.province === data || postalCode.provinceEn === data || postalCode.county === data || postalCode.countyEn === data ||
                                    postalCode.town === data || postalCode.townEn === data || postalCode.streetName === data || postalCode.streetNameEn === data)
    }yield postalCode).drop(list_start).take(list_length).result
    //tableQuery.filter(_.manageNumberOfBuilding === manageNumber).result.headOption

    dbio
  }



  def readPostalCode(county:String = ""):Future[PostalCode] = db.run{
    val dbio = (for{
      postalCode <- tableQuery if postalCode.county === county
    } yield postalCode).result.headOption.flatMap({
      case Some(postalCode) =>
        DBIO.successful(postalCode)
      case None =>
        DBIO.successful(null)
    })

    dbio
  }

  def list(offset:Long, length:Int) = db.run{
    val dbio = (for{
      postalCode <- tableQuery.sortBy(ps=>ps.streetName.asc).drop(offset).take(length)
    } yield postalCode).result

    dbio
  }



  private class PostalCodesTable(tag: Tag) extends Table[PostalCode](tag,"postal_codes") {
    // primary informations
    def postalCode = column[Option[String]]("postal_code")

    def province = column[Option[String]]("province")                            //인덱스
    def provinceEn = column[Option[String]]("province_en")                      //인덱스
    def county = column[Option[String]]("county")                                //인덱스
    def countyEn = column[Option[String]]("county_en")                          //인덱스
    def town = column[Option[String]]("town")                                    //인덱스
    def townEn = column[Option[String]]("town_en")                               //인덱스
    def streetNameCode = column[Option[String]]("street_name_code")
    def streetName = column[Option[String]]("street_name")                        //인덱스
    def streetNameEn = column[Option[String]]("street_name_en")                   //인덱스

    // additional informations
    def basement = column[Option[String]]("basement")
    def mountain = column[Option[String]]("mountain")

    def primaryNumberOfBuilding = column[Option[String]]("primary_number_of_building")
    def secondaryNumberOfBuilding = column[Option[String]]("secondary_number_of_building")
    def manageNumberOfBuilding = column[Option[String]]("manage_number_of_building", O.PrimaryKey)

    def massDeliveryName = column[Option[String]]("mass_delivery_name")
    def localBuildingName = column[Option[String]]("local_building_name")

    def customaryTownCode = column[Option[String]]("customary_town_code")
    def customaryTownName = column[Option[String]]("customary_town_name")

    def village = column[Option[String]]("village")

    def adminstrativeTownName = column[Option[String]]("administrative_town_name")

    def primaryLotNumber = column[Option[String]]("primary_lot_number")
    def secondaryLotNumber = column[Option[String]]("secondary_lot_number")

    def serialNumberOfTown = column[Option[String]]("serial_number_of_town")

    def previousPostalCode = column[Option[String]]("previous_postal_code")

    def serialNumberOfPostalCode = column[Option[String]]("serial_number_of_postal_code")

    private type StreetNumberAddressTupleType = (
        Option[String],Option[String],Option[String],Option[String],Option[String],
        Option[String],Option[String],Option[String],Option[String]
      )
    private type AdditionalAddressTupleType = (
        Option[String],Option[String],Option[String],Option[String],Option[String],
        Option[String],Option[String],Option[String],Option[String],Option[String],
        Option[String],Option[String],Option[String],Option[String],Option[String], Option[String]
      )

    private type PostalCodeTupleType = (Option[String],StreetNumberAddressTupleType,AdditionalAddressTupleType)


    private val toRow:(PostalCodeTupleType => PostalCode) = {tuple =>
      PostalCode(
       // uuid = tuple._1,
        postalCode = tuple._1,
        streetNumberAddress = StreetNumberAddress.tupled.apply(tuple._2),
        additionalAddress = AdditionalAddress.tupled.apply(tuple._3)
      )
    }

    private val toTuple:(PostalCode => Option[PostalCodeTupleType]) = { code =>
      Some(
        code.postalCode,
        StreetNumberAddress.unapply(code.streetNumberAddress).get,
        AdditionalAddress.unapply(code.additionalAddress).get
      )
    }

    override def * = (
      postalCode,
      (
        province, provinceEn,
        county,countyEn,
        town,townEn,
        streetNameCode, streetName,streetNameEn
      ),
      (
        basement, mountain,
        primaryNumberOfBuilding, secondaryNumberOfBuilding,manageNumberOfBuilding,
        massDeliveryName,localBuildingName,
        customaryTownCode,customaryTownName,
        village,
        adminstrativeTownName,
        primaryLotNumber, secondaryLotNumber,
        serialNumberOfTown,
        previousPostalCode,serialNumberOfPostalCode
        )
      ).shaped[PostalCodeTupleType] <> (
        toRow, toTuple
      )

    def idxPostalCodes = index("idx_postal_codes",(province, provinceEn, county, countyEn, town, townEn, streetName, streetNameEn) )
  }
}