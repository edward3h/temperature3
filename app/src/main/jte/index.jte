@import java.util.List
@import org.ethelred.temperature3.Temperature
@import org.ethelred.temperature3.TemperatureReading
@import org.ethelred.temperature3.Formatting

@param Formatting formatting
@param List<TemperatureReading> readings

<html>
<head>
    <title>Temperatures</title>
</head>
<body>
<h1>Temperatures</h1>
@if (readings.isEmpty())
<div>No readings.</div>
@else
<table>
    <tr><th>Time</th><th>Temperature</th><th>Name</th></tr>
    @for (var reading: readings)
        <tr>
            <td>${formatting.formatDateTime(reading.recordedAt())}</td>
            <td>${formatting.formatTemperature(reading.temperature(), Temperature.Unit.FAHRENHEIT)}</td>
            <td>${reading.sourceName()}</td></tr>
    @endfor
</table>
@endif
</body>
</html>