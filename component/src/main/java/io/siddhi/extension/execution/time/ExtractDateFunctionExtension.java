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
import java.util.Date;

/**
 * date(dateValue,dateFormat)
 * Returns date part from a date or date/time expression.
 * dateValue - value of date. eg: "2014-11-11 13:23:44.657", "2014-11-11"
 * dateFormat - Date format of the provided date value. eg: yyyy-MM-dd HH:mm:ss.SSS
 * Accept Type(s) for date(dateValue,dateFormat):
 * dateValue : STRING
 * dateFormat : STRING
 * Return Type(s): STRING
 */


/**
 * Class representing the Time date implementation.
 */
@Extension(
        name = "date",
        namespace = "time",
        description = "Extracts the date part of a date or date-time and return it in `yyyy-MM-dd` format.",
        parameters = {
                @Parameter(name = "date.value",
                        description = "The value of the date. " +
                                "For example, `2014-11-11 13:23:44.657`, `2014-11-11`, " +
                                "`13:23:44.657`.",
                        type = {DataType.STRING},
                        dynamic = true),
                @Parameter(name = "date.format",
                        description = "The format of the date value provided. " +
                                "For example, `yyyy/MM/dd HH:mm:ss.SSS`.",
                        type = {DataType.STRING},
                        dynamic = true,
                        optional = true,
                        defaultValue = "`yyyy-MM-dd HH:mm:ss.SSS`"),
        },
        parameterOverloads = {
                @ParameterOverload(parameterNames = {"date.value", "date.format"}),
                @ParameterOverload(parameterNames = {"date.value"})
        },
        returnAttributes = @ReturnAttribute(
                description = "Returns the extracted date value from a date or date-time in `yyyy-MM-dd` format.",
                type = {DataType.STRING}),
        examples = {
                @Example(
                        syntax = "time:date('2014/11/11 13:23:44', 'yyyy/MM/dd HH:mm:ss')",
                        description = "Extracts the date and returns `2014-11-11`."
                ),
                @Example(
                        syntax = "time:date('2014-11-23 13:23:44.345')",
                        description = "Extracts the date and returns `2014-11-13`."
                ),
                @Example(
                        syntax = "time:date('13:23:44', 'HH:mm:ss')",
                        description = "Extracts the date and returns `1970-01-01`."
                )
        }
)
public class ExtractDateFunctionExtension extends FunctionExecutor {

    private static final Logger log = Logger.getLogger(ExtractDateFunctionExtension.class);
    private Attribute.Type returnType = Attribute.Type.STRING;

    @Override
    protected StateFactory init(ExpressionExecutor[] attributeExpressionExecutors,
                                ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        if (attributeExpressionExecutors.length > 2) {
            throw new SiddhiAppValidationException("Invalid no of arguments passed to time:date(dateValue," +
                    "dateFormat) function, " + "required 2, but found " +
                    attributeExpressionExecutors.length);
        }
        if (attributeExpressionExecutors[0].getReturnType() != Attribute.Type.STRING) {
            throw new SiddhiAppValidationException("Invalid parameter type found for the first argument of " +
                    "time:date(dateValue,dateFormat) function, " + "required " +
                    Attribute.Type.STRING + " but found " +
                    attributeExpressionExecutors[0].getReturnType().toString());
        }
        //User can omit sending the dateFormat thus using a default CEP Time format
        if (attributeExpressionExecutors.length > 0) {
            if (attributeExpressionExecutors[1].getReturnType() != Attribute.Type.STRING) {
                throw new SiddhiAppValidationException("Invalid parameter type found for the second argument of " +
                        "time:date(dateValue,dateFormat) function, " +
                        "required " + Attribute.Type.STRING + " but found " +
                        attributeExpressionExecutors[1].getReturnType().toString());
            }
        }
        return null;

    }

    @Override
    protected Object execute(Object[] data, State state) {
        String userFormat;
        if (data[0] == null) {
            return null;
        }
        if (data.length > 0) {
            if (data[1] == null) {
                return null;
            }
            userFormat = (String) data[1];
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
            FastDateFormat dataFormat = FastDateFormat.getInstance(TimeExtensionConstants.EXTENSION_TIME_DATE_FORMAT);
            return dataFormat.format(userSpecifiedDate);
        } catch (ParseException e) {
            String errorMsg = "Provided format " + userFormat + " does not match with the timestamp " + source + ". "
                    + e.getMessage();
            throw new SiddhiAppRuntimeException(errorMsg, e);
        } catch (ClassCastException e) {
            String errorMsg = "Provided Data type cannot be cast to desired format. " + e.getMessage();
            throw new SiddhiAppRuntimeException(errorMsg, e);
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

