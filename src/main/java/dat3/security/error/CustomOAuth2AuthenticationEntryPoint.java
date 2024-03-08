package dat3.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.server.resource.BearerTokenError;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomOAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {
  private static final Logger logger = LoggerFactory.getLogger(CustomOAuth2AuthenticationEntryPoint.class);
  private String realmName;
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
          throws IOException {
    logger.warn(e.getLocalizedMessage());
    //logger.warn(e.getLocalizedMessage(),e);
    HttpStatus status = HttpStatus.UNAUTHORIZED;
    String errorMessage = "Insufficient authentication details";
    Map<String, String> parameters = new LinkedHashMap<>();
    if (Objects.nonNull(realmName)) {
      parameters.put("realm", realmName);
    }
    if (e instanceof OAuth2AuthenticationException) {
      OAuth2Error error = ((OAuth2AuthenticationException) e).getError();
      parameters.put("error", error.getErrorCode());
      if (StringUtils.hasText(error.getDescription())) {
        errorMessage = error.getDescription();
        parameters.put("error_description", errorMessage);
      }
      if (StringUtils.hasText(error.getUri())) {
        parameters.put("error_uri", error.getUri());
      }
      if (error instanceof BearerTokenError) {
        BearerTokenError bearerTokenError = (BearerTokenError) error;
        if (StringUtils.hasText(bearerTokenError.getScope())) {
          parameters.put("scope", bearerTokenError.getScope());
        }
        status = ((BearerTokenError) error).getHttpStatus();
      }
    }

    Map<String, String> errorResponse = new HashMap<>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    errorResponse.put("timestamp", df.format(new Date()));
    errorResponse.put("status", ""+status);
    errorResponse.put("error", e.getLocalizedMessage());
    errorResponse.put("message", errorMessage);
    errorResponse.put("path", request.getRequestURI());


    String wwwAuthenticate = computeWWWAuthenticateHeaderValue(parameters);
    response.addHeader("WWW-Authenticate", wwwAuthenticate);
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ObjectMapper mapper = new ObjectMapper();

    response.getWriter().write(mapper.writeValueAsString(errorResponse));
  }

  public static String computeWWWAuthenticateHeaderValue(Map<String, String> parameters) {
    StringJoiner wwwAuthenticate = new StringJoiner(", ", "Bearer ", "");
    if (!parameters.isEmpty()) {
      parameters.forEach((k, v) -> wwwAuthenticate.add(k + "=\"" + v + "\""));
    }
    return wwwAuthenticate.toString();
  }
}
