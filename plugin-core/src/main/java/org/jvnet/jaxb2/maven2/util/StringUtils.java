package org.jvnet.jaxb2.maven2.util;

import javax.annotation.Nonnull;

public final class StringUtils
{
  /**
   * Checks if a (trimmed) String is <code>null</code> or empty.
   *
   * @param string
   *        the String to check
   * @return <code>true</code> if the string is <code>null</code>, or length
   *         zero once trimmed.
   */
  public static boolean isEmptyTrimmed (final String string)
  {
    return string == null || string.trim ().length () == 0;
  }

  @Nonnull
  public static String escapeSpace (@Nonnull final String url)
  {
    // URLEncoder doesn't work.
    final StringBuilder buf = new StringBuilder (url.length () * 3);
    for (final char c : url.toCharArray ())
    {
      // TODO: not sure if this is the only character that needs to be
      // escaped.
      if (c == ' ')
        buf.append ("%20");
      else
        buf.append (c);
    }
    return buf.toString ();
  }
}
