package com.dsf.example.play

/**
  * Created by chaehb on 8/18/16.
  */
object ApplicationConfig {
  var DatabaseState = DBState.NotInitialized
}


object DBState {
  val NotInitialized = 0
  val NoData = 1

  val Initializing = 2

  val Ready = 100
}