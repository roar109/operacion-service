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
		Expression exp = new Expression(Boolean.FALSE);
		
		if(request.payload().isPresent()){
			final String json = new String(request.payload().get().toByteArray());
			exp = gson.fromJson(json, Expression.class);
			OperationHelper oh = new OperationHelper();
			oh.evaluate(exp);
		}
		
		//TODO if is null what we should do?
		return  Response.ok().withPayload(ByteString.encodeUtf8(gson.toJson(exp)));
	}
}
