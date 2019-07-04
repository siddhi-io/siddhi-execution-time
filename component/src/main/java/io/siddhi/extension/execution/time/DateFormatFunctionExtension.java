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
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * dateFormat(dateValue,dateTargetFormat,dateSourceFormat)/dateFormat(dateValue,dateTargetFormat)/
 * dateFormat(timestampInMilliseconds,dateTargetFormat)
 * Returns a formatted date string.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * dateTargetFormat - Date format which need to be converted to. eg: yyyy/MM/dd HH:mm:ss
 * dateSourceFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * timestampInMilliseconds - date value in milliseconds.(from the epoch) eg: 1415712224000L
 * dateTargetFormat - Date format which need to be converted to. eg: yyyy/MM/dd HH:mm:ss
 * Accept Type(s) for dateFormat(dateValue,dateTargetFormat,dateSourceFormat):
 * dateValue : STRING
 * dateTargetFormat : STRING
 * dateSourceFormat : STRING
 * Accept Type(s) for dateFormat(timestampInMilliseconds,dateTargetFormat):
 * timestampInMilliseconds : LONG
 * dateTargetFormat : STRING
 * Return Type(s): STRING
 */

/**
 * Class representing the Time dateFormat implementation.
 */
@Extension(
        name = "dateFormat",
        namespace = "time",
        description = "Formats the data in string or milliseconds format to the given date format.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, `13:23:44.657`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-"),
                @Parameter(name = "date.target.format",
                        description = "The format of the date into which the date value needs to be converted. " +
                                "For example, `yyyy/MM/dd HH:mm:ss`.",
                        dynamic = true,
                        type = {DataType.STRING}),
                @Parameter(name = "date.source.format",
                        description = "The format input date.value." +
                                "For example, `yyyy-MM-dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
                @Parameter(name = "timestamp.in.milliseconds",
                        description = "The date value in milliseconds from the epoch. For example, `1415712224000L`.",
                        type = {DataType.LONG},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-")
        },
        parameterOverloads = {
                @ParameterOverload(parameterNames = {"date.value", "date.target.format", "date.source.format"}),
                @ParameterOverload(parameterNames = {"date.value", "date.target.format"}),
                @ParameterOverload(parameterNames = {"timestamp.in.milliseconds", "date.target.format"})
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns the formatted date based on the date.target.format property.",
                type = {DataType.STRING}),
        examples = {

                @Example(
                        syntax = "time:dateFormat('2014/11/11 13:23:44', 'mm:ss', 'yyyy/MM/dd HH:mm:ss') ",
                        description = "Converts date based on the target date format `mm:ss` and returns `23:44`."
                ),

                @Example(
                        syntax = "time:dateFormat('2014-11-11 13:23:44', 'HH:mm:ss') ",
                        description = "Converts date based on the target date format `HH:mm:ss` and returns `13:23:44`."
                ),
                @Example(
                        syntax = "time:dateFormat(1415692424000L, 'yyyy-MM-dd') ",
                        description = "Converts date in millisecond based on the target date format `yyyy-MM-dd` " +
                                "and returns `2014-11-11`."
                )
        }
)
public class DateFormatFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private static final Logger log = Logger.getLogger(DateFormatFunctionExtension.class);
    private boolean useDefaultDateFormat = false;
    private String sourceDateFormat = null;
    private Calendar calInstance = Calendar.getInstance();

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG && attributeExpressionExecutors
                .length == 2) {
            useDefaultDateFormat = true;
            sourceDateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        }

        if (attributeExpressionExecutors.length == 3) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                        "time:dateFormat(dateValue,dateTargetFormat,dateSourceFormat) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:dateFormat(dateValue,dateTargetFormat,dateSourceFormat) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:dateFormat(dateValue,dateTargetFormat,dateSourceFormat) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[2].getReturnType().toString());
            }
        } else if (attributeExpressionExecutors.length == 2) {
            if (useDefaultDateFormat) {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                            "for the first argument of " +
                            "time:dateFormat(dateValue,dateTargetFormat,dateSourceFormat) function, " + "required "
                            + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[0].getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                            "for the second argument of " +
                            "time:dateFormat(dateValue,dateTargetFormat,dateSourceFormat) function, " + "required "
                            + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[1].getReturnType().toString());
                }
            } else {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG) {
                    throw new SiddhiAppValidationException("Invalid parameter type " +
                            "found for the first argument of " +
                            "time:dateFormat(timestampInMilliseconds,dateTargetFormat) function, " +
                            "" + "required " + Attribute.Type.LONG +
                            " but found " + attributeExpressionExecutors[0].getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                            "for the second argument of " +
                            "time:dateFormat(timestampInMilliseconds,dateTargetFormat) function, " +
                            "" + "required " + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[1].getReturnType().toString());
                }
            }
        } else {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to dateFormat() function, " +
                    "required 2 or 3, but found " + attributeExpressionExecutors.length);
        }
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {

        Date userSpecifiedSourceDate;


        if (data.length == 3 || useDefaultDateFormat) {

            String sourceDate = null;
            String targetDataFormat;
            FastDateFormat userSpecifiedSourceFormat;

            try {
                if (data[0] == null) {
                    throw new SiddhiAppRuntimeException("Invalid input given to time:dateFormat(dateValue," +
                            "dateTargetFormat,dateSourceFormat) function" + ". First " + "argument cannot be null");
                }
                if (data[1] == null) {
                    throw new SiddhiAppRuntimeException("Invalid input given to time:dateFormat(dateValue," +
                            "dateTargetFormat,dateSourceFormat) function" + ". Second " + "argument cannot be null");
                }
                if (!useDefaultDateFormat) {
                    if (data[2] == null) {
                        throw new SiddhiAppRuntimeException("Invalid input given to time:dateFormat(dateValue," +
                                "dateTargetFormat,dateSourceFormat) function" + ". Third " + "argument cannot be null");
                    }
                    sourceDateFormat = (String) data[2];
                }

                sourceDate = (String) data[0];
                targetDataFormat = (String) data[1];
                userSpecifiedSourceFormat = FastDateFormat.getInstance(sourceDateFormat);
                userSpecifiedSourceDate = userSpecifiedSourceFormat.parse(sourceDate);
                // Format the Date to specified Format
                FastDateFormat targetFormat = FastDateFormat.getInstance(targetDataFormat);
                return targetFormat.format(userSpecifiedSourceDate);
            } catch (ParseException e) {
                String errorMsg = "Provided format " + sourceDateFormat + " does not match with the timestamp " +
                        sourceDate + " " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        } else if (data.length == 2) {

            if (data[0] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to dateFormat(timestampInMilliseconds," +
                        "dateTargetFormat) function" + ". First " + "argument cannot be null");
            }
            if (data[1] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to dateFormat(timestampInMilliseconds," +
                        "dateTargetFormat) function" + ". Second " + "argument cannot be null");
            }

            // Format the Date to specified Format
            FastDateFormat targetFormat;
            long dateInMills;
            try {
                String targetDataFormat = (String) data[1];
                // Format the Date to specified Format
                targetFormat = FastDateFormat.getInstance(targetDataFormat);
                dateInMills = (Long) data[0];
                calInstance.setTimeInMillis(dateInMills);
                userSpecifiedSourceDate = calInstance.getTime();
                String formattedNewDateValue = targetFormat.format(userSpecifiedSourceDate);
                return formattedNewDateValue;
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }

        } else {
            throw new SiddhiAppRuntimeException("Invalid set of arguments given to time:dateFormat() function." +
                    "Arguments should be either 2 or 3. ");
        }
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
