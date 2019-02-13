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
import org.wso2.extension.siddhi.execution.time.util.UnitTestAppender;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiAppCreationException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import org.wso2.siddhi.core.util.SiddhiTestHelper;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class ExtractAttributesFunctionExtensionTestCase {

    private static Logger log = Logger.getLogger(ExtractAttributesFunctionExtensionTestCase.class);
    private volatile boolean eventArrived;
    private int waitTime = 50;
    private int timeout = 30000;
    private AtomicInteger eventCount;
    LocalDateTime currentTime = LocalDateTime.now();

    @BeforeMethod
    public void init() {
        eventArrived = false;
        eventCount = new AtomicInteger(0);
    }

    @Test
    public void extractAttributesFunctionExtension() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string,dateValue string,dateFormat string,"
                + "timestampInMilliseconds long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    log.info(
                            "Event : " + eventCount.get() + ",YEAR : " + inEvent.getData(1) + "," + "MONTH : " + inEvent
                                    .getData(2) + ",HOUR : " + inEvent.getData(3));

                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-3-11 02:23:44", "yyyy-MM-dd hh:mm:ss", 1394484824000L });
        inputHandler.send(new Object[] { "IBM", "2014-3-11 02:23:44", "yyyy-MM-dd hh:mm:ss", 1394484824000L });
        inputHandler.send(new Object[] { "IBM", "2014-3-11 22:23:44", "yyyy-MM-dd hh:mm:ss", 1394556804000L });
        SiddhiTestHelper.waitForEvents(waitTime, 3, eventCount, timeout);
        AssertJUnit.assertEquals(3, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void extractAttributesFunctionExtension2() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionInvalidFormatTestCase");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler
                .send(new Object[] { "IBM", "2014:3-11 02:23:44", "yyyy-MM-dd hh:mm:ss", System.currentTimeMillis() });
        AssertJUnit.assertTrue(appender.getMessages().contains("Provided format yyyy-MM-dd hh:mm:ss does not match "
                                                                       + "with the timestamp 2014:3-11 02:23:44"));
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension3() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionInvalidParameterTypeInSecondArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue int,dateFormat string,timestampInMilliseconds "
                        + "long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension4() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionInvalidParameterTypeInThirdArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue string,dateFormat int,timestampInMilliseconds "
                        + "long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension5() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionInvalidParameterTypeInFirstArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension6() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionInvalidNoOfArgumentTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract('HOUR') as HOUR " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void extractAttributesFunctionExtension7() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionSecondArgumentNullTestCase");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null, "yyyy-MM-dd hh:mm:ss", System.currentTimeMillis() });
        Thread.sleep(100);
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:extract(unit,dateValue,"
                                                                       + "dateFormat) function. Second argument cannot"
                                                                       + " be null"));
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void extractAttributesFunctionExtension8() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionThirdArgumentNullTestCase");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('YEAR',dateValue,dateFormat) as YEAR,time:extract('MONTH',dateValue,"
                + "dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:3-11 02:23:44", null, System.currentTimeMillis() });
        Thread.sleep(100);
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:extract(unit,dateValue,"
                                                                       + "dateFormat) function. Third argument cannot "
                                                                       + "be null"));
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension9() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionTestCaseInvalidParameterFirstArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue string,dateFormat string,unit long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract(unit,dateValue,dateFormat) as YEAR insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension10() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionTestCaseInvalidParameterSecondArgumentLengthTwo");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue int,dateFormat string,unit string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract(unit,dateValue) as YEAR insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension11() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionTestCaseInvalidParameterLengthTwoDefaultDateFalseSecondArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,timestampInMilliseconds long,unit int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract(timestampInMilliseconds,unit) as YEAR insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractAttributesFunctionExtension12() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionUnitValueConstantTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,timestampInMilliseconds string,unit string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract(timestampInMilliseconds,unit) as YEAR insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void extractAttributesFunctionExtension13() throws InterruptedException {

        log.info("ExtractAttributesFunctionExtensionProcessedCalenderTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:extract('SECOND',dateValue,dateFormat) as SECOND,time:extract('MONTH',"
                + "dateValue,dateFormat) as MONTH,time:extract(timestampInMilliseconds,'HOUR') as HOUR,"
                + "time:extract(timestampInMilliseconds,'MINUTE') as MINUTE,"
                + "time:extract(timestampInMilliseconds,'HOUR_OF_DAY') as HOUR_OF_DAY " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event event : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals(44, event.getData(1));
                        AssertJUnit.assertEquals(3, event.getData(2));
                        AssertJUnit.assertEquals(currentTime.getHour(), event.getData(3));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals(44, event.getData(1));
                        AssertJUnit.assertEquals(3, event.getData(2));
                        AssertJUnit.assertEquals(currentTime.getHour(), event.getData(3));
                    }
                }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler
                .send(new Object[] { "IBM", "2014-3-11 02:23:44", "yyyy-MM-dd hh:mm:ss", System.currentTimeMillis() });
        inputHandler
                .send(new Object[] { "IBM", "2014-3-11 02:23:44", "yyyy-MM-dd hh:mm:ss", System.currentTimeMillis() });
        Thread.sleep(100);
        AssertJUnit.assertEquals(2, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }
}

