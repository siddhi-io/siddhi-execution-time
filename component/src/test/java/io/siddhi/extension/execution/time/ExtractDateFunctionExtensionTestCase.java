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
import io.siddhi.core.stream.StreamJunction;
import io.siddhi.core.stream.input.InputHandler;
import io.siddhi.core.util.EventPrinter;
import io.siddhi.extension.execution.time.util.SiddhiTestHelper;
import io.siddhi.extension.execution.time.util.UnitTestAppender;
import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class ExtractDateFunctionExtensionTestCase {

    private static Logger log = Logger.getLogger(ExtractDateFunctionExtensionTestCase.class);
    private volatile boolean eventArrived;
    private int waitTime = 50;
    private int timeout = 30000;
    private AtomicInteger count;

    @BeforeMethod
    public void init() {
        eventArrived = false;
        count = new AtomicInteger(0);
    }

    @Test
    public void extractDateFunctionExtension() throws InterruptedException {

        log.info("ExtractDateFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateValue,dateFormat) as dateExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    count.incrementAndGet();
                    log.info("Event : " + count.get() + ",dateExtracted : " + inEvent.getData(1));

                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44.657", "yyyy-MM-dd HH:mm:ss.SSS" });
        inputHandler.send(new Object[] { "WSO2", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss" });
        inputHandler.send(new Object[] { "XYZ", "2014-11-11", "yyyy-MM-dd" });
        SiddhiTestHelper.waitForEvents(waitTime, 1, count, timeout);
        siddhiAppRuntime.shutdown();
        AssertJUnit.assertEquals(3, count.get());
        AssertJUnit.assertTrue(eventArrived);

    }

    @Test
    public void extractDateFunctionExtensionTest2() throws InterruptedException {
        log.info("ExtractDateFunctionExtensionWithInvalidFormatTestCase");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateValue,dateFormat) as dateExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014:11:11", "yyyy-MM-dd" });
        siddhiAppRuntime.shutdown();
        AssertJUnit.assertTrue(appender.getMessages().contains("Provided format yyyy-MM-dd does not match with the "
                                                                       + "timestamp 2014:11:11"));

    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractDateFunctionExtension3() throws InterruptedException {

        log.info("ExtractDateFunctionExtensionInvalidParameterTypeInFirstArgumentTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, dateValue int,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateValue,dateFormat) as dateExtracted " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractDateFunctionExtension4() throws InterruptedException {

        log.info("ExtractDateFunctionExtensionInvalidParameterTypeInSecondArgumentTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" + "define stream inputStream (symbol string, dateValue string,dateFormat int);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateValue,dateFormat) as dateExtracted " + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

    }

    @Test(expectedExceptions = SiddhiAppCreationException.class)
    public void extractDateFunctionExtension5() throws InterruptedException {

        log.info("ExtractDateFunctionExtensionInvalidNoOfArgumentTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateValue,dateFormat,dateValue) as dateExtracted "
                + "insert into outputStream;");
        siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

    }

    @Test
    public void extractDateFunctionExtension6() throws InterruptedException {

        log.info("ExtractDateFunctionExtensionTestCaseFirstArgumentNull");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateFormat,dateValue) as dateExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });
        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", "2014-11-11 13:23:44.657", null });
        siddhiAppRuntime.shutdown();
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:date(dateValue,dateFormat)"
                                                                       + " function. First argument cannot be null"));

    }

    @Test
    public void extractDateFunctionExtension7() throws InterruptedException {

        log.info("ExtractDateFunctionExtensionTestCaseSecondArgumentNull");
        UnitTestAppender appender = new UnitTestAppender();
        log = Logger.getLogger(StreamJunction.class);
        log.addAppender(appender);
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition =
                "" + "define stream inputStream (symbol string, dateValue string,dateFormat string);";
        String query = ("@info(name = 'query1') " + "from inputStream "
                + "select symbol,time:date(dateFormat,dateValue) as dateExtracted " + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] { "IBM", null, "yyyy-MM-dd HH:mm:ss" });
        siddhiAppRuntime.shutdown();
        AssertJUnit.assertTrue(appender.getMessages().contains("Invalid input given to time:date(dateValue,dateFormat)"
                                                                       + " function. Second argument cannot be null"));
    }
}
