package com.dsf.example.play.modules

import javax.inject.Inject

import com.dsf.example.play.models.pgsql.PostalCodeDAO
import com.dsf.example.play.{ApplicationConfig, DBState}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Created by chaehb on 11/08/2016.
  */
class DatabaseVerify @Inject()(postalCodeDAO: PostalCodeDAO)(implicit ec: ExecutionContext) {

  // check tables
  println("==============")
  println("Verify database ...")
  println("==============")

  // Future[Int]
  // map
  // onComplete, onSuccess & onFailure

  postalCodeDAO.count.onComplete{
    case Success(count) =>
      if(count == 0){
        println("Database not Ready. Please add rows.")
        ApplicationConfig.DatabaseState = DBState.NoData
      }else {
        println("Database Ready!")
        ApplicationConfig.DatabaseState = DBState.Ready
      }
    case Failure(t) =>
      println("Database not Ready. Please initialize it.")
      ApplicationConfig.DatabaseState = DBState.NotInitialized
  }
}
