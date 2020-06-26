package org.tone.notekeeper;

import org.glassfish.jersey.server.ResourceConfig;

public class ApplicationConfig extends ResourceConfig {
	
	private void ApplicationInit(){
        // Resources.
        // packages(OrderResource.class.getPackage().getName());

        // Register CORS filter.
        register(new CORSFilter());
        System.out.println("Server Init");

        // Register the rest you need

    }
	
	public ApplicationConfig () {
        ApplicationInit();
        // Bindings (@Inject)
    }

}
