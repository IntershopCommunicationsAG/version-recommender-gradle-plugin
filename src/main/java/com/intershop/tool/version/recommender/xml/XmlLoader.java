package com.intershop.tool.version.recommender.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XmlLoader
{
    public void exportXML(Object xmlModel, Writer writer) throws JAXBException
    {
        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(xmlModel.getClass());
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(xmlModel, writer);
    }

    @SuppressWarnings("unchecked")
    public <T> T importXML(InputStream inputStream, Class<T> expectedType) throws IOException
    {
        T definition = null;
        try
        {
            System.setProperty("javax.xml.bind.JAXBContext", "com.sun.xml.internal.bind.v2.ContextFactory");
            JAXBContext context = JAXBContext.newInstance(expectedType);
            Unmarshaller um = context.createUnmarshaller();
            definition = (T) um.unmarshal(inputStream);
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }
        return definition;
    }
}
