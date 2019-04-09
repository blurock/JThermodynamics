package info.esblurock.thermodynamics.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ErrorResponse {

	public static void setErrorResponse(HttpServletResponse response, String string) {
		try {
			response.getWriter().println(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
