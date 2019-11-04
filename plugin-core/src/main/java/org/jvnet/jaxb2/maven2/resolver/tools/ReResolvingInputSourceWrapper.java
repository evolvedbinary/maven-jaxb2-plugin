package org.jvnet.jaxb2.maven2.resolver.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ReResolvingInputSourceWrapper extends InputSource
{

  private final EntityResolver entityResolver;
  private final InputSource inputSource;

  public ReResolvingInputSourceWrapper (final EntityResolver entityResolver,
                                        final InputSource inputSource,
                                        final String publicId,
                                        final String systemId)
  {
    this.entityResolver = entityResolver;
    this.inputSource = inputSource;
    this.setPublicId (publicId);
    this.setSystemId (systemId);
  }

  @Override
  public Reader getCharacterStream ()
  {
    final Reader originalReader = inputSource.getCharacterStream ();
    if (originalReader == null)
    {
      return null;
    }
    try
    {
      final InputSource resolvedEntity = this.entityResolver.resolveEntity (getPublicId (), getSystemId ());
      if (resolvedEntity != null)
      {
        return resolvedEntity.getCharacterStream ();
      }
      return originalReader;
    }
    catch (final IOException | SAXException saxex)
    {
      return originalReader;
    }
  }

  @Override
  public void setCharacterStream (final Reader characterStream)
  {}

  @Override
  public InputStream getByteStream ()
  {
    final InputStream originalInputStream = inputSource.getByteStream ();
    if (originalInputStream == null)
    {
      return null;
    }
    try
    {
      final InputSource resolvedEntity = this.entityResolver.resolveEntity (getPublicId (), getSystemId ());
      if (resolvedEntity != null)
      {
        return resolvedEntity.getByteStream ();
      }
      return originalInputStream;
    }
    catch (final IOException | SAXException saxex)
    {
      return originalInputStream;
    }
  }

  @Override
  public void setByteStream (final InputStream byteStream)
  {}
}
