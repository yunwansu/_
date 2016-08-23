package com.dsf.example.play.models.pgsql

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.jdbc.meta.MTable
import slick.lifted.ProvenShape

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by chaehb on 18/08/2016.
  */
case class Sample(id:Long, name:String)

class SampleTableDAO @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext){
  private val dbConfig = dbConfigProvider.get[JdbcProfile]
  import dbConfig._
  import driver.api._

  private val tableQuery = TableQuery[SampleTable]

  def createTable = db.run {
        println("create ...")
        tableQuery.schema.create
  }

  private class SampleTable(tag:Tag) extends Table[Sample](tag,"SampleTBL"){
    def id = column[Long]("id",O.PrimaryKey)
    def name = column[String]("name")

    override def * : ProvenShape[Sample] = (id,name) <> ( (Sample.apply _).tupled,Sample.unapply)
  }
}
