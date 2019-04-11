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
import io.siddhi.query.api.definition.Attribute;
import io.siddhi.query.api.exception.SiddhiAppValidationException;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.log4j.Logger;
import org.wso2.extension.siddhi.execution.time.util.TimeExtensionConstants;

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
        description = "This function returns a formatted date string.If the first argument is of 'String' type, then" +
                    " the function accepts three " +
                      "parameters with the last parameter as an optional parameter.The order of the parameters" +
                    " should be dateFormat(dateValue,dateTargetFormat,dateSourceFormat). " +
                    "Instead, if the first argument is of 'Long' type, then it " +
                      "accepts two parameters.In this case, the order of the parameter " +
                    "should be dateFormat(timestampInMilliseconds, dateTargetFormat).",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. For example," +
                                " \"2014-11-11 13:23:44.657\", \"2014-11-11\" , " +
                                      "\"13:23:44.657\".",
                        type = {DataType.STRING}),
                @Parameter(name = "date.target.format",
                        description = "The format of the date into which the date value needs to be converted. " +
                                "For example, 'yyyy/MM/dd HH:mm:ss'.",
                        type = {DataType.STRING}),
                @Parameter(name = "date.source.format",
                        description = "The format in which the data value is present in the input stream." +
                                "For example, 'yyyy-MM-dd HH:mm:ss.SSS'.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "yyyy-MM-dd HH:mm:ss.SSS"),
                @Parameter(name = "timestamp.in.milliseconds",
                        description = "The date value in milliseconds from the epoch. For example, 1415712224000L.",
                        type = {DataType.LONG})
        },
        returnAttributes = @ReturnAttribute(
                description = "The formatted data that is returned. The returned value is of 'String' type.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "define stream InputStream (symbol string,"
                                + "dateValue string,sourceFormat string,timestampInMilliseconds long,"
                                + "targetFormat string);\n"
                                + "from InputStream\n"
                                + "select symbol"
                                + "time:dateFormat(dateValue,targetFormat,sourceFormat) as formattedDate,"
                                + "time:dateFormat(timestampInMilliseconds,targetFormat) as formattedUnixDate\n"
                                + "insert into OutputStream;",
                        description = "This query formats the 'dateValue' in the 'InputStream' which is in "
                                + "the 'sourceFormat' to the 'targetFormat' as 'formattedData'. It also formats"
                                + " 'timestampInMilliseconds' which is in milliseconds to the 'targetFormat'"
                                + " as 'formattedUnixDate'. The function then returns the symbol " +
                                "'formattedDate' and 'formattedUnixDate' to the"
                                + " 'OutputStream'."
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
    protected StateFactory<State> init(ExpressionExecutor[] attributeExpressionExecutors,
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
