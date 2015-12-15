package org.pluto.service.operacion.handlers;

import org.pluto.service.operacion.model.Expression;
import org.pluto.service.operacion.util.OperationHelper;

import com.google.gson.Gson;
import com.spotify.apollo.Request;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;

import okio.ByteString;

public class OperationHandler {
	
	private final static Gson gson = new Gson();

	public static Response<ByteString> handleRequest(RequestContext requestContext){
		return handleRequest(requestContext.request());
	}
	
	private static Response<ByteString> handleRequest(final Request request){
		Expression expressionObject = null;
		
		if(request.payload().isPresent()){
			final String json = new String(request.payload().get().toByteArray());
			expressionObject = gson.fromJson(json, Expression.class);
			evaluateExpression(expressionObject);
		}else{
			expressionObject = new Expression(Boolean.FALSE, "No payload in request");
		}
		
		return  createHandlerResponse(expressionObject);
	}
	
	private static void evaluateExpression(final Expression expression){
		new OperationHelper().evaluate(expression);
	}
	
	private static Response<ByteString> createHandlerResponse(final Expression expression){
		return Response.ok().withPayload(ByteString.encodeUtf8(gson.toJson(expression)));
	}
}
