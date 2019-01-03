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

import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.execution.time.util.TimeExtensionConstants;
import org.wso2.siddhi.annotation.Example;
import org.wso2.siddhi.annotation.Extension;
import org.wso2.siddhi.annotation.Parameter;
import org.wso2.siddhi.annotation.ReturnAttribute;
import org.wso2.siddhi.annotation.util.DataType;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.exception.OperationNotSupportedException;
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;
import org.wso2.siddhi.core.executor.ConstantExpressionExecutor;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.exception.SiddhiAppValidationException;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * dateSub(dateValue,expr,unit,dateFormat)/dateSub(dateValue,expr,unit)/dateSub(timestampInMilliseconds,expr,unit)
 * Returns subtracted specified time interval to a date.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * unit - Which part of the date format you want to manipulate. eg: "MINUTE" , "HOUR" , "MONTH" , "YEAR" , "QUARTER" ,
 * "WEEK" , "DAY" , "SECOND"
 * expr - In which amount, selected date format part should be decremented. eg: 2 ,5 ,10 etc
 * dateFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * timestampInMilliseconds - date value in milliseconds.(from the epoch) eg: 1415712224000L
 * Accept Type(s) for dateSub(dateValue,expr,unit,dateFormat):
 * dateValue : STRING
 * expr : INT
 * unit : STRING
 * dateFormat : STRING
 * Accept Type(s) for dateSub(timestampInMilliseconds, expr,unit):
 * timestampInMilliseconds : LONG
 * expr : INT
 * unit : STRING
 * Return Type(s): STRING
 */

/**
 * Class representing the Time dateAdd implementation.
 */
@Extension(
        name = "dateSub",
        namespace = "time",
        description = "This function returns the date after subtracting a specified time interval from it. " +
                      "If a parameter of 'String' type is passed as the first argument, then the function accepts " +
                "four parameters with the last parameter, i.e., 'date.format' as an optional one." +
                "If a parameter of 'Long' type is passed as the first argument, then the function accepts three" +
                      " parameters, i.e., 'timestamp.in.milliseconds', 'expr' and 'unit' in the given order.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of date. For example, \"2014-11-11 13:23:44.657\", \"2014-11-11\" , " +
                                      "\"13:23:44.657\".",
                        type = {DataType.STRING}),
                @Parameter(name = "expr",
                        description = "The amount by which the selected part of the date should be incremented. " +
                                "For example, 2 ,5 ,10," +
                                      " etc.",
                        type = {DataType.INT}),
                @Parameter(name = "unit",
                        description = "The part of the date that is required to be modified. For example," +
                                " \"MINUTE\" , \"HOUR\" , \"MONTH\" , \"YEAR\" , \"QUARTER\" ,\n" +
                                      "\"WEEK\" , \"DAY\" , \"SECOND\".",
                        type = {DataType.STRING}),
                @Parameter(name = "date.format",
                        description = "The date format of the date value provided. For example," +
                                " 'yyyy-MM-dd HH:mm:ss.SSS'",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "yyyy-MM-dd HH:mm:ss.SSS"),
                @Parameter(name = "timestamp.in.milliseconds",
                        description = "The date value in milliseconds from the epoch. For example, 1415712224000L",
                        type = {DataType.LONG})
        },
        returnAttributes = @ReturnAttribute(
                description = "This returns the date after subtracting the specified time from " +
                        "the given date format. " +
                        "The type returned is String.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "define stream InputStream (symbol string,dateValue string,dateFormat string," +
                                "expr int);\n" +
                                "from InputStream\n " +
                                "select symbol , time:dateAdd(dateValue,expr,'YEAR',dateFormat) as yearSubtracted\n" +
                                "insert into OutputStream;",
                        description = "This query gets the date value from the input stream, decrements the 'YEAR'" +
                                "value of the 'dateValue' by the 'expr' value given, " +
                                "formats the resultant value into the" +
                                " 'dateFormat' format in the input stream and returns the formatted value to" +
                                " the 'OutputStream' as 'yearSubtracted' with the symbol."
                ),
                @Example(
                        syntax = "define stream InputStream (symbol string,dateValue string,dateFormat string," +
                                "timestampInMilliseconds long,expr int);\n" +
                                "from InputStream\n " +
                                "time:dateSub(timestampInMilliseconds,expr,'HOUR') as hourSubtractedMills\n " +
                                "insert into OutputStream;",
                        description = "This query gets the value of the 'timestampInMilliseconds' " +
                                "from the input stream,"
                                + " subtracts the 'expr' number of hours from it and returns the "
                                + " resultant value in milliseconds as 'hourSubtractedMills', to the 'OutputStream' "
                                + "with the symbol."
                )
        }
)
public class DateSubFunctionExtension extends FunctionExecutor {

    private Attribute.Type returnType = Attribute.Type.STRING;
    private static final Logger log = Logger.getLogger(DateSubFunctionExtension.class);
    private boolean useDefaultDateFormat = false;
    private String dateFormat = null;
    private Calendar calInstance = Calendar.getInstance();
    private String unit = null;

    @Override
    protected void init(ExpressionExecutor[] expressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {

        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG && attributeExpressionExecutors
                                                                                              .length == 3) {
            useDefaultDateFormat = true;
            dateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        }
        if (attributeExpressionExecutors.length == 4) {
            if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                                                       "time:dateSub(dateValue,expr,unit,dateFormat) function, " +
                                                       "required " + Attribute.Type.STRING + " but found " +
                                                       attributeExpressionExecutors[0].getReturnType().toString());
            }
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                                                           "time:dateSub(dateValue,expr,unit,dateFormat) function, " +
                                                           "required " + Attribute.Type.INT + " but found " +
                                                           attributeExpressionExecutors[1].getReturnType().toString());
            }
            if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the third argument of " +
                                                           "time:dateSub(dateValue,expr,unit,dateFormat) function, " +
                                                           "required " + Attribute.Type.STRING + " but found " +
                                                           attributeExpressionExecutors[2].getReturnType().toString());
            }
            if (attributeExpressionExecutors[3].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the fourth argument of " +
                                                           "time:dateSub(dateValue,expr,unit,dateFormat) function, " +
                                                           "required " + Attribute.Type.STRING + " but found " +
                                                           attributeExpressionExecutors[3].getReturnType().toString());
            }
        } else if (attributeExpressionExecutors.length == 3) {
            if (useDefaultDateFormat) {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                                                               "for the first argument of " +
                                                               "time:dateSub(dateValue,expr,unit) function, " +
                                                               "required " + Attribute.Type.STRING + " but found " +
                                                               attributeExpressionExecutors[0].
                                                                       getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                                                               "for the second argument of " +
                                                               "time:dateSub(dateValue,expr,unit) function, " +
                                                               "required " + Attribute.Type.INT + " but found " +
                                                               attributeExpressionExecutors[1].
                                                                       getReturnType().toString());
                }
                if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                                                               "for the second argument of " +
                                                               "time:dateSub(dateValue,expr,unit) function, " +
                                                               "required " + Attribute.Type.STRING + " but found " +
                                                               attributeExpressionExecutors[2].
                                                                       getReturnType().toString());
                }
            } else {
                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.LONG) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                                                               "for the first argument of " +
                                                               "time:dateSub(timestampInMilliseconds,expr,unit) " +
                                                               "function, " + "required " + Attribute.Type.LONG +
                                                               " but found " + attributeExpressionExecutors[0]
                                                                       .getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.INT) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                                                               "for the second argument of " +
                                                               "time:dateSub(timestampInMilliseconds,expr,unit) " +
                                                               "function, " + "required " + Attribute.Type.INT +
                                                               " but found " + attributeExpressionExecutors[1]
                                                                       .getReturnType().toString());
                }
                if (attributeExpressionExecutors[2].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found " +
                                                               "for the second argument of " +
                                                               "time:dateSub(timestampInMilliseconds,expr,unit) " +
                                                               "function, " + "required " + Attribute.Type.STRING +
                                                               " but found " + attributeExpressionExecutors[2]
                                                                       .getReturnType().toString());
                }
            }
        } else {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:dateSub() function, " +
                                                       "required 3 or 4, but found " +
                                                       attributeExpressionExecutors.length);
        }

        if (attributeExpressionExecutors[2] instanceof ConstantExpressionExecutor) {
            unit = ((String) ((ConstantExpressionExecutor) attributeExpressionExecutors[2]).getValue()).toUpperCase
                    (Locale.getDefault());
        } else {
            throw new OperationNotSupportedException("unit value has to be a constant");
        }

    }

    @Override
    protected Object execute(Object[] data) {

        int expression;
        String date = null;
        FastDateFormat formattedDate;

        if (data.length == 4 || useDefaultDateFormat) {
            try {
                if (data[0] == null) {
                    throw new SiddhiAppRuntimeException("Invalid input given to str:dateSub(date,expr," +
                                                            "unit,dateFormat) function" + ". First " +
                                                            "argument cannot be null");
                }
                if (data[1] == null) {
                    throw new SiddhiAppRuntimeException("Invalid input given to str:dateSub(date,expr," +
                                                            "unit,dateFormat) function" + ". Second " +
                                                            "argument cannot be null");
                }
                if (!useDefaultDateFormat) {
                    if (data[3] == null) {
                        throw new SiddhiAppRuntimeException("Invalid input given to str:dateSub(date,expr," +
                                                            "unit,dateFormat) function" + ". Fourth " +
                                                            "argument cannot be null");
                    }
                    dateFormat = (String) data[3];
                }

                date = (String) data[0];
                expression = (Integer) data[1];
                expression = -expression;
                formattedDate = FastDateFormat.getInstance(dateFormat);
                Date userSpecifiedDate = formattedDate.parse(date);
                calInstance.setTime(userSpecifiedDate);
                getProcessedCalenderInstance(unit, calInstance, expression);
                return formattedDate.format(calInstance.getTime());
            } catch (ParseException e) {
                String errorMsg = "Provided format " + dateFormat + " does not match with the timestamp " + date + e
                        .getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }

        } else if (data.length == 3) {

            if (data[0] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to time:dateSub(timestampInMilliseconds," +
                                                        "expr,unit) function" + ". First " + "argument cannot be null");
            }
            if (data[1] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to time:dateSub(timestampInMilliseconds," +
                                                        "expr,unit) function" + ". Second " +
                                                        "argument cannot be null");
            }
            if (data[2] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to time:dateSub(timestampInMilliseconds," +
                                                        "expr,unit) function" + ". Third " + "argument cannot be null");
            }

            try {
                long dateInMills = (Long) data[0];
                calInstance.setTimeInMillis(dateInMills);
                expression = (Integer) data[1];
                expression = -expression;
                getProcessedCalenderInstance(unit, calInstance, expression);
                return String.valueOf((calInstance.getTimeInMillis()));
            } catch (ClassCastException e) {
                String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        } else {
            throw new SiddhiAppRuntimeException("Invalid set of arguments given to time:dateSub() function." +
                                                    "Arguments should be either 3 or 4. ");
        }
    }

    private Calendar getProcessedCalenderInstance(String unit, Calendar calInstance, int expression) {

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
    protected Object execute(Object data) {
        return null; //Since the EpochToDateFormat function takes in 2 parameters, this method does not get
        // called. Hence, not implemented.

    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

    @Override
    public Map<String, Object> currentState() { //No need to maintain a state.
        return null;
    }

    @Override
    public void restoreState(Map<String, Object> state) {
        //Since there's no need to maintain a state, nothing needs to be done here.
    }
}
