package org.jvnet.jaxb2.maven2;

import org.apache.maven.model.FileSet;

public class ResourceEntry
{
  private FileSet fileset;

  public FileSet getFileset ()
  {
    return fileset;
  }

  public void setFileset (final FileSet fileset)
  {
    this.fileset = fileset;
  }

  private String url;

  public String getUrl ()
  {
    return url;
  }

  public void setUrl (final String url)
  {
    this.url = url;
  }

  private DependencyResource dependencyResource;

  public DependencyResource getDependencyResource ()
  {
    return dependencyResource;
  }

  public void setDependencyResource (final DependencyResource dependencyResource)
  {
    this.dependencyResource = dependencyResource;
  }

  @Override
  public String toString ()
  {
    if (getFileset () != null)
      return getFileset ().toString ();
    if (getUrl () != null)
      return "URL {" + getUrl ().toString () + "}";
    if (getDependencyResource () != null)
      return getDependencyResource ().toString ();
    return "Empty resource entry {}";
  }
}
