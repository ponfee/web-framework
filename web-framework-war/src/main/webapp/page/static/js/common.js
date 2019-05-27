String.prototype.startWith = function(str) {
    return new RegExp("^" + str).test(this);
};
String.prototype.endWith = function(str) {
    return new RegExp(str + "$").test(this);
};
String.prototype.isNull = function() {
    return this == undefined || this == null;
};
String.prototype.isEmpty = function() {
    return /^\s*$/.test(this);
};
String.prototype.trim=function() {
    return this.replace(/^[\r\n\s\t]*|[\r\n\s\t]*$/g, "");
};
String.prototype.equals=function(s) {
  return this==s;
};
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
    if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
        return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi" : "g")), replaceWith);
    } else {
        return this.replace(reallyDo, replaceWith);
    }
};
String.prototype.toHex=function (){
    var val = "";
    for ( var i = 0; i < this.length; i++) {
        val += this.charCodeAt(i).toString(16);
    }
    return val;
}
String.prototype.format = function(args) {
    if (arguments.length < 1) return this;
    var result = this;
    if (arguments.length == 1 && typeof args === "object" 
        && Object.prototype.toString.apply(args) !== "[object Array]") {
        for (var key in args) {
            var reg = new RegExp("({:" + key + "})", "g");
            result = result.replace(reg, args[key]);
        }
    } else {
        if (arguments.length > 1) args = Array.prototype.slice.call(arguments); // arguments并非真正的数组，所以要转换
        else if (arguments.length == 1 && Object.prototype.toString.apply(args) !== "[object Array]") args = [args];
        for (var i = 0; i < args.length; i++) {
            var reg= new RegExp("({:" + i + "})", "g");
            result = result.replace(reg, args[i]);
        }
    }
    return result;
};

Array.prototype.contains = function(item) {
    return RegExp("(^|,)" + item.toString() + "($|,)").test(this);
};


Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1, //月份           
        "d+" : this.getDate(), //日           
        "h+" : this.getHours()%12 == 0 ? 12 : this.getHours()%12, //小时           
        "H+" : this.getHours(), //小时           
        "m+" : this.getMinutes(), //分           
        "s+" : this.getSeconds(), //秒           
        "q+" : Math.floor((this.getMonth()+3)/3), //季度           
        "S" : this.getMilliseconds() //毫秒           
    };           
    var week = {
        "0" : "\\u65e5",           
        "1" : "\\u4e00",           
        "2" : "\\u4e8c",           
        "3" : "\\u4e09",           
        "4" : "\\u56db",           
        "5" : "\\u4e94",           
        "6" : "\\u516d"          
    };           
    if(/(y+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));           
    }           
    if(/(E+)/.test(fmt)){
        fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "\\u661f\\u671f" : "\\u5468") : "")+week[this.getDay()+""]);           
    }           
    for(var k in o){
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));           
        }
    }
    return   eval("'" + fmt + "'");
}         

// 千分位
function commafy(num) {
    num = num + '';
    var reg = /(-?\d+)(\d{3})/;
    if(reg.test(num)){
      num = num.replace(reg, '$1,$2');
    }
    return num;
}

/**
 * window.open()封装
 * @param {Object} url
 * @param {Object} target
 * @param {Object} winname
 * @param {Object} params
 */
function openWin(url, method, params, target, winname) {
    var tempForm = document.createElement("form");
    var time = new Date().getTime();
    tempForm.id = "form_"+time;
    tempForm.action = url;
    tempForm.method = method || 'post';
    tempForm.target = target || '_blank';
    winname = winname || 'win_'+time;
    params = params || {};
    var hideInput = null;
    for ( var name in params) {
        hideInput = document.createElement("input");
        hideInput.type = "hidden";
        hideInput.name = name;
        hideInput.value = params[name];
        tempForm.appendChild(hideInput);
    }
    document.body.appendChild(tempForm);
    tempForm.submit();
    document.body.removeChild(tempForm);
}

/**
 * 阻止事件冒泡
 * @param {Object} evt
 */
function preventEvent(evt) {
    evt = evt ? evt : (window.event ? window.event : null);
    if (window.event) {
        evt.returnValue = false;
        evt.cancelBubble = true;
    } else {
        evt.preventDefault();
        evt.stopPropagation();
    }
    return evt;
}

/**
 * 说明：本代码可自由复制修改并且使用，但请保留作者信息！
 * Author: Kevin  WebSite: http://iulog.com/  QQ:251378427
 * JS 操作 URL 函数使用说明：
 * 初始化 var myurl=new UrlHandler(); //也可以自定义URL： var myurl=new UrlHandler('http://iulog.com/?sort=js'); 
 * 读取url参数值 var val=myurl.get('abc'); // 读取参数abc的值
 * 设置url参数 myurl.set("arg",data); // 新增/修改 一个arg参数的值为data
 * 移除url参数 myurl.remove("arg"); //移除arg参数
 * 获取处理后的URL myurl.url();//一般就直接执行转跳 location.href=myurl.url();
 * 调试接口：myurl.debug(); //修改该函数进行调试
 * @param {Object} url
 * @memberOf {TypeName} 
 * @return {TypeName} 
 */
function UrlHandler(url){
    var ourl=(url||window.location.href).toString();
    var href="";//?前面部分
    var params={};//url参数对象
    var jing="";//#及后面部分
    var init=function(){
        var str=ourl;
        var index=str.indexOf("#", 0);
        if(index>0){
            jing=str.substr(index);
            str=str.substring(0,index);
        }
        index=str.indexOf("?");
        if(index>0){
            href=str.substring(0,index);
            str=str.substr(index+1);
            var parts=str.split("&");
            for(var i=0;i<parts.length;i++){
                var kv=parts[i].split("=");
                params[kv[0]]=kv[1];
            }
        }else{
            href=ourl;
            params={};
        }
    };
    this.set=function(key,val){
        params[key]=encodeURIComponent(val);
    };
    this.remove=function(key){
        if(key in params) params[key]=undefined;
    };
    this.get=function(key){
        return params[key];
    };
    this.url=function(key){
        var strurl=href;
        var objps=[];
        for(var k in params){
            if(params[k]){
                objps.push(k+"="+params[k]);
            }
        }
        if(objps.length>0){
            strurl+="?"+objps.join("&");
        }
        if(jing.length>0){
            strurl+=jing;
        }
        return strurl;
    };
    this.debug=function(){
        // 以下调试代码自由设置
        var objps=[];
        for(var k in params){
            objps.push(k+"="+params[k]);
        }
        alert(objps);//输出params的所有值
    };
    init();
}
