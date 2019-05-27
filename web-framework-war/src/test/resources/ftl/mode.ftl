hello ${name}
hi ${abc!}<#-- 不存在或为空不输出 -->
${def!'12312'}<#-- 不存在或为空输出'12312' -->
${(animals.python.price)!0}<#-- 输出数字0 -->
<#if (animals.python.price)??><#-- 判断是否存在 -->
  animals.python.price
<#else>
  not found
</#if>

<#if "world"="${name}">hello world!</#if>
