package com.itheima.serach;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.net.InetAddress;

/**
 * 获取连接对象
 */
public class serach {

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


    //搜索
    @Test
    public void searchdemo()throws Exception{
        //1.获取链接对象
        TransportClient transportClient = getClient();
        //2.创建搜索条件
       // QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        //3.根据字符串搜索
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("我想看书").defaultField("context");

        transportClient.close();
    }



}
