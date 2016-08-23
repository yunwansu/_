package com.dsf.example.play.models.entities

import java.util.UUID

import play.api.libs.json.Json

/**
  * Created by chaehb on 11/08/2016.
  */
case class PostalCode(
                       uuid:UUID,

                       postalCode:Option[String]=None, //우편번호 - 0

                        streetNumberAddress: StreetNumberAddress,

                        additionalAddress: AdditionalAddress
                     )

case class StreetNumberAddress(
                                province:Option[String]=None, provinceEn:Option[String]=None, // 시도 - 1, 2
                                county:Option[String]=None, countyEn:Option[String]=None, //시군구 - 3, 4
                                town:Option[String]=None, townEn:Option[String]=None, //읍면동 - 5, 6

                                streetNameCode:Option[String]=None, streetName:Option[String]=None, streetNameEn:Option[String]=None // 도로명코드, 도로명 - 7, 8, 9
                              )

case class AdditionalAddress(
                              basement:Option[String]=None, mountain:Option[String]=None, // '지하'여부, '산'여부 - 10, 20

                              primaryNumberOfBuilding:Option[String]=None, secondaryNumberOfBuilding:Option[String]=None, manageNumberOfBuilding:Option[String]=None,// 건물번호- 본번, 부번, 관리번호 - 11, 12, 13
                              massDeliveryName:Option[String]=None, // 대량배달처명 - 14
                              localBuildingName:Option[String]=None, // 시군구용건물명 - 15
                              customaryTownCode:Option[String]=None, customaryTownName:Option[String]=None, // 법정동코드, 법정동명 - 16, 17
                              village:Option[String]=None, // '리'명 - 18
                              administrativeTownName:Option[String]=None, // 행정동명 - 19
                              primaryLotNumber:Option[String]=None,secondaryLotNumber:Option[String]=None, // 지번 -  본번, 부번 - 21, 23
                              serialNumberOfTown:Option[String]=None, // 읍면동 일련번호 - 22
                              previousPostalCode:Option[String]=None, // 구우편번호 - 24
                              serialNumberOfPostalCode:Option[String]=None // 우편번 일련번호 - 25
                            )

case class StreetNumberAddressPostalCode(uuid:UUID,postalCode:Option[String],province:Option[String], county:Option[String], town:Option[String], streetNameCode:Option[String], streetName:Option[String])
object StreetNumberAddressPostalCode {
  implicit val jsonFormat = Json.format[StreetNumberAddressPostalCode]

//  def tupled = (this.apply _).tupled
}