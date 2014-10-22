package sk.fiit.stuba.ir.badal.test;

import org.junit.Assert;
import org.junit.Test;
import sk.fiit.stuba.ir.badal.main.Controller;
import sk.fiit.stuba.ir.badal.matcher.Result;

/**
 * Created by delve on 11/10/14.
 */
public class MainTest {

    @Test
    public void testTwoPersonsNegative() {
        String miroslavSatan = "04nfrq";
        String johnClayton = "0zn10r8";
        Controller cont = new Controller(miroslavSatan, johnClayton);
        Result result = cont.setUpModels();
        Assert.assertEquals(result.toString(), "[No, 200]");
    }

    @Test
    public void testTwoPersonsPositive() {
        String miroslavSatan = "04nfrq";
        String peterBondra = "01wd0p";
        Controller cont = new Controller(miroslavSatan, peterBondra);
        Result result = cont.setUpModels();
        Assert.assertEquals(result.toString(), "[Yes, 100]");
    }

}
