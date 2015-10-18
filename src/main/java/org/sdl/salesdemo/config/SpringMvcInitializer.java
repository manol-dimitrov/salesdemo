package org.sdl.salesdemo.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


/**
 * The following is the Spring MVC Initializer
 * 
 * @author shannonlal
 */
public class SpringMvcInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	
        /**
         * The following method will return the root config
         * @return Class<?>[] 
         */
        @Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { AppConfig.class };
	}
      /**
         * The following method will return the Servlet Config Class
         * @return Class<?>[]
         */
        @Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

}