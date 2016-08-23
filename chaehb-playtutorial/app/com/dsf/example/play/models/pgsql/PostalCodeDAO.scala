package com.dsf.example.play.models.pgsql

import java.util.UUID
import javax.inject.Inject

import com.dsf.example.play.models.entities._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

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

  def insertPostalCode(row:PostalCode) = db.run {
    tableQuery += row
  }

  def insertPostalCodes(rows:List[PostalCode]) = db.run {
    tableQuery ++= rows
  }

  def readPostalCode(uuid:UUID):Future[PostalCode] = db.run{
    val dbio = (for{
      postalCode <- tableQuery if postalCode.uuid === uuid
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
    } yield
      (postalCode.uuid, postalCode.postalCode,
      postalCode.province, postalCode.county, postalCode.town,
      postalCode.streetNameCode,postalCode.streetName)).result

    dbio
  }

  private class PostalCodesTable(tag: Tag) extends Table[PostalCode](tag,"PostalCodes") {
    def uuid = column[UUID]("uuid", O.PrimaryKey)
    // primary informations
    def postalCode = column[Option[String]]("postal_code")

    def province = column[Option[String]]("province")
    def provinceEn = column[Option[String]]("province_en")
    def county = column[Option[String]]("county")
    def countyEn = column[Option[String]]("county_en")
    def town = column[Option[String]]("town")
    def townEn = column[Option[String]]("town_en")
    def streetNameCode = column[Option[String]]("street_name_code")
    def streetName = column[Option[String]]("street_name")
    def streetNameEn = column[Option[String]]("street_name_en")

    // additional informations
    def basement = column[Option[String]]("basement")
    def mountain = column[Option[String]]("mountain")

    def primaryNumberOfBuilding = column[Option[String]]("primary_number_of_building")
    def secondaryNumberOfBuilding = column[Option[String]]("secondary_number_of_building")
    def manageNumberOfBuilding = column[Option[String]]("manage_number_of_building")

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

    private type PostalCodeTupleType = (UUID,Option[String],StreetNumberAddressTupleType,AdditionalAddressTupleType)


    private val toRow:(PostalCodeTupleType => PostalCode) = {tuple =>
      PostalCode(
        uuid = tuple._1,
        postalCode = tuple._2,
        streetNumberAddress = StreetNumberAddress.tupled.apply(tuple._3),
        additionalAddress = AdditionalAddress.tupled.apply(tuple._4)
      )
    }

    private val toTuple:(PostalCode => Option[PostalCodeTupleType]) = { code =>
      Some(
        code.uuid,
        code.postalCode,
        StreetNumberAddress.unapply(code.streetNumberAddress).get,
        AdditionalAddress.unapply(code.additionalAddress).get
      )
    }

    override def * = (
      uuid ,
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

    def idxPostalCodesPostalCode = index("idx_postal_codes_postalCode", postalCode, unique = false)

    def idxPostalCodesProvince = index("idx_postal_codes_province", province, unique = false)
    def idxPostalCodesProvinceEn = index("idx_postal_codes_provinceEn", provinceEn, unique = false)

    def idxPostalCodesCounty = index("idx_postal_codes_county", county, unique = false)
    def idxPostalCodesCountyEn = index("idx_postal_codes_countyEn", countyEn, unique = false)

    def idxPostalCodesTown = index("idx_postal_codes_town", town, unique = false)
    def idxPostalCodesTownEn = index("idx_postal_codes_townEn", townEn, unique = false)

    def idxPostalCodesStreetName = index("idx_postal_codes_streetName", streetName, unique = false)
    def idxPostalCodesStreetNameEn = index("idx_postal_codes_streetNameEn", streetNameEn, unique = false)
  }

}
