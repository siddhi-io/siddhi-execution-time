Siddhi Execution Time
======================================

  [![Jenkins Build Status](https://wso2.org/jenkins/job/siddhi/job/siddhi-execution-time/badge/icon)](https://wso2.org/jenkins/job/siddhi/job/siddhi-execution-time/)
  [![GitHub Release](https://img.shields.io/github/release/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/releases)
  [![GitHub Release Date](https://img.shields.io/github/release-date/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/releases)
  [![GitHub Open Issues](https://img.shields.io/github/issues-raw/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/issues)
  [![GitHub Last Commit](https://img.shields.io/github/last-commit/siddhi-io/siddhi-execution-time.svg)](https://github.com/siddhi-io/siddhi-execution-time/commits/master)
  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

The **siddhi-execution-time extension** is a <a target="_blank" href="https://siddhi.io/">Siddhi</a> extension that provides time related functionality such as getting current time, current date, manipulating/formatting dates, etc.
 
For information on <a target="_blank" href="https://siddhi.io/">Siddhi</a> and it's features refer <a target="_blank" href="https://siddhi.io/redirect/docs.html">Siddhi Documentation</a>. 

## Download

* Versions 5.x and above with group id `io.siddhi.extension.*` from <a target="_blank" href="https://mvnrepository.com/artifact/io.siddhi.extension.execution.time/siddhi-execution-time/">here</a>.
* Versions 4.x and lower with group id `org.wso2.extension.siddhi.*` from <a target="_blank" href="https://mvnrepository.com/artifact/org.wso2.extension.siddhi.execution.time/siddhi-execution-time">here</a>.

## Latest API Docs 

Latest API Docs is <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6">5.0.6</a>.

## Features

* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#currentdate-function">currentDate</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Function returns the system time in <code>yyyy-MM-dd</code> format.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#currenttime-function">currentTime</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Function returns system time in the <code>HH:mm:ss</code> format.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#currenttimestamp-function">currentTimestamp</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">When no argument is provided, function returns the system current timestamp in <code>yyyy-MM-dd HH:mm:ss</code> format, and when a timezone is provided as an argument, it converts and return the current system time to the given timezone format.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#date-function">date</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Extracts the date part of a date or date-time and return it in <code>yyyy-MM-dd</code> format.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#dateadd-function">dateAdd</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Adds the specified time interval to a date.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#datediff-function">dateDiff</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Returns difference between two dates in days.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#dateformat-function">dateFormat</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Formats the data in string or milliseconds format to the given date format.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#datesub-function">dateSub</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Subtracts the specified time interval from the given date.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#dayofweek-function">dayOfWeek</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Extracts the day on which a given date falls.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#extract-function">extract</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Function extracts a date unit from the date.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#timestampinmilliseconds-function">timestampInMilliseconds</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Returns the system time or the given time in milliseconds.</p></p></div>
* <a target="_blank" href="https://siddhi-io.github.io/siddhi-execution-time/api/5.0.6/#utctimestamp-function">utcTimestamp</a> *(<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">Function</a>)*<br> <div style="padding-left: 1em;"><p><p style="word-wrap: break-word;margin: 0;">Function returns the system current time in UTC timezone with given date format which defaults to <code>yyyy-MM-dd HH:mm:ss</code> if not provided.</p></p></div>

## Dependencies 

There are no other dependencies needed for this extension. 

## Installation

For installing this extension on various siddhi execution environments refer Siddhi documentation section on <a target="_blank" href="https://siddhi.io/redirect/add-extensions.html">adding extensions</a>.

## Support and Contribution

* We encourage users to ask questions and get support via <a target="_blank" href="https://stackoverflow.com/questions/tagged/siddhi">StackOverflow</a>, make sure to add the `siddhi` tag to the issue for better response.

* If you find any issues related to the extension please report them on <a target="_blank" href="https://github.com/siddhi-io/siddhi-execution-time/issues">the issue tracker</a>.

* For production support and other contribution related information refer <a target="_blank" href="https://siddhi.io/community/">Siddhi Community</a> documentation.
