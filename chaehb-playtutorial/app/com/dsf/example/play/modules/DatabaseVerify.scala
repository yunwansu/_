package com.dsf.example.play.modules

import javax.inject.Inject

import com.dsf.example.play.ApplicationConfig
import com.dsf.example.play.models.pgsql.PostalCodeDAO
import play.api.inject.ApplicationLifecycle

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by chaehb on 11/08/2016.
  */
class DatabaseVerify @Inject()(postalCodeDAO: PostalCodeDAO,lifecycle: ApplicationLifecycle)(implicit ec: ExecutionContext) {

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
        ApplicationConfig.ModuleState = true
    case Failure(t) =>
        println("Database not Ready. Please initialize it.")
        ApplicationConfig.DataBaseReady = false
        ApplicationConfig.ModuleState = true
  }

  lifecycle.addStopHook{() =>
    println("DB Close")
    Future.successful(postalCodeDAO.close)
  }
}
