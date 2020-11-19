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

/**
 * timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone)
 * Returns the converted date string of same format as source.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * dateSourceFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * targetTimezone - Time zone which the date value need to be converted to
 * sourceTimezone - Time zone which source date belongs to

 * Accept Type(s) for timezoneConvert(dateValue,dateSourceFormat,sourceTimezone,targetTimezone)
 * dateValue : STRING
 * dateSourceFormat : STRING
 * sourceTimezone : STRING
 * targetTimezone : STRING
 *
 */

import io.siddhi.annotation.Example;
import io.siddhi.annotation.Extension;
import io.siddhi.annotation.Parameter;
import io.siddhi.annotation.ParameterOverload;
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
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class representing the Timezone conversion implementation.
 */
@Extension(
        name = "timezoneConvert",
        namespace = "time",
        description = "Converts source date to provided target timezone and return the date string",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, `13:23:44.657`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        defaultValue = "-"),
                @Parameter(name = "date.source.format",
                        description = "The format input date.value." +
                                "For example, `yyyy-MM-dd HH:mm:ss.SSS`. This is mandatory if you want to convert to " +
                                "different timezone",
                        type = {DataType.STRING},
                        dynamic = true,
                        defaultValue = "-"),
                @Parameter(name = "target.timezone",
                        description = "The timezone to which the target date need to be converted. " +
                                "For example, `Asia/Kolkata`, `PST`. " +
                                "Get the supported timezone IDs from [here]" +
                                "(https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)",
                        type = {DataType.STRING},
                        dynamic = true,
                        defaultValue = "-"),
                @Parameter(name = "source.timezone",
                        description = "The timezone to which the source time points to. " +
                                "For example, `Asia/Kolkata`, `PST`. " +
                                "Get the supported timezone IDs from [here]" +
                                "(https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "System timezone")

        },
        parameterOverloads = {
                @ParameterOverload(parameterNames = {"date.value", "date.source.format", "target.timezone"}),
                @ParameterOverload(parameterNames = {"date.value", "date.source.format", "target.timezone",
                        "source.timezone"})
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns the converted date based on the target timezone in same source date format.",
                type = {DataType.STRING}),
        examples = {

                @Example(
                        syntax = "time:timezoneConvert('2014/11/11 13:23:44','yyyy/MM/dd HH:mm:ss','UTC', 'IST') ",
                        description = "Converts date based on the target timezone `UTC` considering given source " +
                                "timizone `IST` and returns `2014/11/11 07:53:44`."
                ),

                @Example(
                        syntax = "time:timezoneConvert('2020-11-11 6:23:44', 'yyyy/MM/dd HH:mm:ss', 'CST') ",
                        description = "Converts date based on the target timezone `CST` and since source timezone " +
                                "is not given it will take system timezone as default and " +
                                "returns `2020/11/10 18:53:44`."
                )
        }
)
public class TimezoneConvertFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private static final Logger log = Logger.getLogger(TimezoneConvertFunctionExtension.class);

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {

        if (attributeExpressionExecutors.length == 4) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[2].getReturnType().toString());
            }
            if (attributeExpressionExecutors[3].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the fourth argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[3].getReturnType().toString());
            }
        } else if (attributeExpressionExecutors.length == 3) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:timezoneConvert(dateValue,dateSourceFormat,targetTimezone,sourceTimezone) function, " +
                        "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[2].getReturnType().toString());
            }
        } else {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to timezoneConvert() function, " +
                    "required 3 or 4, but found " + attributeExpressionExecutors.length);
        }
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {
        String sourceDate = null;
        ZoneId sourceZoneId;

        if (data.length == 3) {
            if (data[0] == null || data[1] == null || data[2] == null) {
                return null;
            }
            sourceZoneId = ZoneId.systemDefault();
        } else if (data.length == 4) {
            if (data[0] == null || data[1] == null || data[2] == null || data[3] == null) {
                return null;
            }
            sourceZoneId = ZoneId.of((String) data[3], ZoneId.SHORT_IDS);
        } else {
            throw new SiddhiAppRuntimeException("Invalid set of arguments given to time:timezoneConvert() function." +
                    "Arguments should be either 3 or 4. ");
        }

        ZoneId targetZoneId;
        ZonedDateTime srcZonedDateTime;
        DateTimeFormatter sourceFormat;

        try {
            sourceDate = (String) data[0];
            targetZoneId = ZoneId.of((String) data[2], ZoneId.SHORT_IDS);
            sourceFormat = DateTimeFormatter.ofPattern((String) data[1]);
            LocalDateTime sourceDateTime = LocalDateTime.parse(sourceDate, sourceFormat);
            srcZonedDateTime = sourceDateTime.atZone(sourceZoneId);
        } catch (Exception e) {
            String errorMsg = "";
            if (e instanceof DateTimeParseException) {
                errorMsg = "Provided date value : " + sourceDate + " cannot be parsed by given pattern " + data[1] +
                        ".";
            }
            throw new SiddhiAppRuntimeException(errorMsg + e.getMessage(), e);
        }
        return srcZonedDateTime.withZoneSameInstant(targetZoneId).format(sourceFormat);
    }

    @Override
    protected Object execute(Object data, State state) {
        return null;
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }
}
