package com.thewafflers.tests;
import com.waddle_ware.heslington_hustle.core.Energy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnergyTests {

  // tests to check the class is initialised correctly

  @Test
  public void testgetCurrentEnergy() {
    int limit = 100;

    Energy energy = new Energy(limit);
    assertEquals(limit, energy.getCurrentEnergy(), 0.001);
  }

  // test reset method

  // test isOK method

  // test tryactivity type method

  // test doactivity method

  // test getlimit method
}
