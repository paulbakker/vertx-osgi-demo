package org.amdatu.vertx.demo;

import org.apache.felix.dm.annotation.api.Component;
import org.apache.felix.dm.annotation.api.ServiceDependency;
import org.apache.felix.dm.annotation.api.Start;
import org.apache.felix.dm.annotation.api.Stop;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeEvent;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

@Component
public class Server {

	@ServiceDependency
	Vertx vertx;

	private HttpServer server;

	@Start
	public void start() {
		
		
		Router router = Router.router(vertx);


	    BridgeOptions options = new BridgeOptions().addOutboundPermitted(new PermittedOptions().setAddress("news-feed"));

	    router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options, event -> {

	      if (event.type() == BridgeEvent.Type.SOCKET_CREATED) {
	        System.out.println("A socket was created");
	      }

	      event.complete(true);

	    }));

	    router.route().handler(StaticHandler.create());

	    vertx.createHttpServer().requestHandler(router::accept).listen(8080);

	    vertx.setPeriodic(1000, t -> vertx.eventBus().publish("news-feed", "news from the server!"));
	}
	
	@Stop
	public void stop() {
	     if (server != null) {
	       server.close();
	     }
	  }
}
