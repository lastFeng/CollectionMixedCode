### SpringBoot整合Elasticsearch的简单示例

#### ES配置
   - spring.data.elasticsearch.cluster-name Elasticsearch 集群名。(默认值: elasticsearch)
   - spring.data.elasticsearch.cluster-nodes 集群节点地址列表，用逗号分隔。如果没有指定，就启动一个客户端节点。
   - spring.data.elasticsearch.propertie 用来配置客户端的额外属性。
   - spring.data.elasticsearch.repositories.enabled 开启 Elasticsearch 仓库。(默认值:true。)


#### ES基础术语     ---   类比数据库
   - 索引Index      ---   数据库
   - 类型Type       ---   表
   - 文档Document   ---   行
   - 字段Fields     ---   字段

 解释：

   1. 文档：面向对象观念就是一个对象。文档的位置由_index、_type、_id唯一标识，在ES里面，就是一个大JSON对象，是指定了唯一ID的最底层或根对象
   2. 类型：用户区分索引中的文档，即在索引中对数据逻辑分区。
   3. 索引：用于区分文档成组。