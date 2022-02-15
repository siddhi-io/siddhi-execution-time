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
 * Class representing the Time dateAdd implementation.
 */
@Extension(
        name = "dateAdd",
        namespace = "time",
        description = "Adds the specified time interval to a date.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, `13:23:44.657`.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "-",
                        dynamic = true),
                @Parameter(name = "expr",
                        description = "The amount by which the selected part of the date " +
                                "should be incremented. " +
                                "For example `2` ,`5 `,`10`, etc.",
                        type = {DataType.INT},
                        dynamic = true),
                @Parameter(name = "unit",
                        description = "This is the part of the date that needs to be modified. " +
                                "For example, `MINUTE`, `HOUR`, `MONTH`, `YEAR`, `QUARTER`," +
                                " `WEEK`, `DAY`, `SECOND`.",
                        type = {DataType.STRING}),
                @Parameter(name = "date.format",
                        description = "The format of the date value provided. " +
                                "For example, `yyyy-MM-dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
                @Parameter(name = "timestamp.in.milliseconds",
                        description = "The date value in milliseconds. " +
                                "For example, `1415712224000L`.",
                        type = {DataType.LONG},
                        dynamic = true,
                        optional = true,
                        defaultValue = "-")
        },
        parameterOverloads = {
                @ParameterOverload(parameterNames = {"date.value", "expr", "unit"}),
                @ParameterOverload(parameterNames = {"timestamp.in.milliseconds", "expr", "unit"}),
                @ParameterOverload(parameterNames = {"date.value", "expr", "unit", "date.format"})
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns data value as a `string` in the provided date.format, default date.format or " +
                        "in milliseconds based on the arguments passed to the function.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "time:dateAdd('2014-11-11 13:23:44.657', 5, 'YEAR', 'yyyy-MM-dd HH:mm:ss.SSS')",
                        description = "Adds five years to the given date value and returns `2019-11-11 13:23:44.657`."
                ),
                @Example(
                        syntax = "time:dateAdd('2014-11-11 13:23:44.657', 5, 'YEAR')",
                        description = "Adds five years to the given date value and returns `2019-11-11 13:23:44.657` " +
                                "using the default date.format `yyyy-MM-dd HH:mm:ss.SSS`."
                ),
                @Example(
                        syntax = "time:dateAdd( 1415712224000L, 1, 'HOUR')",
                        description = "Adds one hour and `1415715824000` as a `string`."
                )
        }
)
public class DateAddFunctionExtension extends FunctionExecutor {

    private static final long serialVersionUID = 1L;
    private Attribute.Type returnType = Attribute.Type.STRING;
    private boolean useDefaultDateFormat = false;
    private String dateFormat = null;
    private Calendar calInstance = Calendar.getInstance();
    private String unit = null;

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG && attributeExpressionExecutors
                .length == 3) {
            useDefaultDateFormat = true;
            dateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        }
        if (attributeExpressionExecutors.length == 4) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                        "time:dateAdd(dateValue,expr,unit,dateFormat) function, "
                        + "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:dateAdd(dateValue,expr,unit,dateFormat) function, " +
                        "required " + Attribute.Type.INT + " but found " +
                        attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                        "time:dateAdd(dateValue,expr,unit,dateFormat) function, " +
                        "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[2].getReturnType().toString());
            }
            if (attributeExpressionExecutors[3].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the fourth argument of " +
                        "time:dateAdd(dateValue,expr,unit,dateFormat) function, " +
                        "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[3].getReturnType().toString());
            }
        } else if (attributeExpressionExecutors.length == 3) {
            if (useDefaultDateFormat) {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for " +
                            "the first argument of " + "time:dateAdd" +
                            "(dateValue,expr,unit) function, required " +
                            Attribute.Type.STRING + " but found " +
                            attributeExpressionExecutors[0].getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "second argument of " + "time:dateAdd(dateValue,expr,unit) "
                            + "function, " + "required " + Attribute.Type.INT + " but " +
                            "found " + attributeExpressionExecutors[1]
                            .getReturnType().toString());
                }
                if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "second argument of time:dateAdd(dateValue,expr,unit) " +
                            "function, required " + Attribute.Type.STRING + " but found "
                            + attributeExpressionExecutors[2]
                            .getReturnType().toString());
                }
            } else {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "first argument of " + "time:dateAdd" +
                            "(timestampInMilliseconds,expr,unit) " + "function, " +
                            "required " + Attribute.Type.LONG + " but found " +
                            attributeExpressionExecutors[0].getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "second argument of " + "time:dateAdd" +
                            "(timestampInMilliseconds,expr,unit) " +
                            "function, " + "required " + Attribute.Type.INT +
                            " but found " + attributeExpressionExecutors[1]
                            .getReturnType().toString());
                }
                if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "second argument of " +
                            "time:dateAdd(timestampInMilliseconds,expr,unit) function," +
                            " required " + Attribute.Type.STRING + " but found " +
                            attributeExpressionExecutors[2].getReturnType().toString());
                }
            }
        } else {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:dateAdd() function, " +
                    "required 3 or 4, but found " +
                    attributeExpressionExecutors.length);
        }

        if (attributeExpressionExecutors[2] instanceof ConstantExpressionExecutor) {
            unit = ((String) ((ConstantExpressionExecutor) attributeExpressionExecutors[2]).getValue()).toUpperCase
                    (Locale.getDefault());
        } else {
            throw new OperationNotSupportedException("unit value has to be a constant");
        }
        return null;
    }

    public Calendar getProcessedCalenderInstance(String unit, Calendar calInstance, int expression) {


        if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_YEAR)) {
            calInstance.add(Calendar.YEAR, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_MONTH)) {
            calInstance.add(Calendar.MONTH, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_SECOND)) {
            calInstance.add(Calendar.SECOND, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_MINUTE)) {
            calInstance.add(Calendar.MINUTE, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_HOUR)) {
            calInstance.add(Calendar.HOUR, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_DAY)) {
            calInstance.add(Calendar.DAY_OF_MONTH, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_WEEK)) {
            calInstance.add(Calendar.WEEK_OF_YEAR, expression);

        } else if (unit.equals(TimeExtensionConstants.EXTENSION_TIME_UNIT_QUARTER)) {
            calInstance.add(Calendar.MONTH, expression * 3);
        }

        return calInstance;
    }

    @Override
    protected Object execute(Object[] data, State state) {

        int expression;
        String date = null;
        FastDateFormat formattedDate;

        if (data.length == 4 || useDefaultDateFormat) {
            try {
                if (data[0] == null || data[1] == null) {
                    return null;
                }
                if (!useDefaultDateFormat) {
                    if (data[3] == null) {
                        return null;
                    }
                    dateFormat = (String) data[3];
                }

                date = (String) data[0];
                expression = (Integer) data[1];
                formattedDate = FastDateFormat.getInstance(dateFormat);
                Date userSpecifiedDate = formattedDate.parse(date);
                calInstance.setTime(userSpecifiedDate);
                getProcessedCalenderInstance(unit, calInstance, expression);
                return formattedDate.format(calInstance.getTime());
            } catch (ParseException e) {
                String errorMsg = "Provided format " + dateFormat + " does not match with the timestamp " + date + ". "
                        + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }

        } else if (data.length == 3) {

            if (data[0] == null || data[1] == null || data[2] == null) {
                return null;
            }

            try {
                long dateInMills = (Long) data[0];
                calInstance.setTimeInMillis(dateInMills);
                unit = (String) data[2];
                expression = (Integer) data[1];
                getProcessedCalenderInstance(unit, calInstance, expression);
                return String.valueOf((calInstance.getTimeInMillis()));
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        } else {
            throw new SiddhiAppRuntimeException("Invalid set of arguments given to time:dateAdd() function." +
                    "Arguments should be either 3 or 4. ");
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

/**
 * dateAdd(dateValue,expr,unit,dateFormat)/dateAdd(dateValue,expr,unit)/dateAdd(timestampInMilliseconds,expr,unit)
 * Returns added specified time interval to a date.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * unit - Which part of the date format you want to manipulate. eg: "MINUTE" , "HOUR" , "MONTH" , "YEAR" , "QUARTER" ,
 *        "WEEK" , "DAY" , "SECOND"
 * expr - In which amount, selected date format part should be incremented. eg: 2 ,5 ,10 etc
 * dateFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * timestampInMilliseconds - date value in milliseconds.(from the epoch) eg: 1415712224000L
 * Accept Type(s) for dateAdd(dateValue,expr,unit,dateFormat):
 *         dateValue : STRING
 *         expr : INT
 *         unit : STRING
 *         dateFormat : STRING
 * Accept Type(s) for dateAdd(timestampInMilliseconds,expr,unit):
 *         expr : INT
 *         unit : STRING
 *         timestampInMilliseconds : LONG
 * Return Type(s): STRING
 */
