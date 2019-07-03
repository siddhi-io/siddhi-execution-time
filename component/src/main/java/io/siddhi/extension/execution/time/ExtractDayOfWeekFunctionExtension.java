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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        description = "Extracts the day on which a given date falls.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, `13:23:44.657`.",
                        type = {DataType.STRING}),
                @Parameter(name = "date.format",
                        description = "The format of the date value provided. " +
                                "For example, `yyyy/MM/dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns the day of the week corresponding to the date value given. " +
                        "The values can be one of `Monday`, `Tuesday`, `Wednesday`, `Thursday`, `Friday`, " +
                        "`Saturday`, or `Sunday`.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "time:date('2014/12/11 13:23:44', 'yyyy/MM/dd HH:mm:ss')",
                        description = "Extracts the date and returns `Thursday`."
                ),
                @Example(
                        syntax = "time:date('2014-11-11 13:23:44.345')",
                        description = "Extracts the date and returns `Tuesday`."
                ),
        }
)
public class ExtractDayOfWeekFunctionExtension extends FunctionExecutor {

    private static final Logger log = Logger.getLogger(ExtractDayOfWeekFunctionExtension.class);
    private Attribute.Type returnType = Attribute.Type.STRING;

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
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
        return null;
    }

    @Override
    protected Object execute(Object[] data, State state) {
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

    @Override
    protected Object execute(Object data, State state) {
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
    public Attribute.Type getReturnType() {
        return returnType;
    }

    //private methods
    private String getDayOfWeek(Date date) {
        DateFormat dateFormatDayOfWeek = new SimpleDateFormat(TimeExtensionConstants.EXTENSION_TIME_SIMPLE_DATE_FORMAT);
        return dateFormatDayOfWeek.format(date);
    }


}
