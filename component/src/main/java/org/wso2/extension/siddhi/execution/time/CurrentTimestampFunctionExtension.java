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
import io.siddhi.core.exception.SiddhiAppRuntimeException;
import io.siddhi.core.executor.ExpressionExecutor;
import io.siddhi.core.executor.function.FunctionExecutor;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.query.api.definition.Attribute;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.commons.lang3.time.FastDateFormat;
import org.wso2.extension.siddhi.execution.time.util.TimeExtensionConstants;

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
        description = "If no argument is provided, this function will return the currentSystemTime and if the " +
                "timezone is provided as an argument, it will convert the current systemtime to the given timezone " +
                "and return. This function returns time in 'yyyy-MM-dd HH:mm:ss' format.\n" +
                "To check the available timezone ids, visit " +
                "https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html",
        returnAttributes = @ReturnAttribute(
                description = "The value returned is of 'string' type.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "define stream InputStream (symbol string, price long, volume long);\n" +
                                "from InputStream " +
                                "select symbol , time:currentTimestamp() as currentTimestamp\n" +
                                "insert into OutputStream;",
                        description = "This query returns, symbol from the 'InputStream' and "
                                + "the current time stamp of the system in 'yyyy-MM-dd HH:mm:ss' format"
                                + " as 'currentTimestamp', to the 'OutputStream'."
                ),
                @Example(
                        syntax = "define stream InputStream (symbol string, price long, volume long);\n" +
                                "from InputStream " +
                                "select symbol , time:currentTimestamp(\"Asia/Kolkata\") as currentTimestamp\n" +
                                "insert into OutputStream;",
                        description = "This query returns, symbol from the 'InputStream' and "
                                + "the current time stamp of the system which is converted to Asia/Kolkata timezone, " +
                                "in 'yyyy-MM-dd HH:mm:ss' format as 'currentTimestamp', to the 'OutputStream'."
                ),
                @Example(
                        syntax = "define stream InputStream (symbol string, price long, volume long);\n" +
                                "from InputStream " +
                                "select symbol , time:currentTimestamp(\"CST\") as currentTimestamp\n" +
                                "insert into OutputStream;",
                        description = "This query returns, symbol from the 'InputStream' and "
                                + "the current time stamp of the system which is converted to CST timezone, in " +
                                "'yyyy-MM-dd HH:mm:ss' format as 'currentTimestamp', to the 'OutputStream'."
                )
        }
)
public class CurrentTimestampFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private FastDateFormat dateFormat = null;
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(TimeExtensionConstants.EXTENSION_TIME_CURRENT_TIMESTAMP_FORMAT);

    @Override
    protected StateFactory<State> init(ExpressionExecutor[] expressionExecutors,
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
