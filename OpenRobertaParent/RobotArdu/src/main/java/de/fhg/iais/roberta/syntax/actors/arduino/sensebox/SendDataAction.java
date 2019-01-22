package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class SendDataAction<V> extends Action<V> {

    private static List<Sensor<Void>> listOfSenors = new ArrayList<>();

    private SendDataAction(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DATA_SEND_ACTION"), properties, comment);
        this.setReadOnly();
    }

    private SendDataAction(Expr<V> connection, Expr<V> msg, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DATA_SEND_ACTION"), properties, comment);
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link SendDataAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SendDataAction}
     */
    public static <V> SendDataAction<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SendDataAction<>(properties, comment);
    }

    @Override
    public String toString() {
        return "DataSendAction []";
    }

    @Override
    protected V accept(IVisitor<V> visitor) {
        return ((IArduinoVisitor<V>) visitor).visitDataSendAction(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        for ( Value value : block.getValue() ) {
            try {
                SensorMetaDataBean sensorMetaDataBean = ExternalSensor.extractPortAndModeAndSlot(value.getBlock(), helper);
                Method m =
                    Class
                        .forName(BlockTypeContainer.getByBlocklyName(value.getBlock().getType()).getAstClass().getName())
                        .getDeclaredMethod("make", SensorMetaDataBean.class, BlocklyBlockProperties.class, BlocklyComment.class);
                listOfSenors
                    .add((Sensor<Void>) m.invoke(null, sensorMetaDataBean, helper.extractBlockProperties(value.getBlock()), value.getBlock().getComment()));
            } catch ( NoSuchMethodException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch ( SecurityException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch ( ClassNotFoundException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch ( IllegalAccessException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch ( IllegalArgumentException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch ( InvocationTargetException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return SendDataAction.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    //TODO: fix tests for NXT
    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        for ( Sensor<Void> sensor : this.listOfSenors ) {
            Ast2JaxbHelper.addValue(jaxbDestination, sensor.getKind().getName(), sensor);
        }
        return jaxbDestination;
    }

    public static List<Sensor<Void>> getListOfSenors() {
        return listOfSenors;
    }

}
