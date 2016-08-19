package com.dsf.example.play.modules

import java.io.File
import java.util.UUID
import javax.inject.Inject

import com.dsf.example.play.ApplicationConfig
import com.dsf.example.play.models.entities.{AdditionalAddress, PostalCode, StreetNumberAddress}
import com.dsf.example.play.models.pgsql.PostalCodeDAO

import scala.concurrent.ExecutionContext
import scala.io.Source
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
      println("Database Ready!")
      ApplicationConfig.DataBaseReady = true
    case Failure(t) =>
      println("Database not Ready. Please initialize it.")
      ApplicationConfig.DataBaseReady = false
  }
}
