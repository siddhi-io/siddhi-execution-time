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
import io.siddhi.annotation.ParameterOverload;
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
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.TimeZone;

/**
 * utcTimestamp()
 * Returns System time in yyyy-MM-dd HH:mm:ss format.
 * Return Type(s): STRING
 */

/**
 * Class representing the Time extract implementation.
 */
@Extension(
        name = "utcTimestamp",
        namespace = "time",
        description = "Function returns the system current time in UTC timezone with given date format which " +
                "defaults to `yyyy-MM-dd HH:mm:ss` if not provided.",
        parameters = {
                @Parameter(name = "date.format",
                        description = "The format of the date value provided. " +
                                "For example, `yyyy/MM/dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss`")
        },
        parameterOverloads = {
                @ParameterOverload(),
                @ParameterOverload(parameterNames = {"date.format"})
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns the system current time in UTC timezone with given date format which " +
                        "defaults to `yyyy-MM-dd HH:mm:ss` format if not provided explicitly",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax =  "time:utcTimestamp()",
                        description = "Returns the system current time in UTC timezone with `yyyy-MM-dd HH:mm:ss` " +
                                "format, and a sample output will be like `2019-07-03 09:58:34`."
                ),
                @Example(
                        syntax = "time:utcTimestamp('yyyy-MM-DD HH:MM:ss.SSS')",
                        description = "Returns the system current time in UTC timezone with `yyyy-MM-dd HH:mm:ss.SSS` "
                                + "format, and a sample output will be like `2020-07-15 10:49:41.103`."
                )
        }
)
public class UTCTimestampFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private FastDateFormat fastDateFormat = null;
    private String dateFormat = null;

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors.length > 0) {
            if (attributeExpressionExecutors.length == 1) {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "first argument of " + "time:utcTimestamp(dateFormat) function, " + "required " +
                            Attribute.Type.STRING + " but found " + attributeExpressionExecutors[1]
                                    .getReturnType().toString());
                }
            } else {
                throw new SiddhiAppValidationException("Invalid no of arguments passed to " +
                        "time:utcTimestamp() function, required none or 1, but found " +
                        attributeExpressionExecutors.length);
            }
        } else {
            dateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
            fastDateFormat = FastDateFormat.getInstance(TimeExtensionConstants.EXTENSION_TIME_UTC_TIMESTAMP_FORMAT,
                    TimeZone.getTimeZone(TimeExtensionConstants.EXTENSION_TIME_TIME_ZONE));
        }
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {
        return null;
    }

    @Override
    protected Object execute(Object data, State state) {
        Date now = new Date();
        if (data != null) {
            dateFormat = (String) data;
            fastDateFormat = FastDateFormat.getInstance(dateFormat,
                    TimeZone.getTimeZone(TimeExtensionConstants.EXTENSION_TIME_TIME_ZONE));
        }
        return fastDateFormat.format(now);
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

}
