siddhi-execution-time
======================================

The **siddhi-execution-time extension** is an extension to <a target="_blank" href="https://wso2.github.io/siddhi">Siddhi</a> that provides time related functionality to Siddhi such as getting current time, current date, manipulating/formatting dates and etc.

**Note** : All date formats will follow [Fast Date Format](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/time/FastDateFormat.html)

Find some useful links below:

* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time">Source code</a>
* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/releases">Releases</a>
* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/issues">Issue tracker</a>

## Latest API Docs 

Latest API Docs is <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0">5.0.0</a>.

## How to use 

**Using the extension in <a target="_blank" href="https://github.com/wso2/product-sp">WSO2 Stream Processor</a>**

* You can use this extension in the latest <a target="_blank" href="https://github.com/wso2/product-sp/releases">WSO2 Stream Processor</a> that is a part of <a target="_blank" href="http://wso2.com/analytics?utm_source=gitanalytics&utm_campaign=gitanalytics_Jul17">WSO2 Analytics</a> offering, with editor, debugger and simulation support. 

* This extension is shipped by default with WSO2 Stream Processor, if you wish to use an alternative version of this extension you can replace the component <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/releases">jar</a> that can be found in the `<STREAM_PROCESSOR_HOME>/lib` directory.

**Using the extension as a <a target="_blank" href="https://wso2.github.io/siddhi/documentation/running-as-a-java-library">java library</a>**

* This extension can be added as a maven dependency along with other Siddhi dependencies to your project.

```
     <dependency>
        <groupId>io.siddhi.extension.execution.time</groupId>
        <artifactId>siddhi-execution-time</artifactId>
        <version>x.x.x</version>
     </dependency>
```

## Jenkins Build Status

---

|  Branch | Build Status |
| :------ |:------------ | 
| master  | [![Build Status](https://wso2.org/jenkins/job/siddhi/job/siddhi-execution-time/badge/icon)](https://wso2.org/jenkins/job/siddhi/job/siddhi-execution-time/) |

---

## Features

* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#currentdate-function">currentDate</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the system time in 'yyyy-MM-dd' format.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#currenttime-function">currentTime</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns system time in the 'HH:mm:ss' format.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#currenttimestamp-function">currentTimestamp</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>If no argument is provided, this function will return the currentSystemTime and if the timezone is provided as an argument, it will convert the current systemtime to the given timezone and return. This function returns time in 'yyyy-MM-dd HH:mm:ss' format.<br>To check the available timezone ids, visit https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#date-function">date</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the date part of a date or date/time expression.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#dateadd-function">dateAdd</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the specified time interval added to a date.If a parameter of 'STRING' type is passed as the first argument, the function accepts four parameters with the last parameter, i.e., 'dateFormat', as an optional one. If a parameter of 'LONG' type is passed as the first argument, the function accepts three parameters, i.e., 'timestampInMilliseconds', 'expr' and 'unit' in the given order.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#datediff-function">dateDiff</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p> This function returns the time in days, between two dates. Two arguments of 'String' type are sent as the first two parameters. The function can accept four parameters,the last two parameters corresponding to the date formats being optional ones. The order of the parameters should be dateDiff(date.value1,date.value2,date.format1,date.format2). Instead, if two arguments of 'Long' type are sent as the first two parameters, the order of the parameters should be dateDiff(timestamp.in.milliseconds1,timestamp.in.milliseconds2). </p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#dateformat-function">dateFormat</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns a formatted date string.If the first argument is of 'String' type, then the function accepts three parameters with the last parameter as an optional parameter.The order of the parameters should be dateFormat(dateValue,dateTargetFormat,dateSourceFormat). Instead, if the first argument is of 'Long' type, then it accepts two parameters.In this case, the order of the parameter should be dateFormat(timestampInMilliseconds, dateTargetFormat).</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#datesub-function">dateSub</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the date after subtracting a specified time interval from it. If a parameter of 'String' type is passed as the first argument, then the function accepts four parameters with the last parameter, i.e., 'date.format' as an optional one.If a parameter of 'Long' type is passed as the first argument, then the function accepts three parameters, i.e., 'timestamp.in.milliseconds', 'expr' and 'unit' in the given order.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#dayofweek-function">dayOfWeek</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the day on which a given date falls.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#extract-function">extract</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns date attributes from a date expression. If the first argument passed is of 'String' type then the function accepts three arguments with the last parameter, i.e., 'date.format' as an optional one. The order of the parameter is extract(unit,date.value,date.format). Instead, if the first argument passed is of 'Long' type, then the function accepts two parameters.In this case, the parameter order is extract(timestamp.in.milliseconds,unit).</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#timestampinmilliseconds-function">timestampInMilliseconds</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the system time or given time in milliseconds.If two parameters of 'String' type are sent as the first argument, the order of the parameters should be timestampInMilliseconds(date.value,date.format) with the last parameter, i.e., 'date.format', as the optional oneInstead, if no argument method is invoked, the system time is returned in milliseconds.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/5.0.0/#utctimestamp-function">utcTimestamp</a> *<a target="_blank" href="http://siddhi.io/documentation/siddhi-5.x/query-guide-5.x/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the system time in 'yyyy-MM-dd HH:mm:ss' format.</p></div>

## How to Contribute
 
  * Please report issues at <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/issues">GitHub Issue Tracker</a>.
  
  * Send your contributions as pull requests to <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/tree/master">master branch</a>. 
 
## Contact us 

 * Post your questions with the <a target="_blank" href="http://stackoverflow.com/search?q=siddhi">"Siddhi"</a> tag in <a target="_blank" href="http://stackoverflow.com/search?q=siddhi">Stackoverflow</a>. 
 
 * Siddhi developers can be contacted via the mailing lists:
 
    Developers List   : [dev@wso2.org](mailto:dev@wso2.org)
    
    Architecture List : [architecture@wso2.org](mailto:architecture@wso2.org)
 
## Support 

* We are committed to ensuring support for this extension in production. Our unique approach ensures that all support leverages our open development methodology and is provided by the very same engineers who build the technology. 

* For more details and to take advantage of this unique opportunity contact us via <a target="_blank" href="http://wso2.com/support?utm_source=gitanalytics&utm_campaign=gitanalytics_Jul17">http://wso2.com/support/</a>. 
