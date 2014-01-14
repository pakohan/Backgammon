package controllers.de.htwg.upfaz.backgammon.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TestPersister implements Persister {

	private Map<UUID, Map<String, GameMap>> db = new HashMap<UUID, Map<String, GameMap>>();

	private GameMap gm;

	@Override
	public UUID createGame(GameMap map) {
		UUID id = UUID.randomUUID();
		Map<String, GameMap> map2 = new HashMap<String, GameMap>();
		map2.put("0", map);
		db.put(id, map2);
		if (map.isNew()) {
			map.set_rev("0");
		}

		return id;
	}

	@Override
	public void saveGame(GameMap map) {
		gm = map;
	}

	@Override
	public void closeDB() {
	}

	@Override
	public GameMap loadGame(UUID id, int rev) {
		if (rev == -1)
			rev = 0;
		return db.get(id).get(rev + "");
	}

}
