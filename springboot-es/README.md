### SpringBoot整合Elasticsearch的简单示例

#### ES配置
   - spring.data.elasticsearch.cluster-name Elasticsearch 集群名。(默认值: elasticsearch)
   - spring.data.elasticsearch.cluster-nodes 集群节点地址列表，用逗号分隔。如果没有指定，就启动一个客户端节点。
   - spring.data.elasticsearch.propertie 用来配置客户端的额外属性。
   - spring.data.elasticsearch.repositories.enabled 开启 Elasticsearch 仓库。(默认值:true。)