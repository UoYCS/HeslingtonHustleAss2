package com.thewafflers.tests;

import com.waddle_ware.heslington_hustle.core.*;

import org.junit.Test;

import static org.junit.Assert.*;

public class EnergyTests {

  @Test
  public void testGetCurrentEnergy() {
    int limit = 100;

    Energy energy = new Energy(limit);
    assertEquals(limit, energy.getCurrentEnergy(), 0.001);
  }

  @Test
  public void testResetEnergy(){

    int limit = 100;
    Energy energy = new Energy(limit);
    energy.doActivity(ActivityType.Study);
    energy.reset();
    assertEquals(energy.getCurrentEnergy(),limit, 0.001);

  }



  /**
   * This tests whether the various methods used to check if the user
   * correctly outputs that there is enough energy to do so when there is
   *
   * Inputs over following tests
   */
  @Test
  public void testValidEnergyForActivity(){
    int limit = Energy.ENERGY_PER_STUDY * -1;
    Energy energy = new Energy(limit);

    ResourceExitConditions output = energy.tryActivityType(ActivityType.Study);
    assertSame(output.getTypes(), ResourceTypes.Energy); // always true
    assertSame("Returns is ok when enough energy to study",
            output.getConditions(), ExitConditions.IsOk);

    limit = Energy.ENERGY_PER_RECREATIONAL * -1;
    energy = new Energy(limit);

    output = energy.tryActivityType(ActivityType.Recreation);
    assertSame(output.getTypes(), ResourceTypes.Energy); // always true
    assertSame("Returns is ok when enough energy to do recreation",
            output.getConditions(), ExitConditions.IsOk);

    // food increases energy, 2 boundaries, valid action energy between 0 and max-1
    limit = 5;
    energy = new Energy(limit);
    energy.setcurrent(limit - 1); // this is for test whether energy
    output = energy.tryActivityType(ActivityType.Food);
    assertSame(output.getTypes(),ResourceTypes.Energy); // always true
    assertSame("Returns ok when when valid to eat as below max",
            output.getConditions(),ExitConditions.IsOk);

    energy.setcurrent(0);
    output = energy.tryActivityType(ActivityType.Food);
    assertSame(output.getTypes(),ResourceTypes.Energy);
    assertSame("Returns ok when valid to eat at minimum boundary (0)",
            output.getConditions(),ExitConditions.IsOk);


  }

  /**
   * Tests that you cant study with too little energy
   * Input space boundary: energy = energy needed - 1
   */
  @Test
  public void testNotEnoughEnergyToStudy(){
      int limit = Energy.ENERGY_PER_STUDY * -1;
      Energy energy = new Energy(limit);
      energy.setcurrent(limit - 1);
      ResourceExitConditions output = energy.tryActivityType(ActivityType.Study);
      assertSame("If not enough energy to study then output should be too low",
              output.getConditions(),ExitConditions.TooLow);


  }

  /**
   * Tests that you cant study with too little energy
   *
   * Input space boundary: energy = energy needed - 1
   */


  @Test
  public void testNotEnoughEnergyToRecreate(){
    int limit = Energy.ENERGY_PER_RECREATIONAL * -1;
    Energy energy = new Energy(limit);
    energy.setcurrent(limit - 1);
    ResourceExitConditions output = energy.tryActivityType(ActivityType.Study);
    assertSame("If not enough energy to recreate then output should be too low",
            output.getConditions(),ExitConditions.TooLow);


  }

  /**
   * Tests that you cant eat when at full energy
   *
   * Input space boundary: energy at limit
   */
  @Test
  public void testTooMuchEnergyToEat(){
    int limit = 4;
    Energy energy = new Energy(limit);
    ResourceExitConditions output = energy.tryActivityType(ActivityType.Food);
    assertSame("If max energy then cant eat",
            output.getConditions(),ExitConditions.TooHigh);
  }

  /**
   * tests that sleep does not output IsOk
   *
   * ActivityType.Sleep should not be passed in but if it is then it should fail.
   *
   */
  @Test
  public void testSleepPassedIntoActivityType(){
    int limit = 4;
    Energy energy = new Energy(limit);
    ResourceExitConditions output = energy.tryActivityType(ActivityType.Sleep);
    assertNotSame("If sleep is passed into tryactivity method, it should not output valid.",
            output.getConditions(),ExitConditions.IsOk);

  }

  // dont need a test for too little energy as that would mean energy is below 0
  // which should be caught by other tests


  /**
   * Tests that getLimit successfully returns limit
   *
   */
  @Test
  public void testGetLimit() {
    int limit = 4;
    Energy energy = new Energy(limit);
    assertSame("Tests that getlimit returns the limit used when making instance.",
            limit, energy.getLimit());

  }
 // ^^^ everything above is verification test





  /**
   * This test checks whether energy change by the amount specified by the constants
   * And also checks that the change in energy meets specified requirements
   * i.e. energy reduces when studying and recreating, and increases when eating
   *
   * This also fulfills FR_Energy_Amount. Validation test.
   */


  @Test
  public void testDoActivity(){

    int current_energy = 100;
    Energy energy = new Energy(current_energy);
    energy.doActivity(ActivityType.Study);

    assertEquals("Energy is reduced by study constant when studying"
            ,current_energy + Energy.ENERGY_PER_STUDY, energy.getCurrentEnergy());

    assertTrue("Energy is reduced when studying",
            energy.getCurrentEnergy() < current_energy);

    current_energy = energy.getCurrentEnergy();
    energy.doActivity(ActivityType.Food);

    assertEquals("Energy is increased by food constant when eating",
            current_energy + Energy.ENERGY_PER_FOOD, energy.getCurrentEnergy());
    assertTrue("Energy is increased when eating",
            energy.getCurrentEnergy() > current_energy);

    current_energy = energy.getCurrentEnergy();
    energy.doActivity(ActivityType.Recreation);

    assertEquals("Energy is reduced by recreation constant when recreating",
            current_energy + Energy.ENERGY_PER_RECREATIONAL, energy.getCurrentEnergy());
    assertTrue("energy is reduced when recreating",
            energy.getCurrentEnergy() < current_energy);

    current_energy = energy.getCurrentEnergy();
    energy.doActivity(ActivityType.Sleep);


      assertEquals("Sleep should not be passed into the doactivity method, if so it should not affect amount of energy", energy.getCurrentEnergy(), current_energy);
  }

}
