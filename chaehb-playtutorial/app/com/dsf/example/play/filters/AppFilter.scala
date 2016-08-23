package com.dsf.example.play.filters

import javax.inject.Inject

import akka.stream.Materializer
import com.dsf.example.play.{ApplicationConfig, DBState}
import play.api.mvc.{Filter, RequestHeader, Result, Results}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by chaehb on 8/18/16.
  */
class AppFilter @Inject()(implicit override val mat: Materializer, exec: ExecutionContext) extends Filter {

  override def apply(filter: (RequestHeader) => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    filter(requestHeader).map(result =>{
      if(ApplicationConfig.DatabaseState == DBState.Ready){
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
