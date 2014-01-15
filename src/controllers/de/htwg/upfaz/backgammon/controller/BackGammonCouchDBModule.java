package controllers.de.htwg.upfaz.backgammon.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import controllers.de.htwg.upfaz.backgammon.controller.Persister;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

public final class BackGammonCouchDBModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("databaseOfBackgammon")).toInstance("backgammon");
        bind(String.class).annotatedWith(Names.named("databaseHost")).toInstance("localhost");
        bind(Integer.class).annotatedWith(Names.named("databasePort")).toInstance(5984);
        bind(String.class).annotatedWith(Names.named("databaseURL")).toInstance("http://localhost");

        bind(Persister.class).to(CouchDBPersister.class).in(Singleton.class);
    }

    @Provides
    HttpClient getHttpClient(@Named("databaseHost") String databaseHost, @Named("databasePort") int databasePort) {
        return new StdHttpClient.Builder().host(databaseHost).port(databasePort).username("pakohan").password("pakohan").build();
    }

    @Provides
    CouchDbInstance getStdCouchDbInstance(HttpClient httpClient) {
        return new StdCouchDbInstance(httpClient);
    }

    @Provides
    @Named("dbConnector")
    CouchDbConnector getBoatStdCouchDbConnector(@Named("databaseOfBackgammon") String databaseName, CouchDbInstance couchDbInstance) {
        return new StdCouchDbConnector(databaseName, couchDbInstance);
    }

}
