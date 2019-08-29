package com.itheima.index;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.index.pojo.Blog;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class index {


    /**
     * 获取连接对象
     */
    public static TransportClient getClient() throws Exception {
        //1：配置es信息
        Settings settings = Settings.EMPTY;
        //2.改集群名
        Settings settings1 = Settings.builder().put("cluster.name","cluster-es").build();
        //3.创建客户端对象
        TransportClient transportClient = new PreBuiltTransportClient(settings);
        //4.指定服务器的地址和ip
        transportClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"),9300));
        //5.返回
        return transportClient;
    }

    @Test
    public void testClient() throws  Exception{
        System.out.println(getClient());
    }

    /**
     * 创建索引库
     */
    @Test
    public void createIndexResp() throws Exception{
        // 1 :获取链接对象
        TransportClient transportClient = getClient();
        // 2: 创建索引库 http://localhost:9200/blog---put请求---no data----springmvc
        CreateIndexResponse response = transportClient.admin().indices().prepareCreate("blog").get();
        System.out.println(response.isShardsAcked());
        System.out.println(response.isShardsAcked());
        System.out.println(response.index());
        // 3:释放资源
        transportClient.close();
    }

    /**
     * 添加索引数据
     */
    @Test
    public void createIndexDocument() throws Exception{
    // 1 :获取链接对象
    TransportClient transportClient = getClient();
    // 2: 创建索引  {"id":1,"title":"sdfsdfsd"} --
    XContentBuilder builder = XContentFactory.jsonBuilder()
               .startObject()
               .field("id",3)
               .field("title","elasticsearch是一个基于lucene的搜索服务")
               .field("content","ElasticSearch是一个基于Lucene的搜索服务器。" +
                              "它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。" +
                              "Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，" +
                              "是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，" +
                              "可靠，快速，安装使用方便。")
               .endObject();
    // 3.写入json对象 es数据库中
        IndexResponse indexResponse = transportClient.prepareIndex("blog", "content")
                .setSource(builder)
               .get();
        System.out.println(indexResponse.status());
    // 4:释放资源
        transportClient.close();
    }

    /**
     * 基于实体类的方式创建文档数据
     */
    @Test
    public void createPojo() throws Exception{
        // 1 :获取链接对象
        TransportClient transportClient = getClient();
        // 2: 创建索引
        Blog blog = new Blog();
        blog.setId(3L);
        blog.setPic("aaa.jpg");
        blog.setPrice(33.3f);
        blog.setTitle("es使用Java开发并使用Lucene作为其核心来实\n" +
                "现所有索引和搜索的功能");
        blog.setContent("solr是一个基于Lucene的搜索服务器。\" +\n" +
                "                       \"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。\" +\n" +
                "                       \"Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，\" +\n" +
                "                       \"是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，\" +\n" +
                "                       \"可靠，快速，安装使用方便。");
        // 3.转换成json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(blog);
        IndexResponse indexResponse = transportClient.prepareIndex("blog","context",blog.getId()+"")
                .setSource(json, XContentType.JSON)
                .get();
        System.out.println(indexResponse.status());
        // 4:释放资源
        transportClient.close();
    }
    /**
     * 修改
     */

    @Test
    public void updatePojo()throws Exception{
        //1.获取链接对象
        TransportClient transportClient = getClient();
        //2.创建索引
        Blog blog = new Blog();
        blog.setId(2L);
        blog.setPic("www.tet");
        blog.setPrice(66.6f);
        blog.setTitle("ES修改内容");
        blog.setContent("链接：https://pan.baidu.com/s/1j3XdYyiN2xD8a-_-umKKNA \n" +
                "提取码：tlrc \n" +
                "复制这段内容后打开百度网盘手机App，操作更方便哦");
        //3.转换成json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(blog);
        IndexResponse indexResponse = transportClient.prepareIndex("blog","context",blog.getId()+"")
                .setSource(json,XContentType.JSON)
                .get();
        System.out.println(indexResponse.status());
        //4.释放资源
        transportClient.close();
    }

    /**
     *  删除
     */
    @Test
    public void deletePojo()throws Exception{
        //1.创建链接
        TransportClient transportClient = getClient();
        //2.根据id删除
        DeleteResponse deleteResponse = transportClient.prepareDelete("blog","context","2").get();
        System.out.println(deleteResponse.status());
        //3.释放资源
        transportClient.close();
    }

}
