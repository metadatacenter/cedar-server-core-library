package org.metadatacenter.cedar.util.dw;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.AuthorizationKeycloakAndApiKeyResolver;
import org.metadatacenter.server.security.IAuthorizationResolver;
import org.metadatacenter.server.security.KeycloakDeploymentProvider;
import org.metadatacenter.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import static org.eclipse.jetty.servlets.CrossOriginFilter.*;

public abstract class CedarMicroserviceApplication<T extends CedarMicroserviceConfiguration> extends Application<T> {

  private static final Logger log = LoggerFactory.getLogger(CedarMicroserviceApplication.class);
  private static final List<String> HTTP_HEADERS;
  private static final List<String> HTTP_METHODS;

  protected static CedarConfig cedarConfig;
  protected static UserService userService;

  static {
    HTTP_HEADERS = new ArrayList<>();
    HTTP_HEADERS.add("X-Requested-With");
    HTTP_HEADERS.add("Content-Type");
    HTTP_HEADERS.add("Accept");
    HTTP_HEADERS.add("Origin");
    HTTP_HEADERS.add("Referer");
    HTTP_HEADERS.add("User-Agent");
    HTTP_HEADERS.add("Authorization");
    HTTP_HEADERS.add(CedarConstants.HTTP_HEADER_DEBUG);

    HTTP_METHODS = new ArrayList<>();
    HTTP_METHODS.add("OPTIONS");
    HTTP_METHODS.add("GET");
    HTTP_METHODS.add("PUT");
    HTTP_METHODS.add("POST");
    HTTP_METHODS.add("DELETE");
    HTTP_METHODS.add("HEAD");
    HTTP_METHODS.add("PATCH");
  }

  @Override
  public void initialize(Bootstrap<T> bootstrap) {
    log.info("********** Initializing CEDAR microservice " + getName());
    // Enable variable substitution with environment variables
    bootstrap.setConfigurationSourceProvider(
        new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(),
            new EnvironmentVariableSubstitutor()
        )
    );
    //Initialize config
    cedarConfig = CedarConfig.getInstance();

    //Initialize user service
    CedarDataServices.initializeUserService(cedarConfig);
    userService = CedarDataServices.getUserService();

    //Initialize Keycloak
    KeycloakDeploymentProvider.getInstance();
    // Init Authorization Resolver
    IAuthorizationResolver authResolver = new AuthorizationKeycloakAndApiKeyResolver();
    Authorization.setAuthorizationResolver(authResolver);
    Authorization.setUserService(CedarDataServices.getUserService());

    bootstrap.addBundle(new AssetsBundle("/assets/swagger-api/swagger.json", "/swagger-api/swagger.json"));

    //Continue with the app
    initializeApp(bootstrap);
  }

  @Override
  public void run(T configuration, Environment environment) throws Exception {
    log.info("**************************************************************");
    log.info("********** Running CEDAR microservice " + getName());
    int httpPort = getHttpPort(configuration);
    log.info("********** HTTP  Port:" + httpPort);
    int adminPort = getAdminPort(configuration);
    log.info("********** Admin Port:" + adminPort);
    setupEnvironment(environment);
    runApp(configuration, environment);
  }

  private int getHttpPort(T configuration) {
    int httpPort = 0;
    DefaultServerFactory serverFactory = (DefaultServerFactory) configuration.getServerFactory();
    for (ConnectorFactory connector : serverFactory.getApplicationConnectors()) {
      if (connector.getClass().isAssignableFrom(HttpConnectorFactory.class)) {
        httpPort = ((HttpConnectorFactory) connector).getPort();
        break;
      }
    }
    return httpPort;
  }

  private int getAdminPort(T configuration) {
    int httpPort = 0;
    DefaultServerFactory serverFactory = (DefaultServerFactory) configuration.getServerFactory();
    for (ConnectorFactory connector : serverFactory.getAdminConnectors()) {
      if (connector.getClass().isAssignableFrom(HttpConnectorFactory.class)) {
        httpPort = ((HttpConnectorFactory) connector).getPort();
        break;
      }
    }
    return httpPort;
  }

  protected void setupEnvironment(Environment environment) {
    // Register Exception Mapper
    environment.jersey().register(new CedarCedarExceptionMapper());
    environment.jersey().register(new CedarExceptionMapper());

    // Enable CORS headers
    final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    String httpOrigins = "*";
    String httpHeaders = StringUtils.join(HTTP_HEADERS, ",");
    String httpMethods = StringUtils.join(HTTP_METHODS, ",");
    log.info("Setting up CORS...");
    log.info(ALLOWED_ORIGINS_PARAM + ":" + httpOrigins);
    log.info(ALLOWED_HEADERS_PARAM + ":" + httpHeaders);
    log.info(ALLOWED_METHODS_PARAM + ":" + httpMethods);
    cors.setInitParameter(ALLOWED_ORIGINS_PARAM, httpOrigins);
    cors.setInitParameter(ALLOWED_HEADERS_PARAM, httpHeaders);
    cors.setInitParameter(ALLOWED_METHODS_PARAM, httpMethods);
    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }

  protected abstract void initializeApp(Bootstrap<T> bootstrap);

  protected abstract void runApp(T configuration, Environment environment);
}
