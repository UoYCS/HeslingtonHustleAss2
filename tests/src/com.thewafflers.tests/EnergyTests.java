package com.thewafflers.tests;
import com.waddle_ware.heslington_hustle.core.Energy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnergyTests {
  @Test
  public void testgetCurrentEnergy() {
    int limit = 100;

    Energy energy = new Energy(limit);
    assertEquals(limit, energy.getCurrentEnergy(), 0.001);
  }
}
