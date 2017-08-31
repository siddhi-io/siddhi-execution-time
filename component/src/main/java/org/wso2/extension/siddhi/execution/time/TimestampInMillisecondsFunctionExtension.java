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
import java.util.Date;
import java.util.Map;

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
        description = "This methods returns system time or given time in milliseconds.If two STRING parameters are " +
                      "sent as the first argument, the parameter order should be timestampInMilliseconds(date.value," +
                      "date.format) with last parameter as the optional parameter with is date.format.Else if no " +
                      "argument method invoked then system time will be returned in milliseconds.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "value of date. eg: \"2014-11-11 13:23:44.657\", \"2014-11-11\" , " +
                                      "\"13:23:44.657\".",
                        type = {DataType.STRING}),
                @Parameter(name = "date.format",
                        description = "Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "yyyy-MM-dd HH:mm:ss.SSS")
        },
        returnAttributes = @ReturnAttribute(
                description = "Returned type will be long.",
                type = {DataType.LONG}),
        examples = {
                @Example(
                        syntax = "define stream inputStream (symbol string, price long, volume long);\n" +
                                 "from inputStream\n" +
                                 "select symbol , time:timestampInMilliseconds('2007-11-30 10:30:19'," +
                                 "'yyyy-MM-DD HH:MM:SS') as timestampInMilliseconds\n" +
                                 "insert into outputStream;",
                        description = "The query convert the 2007-11-30 10:30:19 which is in yyyy-MM-DD HH:MM:SS"
                                + " format to the milliseconds as timestampInMilliseconds and return symbol and "
                                + "timestampInMilliseconds to the output stream"
                ),

                @Example(
                        syntax = "define stream inputStream (symbol string, price long, volume long);\n" +
                                "from inputStream\n" +
                                "select symbol , time:timestampInMilliseconds()" +
                                "as timestampInMilliseconds\n" +
                                "insert into outputStream;",
                        description = "The query get the system time in milliseconds"
                                + " as timestampInMilliseconds and return symbol and "
                                + "timestampInMilliseconds to the output stream"
                )
        }
)
public class TimestampInMillisecondsFunctionExtension extends FunctionExecutor {

    private static final Logger log = Logger.getLogger(TimestampInMillisecondsFunctionExtension.class);
    private Attribute.Type returnType = Attribute.Type.LONG;
    private boolean useDefaultDateFormat = false;
    private String dateFormat = null;

    @Override
    protected void init(ExpressionExecutor[] expressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {

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
    }

    @Override
    protected Object execute(Object[] data) {

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
                String errorMsg = "Provided format " + dateFormat + " does not match with the timestamp " + source + e
                        .getMessage();
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
    protected Object execute(Object data) {
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
