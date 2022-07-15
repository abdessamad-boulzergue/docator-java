package com.apos;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyContentHandler implements ContentHandler {
    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("START");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("END");

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String attrs = IntStream.range(0,atts.getLength())
                        .mapToObj(i -> atts.getQName(i))
                                .collect(Collectors.joining(" "));
        System.out.println("START-ELE : " + qName+" : " + attrs);

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("END-ELE : " + qName );

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {
        System.out.println("PROCESS: " + target+" : " + data);

    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        System.out.println("SKIP: " + name);
    }
}
