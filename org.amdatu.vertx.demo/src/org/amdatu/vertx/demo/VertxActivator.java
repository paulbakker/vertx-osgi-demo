package org.amdatu.vertx.demo;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

public class VertxActivator extends DependencyActivatorBase{

	@Override
	public void init(BundleContext bc, DependencyManager dm) throws Exception {
		Vertx vertx = Vertx.vertx();
		
		dm.add(createComponent().setInterface(Vertx.class.getName(), null).setImplementation(vertx));
		dm.add(createComponent().setInterface(EventBus.class.getName(), null).setImplementation(vertx.eventBus()));
		
	}

}
