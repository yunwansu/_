package com.dsf.example.play.modules

import javax.inject.{Inject, Singleton}

import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

/**
  * Created by chaehb on 18/08/2016.
  */
//@Singleton
class AppModule @Inject()(lifecycle: ApplicationLifecycle) {
  // startup message
  println("==============")
  println("App started.")
  println("==============")

  lifecycle.addStopHook{() =>
    Future.successful(appStopMessage)
  }

  def appStopMessage = {
    // stop message
    println("==============")
    println("App stopped.")
    println("==============")
  }
}
