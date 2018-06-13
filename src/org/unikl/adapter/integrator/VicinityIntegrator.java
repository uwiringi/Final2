package org.unikl.adapter.integrator;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

public class VicinityIntegrator {
	private static final String BUNDLE_ID = "org.unikl.adapter.integrator";
	private static final Logger s_logger = LoggerFactory.getLogger(VicinityIntegrator.class);
	private ServiceRegistration<UniklResourceContainer> registration;
    ConfigurationAdmin configurationAdmin;
	private ServiceRegistration json_registration;
	
	protected void activate(ComponentContext context) {
		s_logger.debug("[" + BUNDLE_ID + "]" + " activating...");

        Configuration configuration = null;
		try {
			configuration = configurationAdmin.getConfiguration("com.eclipsesource.jaxrs.connector", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		///////////////////
        Dictionary<String, Object> props = configuration.getProperties();
        if (props == null) {
            props = new Hashtable<String, Object>();
        }
        props.put("root", "/objects");
        try {
			configuration.update(props);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		///////////////////
		
		registration = context.getBundleContext().registerService(UniklResourceContainer.class, UniklResourceContainer.getInstance(), null);
		
	    JacksonJsonProvider service = new JacksonJsonProvider();
	    json_registration = context.getBundleContext().registerService(JacksonJsonProvider.class.getName(), service, null);
		s_logger.debug("[" + BUNDLE_ID + "]" + " activated!");
	}

	protected void deactivate(ComponentContext componentContext) {
		s_logger.debug("[" + BUNDLE_ID + "]" + " deactivating...");

		registration.unregister();
		json_registration.unregister();
		
		s_logger.debug("[" + BUNDLE_ID + "]" + " deactivated!");
	}
	
    protected void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = configurationAdmin;
    }

    protected void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
        this.configurationAdmin = null;
    }
}
