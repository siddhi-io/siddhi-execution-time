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
import io.siddhi.extension.execution.time.util.TimeExtensionConstants;
import io.siddhi.query.api.definition.Attribute;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

/**
 * currentTime()
 * Returns System Time in HH:mm:ss format.
 * Return Type(s): STRING
 */

/**
 * Class representing the Time currentTime implementation.
 */
@Extension(
        name = "currentTime",
        namespace = "time",
        description = "Function returns system time in the `HH:mm:ss` format.",
        returnAttributes = @ReturnAttribute(
                description = "Returns system time in the `HH:mm:ss` format.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "time:currentTime()",
                        description = "Returns the current date in the `HH:mm:ss` format, such as `15:23:24`."
                )
        }
)
public class CurrentTimeFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private FastDateFormat dateFormat = null;

    @Override
    protected StateFactory init(ExpressionExecutor[] expressionExecutors,
                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        dateFormat = FastDateFormat.getInstance(TimeExtensionConstants.EXTENSION_TIME_CURRENT_TIME_FORMAT);
        return null;

    }

    @Override
    protected Object execute(Object[] data, State extensionState) {
        return null;
    }

    @Override
    protected Object execute(Object data, State extensionState) {
        Date now = new Date();
        return dateFormat.format(now);
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

}
