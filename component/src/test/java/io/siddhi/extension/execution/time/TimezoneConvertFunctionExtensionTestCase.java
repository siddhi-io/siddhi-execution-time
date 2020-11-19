/*
 *
 *  * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *  *
 *  * WSO2 Inc. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied. See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package io.siddhi.extension.execution.time;

import io.siddhi.core.SiddhiAppRuntime;
import io.siddhi.core.SiddhiManager;
import io.siddhi.core.event.Event;
import io.siddhi.core.query.output.callback.QueryCallback;
import io.siddhi.core.stream.input.InputHandler;
import io.siddhi.core.util.EventPrinter;
import org.apache.log4j.Logger;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class TimezoneConvertFunctionExtensionTestCase {

    private static Logger log = Logger.getLogger(TimezoneConvertFunctionExtensionTestCase.class);
    private volatile boolean eventArrived;
    private AtomicInteger eventCount;

    @BeforeMethod
    public void init() {
        eventArrived = false;
        eventCount = new AtomicInteger(0);
    }

    @Test
    public void timezoneConvertFunctionWithAllArgumentsTestCase() throws InterruptedException {

        log.info("timezoneConvertFunctionWithAllArgumentsTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,targetTimezone string,sourceTimezone string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:timezoneConvert(dateValue,sourceFormat,targetTimezone,sourceTimezone) as convertedDate, " +
                "dateValue insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals("2014-11-10 23:53:44", inEvent.getData(1));
                        AssertJUnit.assertEquals("2014-11-11 13:23:44", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals("2016-04-20T14:30:00-0400", inEvent.getData(1));
                        AssertJUnit.assertEquals("2016-04-21T00:00:00+0530", inEvent.getData(2));
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] {
                "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", "PST", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2016-04-21T00:00:00+0530", "yyyy-MM-dd'T'HH:mm:ssZ", "America/New_York", "IST"});
        Thread.sleep(100);
        AssertJUnit.assertEquals(2, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timezoneConvertFunctionWithDifferentTimezonesArgumentsTestCase() throws InterruptedException {

        log.info("timezoneConvertFunctionWithDifferentTimezonesArgumentsTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,targetTimezone string,sourceTimezone string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:timezoneConvert(dateValue,sourceFormat,targetTimezone,sourceTimezone) as convertedDate, " +
                "dateValue insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals("2020-11-17 16:51:19", inEvent.getData(1));
                        AssertJUnit.assertEquals("2020-11-18 06:21:19", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals("2016-04-20T14:30:00-0400", inEvent.getData(1));
                        AssertJUnit.assertEquals("2016-04-21T00:00:00+0530", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 3) {
                        AssertJUnit.assertEquals("2020-11-18 04:54:19", inEvent.getData(1));
                        AssertJUnit.assertEquals("2020-11-18 06:54:19", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 4) {
                        AssertJUnit.assertEquals("2020-11-18 14:26", inEvent.getData(1));
                        AssertJUnit.assertEquals("2020-11-18 06:56", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 5) {
                        AssertJUnit.assertEquals("2020-11-18 01:26", inEvent.getData(1));
                        AssertJUnit.assertEquals("2020-11-18 06:56", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 6) {
                        AssertJUnit.assertEquals("2014/11/11 07:53:44", inEvent.getData(1));
                        AssertJUnit.assertEquals("2014/11/11 13:23:44", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 7) {
                        AssertJUnit.assertEquals("2019-08-07 14:19:10", inEvent.getData(1));
                        AssertJUnit.assertEquals("2019-08-07 07:19:10", inEvent.getData(2));
                    }
                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] {
                "IBM", "2020-11-18 06:21:19", "yyyy-MM-dd HH:mm:ss", "PST", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2016-04-21T00:00:00+0530", "yyyy-MM-dd'T'HH:mm:ssZ", "America/New_York", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2020-11-18 06:54:19", "yyyy-MM-dd HH:mm:ss", "PST", "CST"});
        inputHandler.send(new Object[] {
                "IBM", "2020-11-18 06:56", "yyyy-MM-dd HH:mm", "NST", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2020-11-18 06:56", "yyyy-MM-dd HH:mm", "UTC", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2014/11/11 13:23:44", "yyyy/MM/dd HH:mm:ss", "UTC", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2019-08-07 07:19:10", "yyyy-MM-dd HH:mm:ss", "UTC", "PST"});
        Thread.sleep(100);
        AssertJUnit.assertEquals(7, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }

    @Test
    public void timezoneConvertFunctionWithMandatoryArgumentsTestCase() throws InterruptedException {

        //Note reverting to send source timezone too as expected value will vary according to where the code is built

        log.info("timezoneConvertFunctionWithMandatoryArgumentsTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "define stream inputStream (symbol string,"
                + "dateValue string,sourceFormat string,targetTimezone string,sourceTimezone string);";
        String query = ("@info(name = 'query1') from inputStream select symbol , "
                + "time:timezoneConvert(dateValue,sourceFormat,targetTimezone) as convertedDate,dateValue "
                + "insert into outputStream;");
        SiddhiAppRuntime siddhiAppRuntime = siddhiManager.createSiddhiAppRuntime(inStreamDefinition + query);

        siddhiAppRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);
                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    if (eventCount.intValue() == 1) {
                        AssertJUnit.assertEquals("2014-11-10 23:53:44", inEvent.getData(1));
                        AssertJUnit.assertEquals("2014-11-11 13:23:44", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 2) {
                        AssertJUnit.assertEquals("2016-04-20T14:30:00-0400", inEvent.getData(1));
                        AssertJUnit.assertEquals("2016-04-21T00:00:00+0530", inEvent.getData(2));
                    }
                    if (eventCount.intValue() == 3) {
                        AssertJUnit.assertEquals("2020/11/10 18:53:44", inEvent.getData(1));
                        AssertJUnit.assertEquals("2020/11/11 06:23:44", inEvent.getData(2));
                    }

                }
            }
        });

        InputHandler inputHandler = siddhiAppRuntime.getInputHandler("inputStream");
        siddhiAppRuntime.start();
        inputHandler.send(new Object[] {
                "IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", "PST", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2016-04-21T00:00:00+0530", "yyyy-MM-dd'T'HH:mm:ssZ", "America/New_York", "IST"});
        inputHandler.send(new Object[] {
                "IBM", "2020/11/11 06:23:44", "yyyy/MM/dd HH:mm:ss", "CST", "IST"});
        Thread.sleep(100);
        AssertJUnit.assertEquals(3, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        siddhiAppRuntime.shutdown();
    }


}
