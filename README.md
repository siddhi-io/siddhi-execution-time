Siddhi Execution Time
======================================

  [![Jenkins Build Status](https://wso2.org/jenkins/job/siddhi/job/siddhi-execution-time/badge/icon)](https://wso2.org/jenkins/job/siddhi/job/siddhi-execution-time/)
  [![GitHub (pre-)Release](https://img.shields.io/github/release/siddhi-io/siddhi-execution-time/all.svg)](https://github.com/siddhi-io/siddhi-execution-time/releases)
  [![GitHub (Pre-)Release Date](https://img.shields.io/github/release-date-pre/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/releases)
  [![GitHub Open Issues](https://img.shields.io/github/issues-raw/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/issues)
  [![GitHub Last Commit](https://img.shields.io/github/last-commit/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/commits/master)
  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The **siddhi-execution-time extension** is a <a target="_blank" href="https://siddhi.io/">Siddhi</a> extension that provides basic time handling capabilities such as concat, length, replace all, etc.

For information on <a target="_blank" href="https://siddhi.io/">Siddhi</a> and it's features refer <a target="_blank" href="https://siddhi.io/redirect/docs.html">Siddhi Documentation</a>. 

## Download

* Versions 5.x and above with group id `io.siddhi.extension.*` from <a target="_blank" href="https://mvnrepository.com/artifact/io.siddhi.extension.execution.time/siddhi-execution-time/">here</a>.
* Versions 4.x and lower with group id `org.wso2.extension.siddhi.*` from <a target="_blank" href="https://mvnrepository.com/artifact/org.wso2.extension.siddhi.execution.time/siddhi-execution-time">here</a>.

## Latest API Docs 

Latest API Docs is <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2">5.0.2</a>.

## Features

* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#currentdate-function">currentDate</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the system time in 'yyyy-MM-dd' format.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#currenttime-function">currentTime</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns system time in the 'HH:mm:ss' format.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#currenttimestamp-function">currentTimestamp</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>If no argument is provided, this function will return the currentSystemTime and if the timezone is provided as an argument, it will convert the current systemtime to the given timezone and return. This function returns time in 'yyyy-MM-dd HH:mm:ss' format.<br>To check the available timezone ids, visit https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#date-function">date</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the date part of a date or date/time expression.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#dateadd-function">dateAdd</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the specified time interval added to a date.If a parameter of 'STRING' type is passed as the first argument, the function accepts four parameters with the last parameter, i.e., 'dateFormat', as an optional one. If a parameter of 'LONG' type is passed as the first argument, the function accepts three parameters, i.e., 'timestampInMilliseconds', 'expr' and 'unit' in the given order.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#datediff-function">dateDiff</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p> This function returns the time in days, between two dates. Two arguments of 'String' type are sent as the first two parameters. The function can accept four parameters,the last two parameters corresponding to the date formats being optional ones. The order of the parameters should be dateDiff(date.value1,date.value2,date.format1,date.format2). Instead, if two arguments of 'Long' type are sent as the first two parameters, the order of the parameters should be dateDiff(timestamp.in.milliseconds1,timestamp.in.milliseconds2). </p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#dateformat-function">dateFormat</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns a formatted date string.If the first argument is of 'String' type, then the function accepts three parameters with the last parameter as an optional parameter.The order of the parameters should be dateFormat(dateValue,dateTargetFormat,dateSourceFormat). Instead, if the first argument is of 'Long' type, then it accepts two parameters.In this case, the order of the parameter should be dateFormat(timestampInMilliseconds, dateTargetFormat).</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#datesub-function">dateSub</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the date after subtracting a specified time interval from it. If a parameter of 'String' type is passed as the first argument, then the function accepts four parameters with the last parameter, i.e., 'date.format' as an optional one.If a parameter of 'Long' type is passed as the first argument, then the function accepts three parameters, i.e., 'timestamp.in.milliseconds', 'expr' and 'unit' in the given order.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#dayofweek-function">dayOfWeek</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the day on which a given date falls.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#extract-function">extract</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns date attributes from a date expression. If the first argument passed is of 'String' type then the function accepts three arguments with the last parameter, i.e., 'date.format' as an optional one. The order of the parameter is extract(unit,date.value,date.format). Instead, if the first argument passed is of 'Long' type, then the function accepts two parameters.In this case, the parameter order is extract(timestamp.in.milliseconds,unit).</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#timestampinmilliseconds-function">timestampInMilliseconds</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the system time or given time in milliseconds.If two parameters of 'String' type are sent as the first argument, the order of the parameters should be timestampInMilliseconds(date.value,date.format) with the last parameter, i.e., 'date.format', as the optional oneInstead, if no argument method is invoked, the system time is returned in milliseconds.</p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.2/#utctimestamp-function">utcTimestamp</a> *<a target="_blank" href="https://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*<br><div style="padding-left: 1em;"><p>This function returns the system time in 'yyyy-MM-dd HH:mm:ss' format.</p></div>

## Dependencies 

There are no other dependencies needed for this extension. 

## Installation

For installing this extension on various siddhi execution environments refer Siddhi documentation section on <a target="_blank" href="https://siddhi.io/redirect/add-extensions.html">adding extensions</a>.

## Support and Contribution

* We encourage users to ask questions and get support via <a target="_blank" href="https://stackoverflow.com/questions/tagged/siddhi">StackOverflow</a>, make sure to add the `siddhi` tag to the issue for better response.

* If you find any issues related to the extension please report them on <a target="_blank" href="https://github.com/siddhi-io/siddhi-execution-time/issues">the issue tracker</a>.

* For production support and other contribution related information refer <a target="_blank" href="https://siddhi.io/community/">Siddhi Community</a> documentation.
