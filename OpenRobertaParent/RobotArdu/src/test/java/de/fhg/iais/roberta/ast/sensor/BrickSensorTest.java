package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class BrickSensorTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void main() throws Exception {
        String a = "BlockAST [project=[[Location [x=-19, y=1], BrickSensor [ENTER, PRESSED, NO_SLOT]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_brick1.xml"));
    }

    @Test
    public void getKey() throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = this.h.generateTransformer("/ast/sensors/sensor_brick1.xml");
        BrickSensor<Void> bs = (BrickSensor<Void>) transformer.getTree().get(0).get(1);
        Assert.assertEquals(new SensorPort("ENTER", "2"), bs.getPort());
    }

    @Test
    public void sensorBrick() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [1, DEFAULT, NO_SLOT]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [BrickSensor [ENTER, PRESSED, NO_SLOT]]\n\n"
                + "]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_brick.xml"));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_brick1.xml");
    }

    @Test
    public void reverseTransformation1() throws Exception {
        this.h.assertTransformationIsOk("/ast/sensors/sensor_brick.xml");
    }
}
