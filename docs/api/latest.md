# API Docs - v5.0.8

!!! Info "Tested Siddhi Core version: *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/">5.1.21</a>*"
    It could also support other Siddhi Core minor versions.

## Time

### currentDate *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Function returns the system time in <code>yyyy-MM-dd</code> format.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:currentDate()
```

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:currentDate()
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the current date in the <code>yyyy-MM-dd</code> format, such as <code>2019-06-21</code>.</p>
<p></p>
### currentTime *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Function returns system time in the <code>HH:mm:ss</code> format.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:currentTime()
```

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:currentTime()
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the current date in the <code>HH:mm:ss</code> format, such as <code>15:23:24</code>.</p>
<p></p>
### currentTimestamp *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">When no argument is provided, function returns the system current timestamp in <code>yyyy-MM-dd HH:mm:ss</code> format, and when a timezone is provided as an argument, it converts and return the current system time to the given timezone format.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:currentTimestamp()
<STRING> time:currentTimestamp(<STRING> timezone)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">timezone</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The timezone to which the current time need to be converted. For example, <code>Asia/Kolkata</code>, <code>PST</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p></td>
        <td style="vertical-align: top">System timezone</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:currentTimestamp()
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns current system time in <code>yyyy-MM-dd HH:mm:ss</code> format, such as <code>2019-03-31 14:07:00</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:currentTimestamp('Asia/Kolkata')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns current system time converted to 'Asia/Kolkata' timezone <code>yyyy-MM-dd HH:mm:ss</code> format, such as <code>2019-03-31 19:07:00</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:currentTimestamp('CST')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns current system time converted to 'CST' timezone <code>yyyy-MM-dd HH:mm:ss</code> format, such as <code>2019-03-31 02:07:00</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p>
<p></p>
### date *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the date part of a date or date-time and return it in <code>yyyy-MM-dd</code> format.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:date(<STRING> date.value, <STRING> date.format)
<STRING> time:date(<STRING> date.value)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided. For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:date('2014/11/11 13:23:44', 'yyyy/MM/dd HH:mm:ss')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the date and returns <code>2014-11-11</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:date('2014-11-23 13:23:44.345')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the date and returns <code>2014-11-13</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:date('13:23:44', 'HH:mm:ss')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the date and returns <code>1970-01-01</code>.</p>
<p></p>
### dateAdd *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Adds the specified time interval to a date.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:dateAdd(<STRING> date.value, <INT> expr, <STRING> unit)
<STRING> time:dateAdd(<LONG> timestamp.in.milliseconds, <INT> expr, <STRING> unit)
<STRING> time:dateAdd(<STRING> date.value, <INT> expr, <STRING> unit, <STRING> date.format)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">expr</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The amount by which the selected part of the date should be incremented. For example <code>2</code> ,<code>5 </code>,<code>10</code>, etc.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">unit</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">This is the part of the date that needs to be modified. For example, <code>MINUTE</code>, <code>HOUR</code>, <code>MONTH</code>, <code>YEAR</code>, <code>QUARTER</code>, <code>WEEK</code>, <code>DAY</code>, <code>SECOND</code>.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The date value in milliseconds. For example, <code>1415712224000L</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:dateAdd('2014-11-11 13:23:44.657', 5, 'YEAR', 'yyyy-MM-dd HH:mm:ss.SSS')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Adds five years to the given date value and returns <code>2019-11-11 13:23:44.657</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateAdd('2014-11-11 13:23:44.657', 5, 'YEAR')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Adds five years to the given date value and returns <code>2019-11-11 13:23:44.657</code> using the default date.format <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateAdd( 1415712224000L, 1, 'HOUR')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Adds one hour and <code>1415715824000</code> as a <code>string</code>.</p>
<p></p>
### dateDiff *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns difference between two dates in days.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<INT> time:dateDiff(<STRING> date.value1, <STRING> date.value2, <STRING> date.format1, <STRING> date.format2)
<INT> time:dateDiff(<STRING> date.value1, <STRING> date.value2)
<INT> time:dateDiff(<LONG> timestamp.in.milliseconds1, <LONG> timestamp.in.milliseconds2)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value1</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the first date parameter. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value2</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the second date parameter. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code> , <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format1</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the first date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format2</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the second date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds1</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The first date value in milliseconds from the epoch. For example, <code>1415712224000L</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds2</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The second date value in milliseconds from the epoch. For example, <code>1415712224000L</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:dateDiff('2014-11-11 13:23:44', '2014-11-9 13:23:44', 'yyyy-MM-dd HH:mm:ss', 'yyyy-MM-dd HH:mm:ss')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the date difference between the two given dates as <code>2</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateDiff('2014-11-13 13:23:44', '2014-11-9 13:23:44')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the date difference between the two given dates as <code>4</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateDiff(1415692424000L, 1412841224000L)
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the date difference between the two given dates as <code>33</code>.</p>
<p></p>
### dateFormat *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Formats the data in string or milliseconds format to the given date format.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:dateFormat(<STRING> date.value, <STRING> date.target.format, <STRING> date.source.format)
<STRING> time:dateFormat(<STRING> date.value, <STRING> date.target.format)
<STRING> time:dateFormat(<LONG> timestamp.in.milliseconds, <STRING> date.target.format)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.target.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date into which the date value needs to be converted. For example, <code>yyyy/MM/dd HH:mm:ss</code>.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.source.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format input date.value.For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The date value in milliseconds from the epoch. For example, <code>1415712224000L</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:dateFormat('2014/11/11 13:23:44', 'mm:ss', 'yyyy/MM/dd HH:mm:ss') 
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts date based on the target date format <code>mm:ss</code> and returns <code>23:44</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateFormat('2014-11-11 13:23:44', 'HH:mm:ss') 
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts date based on the target date format <code>HH:mm:ss</code> and returns <code>13:23:44</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateFormat(1415692424000L, 'yyyy-MM-dd') 
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts date in millisecond based on the target date format <code>yyyy-MM-dd</code> and returns <code>2014-11-11</code>.</p>
<p></p>
### dateSub *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Subtracts the specified time interval from the given date.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:dateSub(<STRING> date.value, <INT> expr, <STRING> unit)
<STRING> time:dateSub(<STRING> date.value, <INT> expr, <STRING> unit, <STRING> date.format)
<STRING> time:dateSub(<LONG> timestamp.in.milliseconds, <INT> expr, <STRING> unit)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">expr</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The amount by which the selected part of the date should be decremented. For example <code>2</code> ,<code>5 </code>,<code>10</code>, etc.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">unit</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">This is the part of the date that needs to be modified. For example, <code>MINUTE</code>, <code>HOUR</code>, <code>MONTH</code>, <code>YEAR</code>, <code>QUARTER</code>, <code>WEEK</code>, <code>DAY</code>, <code>SECOND</code>.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The date value in milliseconds. For example, <code>1415712224000L</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:dateSub('2019-11-11 13:23:44.657', 5, 'YEAR', 'yyyy-MM-dd HH:mm:ss.SSS')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Subtracts five years to the given date value and returns <code>2014-11-11 13:23:44.657</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateSub('2019-11-11 13:23:44.657', 5, 'YEAR')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Subtracts five years to the given date value and returns <code>2014-11-11 13:23:44.657</code> using the default date.format <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateSub( 1415715824000L, 1, 'HOUR')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Subtracts one hour and <code>1415712224000</code> as a <code>string</code>.</p>
<p></p>
### dayOfWeek *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the day on which a given date falls.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:dayOfWeek(<STRING> date.value, <STRING> date.format)
<STRING> time:dayOfWeek(<STRING> date.value)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided. For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:date('2014/12/11 13:23:44', 'yyyy/MM/dd HH:mm:ss')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the date and returns <code>Thursday</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:date('2014-11-11 13:23:44.345')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the date and returns <code>Tuesday</code>.</p>
<p></p>
### extract *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Function extracts a date unit from the date.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<INT> time:extract(<STRING> unit, <STRING> date.value)
<INT> time:extract(<STRING> unit, <STRING> date.value, <STRING> date.format)
<INT> time:extract(<STRING> unit, <STRING> date.value, <STRING> date.format, <STRING> locale)
<INT> time:extract(<LONG> timestamp.in.milliseconds, <STRING> unit)
<INT> time:extract(<LONG> timestamp.in.milliseconds, <STRING> unit, <STRING> locale)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">unit</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">This is the part of the date that needs to be modified. For example, <code>MINUTE</code>, <code>HOUR</code>, <code>MONTH</code>, <code>YEAR</code>, <code>QUARTER</code>, <code>WEEK</code>, <code>DAY</code>, <code>SECOND</code>.</p></td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The date value in milliseconds. For example, <code>1415712224000L</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">locale</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">Represents a specific geographical, political or cultural region. For example <code>en_US</code> and <code>fr_FR</code></p></td>
        <td style="vertical-align: top">Current default locale set in the Java Virtual Machine.</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">No</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:extract('YEAR', '2019/11/11 13:23:44.657', 'yyyy/MM/dd HH:mm:ss.SSS')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the year amount and returns <code>2019</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:extract('DAY', '2019-11-12 13:23:44.657')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the day amount and returns <code>12</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:extract(1394556804000L, 'HOUR')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Extracts the hour amount and returns <code>22</code>.</p>
<p></p>
### timestampInMilliseconds *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the system time or the given time in milliseconds.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<LONG> time:timestampInMilliseconds()
<LONG> time:timestampInMilliseconds(<STRING> date.value, <STRING> date.format)
<LONG> time:timestampInMilliseconds(<STRING> date.value)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</p></td>
        <td style="vertical-align: top">Current system time</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided. For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:timestampInMilliseconds()
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the system current time in milliseconds.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:timestampInMilliseconds('2007-11-30 10:30:19', 'yyyy-MM-DD HH:MM:SS')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts <code>2007-11-30 10:30:19</code> in <code>yyyy-MM-DD HH:MM:SS</code> format to  milliseconds as <code>1170131400019</code>.</p>
<p></p>
<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:timestampInMilliseconds('2007-11-30 10:30:19.000')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts <code>2007-11-30 10:30:19</code> in <code>yyyy-MM-DD HH:MM:ss.SSS</code> format to  milliseconds as <code>1196398819000</code>.</p>
<p></p>
### timezoneConvert *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts source datetime to provided target timezone and return the datetime string</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:timezoneConvert(<STRING> date.value, <STRING> date.source.format, <STRING> target.timezone)
<STRING> time:timezoneConvert(<STRING> date.value, <STRING> date.source.format, <STRING> target.timezone, <STRING> source.timezone)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The value of the date with time. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11 05:23:44</code>, <code>2014/11/11 24:10:44.657</code>.</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.source.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format input date.value.For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>. This is mandatory if you want to convert to different timezone</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">target.timezone</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The timezone to which the target date need to be converted. For example, <code>Asia/Kolkata</code>, <code>PST</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p></td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">source.timezone</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The timezone to which the source time points to. For example, <code>Asia/Kolkata</code>, <code>PST</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p></td>
        <td style="vertical-align: top">System timezone</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:timezoneConvert('2014/11/11 13:23:44','yyyy/MM/dd HH:mm:ss','UTC', 'IST') 
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts datetime based on the target timezone <code>UTC</code> considering given source timizone <code>IST</code> and returns <code>2014/11/11 07:53:44</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:timezoneConvert('2020-11-11 06:23:44', 'yyyy/MM/dd HH:mm:ss', 'CST') 
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Converts datetime based on the target timezone <code>CST</code> and since source timezone is not given it will take system timezone as default and returns <code>2020-11-11 00:53:44</code>.</p>
<p></p>
### utcTimestamp *<a target="_blank" href="http://siddhi.io/en/v5.1/docs/query-guide/#function">(Function)</a>*
<p></p>
<p style="word-wrap: break-word;margin: 0;">Function returns the system current time in UTC timezone with given date format which defaults to <code>yyyy-MM-dd HH:mm:ss</code> if not provided.</p>
<p></p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:utcTimestamp()
<STRING> time:utcTimestamp(<STRING> date.format)
```

<span id="query-parameters" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">QUERY PARAMETERS</span>
<table>
    <tr>
        <th>Name</th>
        <th style="min-width: 20em">Description</th>
        <th>Default Value</th>
        <th>Possible Data Types</th>
        <th>Optional</th>
        <th>Dynamic</th>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word"><p style="word-wrap: break-word;margin: 0;">The format of the date value provided.This date format can be anything which complies to SimpleDateFormat compatible patterns.For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</p></td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:utcTimestamp()
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the system current time in UTC timezone with <code>yyyy-MM-dd HH:mm:ss</code> format, and a sample output will be like <code>2019-07-03 09:58:34</code>.</p>
<p></p>
<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:utcTimestamp('yyyy-MM-DD HH:MM:ss.SSS')
```
<p></p>
<p style="word-wrap: break-word;margin: 0;">Returns the system current time in UTC timezone with <code>yyyy-MM-dd HH:mm:ss.SSS</code> format, and a sample output will be like <code>2020-07-15 10:49:41.103</code>.</p>
<p></p>
