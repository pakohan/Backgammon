package controllers.de.htwg.upfaz.backgammon.controller;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Persister.class).to(TestPersister.class).in(Singleton.class);
	}

}
