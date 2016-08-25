package com.dsf.example.play.models.entities

import play.api.libs.json.Json

/**
  * Created by chaehb on 11/08/2016.
  */
case class PostalCode(
                       //uuid:UUID,

                       postalCode:Option[String]=None, //우편번호 - 0

                        streetNumberAddress: StreetNumberAddress,

                        additionalAddress: AdditionalAddress
                     )
object PostalCode {
  implicit val streetFormat = Json.format[StreetNumberAddress]
  implicit val additionalFromat = Json.format[AdditionalAddress]
  implicit val jsonFormat = Json.format[PostalCode]/*{
   def writes(p : PostalCode): JsValue = {
     Json.obj(
       "postalCode" -> JsString(p.postalCode.get)
       /*"province" -> JsString(p.streetNumberAddress.province.get),
       "provinceEn" -> JsString(p.streetNumberAddress.provinceEn.get),
       "county" -> JsString(p.streetNumberAddress.county.get),
       "countyEn" -> JsString(p.streetNumberAddress.countyEn.get),
       "town" -> JsString(p.streetNumberAddress.town.get),
       "townEn" -> JsString(p.streetNumberAddress.townEn.get),
       "basement" -> JsString(p.additionalAddress.basement.get),
       "mountain" -> JsString(p.additionalAddress.mountain.get),
       "primaryNumberOfBuilding" -> JsString(p.additionalAddress.primaryNumberOfBuilding.get),
       "secondaryNumberOfBuilding" -> JsString(p.additionalAddress.secondaryNumberOfBuilding.get),
       "manageNumberOfBuilding" -> JsString(p.additionalAddress.manageNumberOfBuilding.get),
       "massDeliveryName" -> JsString(p.additionalAddress.massDeliveryName.get),
       "localBuildingName" -> JsString(p.additionalAddress.localBuildingName.get),
       "customaryTownCode" -> JsString(p.additionalAddress.customaryTownCode.get),
       "customaryTownName" -> JsString(p.additionalAddress.customaryTownName.get),
       "village" -> JsString(p.additionalAddress.village.get),
       "administrativeTownName" -> JsString(p.additionalAddress.administrativeTownName.get),
       "primaryLotNumber" -> JsString(p.additionalAddress.primaryLotNumber.get),
       "secondaryLotNumber" -> JsString(p.additionalAddress.secondaryLotNumber.get),
       "serialNumberOfTown" -> JsString(p.additionalAddress.serialNumberOfTown.get),
       "previousPostalCode" -> JsString(p.additionalAddress.previousPostalCode.get),
       "serialNumberOfPostalCode" -> JsString(p.additionalAddress.serialNumberOfPostalCode.get)*/
     )
   }
  }*/
}

case class StreetNumberAddress(
                                province:Option[String]=None, provinceEn:Option[String]=None, // 시도 - 1, 2
                                county:Option[String]=None, countyEn:Option[String]=None, //시군구 - 3, 4
                                town:Option[String]=None, townEn:Option[String]=None, //읍면동 - 5, 6

                                streetNameCode:Option[String]=None, streetName:Option[String]=None, streetNameEn:Option[String]=None // 도로명코드, 도로명 - 7, 8, 9
                              )
/*object StreetNumberAddress {
  implicit val jsonFormat = Json.format[StreetNumberAddress]/*{
    def writes(s : StreetNumberAddress):JsValue = {
      Json.obj(
        "province" -> JsString(s.province.get),
        "provinceEn" -> JsString(s.provinceEn.get),
        "county" -> JsString(s.county.get),
        "countyEn" -> JsString(s.countyEn.get),
        "town" -> JsString(s.town.get),
        "townEn" -> JsString(s.townEn.get),
        "streetNameCode" -> JsString(s.streetNameCode.get),
        "streetName" -> JsString(s.streetName.get),
        "streetNameEn" -> JsString(s.streetNameEn.get)
      )
    }
  }*/
}*/

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
/*object AdditionalAddress {
  implicit val jsonFormat = Json.format[StreetNumberAddress]/*{
    def writes(a : AdditionalAddress):JsValue = {
      Json.obj(
        "basement" -> JsString(a.basement.get),
        "mountain" -> JsString(a.mountain.get),
        "primaryNumberOfBuilding" -> JsString(a.primaryNumberOfBuilding.get),
        "secondaryNumberOfBuilding" -> JsString(a.secondaryNumberOfBuilding.get),
        "manageNumberOfBuilding" -> JsString(a.manageNumberOfBuilding.get),
        "massDeliveryName" -> JsString(a.massDeliveryName.get),
        "localBuildingName" -> JsString(a.localBuildingName.get),
        "customaryTownCode" -> JsString(a.customaryTownCode.get),
        "customaryTownName" -> JsString(a.customaryTownName.get),
        "village" -> JsString(a.village.get),
        "administrativeTownName" -> JsString(a.administrativeTownName.get),
        "primaryLotNumber" -> JsString(a.primaryLotNumber.get),
        "secondaryLotNumber" -> JsString(a.secondaryLotNumber.get),
        "serialNumberOfTown" -> JsString(a.serialNumberOfTown.get),
        "previousPostalCode" -> JsString(a.previousPostalCode.get),
        "serialNumberOfPostalCode" -> JsString(a.serialNumberOfPostalCode.get)
      )
    }
  }*/
}*/

case class DataTables_ResponData(           //Response Data
                                draw:Int,
                                recordsTotal:Int,
                                recordsFiltered:Int,
                                data:String
                                )
object DataTables_ResponData {
  implicit val responeFormat = Json.format[DataTables_ResponData] //ResponData to JsonFormat
}