package org.metadatacenter.cedar.util.dw;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.security.Authorization;
import org.metadatacenter.server.security.AuthorizationKeycloakAndApiKeyResolver;
import org.metadatacenter.server.security.IAuthorizationResolver;
import org.metadatacenter.server.security.KeycloakDeploymentProvider;
import org.metadatacenter.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

import static org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_HEADERS_PARAM;
import static org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_METHODS_PARAM;
import static org.eclipse.jetty.servlets.CrossOriginFilter.ALLOWED_ORIGINS_PARAM;

public abstract class CedarMicroserviceApplication<T extends Configuration> extends Application<T> {

  private static final Logger log = LoggerFactory.getLogger(CedarMicroserviceApplication.class);

  protected static CedarConfig cedarConfig;
  protected static UserService userService;

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

    //Continue with the app
    initializeApp(bootstrap);
  }

  @Override
  public void run(T configuration, Environment environment) throws Exception {
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
    cors.setInitParameter(ALLOWED_ORIGINS_PARAM, "*");
    cors.setInitParameter(ALLOWED_HEADERS_PARAM,
        "X-Requested-With,Content-Type,Accept,Origin,Referer,User-Agent,Authorization");
    cors.setInitParameter(ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD,PATCH");

    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }

  protected abstract void initializeApp(Bootstrap<T> bootstrap);

  protected abstract void runApp(T configuration, Environment environment);
}
