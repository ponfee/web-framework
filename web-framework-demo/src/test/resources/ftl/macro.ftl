<#macro greet person="alice">
  <font size="+2">Hello ${person}!</font>
</#macro>

<@greet />
<@greet person="tom"/>
<@greet person="${hello}"/>
<@greet person=hello/>
