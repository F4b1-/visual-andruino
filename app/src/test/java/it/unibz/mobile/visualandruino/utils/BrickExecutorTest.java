package it.unibz.mobile.visualandruino.utils;


import org.junit.Test;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.ArduinoCommandBrick;
import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.ComparatorTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class BrickExecutorTest {

    @Test
    public void executeIfTest() throws Exception {

        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        /*Parameter val=new Parameter();
        val.setValue(String.valueOf((2)));
        arr.add(val );*/
        BrickBuilder bbSub = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);
        //bbSub.setCommandId(3);
        Brick itemSub= bbSub.buildBrick();

        ArrayList<Brick> subList=new ArrayList<Brick>();
        subList.add(itemSub);


        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        Parameter valInternal=new Parameter();
        valInternal.setValue(String.valueOf((2)));
        arrInternal.add(valInternal);

        Parameter valInternalComp=new Parameter();
        valInternalComp.setValue(ComparatorTypes.GREATER.toString());
        arrInternal.add(valInternalComp);

        Parameter valInternalRef=new Parameter();
        valInternalRef.setValue(String.valueOf((3)));
        arrInternal.add(valInternalRef);

        BrickBuilder bb = new BrickBuilder("test", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.IF);
        bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, "12", beMock);
        verify(beMock, times(0)).executeBlocks(item.getSubBricks(), "12");
    }

    @Test
    public void executeIfFalseTest() throws Exception {

        ArrayList<Parameter> arr=new ArrayList<Parameter>();
        /*Parameter val=new Parameter();
        val.setValue(String.valueOf((2)));
        arr.add(val );*/
        BrickBuilder bbSub = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);
        //bbSub.setCommandId(3);
        Brick itemSub= bbSub.buildBrick();

        ArrayList<Brick> subList=new ArrayList<Brick>();
        subList.add(itemSub);


        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        Parameter valInternal=new Parameter();
        valInternal.setValue(String.valueOf((3)));
        arrInternal.add(valInternal);

        Parameter valInternalComp=new Parameter();
        valInternalComp.setValue(ComparatorTypes.GREATER.toString());
        arrInternal.add(valInternalComp);

        Parameter valInternalRef=new Parameter();
        valInternalRef.setValue(String.valueOf((2)));
        arrInternal.add(valInternalRef);

        BrickBuilder bb = new BrickBuilder("test", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.IF);
        bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, "12", beMock);
        verify(beMock, times(1)).executeBlocks(item.getSubBricks(), "12");
    }


    @Test
    public void executeForTest() throws Exception {

        ArrayList<Parameter> arr=new ArrayList<Parameter>();

        BrickBuilder bbSub = new BrickBuilder("test", BrickTypes.ARDUINO_COMMAND, arr);

        Brick itemSub= bbSub.buildBrick();

        ArrayList<Brick> subList=new ArrayList<Brick>();
        subList.add(itemSub);


        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        Parameter valInternal=new Parameter();
        valInternal.setValue(String.valueOf((1)));
        arrInternal.add(valInternal);

        Parameter valInternalComp=new Parameter();
        valInternalComp.setValue(ComparatorTypes.GREATER.toString());
        arrInternal.add(valInternalComp);

        Parameter valInternalRef=new Parameter();
        valInternalRef.setValue(String.valueOf((3)));
        arrInternal.add(valInternalRef);

        BrickBuilder bb = new BrickBuilder("test", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.FOR);
        bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, "12", beMock);
        verify(beMock, times(2)).executeBlocks(item.getSubBricks(), "12");
    }



}
