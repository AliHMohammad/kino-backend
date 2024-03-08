package dat3.security.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.web.access.AccessDeniedHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class CustomOAuth2AccessDeniedHandler implements AccessDeniedHandler {
  public static final Logger logger = LoggerFactory.getLogger(CustomOAuth2AccessDeniedHandler.class);
  private String realmName;
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
          throws IOException {
    logger.warn(e.getMessage());
    //logger.warn(e.getMessage(),e);

    Map<String, String> parameters = new LinkedHashMap<>();
    String errorMessage = e.getLocalizedMessage();
    if (Objects.nonNull(realmName)) {
      parameters.put("realm", realmName);
    }
    if (request.getUserPrincipal() instanceof AbstractOAuth2TokenAuthenticationToken) {
      errorMessage = "The request requires privileges other than those provided by the access token.";
      parameters.put("error", "insufficient_scope");
      parameters.put("error_description", errorMessage);
      parameters.put("error_uri", "https://tools.ietf.org/html/rfc6750#section-3.1");
    }
    Map<String, String> errorResponse = new HashMap<>();
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    errorResponse.put("timestamp", df.format(new Date()));
    errorResponse.put("status", ""+HttpStatus.FORBIDDEN.toString());
    errorResponse.put("error", e.getLocalizedMessage());
    errorResponse.put("message", errorMessage);
    errorResponse.put("path", request.getRequestURI());

    //Refactor
    String wwwAuthenticate = CustomOAuth2AuthenticationEntryPoint.computeWWWAuthenticateHeaderValue(parameters);
    response.addHeader("WWW-Authenticate", wwwAuthenticate);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    ObjectMapper mapper = new ObjectMapper();

    response.getWriter().write(mapper.writeValueAsString(errorResponse));
  }
  public void setRealmName(String realmName) {
    this.realmName = realmName;
  }
}
