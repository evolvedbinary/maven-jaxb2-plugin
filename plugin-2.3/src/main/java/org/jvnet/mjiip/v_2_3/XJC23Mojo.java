package org.jvnet.mjiip.v_2_3;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jvnet.jaxb2.maven2.RawXJC2Mojo;
import org.xml.sax.InputSource;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.ModelLoader;
import com.sun.tools.xjc.Options;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.xsom.XSSchema;
import com.sun.xml.xsom.XSSchemaSet;

/**
 * JAXB 2.x Mojo.
 *
 * @author Aleksei Valikov (valikov@gmx.net)
 */
@Mojo (name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class XJC23Mojo extends RawXJC2Mojo <Options>
{

  private final org.jvnet.jaxb2.maven2.OptionsFactory <Options> optionsFactory = new OptionsFactory ();

  @Override
  protected org.jvnet.jaxb2.maven2.OptionsFactory <Options> getOptionsFactory ()
  {
    return optionsFactory;
  }

  @Override
  public void doExecute (final Options options) throws MojoExecutionException
  {
    final Model model = loadModel (options);
    final Outline outline = generateCode (model);
    writeCode (outline);

  }

  protected Model loadModel (final Options options) throws MojoExecutionException
  {
    if (getVerbose ())
    {
      getLog ().info ("Parsing input schema(s)...");
    }
    getLog ().info ("Input schemas: " + Arrays.toString (options.getGrammars ()));
    for (final InputSource x : options.getGrammars ())
    {
      getLog ().info ("  Next InputSource");
      getLog ().info ("    publicID: " + x.getPublicId ());
      getLog ().info ("    systemID: " + x.getSystemId ());
      getLog ().info ("    encoding: " + x.getEncoding ());
      getLog ().info ("    byteStream: " + x.getByteStream ());
      getLog ().info ("    charStream: " + x.getCharacterStream ());
    }
    final Model model = ModelLoader.load (options,
                                          new JCodeModel (),
                                          new LoggingErrorReceiver ("Error while parsing schema(s).",
                                                                    getLog (),
                                                                    getVerbose ()));

    if (model == null)
      throw new MojoExecutionException ("Unable to parse input schema(s). Error messages should have been provided.");
    try
    {
      final Field f = model.getClass ().getDeclaredField ("schemaComponent");
      final XSSchemaSet xs = (XSSchemaSet) f.get (model);
      getLog ().info ("schemaComponent = " + xs);
      if (xs != null)
      {
        final Iterator <XSSchema> it = xs.iterateSchema ();
        while (it.hasNext ())
        {
          final XSSchema a = it.next ();
          getLog ().info ("  XSSchema = " + a);
        }
      }
    }
    catch (final Exception ex)
    {}
    return model;
  }

  protected Outline generateCode (final Model model) throws MojoExecutionException
  {
    if (getVerbose ())
    {
      getLog ().info ("Compiling input schema(s)...");
    }

    final Outline outline = model.generateCode (model.options,
                                                new LoggingErrorReceiver ("Error while generating code.",
                                                                          getLog (),
                                                                          getVerbose ()));
    if (outline == null)
    {
      throw new MojoExecutionException ("Failed to compile input schema(s)! Error messages should have been provided.");
    }
    else
    {
      return outline;
    }
  }

  protected void writeCode (final Outline outline) throws MojoExecutionException
  {

    if (getWriteCode ())
    {
      final Model model = outline.getModel ();
      final JCodeModel codeModel = model.codeModel;
      final File targetDirectory = model.options.targetDir;
      if (getVerbose ())
      {
        getLog ().info (MessageFormat.format ("Writing output to [{0}].", targetDirectory.getAbsolutePath ()));
      }
      try
      {
        if (getCleanPackageDirectories ())
        {
          if (getVerbose ())
          {
            getLog ().info ("Cleaning package directories.");
          }
          cleanPackageDirectories (targetDirectory, codeModel);
        }
        final CodeWriter codeWriter = new LoggingCodeWriter (model.options.createCodeWriter (),
                                                             getLog (),
                                                             getVerbose ());
        codeModel.build (codeWriter);
      }
      catch (final IOException e)
      {
        throw new MojoExecutionException ("Unable to write files: " + e.getMessage (), e);
      }
    }
    else
    {
      getLog ().info ("The [writeCode] setting is set to false, the code will not be written.");
    }
  }

  private void cleanPackageDirectories (final File targetDirectory, final JCodeModel codeModel)
  {
    for (final Iterator <JPackage> packages = codeModel.packages (); packages.hasNext ();)
    {
      final JPackage _package = packages.next ();
      final File packageDirectory;
      if (_package.isUnnamed ())
      {
        packageDirectory = targetDirectory;
      }
      else
      {
        packageDirectory = new File (targetDirectory, _package.name ().replace ('.', File.separatorChar));
      }
      if (packageDirectory.isDirectory ())
      {
        if (isRelevantPackage (_package))
        {
          if (getVerbose ())
          {
            getLog ().info (MessageFormat.format ("Cleaning directory [{0}] of the package [{1}].",
                                                  packageDirectory.getAbsolutePath (),
                                                  _package.name ()));
          }
          cleanPackageDirectory (packageDirectory);
        }
        else
        {
          if (getVerbose ())
          {
            getLog ().info (MessageFormat.format ("Skipping directory [{0}] of the package [{1}] as it does not contain generated classes or resources.",
                                                  packageDirectory.getAbsolutePath (),
                                                  _package.name ()));
          }
        }
      }
    }
  }

  private boolean isRelevantPackage (final JPackage _package)
  {
    if (_package.propertyFiles ().hasNext ())
    {
      return true;
    }
    final Iterator <JDefinedClass> classes = _package.classes ();
    for (; classes.hasNext ();)
    {
      final JDefinedClass _class = classes.next ();
      if (!_class.isHidden ())
      {
        return true;
      }
    }
    return false;
  }
}
