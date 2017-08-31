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
import org.wso2.siddhi.core.query.output.callback.QueryCallback;
import org.wso2.siddhi.core.stream.input.InputHandler;
import org.wso2.siddhi.core.util.EventPrinter;
import util.SiddhiTestHelper;

import java.util.concurrent.atomic.AtomicInteger;



public class DateAddFunctionExtensionTestCase {

    private static final Logger log = Logger.getLogger(DateAddFunctionExtensionTestCase.class);
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

        String inStreamDefinition = "" +
                "define stream inputStream (symbol string,dateValue string,dateFormat string," +
                                    "timestampInMilliseconds long,expr int);";
        String query = ("@info(name = 'query1') " +
                "from inputStream " +
                "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearAdded," +
                "time:dateAdd(dateValue,expr,'MONTH',dateFormat) as monthAdded," +
                "time:dateAdd(timestampInMilliseconds,expr,'HOUR') as yearAddedMills " +
                "insert into outputStream;");
        SiddhiAppRuntime executionPlanRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);


        executionPlanRuntime.addCallback("query1", new QueryCallback() {

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

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
        executionPlanRuntime.start();
        inputHandler.send(new Object[]{"IBM", "2014-11-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2});
        inputHandler.send(new Object[]{"IBM", "2010-05-11 13:23:44", "yyyy-MM-dd HH:mm:ss", 1415692424000L, 2});
        SiddhiTestHelper.waitForEvents(100, 2, count, 60000);
        AssertJUnit.assertEquals(2, count.get());
        AssertJUnit.assertTrue(eventArrived);
        executionPlanRuntime.shutdown();
    }
}
