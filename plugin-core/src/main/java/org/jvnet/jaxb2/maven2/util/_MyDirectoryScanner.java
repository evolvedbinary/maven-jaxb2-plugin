package org.jvnet.jaxb2.maven2.util;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.codehaus.plexus.util.AbstractScanner;

/**
 * Class for scanning a directory for files/directories which match certain
 * criteria.
 * <p>
 * These criteria consist of selectors and patterns which have been specified.
 * With the selectors you can select which files you want to have included.
 * Files which are not selected are excluded. With patterns you can include or
 * exclude files based on their filename.
 * <p>
 * The idea is simple. A given directory is recursively scanned for all files
 * and directories. Each file/directory is matched against a set of selectors,
 * including special support for matching against filenames with include and and
 * exclude patterns. Only files/directories which match at least one pattern of
 * the include pattern list or other file selector, and don't match any pattern
 * of the exclude pattern list or fail to match against a required selector will
 * be placed in the list of files/directories found.
 * <p>
 * When no list of include patterns is supplied, "**" will be used, which means
 * that everything will be matched. When no list of exclude patterns is
 * supplied, an empty list is used, such that nothing will be excluded. When no
 * selectors are supplied, none are applied.
 * <p>
 * The filename pattern matching is done as follows: The name to be matched is
 * split up in path segments. A path segment is the name of a directory or file,
 * which is bounded by <code>File.separator</code> ('/' under UNIX, '\' under
 * Windows). For example, "abc/def/ghi/xyz.java" is split up in the segments
 * "abc", "def","ghi" and "xyz.java". The same is done for the pattern against
 * which should be matched.
 * <p>
 * The segments of the name and the pattern are then matched against each other.
 * When '**' is used for a path segment in the pattern, it matches zero or more
 * path segments of the name.
 * <p>
 * There is a special case regarding the use of <code>File.separator</code>s at
 * the beginning of the pattern and the string to match:<br>
 * When a pattern starts with a <code>File.separator</code>, the string to match
 * must also start with a <code>File.separator</code>. When a pattern does not
 * start with a <code>File.separator</code>, the string to match may not start
 * with a <code>File.separator</code>. When one of these rules is not obeyed,
 * the string will not match.
 * <p>
 * When a name path segment is matched against a pattern path segment, the
 * following special characters can be used:<br>
 * '*' matches zero or more characters<br>
 * '?' matches one character.
 * <p>
 * Examples:
 * <p>
 * "**\*.class" matches all .class files/dirs in a directory tree.
 * <p>
 * "test\a??.java" matches all files/dirs which start with an 'a', then two more
 * characters and then ".java", in a directory called test.
 * <p>
 * "**" matches everything in a directory tree.
 * <p>
 * "**\test\**\XYZ*" matches all files/dirs which start with "XYZ" and where
 * there is a parent directory called test (e.g. "abc\test\def\ghi\XYZ123").
 * <p>
 * Case sensitivity may be turned off if necessary. By default, it is turned on.
 * <p>
 * Example of usage:
 * 
 * <pre>
 * String [] includes = { "**\\*.class" };
 * String [] excludes = { "modules\\*\\**" };
 * ds.setIncludes (includes);
 * ds.setExcludes (excludes);
 * ds.setBasedir (new File ("test"));
 * ds.setCaseSensitive (true);
 * ds.scan ();
 *
 * System.out.println ("FILES:");
 * String [] files = ds.getIncludedFiles ();
 * for (int i = 0; i < files.length; i++)
 * {
 *   System.out.println (files[i]);
 * }
 * </pre>
 * 
 * This will scan a directory called test for .class files, but excludes all
 * files in all proper subdirectories of a directory called "modules"
 *
 * @author Arnout J. Kuiper <a href="mailto:ajkuiper@wxs.nl">ajkuiper@wxs.nl</a>
 * @author Magesh Umasankar
 * @author <a href="mailto:bruce@callenish.com">Bruce Atherton</a>
 * @author <a href="mailto:levylambert@tiscali-dsl.de">Antoine Levy-Lambert</a>
 */
public class _MyDirectoryScanner extends AbstractScanner
{

  /** The base directory to be scanned. */
  protected File basedir;

  /**
   * The files which matched at least one include and no excludes and were
   * selected.
   */
  protected Vector <String> filesIncluded;

  /** The files which did not match any includes or selectors. */
  protected Vector <String> filesNotIncluded;

  /**
   * The files which matched at least one include and at least one exclude.
   */
  protected Vector <String> filesExcluded;

  /**
   * The directories which matched at least one include and no excludes and were
   * selected.
   */
  protected Vector <String> dirsIncluded;

  /** The directories which were found and did not match any includes. */
  protected Vector <String> dirsNotIncluded;

  /**
   * The directories which matched at least one include and at least one
   * exclude.
   */
  protected Vector <String> dirsExcluded;

  /**
   * The files which matched at least one include and no excludes and which a
   * selector discarded.
   */
  protected Vector <String> filesDeselected;

  /**
   * The directories which matched at least one include and no excludes but
   * which a selector discarded.
   */
  protected Vector <String> dirsDeselected;

  /** Whether or not our results were built by a slow scan. */
  protected boolean haveSlowResults = false;

  /**
   * Whether or not symbolic links should be followed.
   *
   * @since Ant 1.5
   */
  private boolean followSymlinks = true;

  /** Whether or not everything tested so far has been included. */
  protected boolean everythingIncluded = true;

  /**
   * Sole constructor.
   */
  public _MyDirectoryScanner ()
  {}

  /**
   * Sets the base directory to be scanned. This is the directory which is
   * scanned recursively. All '/' and '\' characters are replaced by
   * <code>File.separatorChar</code>, so the separator used need not match
   * <code>File.separatorChar</code>.
   *
   * @param basedir
   *        The base directory to scan. Must not be <code>null</code>.
   */
  public void setBasedir (final String basedir)
  {
    setBasedir (new File (basedir.replace ('/', File.separatorChar).replace ('\\', File.separatorChar)));
  }

  /**
   * Sets the base directory to be scanned. This is the directory which is
   * scanned recursively.
   *
   * @param basedir
   *        The base directory for scanning. Should not be <code>null</code>.
   */
  public void setBasedir (final File basedir)
  {
    this.basedir = basedir;
  }

  /**
   * Returns the base directory to be scanned. This is the directory which is
   * scanned recursively.
   *
   * @return the base directory to be scanned
   */
  public File getBasedir ()
  {
    return basedir;
  }

  /**
   * Sets whether or not symbolic links should be followed.
   *
   * @param followSymlinks
   *        whether or not symbolic links should be followed
   */
  public void setFollowSymlinks (final boolean followSymlinks)
  {
    this.followSymlinks = followSymlinks;
  }

  /**
   * Returns whether or not the scanner has included all the files or
   * directories it has come across so far.
   *
   * @return <code>true</code> if all files and directories which have been
   *         found so far have been included.
   */
  public boolean isEverythingIncluded ()
  {
    return everythingIncluded;
  }

  /**
   * Scans the base directory for files which match at least one include pattern
   * and don't match any exclude patterns. If there are selectors then the files
   * must pass muster there, as well.
   *
   * @exception IllegalStateException
   *            if the base directory was set incorrectly (i.e. if it is
   *            <code>null</code>, doesn't exist, or isn't a directory).
   */
  public void scan () throws IllegalStateException
  {
    if (basedir == null)
    {
      throw new IllegalStateException ("No basedir set");
    }
    if (!basedir.exists ())
    {
      throw new IllegalStateException ("basedir " + basedir + " does not exist");
    }
    if (!basedir.isDirectory ())
    {
      throw new IllegalStateException ("basedir " + basedir + " is not a directory");
    }

    setupDefaultFilters ();

    filesIncluded = new Vector <String> ();
    filesNotIncluded = new Vector <String> ();
    filesExcluded = new Vector <String> ();
    filesDeselected = new Vector <String> ();
    dirsIncluded = new Vector <String> ();
    dirsNotIncluded = new Vector <String> ();
    dirsExcluded = new Vector <String> ();
    dirsDeselected = new Vector <String> ();

    if (isIncluded (""))
    {
      if (!isExcluded (""))
      {
        if (isSelected ("", basedir))
        {
          dirsIncluded.addElement ("");
        }
        else
        {
          dirsDeselected.addElement ("");
        }
      }
      else
      {
        dirsExcluded.addElement ("");
      }
    }
    else
    {
      dirsNotIncluded.addElement ("");
    }
    scandir (basedir, "", true);
  }

  /**
   * Top level invocation for a slow scan. A slow scan builds up a full list of
   * excluded/included files/directories, whereas a fast scan will only have
   * full results for included files, as it ignores directories which can't
   * possibly hold any included files/directories.
   * <p>
   * Returns immediately if a slow scan has already been completed.
   */
  protected void slowScan ()
  {
    if (haveSlowResults)
    {
      return;
    }

    final String [] excl = new String [dirsExcluded.size ()];
    dirsExcluded.copyInto (excl);

    final String [] notIncl = new String [dirsNotIncluded.size ()];
    dirsNotIncluded.copyInto (notIncl);

    for (final String aElement : excl)
    {
      if (!couldHoldIncluded (aElement))
      {
        scandir (new File (basedir, aElement), aElement + File.separator, false);
      }
    }

    for (final String aElement : notIncl)
    {
      if (!couldHoldIncluded (aElement))
      {
        scandir (new File (basedir, aElement), aElement + File.separator, false);
      }
    }

    haveSlowResults = true;
  }

  /**
   * Scans the given directory for files and directories. Found files and
   * directories are placed in their respective collections, based on the
   * matching of includes, excludes, and the selectors. When a directory is
   * found, it is scanned recursively.
   *
   * @param dir
   *        The directory to scan. Must not be <code>null</code>.
   * @param vpath
   *        The path relative to the base directory (needed to prevent problems
   *        with an absolute path when using dir). Must not be
   *        <code>null</code>.
   * @param fast
   *        Whether or not this call is part of a fast scan.
   * @throws IOException
   * @see #filesIncluded
   * @see #filesNotIncluded
   * @see #filesExcluded
   * @see #dirsIncluded
   * @see #dirsNotIncluded
   * @see #dirsExcluded
   * @see #slowScan
   */
  protected void scandir (final File dir, final String vpath, final boolean fast)
  {
    String [] newfiles = dir.list ();

    if (newfiles == null)
    {
      /*
       * two reasons are mentioned in the API docs for File.list (1) dir is not
       * a directory. This is impossible as we wouldn't get here in this case.
       * (2) an IO error occurred (why doesn't it throw an exception then???)
       */

      /*
       * [jdcasey] (2) is apparently happening to me, as this is killing one of
       * my tests... this is affecting the assembly plugin, fwiw. I will
       * initialize the newfiles array as zero-length for now. NOTE: I can't
       * find the problematic code, as it appears to come from a native method
       * in UnixFileSystem...
       */
      /*
       * [bentmann] A null array will also be returned from list() on NTFS when
       * dir refers to a soft link or junction point whose target is not
       * existent.
       */
      newfiles = new String [0];

      // throw new IOException( "IO error scanning directory " +
      // dir.getAbsolutePath() );
    }

    if (!followSymlinks)
    {
      final Vector <String> noLinks = new Vector <String> ();
      for (final String aNewfile : newfiles)
      {
        try
        {
          if (isSymbolicLink (dir, aNewfile))
          {
            final String name = vpath + aNewfile;
            final File file = new File (dir, aNewfile);
            if (file.isDirectory ())
            {
              dirsExcluded.addElement (name);
            }
            else
            {
              filesExcluded.addElement (name);
            }
          }
          else
          {
            noLinks.addElement (aNewfile);
          }
        }
        catch (final IOException ioe)
        {
          final String msg = "IOException caught while checking " + "for links, couldn't get cannonical path!";
          // will be caught and redirected to Ant's logging system
          System.err.println (msg);
          noLinks.addElement (aNewfile);
        }
      }
      newfiles = new String [noLinks.size ()];
      noLinks.copyInto (newfiles);
    }

    for (final String aNewfile : newfiles)
    {
      final String name = vpath + aNewfile;
      final File file = new File (dir, aNewfile);
      if (file.isDirectory ())
      {
        if (isIncluded (name))
        {
          if (!isExcluded (name))
          {
            if (isSelected (name, file))
            {
              dirsIncluded.addElement (name);
              if (fast)
              {
                scandir (file, name + File.separator, fast);
              }
            }
            else
            {
              everythingIncluded = false;
              dirsDeselected.addElement (name);
              if (fast && couldHoldIncluded (name))
              {
                scandir (file, name + File.separator, fast);
              }
            }

          }
          else
          {
            everythingIncluded = false;
            dirsExcluded.addElement (name);
            if (fast && couldHoldIncluded (name))
            {
              scandir (file, name + File.separator, fast);
            }
          }
        }
        else
        {
          everythingIncluded = false;
          dirsNotIncluded.addElement (name);
          if (fast && couldHoldIncluded (name))
          {
            scandir (file, name + File.separator, fast);
          }
        }
        if (!fast)
        {
          scandir (file, name + File.separator, fast);
        }
      }
      else
        if (file.isFile ())
        {
          if (isIncluded (name))
          {
            if (!isExcluded (name))
            {
              if (isSelected (name, file))
              {
                filesIncluded.addElement (name);
              }
              else
              {
                everythingIncluded = false;
                filesDeselected.addElement (name);
              }
            }
            else
            {
              everythingIncluded = false;
              filesExcluded.addElement (name);
            }
          }
          else
          {
            everythingIncluded = false;
            filesNotIncluded.addElement (name);
          }
        }
    }
  }

  /**
   * Tests whether a name should be selected.
   *
   * @param name
   *        the filename to check for selecting
   * @param file
   *        the java.io.File object for this filename
   * @return <code>false</code> when the selectors says that the file should not
   *         be selected, <code>true</code> otherwise.
   */
  protected boolean isSelected (final String name, final File file)
  {
    return true;
  }

  /**
   * Returns the names of the files which matched at least one of the include
   * patterns and none of the exclude patterns. The names are relative to the
   * base directory.
   *
   * @return the names of the files which matched at least one of the include
   *         patterns and none of the exclude patterns.
   */
  public String [] getIncludedFiles ()
  {
    final String [] files = new String [filesIncluded.size ()];
    filesIncluded.copyInto (files);
    return files;
  }

  /**
   * Returns the names of the files which matched none of the include patterns.
   * The names are relative to the base directory. This involves performing a
   * slow scan if one has not already been completed.
   *
   * @return the names of the files which matched none of the include patterns.
   * @see #slowScan
   */
  public String [] getNotIncludedFiles ()
  {
    slowScan ();
    final String [] files = new String [filesNotIncluded.size ()];
    filesNotIncluded.copyInto (files);
    return files;
  }

  /**
   * Returns the names of the files which matched at least one of the include
   * patterns and at least one of the exclude patterns. The names are relative
   * to the base directory. This involves performing a slow scan if one has not
   * already been completed.
   *
   * @return the names of the files which matched at least one of the include
   *         patterns and at at least one of the exclude patterns.
   * @see #slowScan
   */
  public String [] getExcludedFiles ()
  {
    slowScan ();
    final String [] files = new String [filesExcluded.size ()];
    filesExcluded.copyInto (files);
    return files;
  }

  /**
   * <p>
   * Returns the names of the files which were selected out and therefore not
   * ultimately included.
   * </p>
   * <p>
   * The names are relative to the base directory. This involves performing a
   * slow scan if one has not already been completed.
   * </p>
   *
   * @return the names of the files which were deselected.
   * @see #slowScan
   */
  public String [] getDeselectedFiles ()
  {
    slowScan ();
    final String [] files = new String [filesDeselected.size ()];
    filesDeselected.copyInto (files);
    return files;
  }

  /**
   * Returns the names of the directories which matched at least one of the
   * include patterns and none of the exclude patterns. The names are relative
   * to the base directory.
   *
   * @return the names of the directories which matched at least one of the
   *         include patterns and none of the exclude patterns.
   */
  public String [] getIncludedDirectories ()
  {
    final String [] directories = new String [dirsIncluded.size ()];
    dirsIncluded.copyInto (directories);
    return directories;
  }

  /**
   * Returns the names of the directories which matched none of the include
   * patterns. The names are relative to the base directory. This involves
   * performing a slow scan if one has not already been completed.
   *
   * @return the names of the directories which matched none of the include
   *         patterns.
   * @see #slowScan
   */
  public String [] getNotIncludedDirectories ()
  {
    slowScan ();
    final String [] directories = new String [dirsNotIncluded.size ()];
    dirsNotIncluded.copyInto (directories);
    return directories;
  }

  /**
   * Returns the names of the directories which matched at least one of the
   * include patterns and at least one of the exclude patterns. The names are
   * relative to the base directory. This involves performing a slow scan if one
   * has not already been completed.
   *
   * @return the names of the directories which matched at least one of the
   *         include patterns and at least one of the exclude patterns.
   * @see #slowScan
   */
  public String [] getExcludedDirectories ()
  {
    slowScan ();
    final String [] directories = new String [dirsExcluded.size ()];
    dirsExcluded.copyInto (directories);
    return directories;
  }

  /**
   * <p>
   * Returns the names of the directories which were selected out and therefore
   * not ultimately included.
   * </p>
   * <p>
   * The names are relative to the base directory. This involves performing a
   * slow scan if one has not already been completed.
   * </p>
   *
   * @return the names of the directories which were deselected.
   * @see #slowScan
   */
  public String [] getDeselectedDirectories ()
  {
    slowScan ();
    final String [] directories = new String [dirsDeselected.size ()];
    dirsDeselected.copyInto (directories);
    return directories;
  }

  /**
   * Checks whether a given file is a symbolic link.
   * <p>
   * It doesn't really test for symbolic links but whether the canonical and
   * absolute paths of the file are identical - this may lead to false positives
   * on some platforms.
   * </p>
   *
   * @param parent
   *        the parent directory of the file to test
   * @param name
   *        the name of the file to test.
   * @since Ant 1.5
   */
  public boolean isSymbolicLink (final File parent, final String name) throws IOException
  {
    final File resolvedParent = new File (parent.getCanonicalPath ());
    final File toTest = new File (resolvedParent, name);
    return !toTest.getAbsolutePath ().equals (toTest.getCanonicalPath ());
  }
}
