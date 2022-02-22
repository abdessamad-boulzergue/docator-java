package com.apos.resources.service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.apos.resources.IResource;
import com.apos.resources.Resource;
import com.apos.resources.ResourceStorageService;

/**
 * This a Sample Implementation of the DocumentStorageService saves the uploaded files on the File System on a folder (by default /docs)
 * and return the complete path to the file that will be stored in the form field property.
 * <p>
 * Check that the user that is running the app has write permissions on the storage folder.
 */
@Service
public class ResourceStorageServiceImpl implements ResourceStorageService {

    private Logger log = LoggerFactory.getLogger(ResourceStorageServiceImpl.class);

    /**
     * This is the root folder where the files are going to be stored, please check that the user that is running the app has permissions to read/write inside
     */
    private String storagePath = System.getProperty("com.apos.document.storage",".docs");
    private File storageFile;

    public ResourceStorageServiceImpl(String storagePath) {
        this.storagePath = storagePath;
        this.storageFile = new File(storagePath);
    }

    public ResourceStorageServiceImpl() {
        this.storageFile = new File(this.storagePath);
    }

    @Override
    public IResource buildResource(String name,
                                  long size,
                                  Date lastModified,
                                  Map<String, String> params) {
        String identifier = generateUniquePath();

        return new Resource(identifier,
                                name,
                                size,
                                lastModified);
    }

    @Override
    public IResource saveResource(IResource document,  byte[] content) {

        if (StringUtils.isEmpty(document.getIdentifier())) {
            document.setIdentifier(generateUniquePath());
        }

        File destination = getFileByPath(document.getIdentifier() + File.separator + document.getName());

        try {
            FileUtils.writeByteArrayToFile(destination,
                                           content);
            destination.getParentFile().setLastModified(document.getLastModified().getTime());
            destination.setLastModified(document.getLastModified().getTime());
        } catch (IOException e) {
            log.error("Error writing file {}: {}",
                      document.getName(),
                      e);
        }

        return document;
    }

    @Override
    public IResource getResource(String id) {
        File file = getFileByPath(id);

        if (file.exists() && !file.isFile() && !ArrayUtils.isEmpty(file.listFiles())) {
            try {
                File destination = file.listFiles()[0];
                Resource doc = new Resource(id,
                                                destination.getName(),
                                                destination.length(),
                                                new Date(destination.lastModified()));
                doc.setLoadService(this);
                return doc;
            } catch (Exception e) {
                log.error("Error loading document '{}': {}",
                          id,
                          e);
            }
        }

        return null;
    }

    @Override
    public boolean deleteResource(String id) {
        if (StringUtils.isEmpty(id)) {
            return true;
        }
        return deleteResource(getResource(id));
    }

    @Override
    public boolean deleteResource(IResource doc) {
        if (doc != null) {
            File rootDoc = getDocumentContent(doc);
            if (!ArrayUtils.isEmpty(rootDoc.listFiles())) {
                return deleteFile(rootDoc.listFiles()[0]);
            }
            return deleteFile(rootDoc);
        }
        return true;
    }

    public File getDocumentContent(IResource doc) {
        if (doc != null) {
            return getFileByPath(doc.getIdentifier());
        }
        return null;
    }

    protected boolean deleteFile(File file) {
        try {
            if (file != null) {
                if (file.isFile()) {
                    file.delete();
                    return deleteFile(file.getParentFile());
                } else {
                    if (!file.getName().equals(storagePath)) {
                        String[] list = file.list();
                        if (list == null || list.length == 0) {
                            file.delete();
                            return deleteFile(file.getParentFile());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error deleting file: ",
                      e);
            return false;
        }
        return true;
    }

    /**
     * Generates a random path to store the file to avoid overwritting files with the same name
     * @return A String containging the path where the document is going to be stored.
     */
    protected String generateUniquePath() {
        File parent;
        String destinationPath;
        do {
            destinationPath = UUID.randomUUID().toString();

            parent = getFileByPath(destinationPath);
        } while (parent.exists());

        return destinationPath;
    }

    protected File getFileByPath(String path) {
        return new File(storagePath + File.separator + path);
    }

    @Override
    public List<IResource> listDocuments(Integer page, Integer pageSize) {
        List<IResource> listOfDocs = new ArrayList<IResource>();

        int startIndex = page * pageSize;
        int endIndex = startIndex + pageSize;

        File[] documents = storageFile.listFiles();

        if (documents == null) { // happens when the docs folder doesn't exist
            return Collections.emptyList();
        }

        // make sure the endIndex is not bigger then amount of files
        if (documents.length < endIndex) {
            endIndex = documents.length;
        }
        Arrays.sort(documents,
                Comparator.comparingLong(File::lastModified)
        );
        for (int i = startIndex; i < endIndex; i++) {
            IResource doc = getResource(documents[i].getName());
            listOfDocs.add(doc);
        }
        return listOfDocs;
    }

    @Override
    public byte[] loadContent(String id) {
        File file = getFileByPath(id);

        if (file.exists() && !file.isFile() && !ArrayUtils.isEmpty(file.listFiles())) {
            try {
                File destination = file.listFiles()[0];
                return FileUtils.readFileToByteArray(destination);
            } catch (IOException e) {
                log.error("Unable to laod content due to {}", e.getMessage(), e);
            }
        } 
        
        return null;
    }
}
