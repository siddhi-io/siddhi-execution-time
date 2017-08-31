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
import org.wso2.siddhi.core.util.SiddhiTestHelper;

import java.util.concurrent.atomic.AtomicInteger;

public class CurrentTimestampFunctionExtensionTestCase {

    private static final Logger log = Logger.getLogger(CurrentTimestampFunctionExtensionTestCase.class);
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
    public void currentTimestampFunctionExtension() throws InterruptedException {

        log.info("CurrentTimestampFunctionExtensionTestCase");
        SiddhiManager siddhiManager = new SiddhiManager();

        String inStreamDefinition = "" +
                "define stream inputStream (symbol string, price long, volume long);";
        String query = ("@info(name = 'query1') " +
                "from inputStream select symbol , time:currentTimestamp() as currentTimestamp " +
                "insert into outputStream;");
        SiddhiAppRuntime executionPlanRuntime = siddhiManager
                .createSiddhiAppRuntime(inStreamDefinition + query);

        executionPlanRuntime.addCallback("query1", new QueryCallback() {
            @Override
            public void receive(long timeStamp, Event[] inEvents, Event[] removeEvents) {
                EventPrinter.print(timeStamp, inEvents, removeEvents);

                eventArrived = true;
                for (Event inEvent : inEvents) {
                    eventCount.incrementAndGet();
                    log.info("Event : " + eventCount.get() + ",currentTimestamp : " + inEvent.getData(1));

                }
            }
        });

        InputHandler inputHandler = executionPlanRuntime.getInputHandler("inputStream");
        executionPlanRuntime.start();
        inputHandler.send(new Object[]{"IBM", 700f, 100L});
        inputHandler.send(new Object[]{"WSO2", 60.5f, 200L});
        inputHandler.send(new Object[]{"XYZ", 60.5f, 200L});

        SiddhiTestHelper.waitForEvents(waitTime, 3, eventCount, timeout);
        AssertJUnit.assertEquals(3, eventCount.get());
        AssertJUnit.assertTrue(eventArrived);
        executionPlanRuntime.shutdown();
    }
}
