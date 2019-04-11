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
import java.util.Date;

/**
 * timestampInMilliseconds() / timestampInMilliseconds(dateValue,dateFormat)/timestampInMilliseconds(dateValue)
 * Returns system time in milliseconds.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11" , "13:23:44.657"
 * dateFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * Accept Type(s) for timestampInMilliseconds(dateValue,dateFormat):
 * dateValue : STRING
 * dateFormat : STRING
 * Return Type(s): LONG
 */

/**
 * Class representing the Time extract implementation.
 */
@Extension(
        name = "timestampInMilliseconds",
        namespace = "time",
        description = "This function returns the system time or given time in milliseconds." +
                "If two parameters of 'String' type are sent as the first argument, " +
                "the order of the parameters should be timestampInMilliseconds(date.value," +
                      "date.format) with the last parameter, i.e., 'date.format', as the optional one" +
                "Instead, if no " +
                      "argument method is invoked, the system time is returned in milliseconds.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. For example, \"2014-11-11 13:23:44.657\", " +
                                "\"2014-11-11\" , \"13:23:44.657\".",
                        type = {DataType.STRING}),
                @Parameter(name = "date.format",
                        description = "The date format of the date value provided. For example," +
                                " 'yyyy-MM-dd HH:mm:ss.SSS'.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "yyyy-MM-dd HH:mm:ss.SSS")
        },
        returnAttributes = @ReturnAttribute(
                description = "The value of the parameter returned is of 'long' type.",
                type = {DataType.LONG}),
        examples = {
                @Example(
                        syntax = "define stream InputStream (symbol string, price long, volume long);\n" +
                                 "from InputStream\n" +
                                 "select symbol , time:timestampInMilliseconds('2007-11-30 10:30:19'," +
                                 "'yyyy-MM-DD HH:MM:SS') as timestampInMilliseconds\n" +
                                 "insert into OutputStream;",
                        description = "The query converts 2007-11-30 10:30:19 which is in 'yyyy-MM-DD HH:MM:SS'"
                                + " format to  milliseconds as 'timestampInMilliseconds' and returns the symbol and "
                                + "'timestampInMilliseconds' to the 'OutputStream'."
                ),

                @Example(
                        syntax = "define stream InputStream (symbol string, price long, volume long);\n" +
                                "from InputStream\n" +
                                "select symbol , time:timestampInMilliseconds()" +
                                "as timestampInMilliseconds\n" +
                                "insert into OutputStream;",
                        description = "The query gets the system time in milliseconds"
                                + " as 'timestampInMilliseconds' and returns the symbol from the 'InputStream' and "
                                + "'timestampInMilliseconds' to the 'OutputStream'."
                )
        }
)
public class TimestampInMillisecondsFunctionExtension extends FunctionExecutor {

    private static final Logger log = Logger.getLogger(TimestampInMillisecondsFunctionExtension.class);
    private Attribute.Type returnType = Attribute.Type.LONG;
    private boolean useDefaultDateFormat = false;
    private String dateFormat = null;

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors.length > 0) {
            if (attributeExpressionExecutors.length == 2) {

                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the first " +
                            "argument of " + "time:timestampInMilliseconds" +
                            "(dateValue,dateFormat) function,required " +
                            Attribute.Type.STRING + " but found " +
                            attributeExpressionExecutors[0]
                                    .getReturnType().toString());
                }
                if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "second argument of " + "time:timestampInMilliseconds" +
                            "(dateValue,dateFormat) function, " + "required " +
                            Attribute.Type.STRING + " but found " +
                            attributeExpressionExecutors[1]
                                    .getReturnType().toString());
                }
            } else if (attributeExpressionExecutors.length == 1) {

                if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
                    throw new SiddhiAppValidationException("Invalid parameter type found for the " +
                            "first argument of " + "time:timestampInMilliseconds" +
                            "(dateValue,dateFormat) function, " + "required " +
                            Attribute.Type.STRING + "but found " +
                            attributeExpressionExecutors[0]
                                    .getReturnType().toString());
                }
                useDefaultDateFormat = true;
                dateFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
            } else {
                throw new SiddhiAppValidationException("Invalid no of arguments passed to " +
                        "time:timestampInMilliseconds" +
                        "(dateValue,dateFormat) function, " +
                        "required 2, but found " +
                        attributeExpressionExecutors.length);
            }
        }
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {
        long returnValue;

        if (data.length == 2) {
            if (data[0] == null) {
                throw new SiddhiAppRuntimeException("Invalid input given to " +
                        "time:timestampInMilliseconds(dateValue," +
                        "dateFormat) function" + ". First argument cannot be null");
            }
            if (!useDefaultDateFormat) {
                if (data[1] == null) {
                    throw new SiddhiAppRuntimeException("Invalid input given to " +
                            "time:timestampInMilliseconds(dateValue," +
                            "dateFormat) function" + ". First argument cannot be null");
                }
                dateFormat = (String) data[1];
            }
            String source = (String) data[0];
            FastDateFormat userSpecificFormat = FastDateFormat.getInstance(dateFormat);
            try {
                Date date = userSpecificFormat.parse(source);
                returnValue = date.getTime();
            } catch (ParseException e) {
                String errorMsg = "Provided format " + dateFormat + " does not match with the timestamp " + source +
                        ". " + e.getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
            return returnValue;
        } else {
            throw new SiddhiAppRuntimeException("Invalid set of arguments" + data.length + " given to " +
                    "time:timestampInMilliseconds(dateValue,dateFormat) " +
                    "function. Only two arguments can be provided. ");
        }
    }

    @Override
    protected Object execute(Object data, State state) {
        long returnValue;
        if (data == null) {
            return System.currentTimeMillis();
        }
        String source = (String) data;
        FastDateFormat userSpecificFormat = FastDateFormat.getInstance(dateFormat);
        try {
            Date date = userSpecificFormat.parse(source);
            returnValue = date.getTime();
        } catch (ParseException e) {
            String errorMsg = "Provided format " + dateFormat + " does not match with the timestamp " + source + e
                    .getMessage();
            throw new SiddhiAppRuntimeException(errorMsg, e);
        }
        return returnValue;
    }

    @Override
    public Attribute.Type getReturnType() {
        return returnType;
    }

}
