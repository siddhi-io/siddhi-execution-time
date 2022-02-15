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

package io.siddhi.extension.execution.time;

import io.siddhi.core.SiddhiAppRuntime;
import io.siddhi.core.SiddhiManager;
import io.siddhi.core.event.Event;
import io.siddhi.core.exception.SiddhiAppCreationException;
import io.siddhi.core.query.output.callback.QueryCallback;
import io.siddhi.core.stream.input.InputHandler;
import io.siddhi.core.util.EventPrinter;
import io.siddhi.core.util.SiddhiTestHelper;
import io.siddhi.extension.execution.time.util.UnitTestAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class DateFormatFunctionExtensionTestCase {

    private static final Logger log = (Logger) LogManager.getLogger(DateFormatFunctionExtensionTestCase.class);
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
    public void dateFormatFunctionExtension() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);

                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    log.info("Event : " + eventCount.get() + ",formattedDate : " + inEvent.getData(1) + ","
                            + "formattedMillsDate : " + inEvent.getData(2));

                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, "ss" });
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, "ss" });
        inputHandler.send(new Object[] {
                "IBM", "2014-11-11 13:23:44.657", "yyyy-MM-dd HH:mm:ss.SSS", 1415692424000L, "yyyy-MM-dd"
        });

        SiddhiTestHelper.waitForEvents(waitTime, 3, eventCount, timeout);
        AssertJUnit.assertEquals(3, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateFormatFunctionExtension2() throws InterruptedException {

        log.info("DateFormatFunctionExtensionInvalidFormatTestCase");
        UnitTestAppender appender = new UnitTestAppender("UnitTestAppender", null);
        final Logger logger = (Logger) LogManager.getRootLogger();
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        appender.start();
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] {
                "IBM", "2014:11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, "yyyy-MM-dd"
        });
        Thread.sleep(100);
        AssertJUnit.assertTrue(((UnitTestAppender) logger.getAppenders().
                get("UnitTestAppender")).getMessages().contains("Provided format yyyy-MM-dd HH:mm:ss does not match "
                                                                       + "with the timestamp 2014:11-11 13:23:44 "
                                                                       + "Unparseable"));
        siddhiAppRuntime.shutdown();
        logger.removeAppender(appender);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension3() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseInvalidParameterTypeInFirstArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue int,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension4() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseInvalidParameterTypeInSecondArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat int);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension5() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseInvalidParameterTypeInThirdArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat int,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension6() throws InterruptedException {

        log.info("DateFormatFunctionExtensionInvalidNoOfArgumentTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue) as formattedDate," + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateFormatFunctionExtension7() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseFirstArgumentNul");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                AssertJUnit.assertNull(inEvents[0].getData(1));
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null, "yyyy-MM-dd HH:mm:ss", 1415692424000L, "yyyy-MM-dd" });
        Thread.sleep(100);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateFormatFunctionExtension8() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseSecondArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                AssertJUnit.assertNull(inEvents[0].getData(1));
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, null });
        Thread.sleep(100);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateFormatFunctionExtension9() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseThirdArgumentNul");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                AssertJUnit.assertNull(inEvents[0].getData(1));
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:11-11 13:23:44", null, 1415692424000L, "ss" });
        Thread.sleep(100);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateFormatFunctionExtension10() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseForCastingDesiredFormat");
        UnitTestAppender appender = new UnitTestAppender("UnitTestAppender", null);
        final Logger logger = (Logger) LogManager.getRootLogger();
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        appender.start();
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds string,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, "ss" });
        Thread.sleep(100);
        AssertJUnit.assertTrue(((UnitTestAppender) logger.getAppenders().
                get("UnitTestAppender")).getMessages().contains("Provided Data type cannot be cast to desired format. "
                                                                       + "java.lang.Long cannot be cast to "
                                                                       + "java.lang.String"));
        siddhiAppRuntime.shutdown();
        logger.removeAppender(appender);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension11() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseInvalidParameterTypeFirstArgumentLenghtTwo");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue int,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension12() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseInvalidParameterTypeSecondArgumentLenghtTwo");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat int);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(dateValue,targetFormat) as formattedDate,"
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        ;
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateFormatFunctionExtension13() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseInvalidParameterTypeSecondArgumentLenghtTwoDefaultFormatFalse");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "define stream inputStream (symbol string," + "timestampInMilliseconds long,targetFormat int);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateFormatFunctionExtension14() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseFirstArgumentNull");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                AssertJUnit.assertNull(inEvents[0].getData(1));
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", null, "ss" });
        Thread.sleep(100);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateFormatFunctionExtension15() throws InterruptedException {

        log.info("DateFormatFunctionExtensionTestCaseDesiredFormat");
        UnitTestAppender appender = new UnitTestAppender("UnitTestAppender", null);
        final Logger logger = (Logger) LogManager.getRootLogger();
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        appender.start();
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,timestampInMilliseconds long,targetFormat string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:dateFormat(timestampInMilliseconds,"
                + "targetFormat) as formattedUnixDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", "ss", 1415692424000L });
        Thread.sleep(100);
        AssertJUnit.assertTrue(((UnitTestAppender) logger.getAppenders().
                get("UnitTestAppender")).getMessages().contains("Provided Data type cannot be cast to desired format. "
                                                                       + "java.lang.Long cannot be cast to "
                                                                       + "java.lang.String."));
        siddhiAppRuntime.shutdown();
        logger.removeAppender(appender);
    }
}

