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
import java.util.concurrent.TimeUnit;

/**
 * dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2)/dateDiff(dateValue1,dateValue2)/
 * dateDiff(timestampInMilliseconds1,timestampInMilliseconds2)
 * Returns time(days) between two dates.
 * dateValue1 - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * dateValue2 - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * dateFormat1 - Date format of the provided dateValue1. eg: yyyy-MM-dd HH:mm:ss.SSS
 * dateFormat2 - Date format of the provided dateValue2. eg: yyyy-MM-dd HH:mm:ss.SSS
 * timestampInMilliseconds1 - date value in milliseconds.(from the epoch) eg: 1415712224000L
 * timestampInMilliseconds2 - date value in milliseconds.(from the epoch) eg: 1423456224000L
 * Accept Type(s) for dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2):
 *         dateValue1 : STRING
 *         dateValue2 : STRING
 *         dateFormat1 : STRING
 *         dateFormat2 : STRING
 * Accept Type(s) for dateDiff(timestampInMilliseconds1,timestampInMilliseconds2):
 *         timestampInMilliseconds1 : LONG
 *         timestampInMilliseconds2 : LONG
 * Return Type(s): INT
 */

/**
 * Class representing the Time dateDiff implementation.
 */
@Extension(
        name = "dateDiff",
        namespace = "time",
        description = "Returns difference between two dates in days.",
        parameters = {
                @Parameter(name = "date.value1",
                        description =  "The value of the first date parameter. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, `13:23:44.657`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-"),
                @Parameter(name = "date.value2",
                        description =  "The value of the second date parameter. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11` , " +
                                "`13:23:44.657`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-"),
                @Parameter(name = "date.format1",
                        description = "The format of the first date value provided. " +
                                "For example, `yyyy-MM-dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
                @Parameter(name = "date.format2",
                        description = "The format of the second date value provided. " +
                                "For example, `yyyy-MM-dd HH:mm:ss.SSS`.",
                        dynamic = true,
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
                @Parameter(name = "timestamp.in.milliseconds1",
                        description = "The first date value in milliseconds from the epoch." +
                                " For example, `1415712224000L`.",
                        type = {DataType.LONG},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-"),
                @Parameter(name = "timestamp.in.milliseconds2",
                        description = "The second date value in milliseconds from the epoch." +
                                " For example, `1415712224000L`.",
                        type = {DataType.LONG},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-")
        },
        parameterOverloads = {
                @ParameterOverload(parameterNames = {"date.value1", "date.value2", "date.format1", "date.format2"}),
                @ParameterOverload(parameterNames = {"date.value1", "date.value2"}),
                @ParameterOverload(parameterNames = {"timestamp.in.milliseconds1", "timestamp.in.milliseconds2"})
        },
        returnAttributes = @ReturnAttribute(
                description = "This returns the difference of the two given dates.",
                type = {DataType.INT}),
        examples = {
                @Example(
                        syntax = "time:dateDiff('2014-11-11 13:23:44', '2014-11-9 13:23:44', " +
                                "'yyyy-MM-dd HH:mm:ss', 'yyyy-MM-dd HH:mm:ss')",
                        description = "Returns the date difference between the two given dates as `2`."
                ),
                @Example(
                        syntax = "time:dateDiff('2014-11-13 13:23:44', '2014-11-9 13:23:44')",
                        description = "Returns the date difference between the two given dates as `4`."
                ),
                @Example(
                        syntax = "time:dateDiff(1415692424000L, 1412841224000L)",
                        description = "Returns the date difference between the two given dates as `33`."
                )
        }
)
public class DateDifferenceFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.LONG;
    private static final Logger log = Logger.getLogger(DateDifferenceFunctionExtension.class);
    private boolean useDefaultDateFormat = false;
    private String firstDateFormat = null;
    private String secondDateFormat = null;
    private Calendar firstCalInstance = Calendar.getInstance();
    private Calendar secondCalInstance = Calendar.getInstance();

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG && attributeExpressionExecutors
                .length == 3 || attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG &&
                attributeExpressionExecutors.length == 2) {
            useDefaultDateFormat = true;
            firstDateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
            secondDateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        }
        if (attributeExpressionExecutors.length == 4) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) " +
                        "function, " + "required " + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[0]
                        .getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[2].getReturnType().toString());
            }
            if (attributeExpressionExecutors[3].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the fourth argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[3].getReturnType().toString());
            }
        } else if (attributeExpressionExecutors.length == 2) {
            if (useDefaultDateFormat) {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for " +
                            "the first argument of " +
                            "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                            + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[0].getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for " +
                            "the second argument of " +
                            "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                            + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[1].getReturnType().toString());
                }
            } else {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for " +
                            "the first argument of " +
                            "time:dateDiff(timestampInMilliseconds1,timestampInMilliseconds2) function, " +
                            "" + "required " + Attribute.Type.LONG +
                            " but found " + attributeExpressionExecutors[0].getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.LONG) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for " +
                            "the second argument of " +
                            "time:dateDiff(timestampInMilliseconds1,timestampInMilliseconds2) function, " +
                            "" + "required " + Attribute.Type.LONG +
                            " but found " + attributeExpressionExecutors[1].getReturnType().toString());
                }
            }

        } else if (attributeExpressionExecutors.length == 3 && useDefaultDateFormat) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for " +
                        "the first argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:dateDiff(dateValue1,dateValue2,dateFormat1,dateFormat2) function, " + "required "
                        + Attribute.Type.STRING +
                        " but found " + attributeExpressionExecutors[2].getReturnType().toString());
            }
        } else {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:dateDiff() function, " +
                    "required 2 or 4, but found " + attributeExpressionExecutors.length);
        }
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {

        String firstDate = null;
        String secondDate;
        FastDateFormat userSpecifiedFirstFormat;
        FastDateFormat userSpecifiedSecondFormat;

        if (data.length == 4 || useDefaultDateFormat) {
            try {
                if (data[0] == null || data[1] == null) {
                    return null;
                }

                if (!useDefaultDateFormat) {
                    if (data[2] == null || data[3] == null) {
                        return null;
                    }
                    firstDateFormat = (String) data[2];
                    secondDateFormat = (String) data[3];
                } else {
                    if (data.length != 2) {
                        firstDateFormat = (String) data[2];
                    }
                }
                firstDate = (String) data[0];
                secondDate = (String) data[1];
                userSpecifiedFirstFormat = FastDateFormat.getInstance(firstDateFormat);
                userSpecifiedSecondFormat = FastDateFormat.getInstance(secondDateFormat);
                Date userSpecifiedFirstDate = userSpecifiedFirstFormat.parse(firstDate);
                firstCalInstance.setTime(userSpecifiedFirstDate);
            } catch (ParseException e) {
                String errorMsg =
                        "Provided format " + firstDateFormat + " does not match with the timestamp " + firstDate + e
                                .getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }

            try {
                Date userSpecifiedSecondDate = userSpecifiedSecondFormat.parse(secondDate);
                secondCalInstance.setTime(userSpecifiedSecondDate);
            } catch (ParseException e) {
                String errorMsg =
                        "Provided format " + secondDateFormat + " does not match with the timestamp " + secondDate + e
                                .getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }

        } else if (data.length == 2) {

            if (data[0] == null || data[1] == null) {
                return null;
            }

            try {
                long firstDateInMills = (Long) data[0];
                long secondDateInMills = (Long) data[1];
                firstCalInstance.setTimeInMillis(firstDateInMills);
                secondCalInstance.setTimeInMillis(secondDateInMills);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        } else {
            throw new SiddhiAppRuntimeException("Invalid set of arguments given to time:dateDiff() function." +
                    "Arguments should be either 2 or 4. ");
        }

        long dateDifference = firstCalInstance.getTimeInMillis() - secondCalInstance.getTimeInMillis();
        return TimeUnit.DAYS.convert(dateDifference, TimeUnit.MILLISECONDS);
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
