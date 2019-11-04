package org.jvnet.jaxb2.maven2.resolver.tools;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class ClasspathCatalogResolver extends com.sun.org.apache.xml.internal.resolver.tools.CatalogResolver
{

  public static final String URI_SCHEME_CLASSPATH = "classpath";

  @Override
  public String getResolvedEntity (final String publicId, final String systemId)
  {
    // System.out.println("Resolving [" + publicId + "], [" + systemId + "].");
    final String result = super.getResolvedEntity (publicId, systemId);
    // System.out.println("Resolved to [" + result+ "].");

    if (result == null)
    {
      // System.err.println(MessageFormat.format(
      // "Could not resolve publicId [{0}], systemId [{1}]",
      // publicId, systemId));
      return null;
    }

    try
    {
      final URI uri = new URI (result);
      if (URI_SCHEME_CLASSPATH.equals (uri.getScheme ()))
      {
        final String schemeSpecificPart = uri.getSchemeSpecificPart ();
        // System.out.println("Resolve [" + schemeSpecificPart + "].");

        final URL resource = Thread.currentThread ().getContextClassLoader ().getResource (schemeSpecificPart);
        if (resource == null)
        {
          // System.out.println("Returning [" + null + "].");
          return null;
        }
        // System.out.println("Returning to [" + resource.toString()+ "].");
        return resource.toString ();
      }
      // System.out.println("Returning to [" + result+ "].");
      return result;
    }
    catch (final URISyntaxException urisex)
    {
      // System.out.println("Returning to [" + result+ "].");
      return result;
    }
  }
}
