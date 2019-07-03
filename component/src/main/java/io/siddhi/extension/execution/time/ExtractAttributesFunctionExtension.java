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
import io.siddhi.core.exception.OperationNotSupportedException;
import io.siddhi.core.exception.SiddhiAppRuntimeException;
import io.siddhi.core.executor.ConstantExpressionExecutor;
import io.siddhi.core.executor.ExpressionExecutor;
import io.siddhi.core.executor.function.FunctionExecutor;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.extension.execution.time.util.TimeExtensionConstants;
import io.siddhi.query.api.definition.Attribute;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * extract(unit,dateValue,dateFormat)/extract(unit,dateValue)/extract(timestampInMilliseconds,unit)
 * Returns date attributes from a date expression.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * unit - Which part of the date format you want to manipulate. eg: "MINUTE" , "HOUR" , "MONTH" , "YEAR" , "QUARTER" ,
 * "WEEK" , "DAY" , "SECOND"
 * dateFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * timestampInMilliseconds - date value in milliseconds.(from the epoch) eg: 1415712224000L
 * Accept Type(s) for extract(unit,dateValue,dateFormat):
 * unit : STRING
 * dateValue : STRING
 * dateFormat : STRING
 * Accept Type(s) for extract(timestampInMilliseconds,unit):
 * timestampInMilliseconds : LONG
 * unit : STRING
 * Return Type(s): INT
 */

/**
 * Class representing the Time extract implementation.
 */
@Extension(
        name = "extract",
        namespace = "time",
        description = "Function extracts a date unit from the date.",
        parameters = {
                @Parameter(name = "unit",
                        description = "This is the part of the date that needs to be modified. " +
                                "For example, `MINUTE`, `HOUR`, `MONTH`, `YEAR`, `QUARTER`," +
                                " `WEEK`, `DAY`, `SECOND`.",
                        type = {DataType.STRING}),
                @Parameter(name = "date.value",
                        description = "The value of the date. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, " +
                                "`13:23:44.657`.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "-"),
                @Parameter(name = "date.format",
                        description = "The format of the date value provided. " +
                                "For example, `yyyy-MM-dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
                @Parameter(name = "timestamp.in.milliseconds",
                        description = "The date value in milliseconds. For example, `1415712224000L`.",
                        type = {DataType.LONG},
                        optional = true,
                        defaultValue = "-")
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns the extracted data unit value.",
                type = {DataType.INT}),
        examples = {
                @Example(
                        syntax = "time:extract('YEAR', '2019/11/11 13:23:44.657', 'yyyy/MM/dd HH:mm:ss.SSS')",
                        description = "Extracts the year amount and returns `2019`."
                ),
                @Example(
                        syntax = "time:extract('DAY', '2019-11-12 13:23:44.657')",
                        description = "Extracts the day amount and returns `12`."
                ),
                @Example(
                        syntax = "time:extract(1394556804000L, 'HOUR')",
                        description = "Extracts the hour amount and returns `22`."
                )
        }
)
public class ExtractAttributesFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.INT;
    private boolean useDefaultDateFormat = false;
    private String dateFormat = null;
    private Calendar cal = Calendar.getInstance();
    private String unit = null;

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG && attributeExpressionExecutors
                .length == 2) {
            useDefaultDateFormat = true;
            dateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        }
        if (attributeExpressionExecutors.length == 3) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                        "time:extract(unit, dateValue, dateFormat) function, " +
                        "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:extract(unit, dateValue, dateFormat) function, " +
                        "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:extract(unit, dateValue, dateFormat) function, " +
                        "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[2].getReturnType().toString());
            }

            if (attributeExpressionExecutors[0] instanceof ConstantExpressionExecutor) {
                unit = ((String) ((ConstantExpressionExecutor) attributeExpressionExecutors[0])
                        .getValue()).toUpperCase(Locale.getDefault());
            } else {
                throw new OperationNotSupportedException("unit value has to be a constant");
            }

        } else if (attributeExpressionExecutors.length == 2) {
            if (useDefaultDateFormat) {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the first " +
                            "argument of " + "time:extract(unit, dateValue)" +
                            " function," + "required " + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[0]
                            .getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the second " +
                            "argument of " + "time:extract(unit, dateValue) " +
                            "function," + "required " + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[1]
                            .getReturnType().toString());
                }
            } else {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the first" +
                            " argument of " + "time:extract" +
                            "(timestampInMilliseconds, unit) function, " + "required "
                            + Attribute.Type.LONG + " but found " +
                            attributeExpressionExecutors[0]
                                    .getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the second" +
                            " argument of " + "time:extract" +
                            "(timestampInMilliseconds, unit) function, " + "required "
                            + Attribute.Type.STRING +
                            " but found " + attributeExpressionExecutors[1]
                            .getReturnType().toString());
                }
            }

            if (attributeExpressionExecutors[1] instanceof ConstantExpressionExecutor) {
                unit = ((String) ((ConstantExpressionExecutor) attributeExpressionExecutors[1])
                        .getValue()).toUpperCase(Locale.getDefault());
            } else {
                throw new OperationNotSupportedException("unit value has to be a constant");
            }

        } else {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:extract() function, " +
                    "required 2 or 3, but found " +
                    attributeExpressionExecutors.length);
        }
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {
        String source = null;
        if (data.length == 3 || useDefaultDateFormat) {
            try {
                if (data[1] == null) {
                    throw new SiddhiAppRuntimeException("Invalid input given to time:extract(unit,dateValue," +
                            "dateFormat) function" + ". Second " +
                            "argument cannot be null");
                }
                if (!useDefaultDateFormat) {
                    if (data[2] == null) {
                        throw new SiddhiAppRuntimeException("Invalid input given to time:extract(unit,dateValue," +
                                "dateFormat) function" + ". Third " +
                                "argument cannot be null");
                    }
                    dateFormat = (String) data[2];
                }

                FastDateFormat userSpecificFormat;
                source = (String) data[1];
                userSpecificFormat = FastDateFormat.getInstance(dateFormat);
                Date userSpecifiedDate = userSpecificFormat.parse(source);
                cal.setTime(userSpecifiedDate);
            } catch (ParseException e) {
                String errorMsg = "Provided format " + dateFormat + " does not match with the timestamp " + source +
                        ". " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        } else {

            if (data[0] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to time:extract(timestampInMilliseconds," +
                        "unit) function" + ". First " + "argument cannot be null");
            }
            try {
                long millis = (Long) data[0];
                cal.setTimeInMillis(millis);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        }

        int returnValue = 0;

        if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_YEAR)) {
            returnValue = cal.get(Calendar.YEAR);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_MONTH)) {
            returnValue = (cal.get(Calendar.MONTH) + 1);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_SECOND)) {
            returnValue = cal.get(Calendar.SECOND);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_MINUTE)) {
            returnValue = cal.get(Calendar.MINUTE);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_HOUR)) {
            returnValue = cal.get(Calendar.HOUR_OF_DAY);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_DAY)) {
            returnValue = cal.get(Calendar.DAY_OF_MONTH);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_WEEK)) {
            returnValue = cal.get(Calendar.WEEK_OF_YEAR);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_QUARTER)) {
            returnValue = (cal.get(Calendar.MONTH) / 3) + 1;
        }
        return returnValue;
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
