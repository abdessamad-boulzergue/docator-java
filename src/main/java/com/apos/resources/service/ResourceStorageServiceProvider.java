package com.apos.resources.service;


import java.util.ServiceLoader;

import com.apos.resources.ResourceStorageService;


/**
 * Responsible for discovery and delivery of <code>ResourceStorageServiceProvider</code> implementations.
 * Default behavior is that is uses ServiceLoader for discovery and if no found it returns default/sample implementation
 */
public class ResourceStorageServiceProvider {

    private static final ServiceLoader<ResourceStorageService> storageServices = ServiceLoader.load(ResourceStorageService.class);
    private static ResourceStorageServiceProvider INSTANCE = new ResourceStorageServiceProvider();
    
    private ResourceStorageService documentStorageService;
    
    private ResourceStorageServiceProvider() {
        discover();
    }
    
    private synchronized void discover() {
        for (ResourceStorageService foundService : storageServices) {
            if (documentStorageService != null) {                
                throw new RuntimeException("Ambiguous ResourceStorageService discovery, found more than one implementation");
            }
            documentStorageService = foundService;
        }
        
        if (documentStorageService == null) {
            documentStorageService = new ResourceStorageServiceImpl();
        }
    }
    
    public static ResourceStorageServiceProvider get() {
        return INSTANCE;
    }
    
    public ResourceStorageService getStorageService() {
        return INSTANCE.documentStorageService;
    }
}
