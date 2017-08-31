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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * dayOfWeek(dateValue,dateFormat)
 * Returns day on which a given date falls.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11"
 * dateFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * Accept Type(s) for dayOfWeek(dateValue,dateFormat):
 * dateValue : STRING
 * dateFormat : STRING
 * Return Type(s): STRING
 * <p>
 * In the case of using only the datevalue as the parameter, user has to follow
 * the following specific format for the date
 * yyyy-MM-dd time
 */

/**
 * Class representing the Time dayOfWeek implementation.
 */
@Extension(
        name = "dayOfWeek",
        namespace = "time",
        description = "This function returns the day on which a given date falls.",
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
                description = "Returned the corresponding day of the week from the date value as a string value.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax =  "define stream inputStream (symbol string, dateValue string,dateFormat string);\n"
                                + "from inputStream\n"
                                + "select symbol,time:dayOfWeek(dateValue,dateFormat) as dayOfWeekExtracted\n"
                                + "insert into outputStream;",
                        description = "The Query extract the day of the week from the date given as dateValue which is"
                                + "in the format 'dateFormat' and return symbol and extracted day as dayOfWeekExtracted"
                                + "to the outputStream."
                )
        }
)
public class ExtractDayOfWeekFunctionExtension extends FunctionExecutor {

    private static final Logger log = Logger.getLogger(ExtractDayOfWeekFunctionExtension.class);
    private Attribute.Type returnType = Attribute.Type.STRING;

    @Override
    protected void init(ExpressionExecutor[] expressionExecutors, ConfigReader configReader,
                        SiddhiAppContext siddhiAppContext) {
        if (attributeExpressionExecutors.length > 2) {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:dayOfWeek(dateValue," +
                                                   "dateFormat) function, " + "required 2, but found " +
                                                   attributeExpressionExecutors.length);
        }
        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
            throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                                                       "time:dayOfWeek(dateValue,dateFormat) function, " +
                                                       "required " + Attribute.Type.STRING +
                                                       " but found " + attributeExpressionExecutors[0]
                                                               .getReturnType().toString());
        }
        //User can omit sending the dateFormat thus using a default CEP Time format
        if (attributeExpressionExecutors.length > 0) {
            if (attributeExpressionExecutors.length > 1 && attributeExpressionExecutors[1].getReturnType()
                                                           != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                                                           "time:dayOfWeek(dateValue,dateFormat) function, " +
                                                           "required " + Attribute.Type.STRING +
                                                           " but found " + attributeExpressionExecutors[1]
                                                                   .getReturnType().toString());
            }
        }

    }

    // this method will be executed for two or more parameters
    @Override
    protected Object execute(Object[] data) {
        String userFormat;
        if (data[0] == null) {
            throw new SiddhiAppRuntimeException("Invalid input given to time:dayOfWeek(dateValue," +
                                                "dateFormat) function" + ". First " + "argument cannot be null");
        }
        if (data.length > 1) {
            if (data[1] == null) {
                throw new SiddhiAppRuntimeException(
                        "Invalid input given to time:dayOfWeek(dateValue,dateFormat) function" + ". Second " +
                        "argument cannot be null");
            } else {
                userFormat = (String) data[1];
            }
        } else {
            userFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        }
        String source = null;
        FastDateFormat userSpecificFormat;
        Date userSpecifiedDate;
        try {
            source = (String) data[0];
            userSpecificFormat = FastDateFormat.getInstance(userFormat);
            userSpecifiedDate = userSpecificFormat.parse(source);
            return getDayOfWeek(userSpecifiedDate);
        } catch (ParseException e) {
            String errorMsg = "Provided format " + userFormat + " does not match with the timestamp " + source + e
                    .getMessage();
            throw new SiddhiAppRuntimeException(errorMsg, e);
        } catch (ClassCastException e) {
            String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
            throw new SiddhiAppRuntimeException(errorMsg, e);
        }
    }


    // this method will be executed for a single parameter
    @Override
    protected Object execute(Object data) {
        String userFormat;
        if (data == null) {
            throw new SiddhiAppRuntimeException("Invalid input given to time:dayOfWeek(dateValue," +
                                                    "dateFormat) function" + ". First " + "argument cannot be null");
        }
        userFormat = TimeExtensionConstants.EXTENSION_TIME_DEFAULT_DATE_FORMAT;
        String source;
        try {
            source = (String) data;
            String inputData[] = source.split(" ");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = format.parse(inputData[0]);
                return getDayOfWeek(date);
            } catch (ParseException e) {
                String errorMsg = "Provided format " + userFormat + " does not match with the timestamp " + source + e
                        .getMessage();
                throw new SiddhiAppRuntimeException(errorMsg, e);
            }
        } catch (ClassCastException e) {
            String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
            throw new SiddhiAppRuntimeException(errorMsg, e);
        }
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

    //private methods
    private String getDayOfWeek(Date date) {
        DateFormat dateFormatDayOfWeek = new SimpleDateFormat(TimeExtensionConstants.EXTENSION_TIME_SIMPLE_DATE_FORMAT);
        return dateFormatDayOfWeek.format(date);
    }


}
