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
import org.wso2.siddhi.core.exception.SiddhiAppRuntimeException;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;
import org.wso2.siddhi.query.api.exception.SiddhiAppValidationException;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
        description = "This function returns a formatted date string.If the first argument is a STRING then function" +
                      " accepts three " +
                      "parameters with last parameter as a optional parameter.Parameter order should be dateFormat" +
                      "(dateValue,dateTargetFormat,dateSourceFormat). Else if first argument is a LONG then function " +
                      "accepts two parameters.Parameter order should be dateFormat(timestampInMilliseconds," +
                      "dateTargetFormat).",
        parameters = {
                @Parameter(name = "date.value",
                        description = "value of date. eg: \"2014-11-11 13:23:44.657\", \"2014-11-11\" , " +
                                      "\"13:23:44.657\".",
                        type = {DataType.STRING}),
                @Parameter(name = "date.target.format",
                        description = "Date format which need to be converted to. eg: yyyy/MM/dd HH:mm:ss.",
                        type = {DataType.STRING}),
                @Parameter(name = "date.source.format",
                        description = "Date format of the provided date.value1. eg: yyyy-MM-dd HH:mm:ss.SSS.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "yyyy-MM-dd HH:mm:ss.SSS"),
                @Parameter(name = "timestamp.in.milliseconds",
                        description = "date value in milliseconds.(from the epoch) eg: 1415712224000L.",
                        type = {DataType.LONG})
        },
        returnAttributes = @ReturnAttribute(
                description = "Return formatted date. Returned type will be string.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "define stream inputStream (symbol string,"
                                + "dateValue string,sourceFormat string,timestampInMilliseconds long,"
                                + "targetFormat string);\n"
                                + "from inputStream\n"
                                + "select symbol"
                                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                                + "time:dateFormat(timestampInMilliseconds,targetFormat) as formattedUnixDate\n"
                                + "insert into outputStream;",
                        description = "This query formats the dateValue in the inputStream which is currently in "
                                + "sourceFormat to the targetFormat as formattedData and formats"
                                + " timestampInMilliseconds which is in millisecond to the targetFormat"
                                + " as formattedUnixDate then return symbol formattedDate and formattedUnixDate to the"
                                + " outputStream."
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
    protected void init(ExpressionExecutor[] expressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {

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

    }

    @Override
    protected Object execute(Object[] data) {

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
    protected Object execute(Object data) {
        return null; //Since the EpochToDateFormat function takes in 2 parameters, this method does not get
        // called. Hence, not implemented.

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

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
