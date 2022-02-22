package com.apos.resources;


import java.util.Date;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.assertj.core.api.Assertions;
import java.nio.file.Files;

public class TestResourceImp {

    private static final String ID = "1234567890";
    private static final String FILENAME = "Document_Name";
    private static final String EXTENSION = ".txt";
    private static final String NAME = FILENAME + EXTENSION;
    private static final Long SIZE = 1024l;
    private static final Date LAST_MODIFIED = new Date();
    private static final String LINK = "downloadLink";
    private static final String CONTENT = "test content";

    @Test
    public void testDefaultConstructor() {
        IResource document = new Resource();

        Assertions.assertThat(document.getIdentifier()).isNotNull();
    }

    @Test
    public void testConstructorWithoutIdentifier() {
        IResource document = new Resource(NAME, SIZE, LAST_MODIFIED);

        Assertions.assertThat(document.getIdentifier()).isNotNull();
        Assertions.assertThat(document.getName()).isNotNull().isEqualTo(NAME);
        Assertions.assertThat(document.getSize()).isEqualTo(SIZE);
        Assertions.assertThat(document.getLastModified()).isEqualTo(LAST_MODIFIED);
    }

    @Test
    public void testConstructorWithIdentifier() {
        IResource document = new Resource(ID,NAME, SIZE,LAST_MODIFIED);

        Assertions.assertThat(document.getIdentifier()).isNotNull().isEqualTo(ID);
        Assertions.assertThat(document.getName()).isNotNull().isEqualTo(NAME);
        Assertions.assertThat(document.getSize()).isEqualTo(SIZE);
        Assertions.assertThat(document.getLastModified()).isEqualTo(LAST_MODIFIED);
    }

    @Test
    public void testFullConstructor() {
        IResource document = new Resource(ID, NAME, SIZE, LAST_MODIFIED, LINK);

        Assertions.assertThat(document.getIdentifier()).isNotNull().isEqualTo(ID);
        Assertions.assertThat(document.getName()).isNotNull().isEqualTo(NAME);
        Assertions.assertThat(document.getSize()).isEqualTo(SIZE);
        Assertions.assertThat(document.getLastModified()).isEqualTo(LAST_MODIFIED);
        Assertions.assertThat(document.getLink()).isNotNull().isNotEmpty().isEqualTo(LINK);
    }

    @Test
    public void testToStringRepresentation() {
        IResource document = new Resource();
        try {
        	assertNotNull(document.toString());
        } catch (Throwable th) {
            fail("toString method must not fire any exception: " + th.getMessage());
        }
    }

    @Test
    public void testToFile() throws Exception {
        IResource document = new Resource(ID, NAME,  SIZE, LAST_MODIFIED, LINK);
        document.setContent(CONTENT.getBytes());

        File file = document.toFile();
        assertNotNull(file);
        assertNotNull(file.getName());
        assertTrue(file.getName().contains(FILENAME));
        assertTrue(file.getName().contains(EXTENSION));
        String fileContent = new String(Files.readAllBytes(file.toPath()));
        assertNotNull(fileContent);
        assertEquals(fileContent, CONTENT);
    }
}
