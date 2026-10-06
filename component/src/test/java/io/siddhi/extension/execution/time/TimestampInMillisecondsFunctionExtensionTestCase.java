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
import io.siddhi.extension.execution.time.util.UnitTestAppender;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class TimestampInMillisecondsFunctionExtensionTestCase {

    private static final Logger log = (Logger) LogManager.
            getLogger(TimestampInMillisecondsFunctionExtensionTestCase.class);
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
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds('2007-11-30 10:30:19','yyyy-MM-DD HH:MM:SS') " + "as "
                + "timestampInMillisecondsWithArguments, " + "time:timestampInMilliseconds('2007-11-30 10:30:19.000') "
                + "as withOnlyDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    log.info("Event : " + eventCount.get() + " timestampInMillisecondsWithAllArguments : " + inEvent
                            .getData(1));
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        Thread.sleep(100);
        AssertJUnit.assertEquals(1, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timestampInMillisecondsWithDateFunctionExtension() throws InterruptedException {

        log.info("TimestampInMillisecondsWithDateFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds('2007-11-30 10:30:19.000') as "
                + "timestampInMillisecondsWithDateArgument insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.
                createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (int cnt = 0; cnt < inEvents.length; cnt++) {
                    eventCount.incrementAndGet();
                    log.info("Event : " + eventCount.get() + " timestampInMillisecondsWithDateArgument : "
                            + inEvents[cnt].getData(1));
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        Thread.sleep(100);
        AssertJUnit.assertEquals(1, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timestampInMillisecondsWithoutArgumentFunctionExtension() throws InterruptedException {

        log.info("TimestampInMillisecondsWithoutArgumentFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds() as "
                + "timestampInMillisecondsWithoutArguments insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.
                createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    log.info("Event : " + eventCount.get() + " timestampInMillisecondsWithoutArguments : " + inEvent
                            .getData(1));
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        Thread.sleep(100);
        AssertJUnit.assertEquals(1, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension2() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionInvalidFormatTypeTestCase");
        UnitTestAppender appender = new UnitTestAppender("UnitTestAppender", null);
        final Logger logger = (Logger) LogManager.getRootLogger();
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        appender.start();
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds('2007:11:30 10:30:19','yyyy-MM-DD HH:MM:SS') as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds('2007-11-30 10:30:19.000') as "
                + "withOnlyDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        Thread.sleep(100);
        AssertJUnit.assertTrue(((UnitTestAppender) logger.getAppenders().
                get("UnitTestAppender")).getMessages().contains("Provided format yyyy-MM-DD HH:MM:SS does not match "
                + "with the timestamp 2007:11:30 10:30:19"));
        siddhiAppRuntime.shutdown();
        logger.removeAppender(appender);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension3() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionInvalidNoOfArgumentsTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds('2007-11-30 10:30:19','yyyy-MM-DD HH:MM:SS') as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds('2007-11-30 10:30:19.000',"
                + "'2007-11-30 10:30:19','2006-11-30 10:30:19') as withOnlyDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

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
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        Thread.sleep(100);
        AssertJUnit.assertEquals(1, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension4() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionTestCaseInvalidFormatLengthTwo");
        UnitTestAppender appender = new UnitTestAppender("UnitTestAppender", null);
        final Logger logger = (Logger) LogManager.getRootLogger();
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        appender.start();
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price int, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds('2007') as "
                + "timestampInMillisecondsWithArguments insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        Thread.sleep(100);
        AssertJUnit.assertTrue(((UnitTestAppender) logger.getAppenders().
                get("UnitTestAppender")).getMessages().contains("Provided format yyyy-MM-dd HH:mm:ss.SSS does not match"
                + " with the timestamp 2007"));
        siddhiAppRuntime.shutdown();
        logger.removeAppender(appender);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension5() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionInvalidParameterFirstArgumentTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds(price,'yyyy-MM-DD HH:MM:SS') as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds('2007-11-30 10:30:19.000') as "
                + "withOnlyDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension6() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionInvalidParameterLengthOne" + "TestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds(volume) as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds('2007-11-30 10:30:19.000') as"
                + " withOnlyDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension7() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionInvalidParameterSecondArgument" + "TestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds('2007-11-30 10:30:19',volume) as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds('2007-11-30 10:30:19.000') as"
                + " withOnlyDate insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension8() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionFirstArgumentNullTestCase");
        String dateTime = "2007-11-30 10:30:19.000";
        final Timestamp timestamp = Timestamp.valueOf(dateTime);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price string, volume long);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds(price,'yyyy-MM-DD HH:MM:SS') as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds(('" + dateTime + "')) as "
                + "withOnlyDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                AssertJUnit.assertNull(inEvents[0].getData(1));
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", null, 100L});
        Thread.sleep(100);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timestampInMillisecondsWithAllArgumentsFunctionExtension9() throws InterruptedException {

        log.info("TimestampInMillisecondsWithAllArgumentsFunctionExtensionSecondArgumentNullTestCase");
        String dateTime = "2007-11-30 10:30:19.000";
        final Timestamp timestamp = Timestamp.valueOf(dateTime);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, price long, volume string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:timestampInMilliseconds(('" + dateTime + "'),volume) as "
                + "timestampInMillisecondsWithArguments, time:timestampInMilliseconds(('" + dateTime + "')) as "
                + "withOnlyDate insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                AssertJUnit.assertNull(inEvents[0].getData(1));
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, null});
        Thread.sleep(100);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timestampInMillisecondsWithIsoOffsetPattern() throws InterruptedException {

        log.info("TimestampInMillisecondsWithIsoOffsetPatternTestCase");
        AssertJUnit.assertEquals(Long.valueOf(1512018019000L),
                timestampInMilliseconds("2017-11-30T10:30:19+05:30", "yyyy-MM-dd'T'HH:mm:ssXXX"));
        AssertJUnit.assertEquals(Long.valueOf(1512037819000L),
                timestampInMilliseconds("2017-11-30T10:30:19Z", "yyyy-MM-dd'T'HH:mm:ssXXX"));
    }

    @Test
    public void timestampInMillisecondsWithGmtOffsetZoneName() throws InterruptedException {

        log.info("TimestampInMillisecondsWithGmtOffsetZoneNameTestCase");
        AssertJUnit.assertEquals(Long.valueOf(1512018019000L),
                timestampInMilliseconds("2017-11-30 10:30:19 GMT+05:30", "yyyy-MM-dd HH:mm:ss z"));
    }

    @Test
    public void timestampInMillisecondsWithDaylightSavingZoneName() throws InterruptedException {

        log.info("TimestampInMillisecondsWithDaylightSavingZoneNameTestCase");
        AssertJUnit.assertEquals(Long.valueOf(1793511000000L),
                timestampInMilliseconds("2026-11-01 01:30:00 EDT", "yyyy-MM-dd HH:mm:ss z"));
    }

    @Test
    public void timestampInMillisecondsWithLowerCaseMonthName() throws InterruptedException {

        log.info("TimestampInMillisecondsWithLowerCaseMonthNameTestCase");
        AssertJUnit.assertEquals(Long.valueOf(1512018019000L),
                timestampInMilliseconds("30-nov-2017 10:30:19 +0530", "dd-MMM-yyyy HH:mm:ss Z"));
    }

    @Test
    public void timestampInMillisecondsRejectsColonOffsetForRfc822Pattern() throws InterruptedException {

        log.info("TimestampInMillisecondsRejectsColonOffsetForRfc822PatternTestCase");
        UnitTestAppender appender = new UnitTestAppender("UnitTestAppender", null);
        final Logger logger = (Logger) LogManager.getRootLogger();
        logger.setLevel(Level.ALL);
        logger.addAppender(appender);
        appender.start();
        try {
            AssertJUnit.assertNull(timestampInMilliseconds("2017-11-30T10:30:19+05:30", "yyyy-MM-dd'T'HH:mm:ssZ"));
            AssertJUnit.assertTrue(appender.getMessages().contains("Provided format yyyy-MM-dd'T'HH:mm:ssZ does not "
                    + "match with the timestamp 2017-11-30T10:30:19+05:30"));
        } finally {
            logger.removeAppender(appender);
        }
    }

    private Long timestampInMilliseconds(String date, String format) throws InterruptedException {

        SiddhiManager siddhiManager = new SiddhiManager();
        String inStreamDefinition = "define stream inputStream (date string, format string);";
        String query = "@info(name = 'query1') from inputStream "
                + "select time:timestampInMilliseconds(date, format) as millis insert into outputStream;";
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        AtomicReference<Long> result = new AtomicReference<>();
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                result.set((Long) inEvents[0].getData(0));
            }
        });
        siddhiAppRuntime.start();
        try {
            siddhiAppRuntime.getInputHandler("inputStream").send(new Object[]{date, format});
            Thread.sleep(100);
            return result.get();
        } finally {
            siddhiAppRuntime.shutdown();
        }
    }
}
