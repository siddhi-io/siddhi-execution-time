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
import org.wso2.extension.siddhi.execution.time.util.SiddhiTestHelper;
import org.wso2.extension.siddhi.execution.time.util.UnitTestAppender;
import org.wso2.siddhi.core.SiddhiAppRuntime;
import org.wso2.siddhi.core.SiddhiManager;
import org.wso2.siddhi.core.event.Event;
import org.wso2.siddhi.core.exception.SiddhiAppCreationException;
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.StreamJunction;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;

import java.util.concurrent.atomic.AtomicInteger;

public class DateAddFunctionExtensionTestCase {

    private static Logger log = Logger.getLogger(DateAddFunctionExtensionTestCase.class);
    private AtomicInteger count = new AtomicInteger(0);
    private volatile boolean eventArrived;
    private int waitTime = 50;
    private int timeout = 30000;
    private AtomicInteger eventCount;

    @BeforeMethod
    public void init() {
        count.set(0);
        eventArrived = false;
        eventCount = new AtomicInteger(0);
    }

    @Test
    public void dateAddFunctionExtension() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string,dateValue string,dateFormat string,"
                + "timestampInMilliseconds long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    count.incrementAndGet();
                    if (count.get() == 1) {
                        AssertJUnit.assertEquals("2016-11-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2015-01-11 13:23:44", event.getData(2));
                        AssertJUnit.assertEquals("1415699624000", event.getData(3));
                        eventArrived = true;
                    }
                    if (count.get() == 2) {
                        AssertJUnit.assertEquals("2012-05-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2010-07-11 13:23:44", event.getData(2));
                        AssertJUnit.assertEquals("1415699624000", event.getData(3));
                        eventArrived = true;
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        SiddhiTestHelper.waitForEvents(100, 2, count, 60000);
        AssertJUnit.assertEquals(2, count.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension2() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterTypeInFirstArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue int,dateFormat string,timestampInMilliseconds "
                        + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension3() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterTypeInSecondArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension4() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidNoOfArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateAddFunctionExtension5() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseFirstArgumentNull");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null, "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:dateAdd("
                                                                       + "date,expr,unit,dateFormat) function. "
                                                                       + "First argument cannot be null"));
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateAddFunctionExtension6() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseFourthArgumentNull");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    count.incrementAndGet();
                    AssertJUnit.assertEquals("2012-05-11 13:23:44", event.getData(1));
                    AssertJUnit.assertEquals("2010-07-11 13:23:44", event.getData(2));
                    AssertJUnit.assertEquals("1415699624000", event.getData(3));
                    eventArrived = true;
                    }
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", null, 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        SiddhiTestHelper.waitForEvents(100, 1, count, 60000);
        AssertJUnit.assertEquals(1, count.get());
        AssertJUnit.assertTrue(eventArrived);
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:dateAdd("
                                                                       + "date,expr,unit,dateFormat) function. "
                                                                       + "Fourth argument cannot be null"));
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateAddFunctionExtension7() throws InterruptedException {
        log.info("DateAddFunctionExtensionTestCaseSecondArgumentNull");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string,dateValue string,dateFormat string,"
                + "timestampInMilliseconds long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    count.incrementAndGet();
                        AssertJUnit.assertEquals("2012-05-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2010-07-11 13:23:44", event.getData(2));
                        AssertJUnit.assertEquals("1415699624000", event.getData(3));
                        eventArrived = true;
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, null });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        SiddhiTestHelper.waitForEvents(100, 1, count, 60000);
        AssertJUnit.assertEquals(1, count.get());
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:dateAdd("
                                                                       + "date,expr,unit,dateFormat) function. "
                                                                       + "Second argument cannot be null"));
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension8() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterTypeInFourthArgument");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue string,dateFormat int,timestampInMilliseconds "
                        + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateAddFunctionExtension9() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidFormat");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:11:11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        AssertJUnit.assertTrue(appender.getMessages().contains("Provided format yyyy-MM-dd HH:mm:ss does not match "
                                                                       + "with the timestamp"));
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension10() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterTypeInFirstArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string,dateValue int,dateFormat string,timestampInMilliseconds "
                        + "long,unit string,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,'YEAR',unit) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension11() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterTypeInSecondArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,unit int,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,'YEAR',unit) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension12() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseForUnitValueConstant");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,unit string,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,unit) as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension13() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterTypeInThirdArgumentElseCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,unit int,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,unit) as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test
    public void dateAddFunctionExtension14() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCase");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", 1415692424000L, "yyyy-MM-dd HH:mm:ss", 2 });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", 1415692424000L, "yyyy-MM-dd HH:mm:ss", 2 });
        AssertJUnit.assertTrue(appender.getMessages().contains("Provided Data type cannot be cast to desired format. "
                                                                       + "java.lang.Long cannot be cast to "
                                                                       + "java.lang.String"));
        siddhiAppRuntime.shutdown();
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension15() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterSecondArgumentElseCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,unit string,expr string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dateAdd(timestampInMilliseconds,expr,unit) as yearAddedMills "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension16() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterThirdArgumentLengthThree");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds"
                + " long,unit int,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:dateAdd(dateValue,expr,unit) as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void dateAddFunctionExtension17() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseInvalidParameterThirdArgumentLengthFour");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long" + ",expr int,unit int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,unit,dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);
        ;
    }

    @Test
    public void dateAddFunctionExtension18() throws InterruptedException {

        log.info("DateAddFunctionExtensionTestCaseFirstArgumentNull");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol ,time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills "
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
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", null, 2 });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", null, 2 });
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:dateAdd("
                                                                       + "timestampInMilliseconds,expr,unit) function. "
                                                                       + "First argument cannot be null"));
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void dateAddFunctionExtension19() throws InterruptedException {

        log.info("DateAddFunctionExtensionProcessedCalenderTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = ""
                + "define stream inputStream (symbol string,dateValue string,dateFormat string,timestampInMilliseconds "
                + "long,expr int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded,"
                + "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded,"
                + "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills, "
                + "time:dateAdd(dateValue,expr,'SECOND',dateFormat) as secondAdded,"
                + "time:dateAdd(dateValue,expr,'MINUTE',dateFormat) as minuteAdded " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {

            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                for (Event event : inEvents) {
                    count.incrementAndGet();
                    if (count.get() == 1) {
                        AssertJUnit.assertEquals("2016-11-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2015-01-11 13:23:44", event.getData(2));
                        AssertJUnit.assertEquals("1415699624000", event.getData(3));
                        eventArrived = true;
                    }
                    if (count.get() == 2) {
                        AssertJUnit.assertEquals("2012-05-11 13:23:44", event.getData(1));
                        AssertJUnit.assertEquals("2010-07-11 13:23:44", event.getData(2));
                        AssertJUnit.assertEquals("1415699624000", event.getData(3));
                        eventArrived = true;
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        inputHandler.send(new Object[] { "IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2 });
        SiddhiTestHelper.waitForEvents(100, 2, count, 60000);
        AssertJUnit.assertEquals(2, count.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }
}

