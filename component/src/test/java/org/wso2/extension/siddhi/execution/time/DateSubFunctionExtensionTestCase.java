/*
 * Copyright (c)  2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.extension.siddhi.execution.time;

import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiAppCreationException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.core.util.SiddhiTestHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class DateSubFunctionExtensionTestCase {

    private static final Logger log = Logger.getLogger(DateSubFunctionExtensionTestCase.class);
    private volatile boolean eventArrived;
    private int waitTime = 50;
    private int timeout = 30000;
    private AtomicInteger eventCount;

    @BeforeMethod
    public void init() {
        eventArrived = false;
        eventCount = new AtomicInteger(0);
    }

    @Test
    public void dateSubFunctionExtension() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string,dateValue string,"
                + "dateFormat string,timestampInMilliseconds long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as "
                + "yearSubtracted,time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    log.info("Event : " + eventCount.get() + ",YEAR_SUBTRACTED : " + inEvent.getData(1) + ","
                            + "MONTH_SUBTRACTED : " + inEvent.getData(2) + "," + "YEAR_SUBTRACTED_IN_MILLS : " + inEvent
                            .getData(3));
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        SiddhiTestHelper.waitForEvents(waitTime, 3, eventCount, timeout);
        AssertJUnit.assertEquals(3, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension2() throws InterruptedException {

        log.info("TestCaseForDateSubFunctionExtensionInvalidArgumentsIntime:dateSubFunction");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr) as yearSubtracted,time:dateSub("
                + "dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') "
                + "as yearSubtractedUnix " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension3() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseInvalidParameterTypeInSecondArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr String);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension4() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseForInvalidParameterTypeInFourthArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue string,dateFormat int,timestampInMilliseconds "
                        + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension5() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseForInvalidParameterTypeInFirstArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue int,dateFormat string,timestampInMilliseconds "
                        + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateSubFunctionExtension6() throws InterruptedException {

        log.info("DateSubFunctionExtensionInvalidFormatTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals("1352620424000", event.getData(3));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals("1352620424000", event.getData(3));
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:11:11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2014,11,11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        Thread.sleep(100);
        AssertJUnit.assertEquals(2, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateSubFunctionExtension7() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseFirstArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals("1352620424000", event.getData(3));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals("1352620424000", event.getData(3));
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null, "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", null, "ss", 1415692424000L, 2 });
        Thread.sleep(100);
        AssertJUnit.assertEquals(2, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateSubFunctionExtension8() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseFourthArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals("1352620424000", event.getData(3));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals("1352620424000", event.getData(3));
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", null, 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2015-11-11 13:23:44", null, 1415692424000L, 2 });
        Thread.sleep(100);
        AssertJUnit.assertEquals(2, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateSubFunctionExtension9() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseSecondArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as "
                + "yearSubtracted,time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') "
                + "as yearSubtractedUnix " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals(null, event.getData(3));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        AssertJUnit.assertEquals(null, event.getData(2));
                        AssertJUnit.assertEquals(null, event.getData(3));
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, null });
        inputHandler.send(new Object[] { "IBM", "2011-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, null });
        Thread.sleep(100);
        AssertJUnit.assertEquals(2, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension10() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseUnitValueConstant");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,expr int,unit string);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,unit) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "'month',dateFormat) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as "
                + "yearSubtractedUnix " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension11() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,unit int ,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,unit) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,"
                + "unit) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as yearSubtractedUnix "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension12() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseInvalidParameterFormatSecondArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,unit int ,expr string);";
        String query = ("@info(name = 'query1') " + "from inputStream select symbol ,time:dateSub(dateValue,expr,"
                + "unit) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as yearSubtractedUnix "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension13() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseInvalidParameterFormatThirdArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,unit int ,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream select symbol ,time:dateSub(dateValue,expr,"
                + "unit) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as yearSubtractedUnix "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension14() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseInvalidParameterFormatFirstArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue int,dateFormat string,timestampInMilliseconds"
                        + " long,unit int ,expr string);";
        String query = ("@info(name = 'query1') " + "from inputStream select symbol ,time:dateSub(dateValue,expr,"
                + "unit) as monthSubtracted,time:dateSub(timestampInMilliseconds,expr,'year') as yearSubtractedUnix "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateSubFunctionExtension15() throws InterruptedException {

        log.info("DateSubFunctionExtensionTestCaseCastDesireFormat");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string,dateValue string,dateFormat string,"
                + "timestampInMilliseconds long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(timestampInMilliseconds,expr,'year')"
                + " as yearSubtractedUnix " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", 1415692424000L, "yyyy-MM-dd HH:mm:ss", 2 });
        Thread.sleep(100);
        AssertJUnit.assertEquals(1, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension16() throws InterruptedException {

        log.info("DateSubFunctionExtensionSecondParameteInvalidLengthThreeDefaultDateFalseTestCaseTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr string,unit string);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(timestampInMilliseconds,expr,unit) as yearSubtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension17() throws InterruptedException {

        log.info("DateSubFunctionExtensionThidParameteInvalidLengthThreeDefaultDateFalseTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int,unit int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(timestampInMilliseconds,expr,unit) as yearSubtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateSubFunctionExtension18() throws InterruptedException {

        log.info("DateSubFunctionExtensionThidParameteInvalidLengthFourTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int,unit int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,unit,dateFormat) as yearSubtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateSubFunctionExtension19() throws InterruptedException {

        log.info("DateSubFunctionExtensionProcessedCalenderTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,expr int);";
        String query = ("@info(name = 'query1') "
                + "from inputStream select symbol ,time:dateSub(dateValue,expr,'year',dateFormat) as yearSubtracted,"
                + "time:dateSub(dateValue,expr,'second',dateFormat) as secondSubtracted,"
                + "time:dateSub(dateValue,expr,'minute',dateFormat) as minuteSubtracted, "
                + "time:dateSub(dateValue,expr,'hour',dateFormat) as hourSubtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals("2012-11-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2014-11-11 13:23:42", event.getData(2));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals("2012-11-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2014-11-11 13:23:42", event.getData(2));
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        Thread.sleep(100);
        AssertJUnit.assertEquals(3, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

}

