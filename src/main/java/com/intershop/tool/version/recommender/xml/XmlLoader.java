package com.intershop.tool.version.recommender.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

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
            JAXBContext context = JAXBContext.newInstance(expectedType);
            Unmarshaller um = context.createUnmarshaller();
            definition = (T)um.unmarshal(inputStream);
        }
        catch(JAXBException e)
        {
            throw new IOException(e);
        }
        return definition;
    }

    public void exportXML(Object xmlModel, File file)
    {
        try
        {
            waitForLockFile(file);
            try (FileWriter writer = new FileWriter(file))
            {
                exportXML(xmlModel, writer);
            }
        }
        catch(IOException|JAXBException|InterruptedException e)
        {
            throw new RuntimeException("Can't export file:" + file.getAbsolutePath(), e);
        }
        finally
        {
            removeLockFile(file);
        }
    }

    private static void removeLockFile(File file)
    {
        File lockFile = getLockFile(file);
        if (lockFile.exists())
        {
            lockFile.delete();
        }

    }

    private static void waitForLockFile(File file) throws InterruptedException, IOException
    {
        File lockFile = getLockFile(file);
        while(lockFile.exists())
        {
            Thread.sleep(10);
        }
        Files.write(lockFile.toPath(), "".getBytes(), StandardOpenOption.CREATE);
    }

    private static File getLockFile(File file)
    {
        return new File(file.getAbsolutePath() + ".lock");
    }

    public <T> T importXML(File file, Class<T> expectedType)
    {
        try
        {
            waitForLockFile(file);
            try (FileInputStream is = new FileInputStream(file))
            {
                return importXML(is, expectedType);
            }
        }
        catch(InterruptedException | IOException e)
        {
            throw new RuntimeException("Can't import file:" + file.getAbsolutePath(), e);
        }
        finally
        {
            removeLockFile(file);
        }
    }
}
