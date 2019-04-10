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

import io.siddhi.annotation.Example;
import io.siddhi.annotation.Extension;
import io.siddhi.annotation.ReturnAttribute;
import io.siddhi.annotation.util.DataType;
import io.siddhi.core.config.SiddhiQueryContext;
import io.siddhi.core.executor.ExpressionExecutor;
import io.siddhi.core.executor.function.FunctionExecutor;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.query.api.definition.Attribute;
import org.apache.commons.lang3.time.FastDateFormat;
import org.wso2.extension.siddhi.execution.time.util.TimeExtensionConstants;

import java.util.Date;
import java.util.Map;

/**
 * currentDate()
 * Returns System Date in yyyy-MM-dd format.
 * Return Type(s): STRING
 */

/**
 * Class representing the Time currentDate implementation.
 */
@Extension(
        name = "currentDate",
        namespace = "time",
        description = "This function returns the system time in 'yyyy-MM-dd' format.",
        returnAttributes = @ReturnAttribute(
                description = "The value returned is of 'string' type.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "define stream InputStream (symbol string, price long, volume long);\n" +
                                 "from InputStream select symbol , time:currentDate() as currentTime \n" +
                                 "insert into OutputStream;",
                        description = "This query returns 'symbol' from the 'InputStream' and the " +
                                "current date and time, to the 'OutputStream'.It returns the " +
                                "current date in the 'yyyy-MM-dd' format."
                )
        }
)
public class CurrentDateFunctionExtension extends FunctionExecutor<CurrentDateFunctionExtension.ExtensionState> {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private FastDateFormat dateFormat = null;

    @Override
    protected StateFactory<ExtensionState> init(ExpressionExecutor[] expressionExecutors,
                                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        dateFormat = FastDateFormat.getInstance(TimeExtensionConstants.EXTENSION_TIME_DATE_FORMAT);
        return () -> new ExtensionState();
    }

    @Override
    protected Object execute(Object[] data) {
        return null; //Since the e function takes in no parameters, this method does not get called. Hence,
        // not implemented.
    }

    @Override
    protected Object execute(Object data) {
        return null;
    }

    @Override
    protected Object execute(Object[] data, ExtensionState extensionState) {
        return null;
    }

    @Override
    protected Object execute(Object data, ExtensionState extensionState) {
        Date now = new Date();
        return dateFormat.format(now);
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

    static class ExtensionState extends State {

        @Override
        public boolean canDestroy() {
            return false;
        }

        @Override
        public Map<String, Object> snapshot() {
            return null;
        }

        @Override
        public void restore(Map<String, Object> map) {
            // No state
        }
    }
}
