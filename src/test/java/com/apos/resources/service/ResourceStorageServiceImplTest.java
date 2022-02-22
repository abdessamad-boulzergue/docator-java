package com.apos.resources.service;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.apos.resources.IResource;
import com.apos.resources.ResourceStorageService;

@RunWith(Parameterized.class)
public class ResourceStorageServiceImplTest {

    private static final String STORAGE_PATH_TEST = "target/docs";
    
    @Parameters(name= "{index}: impl={0}")
    public static Iterable<Object[]> data() {
        System.setProperty("com.apos.document.storage", STORAGE_PATH_TEST);
        System.setProperty("project.resources", STORAGE_PATH_TEST);
        
        return Arrays.asList(new Object[][] { 
            {new ResourceStorageServiceImpl()},
            {ResourceStorageServiceProvider.get().getStorageService()}
        });
    }
    
    private ResourceStorageService documentStorageService;
    
    public ResourceStorageServiceImplTest(ResourceStorageService documentStorageService) {
        this.documentStorageService = documentStorageService;
    }
    
    @AfterClass
    public static void cleanupOnce() {
        System.clearProperty("com.apos.document.storage");
        System.clearProperty("project.resources");
    }
    
    @Before
    public void setup() {
        // clean document storage
        
        File storagePath = new File(STORAGE_PATH_TEST);
        deleteFolder(storagePath);
        
    }
    protected void deleteFolder(File path) {
        File[] directories = path.listFiles();
        if (directories != null) {
            for (File file : directories) {
                if (file.isDirectory()) {
                    deleteFolder(file);
                }
                file.delete();
            }
        }
    }
    
    @Test
    public void testSaveAndGetDocument() {
        byte[] content = "document content".getBytes();
        IResource document = documentStorageService.buildResource("mydoc", content.length, new Date(), new HashMap<String, String>());
        assertNotNull(document.getIdentifier());
        
        documentStorageService.saveResource(document, content);
        
        IResource fromStorage = documentStorageService.getResource(document.getIdentifier());
        assertNotNull(fromStorage);
        
        assertEquals(document.getIdentifier(), fromStorage.getIdentifier());
        assertEquals(document.getName(), fromStorage.getName());
        assertEquals(content.length, fromStorage.getContent().length);
    }
    
    @Test
    public void testSaveAndDeleteDocument() {
        byte[] content = "another document content".getBytes();
        IResource document = documentStorageService.buildResource("mydoc", content.length, new Date(), new HashMap<String, String>());
        assertNotNull(document.getIdentifier());
        
        documentStorageService.saveResource(document, content);
        
        IResource fromStorage = documentStorageService.getResource(document.getIdentifier());
        assertNotNull(fromStorage);
        
        assertEquals(document.getIdentifier(), fromStorage.getIdentifier());
        assertEquals(document.getName(), fromStorage.getName());
        assertEquals(content.length, fromStorage.getContent().length);
        
        documentStorageService.deleteResource(fromStorage);
        
        fromStorage = documentStorageService.getResource(document.getIdentifier());
        assertNull(fromStorage);
    }
    
    @Test
    public void testSaveAndDeleteByIdDocument() {
        byte[] content = "yet another document content".getBytes();
        IResource document = documentStorageService.buildResource("mydoc", content.length, new Date(), new HashMap<String, String>());
        assertNotNull(document.getIdentifier());
        
        documentStorageService.saveResource(document, content);
        
        IResource fromStorage = documentStorageService.getResource(document.getIdentifier());
        assertNotNull(fromStorage);
        
        assertEquals(document.getIdentifier(), fromStorage.getIdentifier());
        assertEquals(document.getName(), fromStorage.getName());
        assertEquals(content.length, fromStorage.getContent().length);
        
        documentStorageService.deleteResource(fromStorage.getIdentifier());
        
        fromStorage = documentStorageService.getResource(document.getIdentifier());
        assertNull(fromStorage);
    }
    
    @Test
    public void testSaveAndListDocuments() {
        long lastModified = System.currentTimeMillis() - 10000;
        for (int i = 0; i < 10; i++) {
            byte[] content = (i +" another document content").getBytes();
            IResource document = documentStorageService.buildResource("mydoc"+i, content.length, new Date(lastModified + i * 1000), new HashMap<String, String>());
            assertNotNull(document.getIdentifier());
            
            documentStorageService.saveResource(document, content);
        }
        
        List<IResource> docs = documentStorageService.listDocuments(0, 5);
        assertNotNull(docs);
        assertEquals(5, docs.size());
        
        assertEquals("mydoc" + 0, docs.get(0).getName());
        assertEquals("mydoc" + 1, docs.get(1).getName());
        assertEquals("mydoc" + 2, docs.get(2).getName());
        assertEquals("mydoc" + 3, docs.get(3).getName());
        assertEquals("mydoc" + 4, docs.get(4).getName());
        
        docs = documentStorageService.listDocuments(1, 5);
        assertNotNull(docs);
        assertEquals(5, docs.size());
        assertEquals("mydoc" + 5, docs.get(0).getName());
        assertEquals("mydoc" + 6, docs.get(1).getName());
        assertEquals("mydoc" + 7, docs.get(2).getName());
        assertEquals("mydoc" + 8, docs.get(3).getName());
        assertEquals("mydoc" + 9, docs.get(4).getName());
        
        docs = documentStorageService.listDocuments(1, 2);
        assertNotNull(docs);
        assertEquals(2, docs.size());
        assertEquals("mydoc" + 2, docs.get(0).getName());
        assertEquals("mydoc" + 3, docs.get(1).getName());
    }
    
    @Test
    public void testListDocumentsLessThanPageSize() {
        byte[] content = "yet another document content".getBytes();
        IResource document = documentStorageService.buildResource("mydoc", content.length, new Date(), new HashMap<String, String>());
        assertNotNull(document.getIdentifier());
        
        documentStorageService.saveResource(document, content);
        
        List<IResource> docs = documentStorageService.listDocuments(0, 5);
        assertNotNull(docs);
        assertEquals(1, docs.size());
        
        assertEquals("mydoc", docs.get(0).getName());
    }

    @Test
    public void testListDocumentsNonexistentDocsFolder() {
        // Delete document storage contents AND the dir itself
        File docsDir = new File(STORAGE_PATH_TEST);
        deleteFolder(docsDir);
        docsDir.delete();

        List<IResource> docs = documentStorageService.listDocuments(0, 10);
        assertTrue(docs.isEmpty());
    }
}
