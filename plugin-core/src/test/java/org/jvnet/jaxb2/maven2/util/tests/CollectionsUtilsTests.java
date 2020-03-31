package org.jvnet.jaxb2.maven2.util.tests;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;

import org.junit.Test;
import org.jvnet.jaxb2.maven2.util.CollectionUtils;

public class CollectionsUtilsTests
{
  @Test
  public void correctlyCompares ()
  {
    final Function <String, String> identity = Function.identity ();
    final Comparator <String> gt = CollectionUtils.<String> gtWithNullAsGreatest ();
    final Comparator <String> lt = CollectionUtils.<String> ltWithNullAsSmallest ();
    assertEquals ("b", CollectionUtils.bestValue (Arrays.<String> asList ("a", "b"), identity, gt));
    assertEquals ("a", CollectionUtils.bestValue (Arrays.<String> asList ("a", "b"), identity, lt));
    assertEquals (null, CollectionUtils.bestValue (Arrays.<String> asList ("a", null, "b"), identity, gt));
    assertEquals (null, CollectionUtils.bestValue (Arrays.<String> asList ("a", null, "b"), identity, lt));
  }
}
