package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ShapeTest.class,
            EllipseTest.class,
            LineTest.class,
            RectTest.class})
public class ModelTestSuite { // nothing
}