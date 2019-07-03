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
import io.siddhi.annotation.Parameter;
import io.siddhi.annotation.ReturnAttribute;
import io.siddhi.annotation.util.DataType;
import io.siddhi.core.config.SiddhiQueryContext;
import io.siddhi.core.exception.SiddhiAppRuntimeException;
import io.siddhi.core.executor.ExpressionExecutor;
import io.siddhi.core.executor.function.FunctionExecutor;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.extension.execution.time.util.TimeExtensionConstants;
import io.siddhi.query.api.definition.Attribute;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.commons.lang3.time.FastDateFormat;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * currentTimestamp() or currentTimestamp(<time-zone>)
 * Returns System Time in yyyy-MM-dd HH:mm:ss format.
 * Return Type(s): STRING
 */

/**
 * Class representing the Time currentTimestamp implementation.
 */
@Extension(
        name = "currentTimestamp",
        namespace = "time",
        description = "When no argument is provided, function returns the system current timestamp in" +
                " `yyyy-MM-dd HH:mm:ss` format, and when a timezone is provided as an argument, " +
                "it converts and return the current system time to the given timezone format.",
        parameters = {
                @Parameter(name = "timezone",
                        description = "The timezone to which the current time need to be converted. " +
                                "For example, `Asia/Kolkata`, `PST`. " +
                                "Get the supported timezone IDs from [here]" +
                                "(https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "System timezone")
        },
        returnAttributes = @ReturnAttribute(
                description = "The value returned is of 'string' type.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "time:currentTimestamp()",
                        description = "Returns current system time in `yyyy-MM-dd HH:mm:ss` format,"
                                + " such as `2019-03-31 14:07:00`."
                ),
                @Example(
                        syntax = "time:currentTimestamp('Asia/Kolkata')",
                        description = "Returns current system time converted to 'Asia/Kolkata' timezone " +
                                "`yyyy-MM-dd HH:mm:ss` format, such as `2019-03-31 19:07:00`. " +
                                "Get the supported timezone IDs from [here]" +
                                "(https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)"
                ),
                @Example(
                        syntax = "time:currentTimestamp('CST')",
                        description = "Returns current system time converted to 'CST' timezone " +
                                "`yyyy-MM-dd HH:mm:ss` format, such as `2019-03-31 02:07:00`. " +
                                "Get the supported timezone IDs from [here]" +
                                "(https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)"
                )
        }
)
public class CurrentTimestampFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private FastDateFormat dateFormat = null;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(TimeExtensionConstants.EXTENSION_TIME_CURRENT_TIMESTAMP_FORMAT);

    @Override
    protected StateFactory init(ExpressionExecutor[] expressionExecutors,
                                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (expressionExecutors.length > 1) {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:currentTimestamp function," +
                    "required 0 or 1, but found " + expressionExecutors.length);
        }

        if (expressionExecutors.length == 1 && expressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
            throw new SiddhiAppValidationException("Invalid parameter type found for the argument of " +
                    "time:currentTimestamp function, " + "required " + Attribute.Type.STRING + " but found " +
                    attributeExpressionExecutors[0].getReturnType().toString());
        }
        dateFormat = FastDateFormat.getInstance(TimeExtensionConstants.EXTENSION_TIME_CURRENT_TIMESTAMP_FORMAT);
        return null;
    }

    @Override
    protected Object execute(Object[] data, State extensionState) {
        return null;
    }

    @Override
    protected Object execute(Object data, State extensionState) {
        if (data == null) {
            return dateFormat.format(new Date());
        } else {
            try {
                ZoneId zoneId = ZoneId.of((String) data, ZoneId.SHORT_IDS);
                ZonedDateTime convertedTime = ZonedDateTime.now().withZoneSameInstant(zoneId);
                return convertedTime.format(DATE_TIME_FORMATTER);
            } catch (DateTimeException e) {
                throw new SiddhiAppRuntimeException("Provided time zone " + data + " is invalid.", e);
            }
        }
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

}
