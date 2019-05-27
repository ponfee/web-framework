${"Hello ${hello}!"}
${"Hello " + hello + "!"}
${hello + hello + hello + hello}
${hello[1]}
${hello?starts_with("hello")?c}

${"It's \"quoted\" and
this is a backslash: \\"}

${'It\'s "quoted" and
this is a backslash: \\'}

${"\x0A9 2010-2017"}

${r"${foo}"} | ${r"C:\foo\bar"}

<#-- 序列 -->
${["winter", "spring", "summer", "autumn"][0]}
<#list ["winter", "spring", "summer", "autumn"] as x>${x} </#list>
<#list ["a", "b", "c", "d"][1..3]+["e","f"] as x>${x} </#list>

${person["name"]}

|${name!}|<#-- 模板语言中没有 null 这个概念，如果默认值被省略了，那么结果将会是空串 -->
|${name!"default"}|
<#if !(exists??)>not exists!</#if>


<#assign ages = {"Joe":23, "Fred":25} + {"Joe":30, "Julia":18}>
- Joe is ${ages.Joe}
- Fred is ${ages.Fred}
- Julia is ${ages.Julia}

${-1.999?int}

${(1=2)?c}
${(1&lt;2)?c}
${(1 lt 2)?c}