package de.fhg.iais.roberta.syntax.actors.arduino.sensebox;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class SendDataAction<V> extends Action<V> {

    private List<Sensor<V>> listOfSensors = new ArrayList<>();

    private SendDataAction(List<Sensor<V>> listOfSensors, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("DATA_SEND_ACTION"), properties, comment);
        this.listOfSensors = listOfSensors;
        this.setReadOnly();
    }

    /**
     * Creates instance of {@link SendDataAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link SendDataAction}
     */
    public static <V> SendDataAction<V> make(List<Sensor<V>> listOfSensors, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new SendDataAction<>(listOfSensors, properties, comment);
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
        List<Sensor<V>> extractedSensors = new ArrayList<>();
        Mutation numberOfPluggedInSensors = block.getMutation();
        BigInteger numberOfSensros = numberOfPluggedInSensors.getItems();
        for ( Value value : block.getValue() ) {
            extractedSensors.add((Sensor<V>) helper.extractValue(block.getValue(), new ExprParam("SENSOR", BlocklyType.ANY)));
        }
        return SendDataAction.make(extractedSensors, helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);

        int numberOfSensors = getListOfSenors().size();
        List<Sensor<V>> sensors = getListOfSenors();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numberOfSensors));
        jaxbDestination.setMutation(mutation);
        for ( int i = 0; i < numberOfSensors; i++ ) {
            Ast2JaxbHelper.addValue(jaxbDestination, BlocklyConstants.SENSOR + i, sensors.get(i));
        }
        return jaxbDestination;
    }

    public List<Sensor<V>> getListOfSenors() {
        return listOfSensors;
    }

}
