package org.pluto.service.operacion;

import org.pluto.service.operacion.handlers.OperationHandler;

import com.spotify.apollo.Environment;
import com.spotify.apollo.Response;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.AsyncHandler;
import com.spotify.apollo.route.Route;

import okio.ByteString;

public class App {

	public static void main(String[] args) throws LoadingException {
		HttpService.boot(App::init, "operacion-service", args);
	}

	static void init(Environment environment) {
		environment.routingEngine().registerAutoRoute(createDefaultRoute())
			.registerAutoRoute(createInformationRoute())
			.registerAutoRoute(createOperationRoute());
	}

	private static Route<? extends AsyncHandler<?>> createDefaultRoute() {
		return Route.sync("GET", "/", rc -> "Working");
	}

	private static Route<? extends AsyncHandler<?>> createInformationRoute() {
		return Route.sync("GET", "/ping", requestContext -> Response.ok().withPayload(ByteString.encodeUtf8("pong!")));
	}

	private static Route<? extends AsyncHandler<?>> createOperationRoute() {
		return Route.sync("POST", "/", OperationHandler::handleRequest);
	}
}
