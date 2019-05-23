# IpBatch
根据业务需求查询ip对应省市code与城市type
有基础构建：
1.基于大佬的 ip2region 项目进行开发
2.处理模块大致有以下几个：
  2.1 excel模块，将省市信息加载进内存进行city相关的信息查询 主要是查询到省code,城市code与name
  2.2 UA解析模块,通过处理UA(http中的user-agent) 在微信中发送的请求中，包含了ip信息 以及手机型号(手机厂商),手机信号,手机系统信息(暂不需要),浏览器信息(暂不需要)
  2.3 ip查询业务 详情可以在github上查询 ip2region 项目
  
