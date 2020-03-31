package org.jvnet.jaxb2.maven2;

import org.apache.maven.plugin.MojoExecutionException;

public interface IOptionsFactory <O>
{
  O createOptions (OptionsConfiguration optionsConfiguration) throws MojoExecutionException;
}
