siddhi-execution-time
======================================

The **siddhi-execution-time extension** is an extension to <a target="_blank" href="https://wso2.github.io/siddhi">Siddhi</a> that provides time related functionality to Siddhi such as getting current time, current date, manipulating/formatting dates and etc.

Find some useful links below:

* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time">Source code</a>
* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/releases">Releases</a>
* <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/issues">Issue tracker</a>

## Latest API Docs 

Latest API Docs is <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8">4.0.8</a>.

## How to use 

**Using the extension in <a target="_blank" href="https://github.com/wso2/product-sp">WSO2 Stream Processor</a>**

* You can use this extension in the latest <a target="_blank" href="https://github.com/wso2/product-sp/releases">WSO2 Stream Processor</a> that is a part of <a target="_blank" href="http://wso2.com/analytics?utm_source=gitanalytics&utm_campaign=gitanalytics_Jul17">WSO2 Analytics</a> offering, with editor, debugger and simulation support. 

* This extension is shipped by default with WSO2 Stream Processor, if you wish to use an alternative version of this extension you can replace the component <a target="_blank" href="https://github.com/wso2-extensions/siddhi-execution-time/releases">jar</a> that can be found in the `<STREAM_PROCESSOR_HOME>/lib` directory.

**Using the extension as a <a target="_blank" href="https://wso2.github.io/siddhi/documentation/running-as-a-java-library">java library</a>**

* This extension can be added as a maven dependency along with other Siddhi dependencies to your project.

```
     <dependency>
        <groupId>org.wso2.extension.siddhi.execution.time</groupId>
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

* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#currentdate-function">currentDate</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns system time in yyyy-MM-dd format.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#currenttime-function">currentTime</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns system time in in HH:mm:ss format.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#currenttimestamp-function">currentTimestamp</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns system time in yyyy-MM-dd HH:mm:ss format.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#date-function">date</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns date part from a date or date/time expression.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#dateadd-function">dateAdd</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns added specified time interval to a date.If a STRING parameter passed as the first argument then function accepts four parameters with last as optional which is the date.format. If a LONG parameter passed as the first argument, then function accepts three parameters which are timestamp.in.milliseconds,expr,unit in order.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#datediff-function">dateDiff</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p> This function returns time(days) between two dates. If two STRING arguments are sent as first two parameters then function should accept four parameters with last two as optional parameters. Parameter order should be dateDiff(date.value1,date.value2,date.format1,date.format2). Else if two LONG arguments are sent as first two parameters then parameter order should be dateDiff(timestamp.in.milliseconds1,timestamp.in.milliseconds2). </p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#dateformat-function">dateFormat</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns a formatted date string.If the first argument is a STRING then function accepts three parameters with last parameter as a optional parameter.Parameter order should be dateFormat(dateValue,dateTargetFormat,dateSourceFormat). Else if first argument is a LONG then function accepts two parameters.Parameter order should be dateFormat(timestampInMilliseconds,dateTargetFormat).</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#datesub-function">dateSub</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns subtracted specified time interval to a date. If a STRING parameter passed as the first argument then function accepts four parameters with last as optional which is the date.format. If a LONG parameter passed as the first argument, then function accepts three parameters which are timestamp.in.milliseconds,expr,unit in order.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#dayofweek-function">dayOfWeek</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns the day on which a given date falls.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#extract-function">extract</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns date attributes from a date expression. If the first argument passed is a STRING then the function will accept three arguments with last parameter as optional which is date.format.Parameter order should be extract(unit,date.value,date.format). Else if the first argument passed is a LONG then function accepts two parameters.Parameter order is extract(timestamp.in.milliseconds,unit).</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#timestampinmilliseconds-function">timestampInMilliseconds</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns system time or given time in milliseconds.If two STRING parameters are sent as the first argument, the parameter order should be timestampInMilliseconds(date.value,date.format) with last parameter as the optional parameter with is date.format.Else if no argument method invoked then system time will be returned in milliseconds.</p></div>
* <a target="_blank" href="https://wso2-extensions.github.io/siddhi-execution-time/api/4.0.8/#utctimestamp-function">utcTimestamp</a> *(<a target="_blank" href="https://wso2.github.io/siddhi/documentation/siddhi-4.0/#function">(Function)</a>)*<br><div style="padding-left: 1em;"><p>This function returns System time in yyyy-MM-dd HH:mm:ss format</p></div>

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
