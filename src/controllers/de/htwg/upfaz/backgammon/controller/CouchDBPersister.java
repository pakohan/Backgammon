package controllers.de.htwg.upfaz.backgammon.controller;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import controllers.de.htwg.upfaz.backgammon.controller.GameMap;
import controllers.de.htwg.upfaz.backgammon.controller.Persister;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import java.util.UUID;

public final class CouchDBPersister extends CouchDbRepositorySupport<GameMap>
        implements Persister {

    @Inject
    public CouchDBPersister(@Named("dbConnector") CouchDbConnector db) {
        super(GameMap.class, db, true);
        super.initStandardDesignDocument();

    }

    @Override
    public UUID createGame(final GameMap map) {
        if (map.isNew()) {
            // ensure that the id is generated and revision is null for saving a new map
            map.setId(UUID.randomUUID().toString());
            map.setRevision(null);
            add(map);
            return map.get_id();
        }

        update(map);
        return map.get_id();
    }

    @Override
    public void saveGame(final GameMap map) {
        db.update(map);
    }

    @Override
    public void closeDB() {
    }

    @Override
    public GameMap loadGame(final UUID id, final int rev) {
        return db.get(GameMap.class, id.toString());
    }
}
