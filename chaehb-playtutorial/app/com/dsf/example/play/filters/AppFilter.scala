package com.dsf.example.play.filters

import javax.inject.{Inject, Singleton}

import akka.stream.Materializer
import com.dsf.example.play.ApplicationConfig
import play.api.mvc.{Filter, RequestHeader, Result, Results}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by chaehb on 8/18/16.
  */
@Singleton
class AppFilter @Inject()(implicit override val mat: Materializer, exec: ExecutionContext) extends Filter {
  Thread.sleep(1000)
  println("=============")
  println("Filter start")
  println("=============")
  override def apply(filter: (RequestHeader) => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    filter(requestHeader).map(result =>{
      if(ApplicationConfig.DataBaseReady){
        if(requestHeader.uri.startsWith("/setup")){
          Results.NotFound
        }else{
          result
        }
      }else{
        if(requestHeader.method == "GET"){
          if(requestHeader.uri != "/setup/setup.html") {
            Results.Redirect("/setup/setup.html")
          }else{
            result
          }
        }else if(requestHeader.method == "POST" && requestHeader.uri == "/setup"){
          result
        }else{
          Results.NotFound
        }
      }
    })
  }
}
