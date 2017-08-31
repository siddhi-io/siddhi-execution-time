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

import org.apache.commons.lang3.time.FastDateFormat;
import org.wso2.extension.siddhi.execution.time.util.TimeExtensionConstants;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.ReturnAttribute;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.util.Date;
import java.util.Map;

/**
 * currentTimestamp()
 * Returns System Time in yyyy-MM-dd HH:mm:ss format.
 * Return Type(s): STRING
 */

/**
 * Class representing the Time currentTimestamp implementation.
 */
@Extension(
        name = "currentTimestamp",
        namespace = "time",
        description = "This methods returns system time in yyyy-MM-dd HH:mm:ss format.",
        returnAttributes = @ReturnAttribute(
                description = "Returned type will be string.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "define stream inputStream (symbol string, price long, volume long);\n" +
                                 "from inputStream select symbol , time:currentTimestamp() as currentTimestamp\n" +
                                 "insert into outputStream;",
                        description = "This query returns symbol from inputStream and"
                                + "current time stamp of the system in yyyy-MM-dd HH:mm:ss format"
                                + " as currentTimestamp to the outputStream"
                )
        }
)
public class CurrentTimestampFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private FastDateFormat dateFormat = null;

    @Override
    protected void init(ExpressionExecutor[] expressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {
        dateFormat = FastDateFormat.getInstance(TimeExtensionConstants.EXTENSION_TIME_CURRENT_TIMESTAMP_FORMAT);
    }

    @Override
    protected Object execute(Object[] data) {
        return null; //Since the e function takes in no parameters, this method does not get called. Hence,
        // not implemented.
    }

    @Override
    protected Object execute(Object data) {
        Date now = new Date();
        return dateFormat.format(now);

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

    @Override
    public Map<String, Object> currentState() { //No need to maintain a state.
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> state) {
        //Since there's no need to maintain a state, nothing needs to be done here.
    }
}
