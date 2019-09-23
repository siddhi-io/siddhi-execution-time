# API Docs - v5.0.3

!!! Info "Tested Siddhi Core version: *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/">5.0.2</a>*"
    It could also support other Siddhi Core minor versions.

## Time

### currentDate *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Function returns the system time in <code>yyyy-MM-dd</code> format.</p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:currentDate()
```

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:currentDate()
```
<p style="word-wrap: break-word">Returns the current date in the <code>yyyy-MM-dd</code> format, such as <code>2019-06-21</code>.</p>

### currentTime *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Function returns system time in the <code>HH:mm:ss</code> format.</p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:currentTime()
```

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:currentTime()
```
<p style="word-wrap: break-word">Returns the current date in the <code>HH:mm:ss</code> format, such as <code>15:23:24</code>.</p>

### currentTimestamp *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">When no argument is provided, function returns the system current timestamp in <code>yyyy-MM-dd HH:mm:ss</code> format, and when a timezone is provided as an argument, it converts and return the current system time to the given timezone format.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The timezone to which the current time need to be converted. For example, <code>Asia/Kolkata</code>, <code>PST</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</td>
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
<p style="word-wrap: break-word">Returns current system time in <code>yyyy-MM-dd HH:mm:ss</code> format, such as <code>2019-03-31 14:07:00</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:currentTimestamp('Asia/Kolkata')
```
<p style="word-wrap: break-word">Returns current system time converted to 'Asia/Kolkata' timezone <code>yyyy-MM-dd HH:mm:ss</code> format, such as <code>2019-03-31 19:07:00</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:currentTimestamp('CST')
```
<p style="word-wrap: break-word">Returns current system time converted to 'CST' timezone <code>yyyy-MM-dd HH:mm:ss</code> format, such as <code>2019-03-31 02:07:00</code>. Get the supported timezone IDs from [here](https://docs.oracle.com/javase/8/docs/api/java/time/ZoneId.html)</p>

### date *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Extracts the date part of a date or date-time and return it in <code>yyyy-MM-dd</code> format.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date value provided. For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</td>
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
<p style="word-wrap: break-word">Extracts the date and returns <code>2014-11-11</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:date('2014-11-23 13:23:44.345')
```
<p style="word-wrap: break-word">Extracts the date and returns <code>2014-11-13</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:date('13:23:44', 'HH:mm:ss')
```
<p style="word-wrap: break-word">Extracts the date and returns <code>1970-01-01</code>.</p>

### dateAdd *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Adds the specified time interval to a date.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">expr</td>
        <td style="vertical-align: top; word-wrap: break-word">The amount by which the selected part of the date should be incremented. For example <code>2</code> ,<code>5 </code>,<code>10</code>, etc.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">unit</td>
        <td style="vertical-align: top; word-wrap: break-word">This is the part of the date that needs to be modified. For example, <code>MINUTE</code>, <code>HOUR</code>, <code>MONTH</code>, <code>YEAR</code>, <code>QUARTER</code>, <code>WEEK</code>, <code>DAY</code>, <code>SECOND</code>.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word">The date value in milliseconds. For example, <code>1415712224000L</code>.</td>
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
<p style="word-wrap: break-word">Adds five years to the given date value and returns <code>2019-11-11 13:23:44.657</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateAdd('2014-11-11 13:23:44.657', 5, 'YEAR')
```
<p style="word-wrap: break-word">Adds five years to the given date value and returns <code>2019-11-11 13:23:44.657</code> using the default date.format <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateAdd( 1415712224000L, 1, 'HOUR')
```
<p style="word-wrap: break-word">Adds one hour and <code>1415715824000</code> as a <code>string</code>.</p>

### dateDiff *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Returns difference between two dates in days.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the first date parameter. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value2</td>
        <td style="vertical-align: top; word-wrap: break-word">The value of the second date parameter. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code> , <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format1</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the first date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format2</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the second date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds1</td>
        <td style="vertical-align: top; word-wrap: break-word">The first date value in milliseconds from the epoch. For example, <code>1415712224000L</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds2</td>
        <td style="vertical-align: top; word-wrap: break-word">The second date value in milliseconds from the epoch. For example, <code>1415712224000L</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:dateDiff('2014-11-11 13:23:44', '2014-11-9 13:23:44', 'yyyy-MM-dd HH:mm:ss', c'yyyy-MM-dd HH:mm:ss')
```
<p style="word-wrap: break-word">Returns the date difference between the two given dates as <code>2</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateDiff('2014-11-13 13:23:44', '2014-11-9 13:23:44')
```
<p style="word-wrap: break-word">Returns the date difference between the two given dates as <code>4</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateDiff(1415692424000L, 1412841224000L)
```
<p style="word-wrap: break-word">Returns the date difference between the two given dates as <code>33</code>.</p>

### dateFormat *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Formats the data in string or milliseconds format to the given date format.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.target.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date into which the date value needs to be converted. For example, <code>yyyy/MM/dd HH:mm:ss</code>.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.source.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format input date.value.For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word">The date value in milliseconds from the epoch. For example, <code>1415712224000L</code>.</td>
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
<p style="word-wrap: break-word">Converts date based on the target date format <code>mm:ss</code> and returns <code>23:44</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateFormat('2014-11-11 13:23:44', 'HH:mm:ss') 
```
<p style="word-wrap: break-word">Converts date based on the target date format <code>HH:mm:ss</code> and returns <code>13:23:44</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateFormat(1415692424000L, 'yyyy-MM-dd') 
```
<p style="word-wrap: break-word">Converts date in millisecond based on the target date format <code>yyyy-MM-dd</code> and returns <code>2014-11-11</code>.</p>

### dateSub *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Subtracts the specified time interval from the given date.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">expr</td>
        <td style="vertical-align: top; word-wrap: break-word">The amount by which the selected part of the date should be decremented. For example <code>2</code> ,<code>5 </code>,<code>10</code>, etc.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">INT</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">unit</td>
        <td style="vertical-align: top; word-wrap: break-word">This is the part of the date that needs to be modified. For example, <code>MINUTE</code>, <code>HOUR</code>, <code>MONTH</code>, <code>YEAR</code>, <code>QUARTER</code>, <code>WEEK</code>, <code>DAY</code>, <code>SECOND</code>.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word">The date value in milliseconds. For example, <code>1415712224000L</code>.</td>
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
<p style="word-wrap: break-word">Subtracts five years to the given date value and returns <code>2014-11-11 13:23:44.657</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:dateSub('2019-11-11 13:23:44.657', 5, 'YEAR')
```
<p style="word-wrap: break-word">Subtracts five years to the given date value and returns <code>2014-11-11 13:23:44.657</code> using the default date.format <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:dateSub( 1415715824000L, 1, 'HOUR')
```
<p style="word-wrap: break-word">Subtracts one hour and <code>1415712224000</code> as a <code>string</code>.</p>

### dayOfWeek *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Extracts the day on which a given date falls.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date value provided. For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</td>
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
<p style="word-wrap: break-word">Extracts the date and returns <code>Thursday</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:date('2014-11-11 13:23:44.345')
```
<p style="word-wrap: break-word">Extracts the date and returns <code>Tuesday</code>.</p>

### extract *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Function extracts a date unit from the date.</p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<INT> time:extract(<STRING> unit, <STRING> date.value, <STRING> date.format)
<INT> time:extract(<STRING> unit, <STRING> date.value)
<INT> time:extract(<LONG> timestamp.in.milliseconds, <STRING> unit)
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
        <td style="vertical-align: top; word-wrap: break-word">This is the part of the date that needs to be modified. For example, <code>MINUTE</code>, <code>HOUR</code>, <code>MONTH</code>, <code>YEAR</code>, <code>QUARTER</code>, <code>WEEK</code>, <code>DAY</code>, <code>SECOND</code>.</td>
        <td style="vertical-align: top"></td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">No</td>
        <td style="vertical-align: top">No</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.value</td>
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date value provided. For example, <code>yyyy-MM-dd HH:mm:ss.SSS</code>.</td>
        <td style="vertical-align: top">`yyyy-MM-dd HH:mm:ss.SSS`</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">timestamp.in.milliseconds</td>
        <td style="vertical-align: top; word-wrap: break-word">The date value in milliseconds. For example, <code>1415712224000L</code>.</td>
        <td style="vertical-align: top">-</td>
        <td style="vertical-align: top">LONG</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
</table>

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:extract('YEAR', '2019/11/11 13:23:44.657', 'yyyy/MM/dd HH:mm:ss.SSS')
```
<p style="word-wrap: break-word">Extracts the year amount and returns <code>2019</code>.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:extract('DAY', '2019-11-12 13:23:44.657')
```
<p style="word-wrap: break-word">Extracts the day amount and returns <code>12</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:extract(1394556804000L, 'HOUR')
```
<p style="word-wrap: break-word">Extracts the hour amount and returns <code>22</code>.</p>

### timestampInMilliseconds *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Returns the system time or the given time in milliseconds.</p>
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
        <td style="vertical-align: top; word-wrap: break-word">The value of the date. For example, <code>2014-11-11 13:23:44.657</code>, <code>2014-11-11</code>, <code>13:23:44.657</code>.</td>
        <td style="vertical-align: top">Current system time</td>
        <td style="vertical-align: top">STRING</td>
        <td style="vertical-align: top">Yes</td>
        <td style="vertical-align: top">Yes</td>
    </tr>
    <tr>
        <td style="vertical-align: top">date.format</td>
        <td style="vertical-align: top; word-wrap: break-word">The format of the date value provided. For example, <code>yyyy/MM/dd HH:mm:ss.SSS</code>.</td>
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
<p style="word-wrap: break-word">Returns the system current time in milliseconds.</p>

<span id="example-2" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 2</span>
```
time:timestampInMilliseconds('2007-11-30 10:30:19', 'yyyy-MM-DD HH:MM:SS')
```
<p style="word-wrap: break-word">Converts <code>2007-11-30 10:30:19</code> in <code>yyyy-MM-DD HH:MM:SS</code> format to  milliseconds as <code>1170131400019</code>.</p>

<span id="example-3" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 3</span>
```
time:timestampInMilliseconds('2007-11-30 10:30:19.000')
```
<p style="word-wrap: break-word">Converts <code>2007-11-30 10:30:19</code> in <code>yyyy-MM-DD HH:MM:ss.SSS</code> format to  milliseconds as <code>1196398819000</code>.</p>

### utcTimestamp *<a target="_blank" href="http://siddhi.io/en/v5.0/docs/query-guide/#function">(Function)</a>*
<p style="word-wrap: break-word">Function returns the system current time in UTC timezone with <code>yyyy-MM-dd HH:mm:ss</code> format.</p>
<span id="syntax" class="md-typeset" style="display: block; font-weight: bold;">Syntax</span>

```
<STRING> time:utcTimestamp()
```

<span id="examples" class="md-typeset" style="display: block; font-weight: bold;">Examples</span>
<span id="example-1" class="md-typeset" style="display: block; color: rgba(0, 0, 0, 0.54); font-size: 12.8px; font-weight: bold;">EXAMPLE 1</span>
```
time:utcTimestamp()
```
<p style="word-wrap: break-word">Returns the system current time in UTC timezone with <code>yyyy-MM-dd HH:mm:ss</code> format, and a sample output will be like <code>2019-07-03 09:58:34</code>.</p>

