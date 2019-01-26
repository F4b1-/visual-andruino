package it.unibz.mobile.visualandruino.utils;


import org.junit.Test;

import java.util.ArrayList;

import it.unibz.mobile.visualandruino.models.Brick;
import it.unibz.mobile.visualandruino.models.InternalBrick;
import it.unibz.mobile.visualandruino.models.Parameter;
import it.unibz.mobile.visualandruino.models.enums.BrickTypes;
import it.unibz.mobile.visualandruino.models.enums.ComparatorTypes;
import it.unibz.mobile.visualandruino.models.enums.InternalSubTypes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class BrickExecutorTest {

    /**
     * Testing the if brick with variable that needs to be looked up.
     * @throws Exception
     */
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
        valInternal.setValue("testVariable");
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

        BrickHelper.getInstance().setSetVariable("testVariable", 2);

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, null, beMock, false);
        verify(beMock, times(0)).executeBlocks(item.getSubBricks(), null, false);
    }

    /**
     * Testing the if brick with actual numerical value.
     * @throws Exception
     */
    @Test
    public void executeIfActualValueTest() throws Exception {

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
        valInternal.setValue("2");
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

        //BrickHelper.getInstance().setSetVariable("testVariable", 2);

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, null, beMock, false);
        verify(beMock, times(0)).executeBlocks(item.getSubBricks(), null, false);
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
        valInternal.setValue("testVariable");
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

        BrickHelper.getInstance().setSetVariable("testVariable", 3);

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, null, beMock, false);
        verify(beMock, times(1)).executeBlocks(item.getSubBricks(), null, false);
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
        valInternal.setValue("testVariable");
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

        BrickHelper.getInstance().setSetVariable("testVariable", 1);

        //  create mock
        BrickExecutor beMock = mock(BrickExecutor.class);
        // define return value for method getUniqueId()
        BrickExecutor be = new BrickExecutor();
        be.executeInternal(item, null, beMock, false);
        verify(beMock, times(2)).executeBlocks(item.getSubBricks(), null, false);
    }


    @Test
    public void executeVariableTest() throws Exception {

        ArrayList<Parameter> arr=new ArrayList<Parameter>();

        BrickBuilder bbSub = new BrickBuilder("commandTest", BrickTypes.ARDUINO_COMMAND, arr);

        Brick itemSub= bbSub.buildBrick();

        ArrayList<Brick> subList=new ArrayList<Brick>();
        subList.add(itemSub);


        ArrayList<Parameter> arrInternal=new ArrayList<Parameter>();
        Parameter valParameterName=new Parameter();
        valParameterName.setValue("testVariable");
        arrInternal.add(valParameterName);


        BrickBuilder bb = new BrickBuilder("testVar", BrickTypes.INTERNAL, arrInternal);
        bb.setSubType(InternalSubTypes.VARIABLE);
        bb.setSubBricks(subList);

        InternalBrick item= (InternalBrick) bb.buildBrick();


        BrickExecutor be = new BrickExecutor();

        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep(600);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }


                BrickCommunicator.getInstance().setRequestedValue("1 ");
            }
        };

        thread.start();
        be.executeInternal(item, null, be, false);


        assertEquals(new Integer(1), BrickHelper.getInstance().getSetVariable("testVariable"));
        assertEquals("<br>Current Variables<br>&#8226;testVariable=1<br/>", BrickHelper.getInstance().getCurrentVariablesFormatted());
    }




}
