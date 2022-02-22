package com.apos.resources.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.apos.resources.IResource;
import com.apos.resources.Resource;
import com.apos.resources.ResourceStorageService;

@Service
@Primary
public class ResourceLoaderService implements ResourceStorageService {

	Logger logger = LoggerFactory.getLogger(ResourceLoaderService.class);

	private File storageFile;
    private String storagePath = System.getProperty("project.resources",".docs");

	public ResourceLoaderService() {
		this.storageFile = new File(storagePath);	
   }
	public String getActionsDefinitionPath() {
		return storagePath;
	}

	private String getResourcePath(String resourceId) {
		return storagePath.concat(File.separator).concat(resourceId);
	}

	public IResource buildResource(String name, long size, Date lastModified, Map<String, String> params) {
		return new Resource(name, name, size, lastModified);
	}

	@Override
	public IResource saveResource(IResource resource, byte[] content) {

		if (StringUtils.isEmpty(resource.getIdentifier())) {
			resource.setIdentifier(resource.getName());
		}
		File destination = new File(getResourcePath(resource));
		try {
			FileUtils.writeByteArrayToFile(destination, content);
			destination.getParentFile().setLastModified(resource.getLastModified().getTime());
			destination.setLastModified(resource.getLastModified().getTime());
		} catch (IOException e) {
			logger.error("Error writing file {}: {}", resource.getName(), e);
		}

		return resource;

	}

	protected String generateUniquePath() {
		File parent;
		String destinationPath;
		do {
			destinationPath = UUID.randomUUID().toString();
			parent = new File(getResourcePath(destinationPath));
		} while (parent.exists());

		return destinationPath;
	}

	@Override
	public IResource getResource(String id) {
		File file = new File(getResourcePath(id));

		if (file.exists() && file.isFile()) {
			try {
				Resource doc = new Resource(id, file.getName(), file.length(), new Date(file.lastModified()));
				doc.setLoadService(this);
				return doc;
			} catch (Exception e) {
				logger.error("Error loading document '{}': {}", id, e);
			}
		}

		return null;

	}

	@Override
	public byte[] loadContent(String id) {
		File file = new File(getResourcePath(id));
		if (file.exists() && file.isFile()) {
			try {
				return FileUtils.readFileToByteArray(file);
			} catch (IOException e) {
				logger.error("Unable to laod content due to {}", e.getMessage(), e);
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
			try {
				Path path = Paths.get(getResourcePath(doc));
				Files.delete(path);
				return true;
			} catch (IOException e) {
				logger.error("deleteResource fails : ", e);
				return false;
			}
		}
		return true;
	}

	private String getResourcePath(IResource doc) {
		return storagePath + File.separator + doc.getName();
	}

	public File getDocumentContent(IResource doc) {
		if (doc != null) {
			return new File(getResourcePath(doc.getName()));
		}
		return null;
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
}
