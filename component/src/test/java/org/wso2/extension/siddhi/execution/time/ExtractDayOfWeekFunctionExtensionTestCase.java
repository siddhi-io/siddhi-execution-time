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

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractDayOfWeekFunctionExtensionTestCase {

    private static final Logger log = Logger.getLogger(ExtractDayOfWeekFunctionExtensionTestCase.class);
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
    public void extractDayOfWeekFunctionExtension() throws InterruptedException {

        log.info("ExtractDayOfWeekFunctionExtensionTestCase ");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                String day = "";
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    day = inEvent.getData(1).toString();
                    outputDays.add(day);
                    log.info("Event : " + eventCount.get() + ",ExtractedDayOfWeek : " + day);
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        String inputDays[] = { "Thursday", "Tuesday", "Wednesday" };
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-12-11 13:23:44.657", "yyyy-MM-dd HH:mm:ss.SSS" });
        inputHandler.send(new Object[] { "WSO2", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss" });
        inputHandler.send(new Object[] { "XYZ", "2014-11-12", "yyyy-MM-dd" });
        SiddhiTestHelper.waitForEvents(waitTime, 3, eventCount, timeout);
        AssertJUnit.assertEquals(inputDays[0], outputDays.get(0));
        AssertJUnit.assertEquals(inputDays[1], outputDays.get(1));
        AssertJUnit.assertEquals(inputDays[2], outputDays.get(2));
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void extractDayOfWeekFunctionExtension2() throws InterruptedException {

        log.info("ExtractDayOfWeekFunctionExtensionTestCase 2");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, dateValue string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue) as dayOfWeekExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                String day = "";
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    day = inEvent.getData(1).toString();
                    outputDays.add(day);
                    log.info("Event : " + eventCount.get() + ",ExtractedDayOfWeek : " + day);
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        String inputDays[] = { "Tuesday", "Sunday", "Monday" };
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44.657" });
        inputHandler.send(new Object[] { "WSO2", "2015-10-18 13:23:44" });
        inputHandler.send(new Object[] { "XYZ", "2014-9-22" });
        SiddhiTestHelper.waitForEvents(waitTime, 3, eventCount, timeout);
        AssertJUnit.assertEquals(inputDays[0], outputDays.get(0));
        AssertJUnit.assertEquals(inputDays[1], outputDays.get(1));
        AssertJUnit.assertEquals(inputDays[2], outputDays.get(2));
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractDayOfWeekFunctionExtension3() throws InterruptedException {

        log.info("ExtractDayOfWeekFunctionExtensionTestCaseInvalidParameterTypeInFirstArgument ");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, dateValue int,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractDayOfWeekFunctionExtension4() throws InterruptedException {

        log.info("ExtractDayOfWeekFunctionExtensionTestCaseInvalidParameterTypeInSecondArgument ");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, dateValue string,dateFormat int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void extractDayOfWeekFunctionExtension5() throws InterruptedException {
        log.info("ExtractDayOfWeekFunctionExtensionTestCaseFirstArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, dateValue string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue) as dayOfWeekExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        eventArrived = true;
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null });
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void extractDayOfWeekFunctionExtension6() throws InterruptedException {
        log.info("ExtractDayOfWeekFunctionExtensionTestCaseSecondArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        eventArrived = true;
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-12-11 13:23:44.657", null });
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void extractDayOfWeekFunctionExtension7() throws InterruptedException {
        log.info("ExtractDayOfWeekFunctionExtensionTestCaseInvalidFormat");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        eventArrived = true;
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:12:11 13:23:44.657", "yyyy-MM-dd HH:mm:ss.SSS" });
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void extractDayOfWeekFunctionExtension8() throws InterruptedException {
        log.info("ExtractDayOfWeekFunctionExtensionTestCaseInvalidInput");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        eventArrived = true;
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null, "yyyy-MM-dd HH:mm:ss.SSS" });
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractDayOfWeekFunctionExtension9() throws InterruptedException {
        log.info("ExtractDayOfWeekFunctionExtensionTestCaseInvalidNoOFArguments");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string,unit int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue,dateValue,unit) as dayOfWeekExtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void extractDayOfWeekFunctionExtension10() throws InterruptedException {

        log.info("ExtractDayOfWeekFunctionExtensionTestCaseInvalidFormatSingleParameter");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dayOfWeek(dateValue) as dayOfWeekExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        final ArrayList<String> outputDays = new ArrayList<String>();

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(null, event.getData(1));
                        eventArrived = true;
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "12" });
        siddhiAppRuntime.shutdown();
    }
}

