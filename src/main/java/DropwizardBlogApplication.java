import config.DropwizardBlogConfiguration;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.oauth.OAuthCredentialAuthFilter;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import io.dropwizard.setup.Environment;

import javax.sql.DataSource;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import auth.DropwizardBlogAuthenticator;
import auth.DropwizardBlogAuthorizer;
import auth.User;
import health.DropwizardBlogApplicationHealthCheck;
import resource.ComposeResource;
import resource.PartsResource;
import resource.UserResource;
import service.MessageBoxService;
import service.MessageService;
import service.PartsService;
import service.UserService;

public class DropwizardBlogApplication extends Application<DropwizardBlogConfiguration> {
    private static final String SQL = "sql";
    private static final String DROPWIZARD_BLOG_SERVICE = "Dropwizard blog service";
    private static final String BEARER = "Bearer";

    public static void main(String[] args) throws Exception {
        new DropwizardBlogApplication().run(args);
    }

    @Override
    public void run(DropwizardBlogConfiguration configuration, Environment environment) {
        // Datasource configuration
        final DataSource dataSource = configuration.getDataSourceFactory().build(environment.metrics(), SQL);
        DBI dbi = new DBI(dataSource);

        // Register Health Check
        DropwizardBlogApplicationHealthCheck healthCheck =
                new DropwizardBlogApplicationHealthCheck(dbi.onDemand(PartsService.class));
        environment.healthChecks().register(DROPWIZARD_BLOG_SERVICE, healthCheck);

        // Register OAuth authentication
        environment.jersey()
                .register(new AuthDynamicFeature(new OAuthCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new DropwizardBlogAuthenticator())
                        .setAuthorizer(new DropwizardBlogAuthorizer()).setPrefix(BEARER).buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);

        // Register resources
        environment.jersey().register(new PartsResource(dbi.onDemand(PartsService.class)));
        environment.jersey().register(new UserResource(dbi.onDemand(UserService.class),dbi.onDemand(MessageBoxService.class),dbi.onDemand(MessageService.class)));
        environment.jersey().register(new ComposeResource(dbi.onDemand(MessageService.class),dbi.onDemand(MessageBoxService.class),dbi.onDemand(UserService.class)));
        environment.jersey().register(new JsonProcessingExceptionMapper(true));
    }
}