package controllers.de.htwg.upfaz.backgammon;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import controllers.de.htwg.upfaz.backgammon.persist.DB4OPersister;
import controllers.de.htwg.upfaz.backgammon.persist.Persister;

public final class BackGammonModule
        extends AbstractModule {

    @Override
    protected void configure() {
        bind(Persister.class).to(DB4OPersister.class).in(Singleton.class);
    }
}
