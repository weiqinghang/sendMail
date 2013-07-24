package com.mongostat.mail

import com.mongodb.BasicDBObject
import com.mongodb.DBCollection
import com.mongodb.Mongo

/**
 * Created with IntelliJ IDEA.
 * User: weicc
 * Date: 13-7-22
 * Time: PM8:28
 * To change this template use File | Settings | File Templates.
 */

def config = Config.config

def sendMail = { subject, content ->
    CommonMail cm = new CommonMail(config.mail)

    cm.setSubject(subject)
    cm.setContent(content)

    cm.sendSimpleMail()
}

def mongoConfig = config.mongodb
def mongoConn = new Mongo(mongoConfig.host, mongoConfig.port)
def dbOrigin = mongoConn.getDB(mongoConfig.dbOrigin)
def collections = { collName ->
    dbOrigin.getCollection(collName)
}
DBCollection.metaClass.find = {conditions = null, fields = null ->
    def cond = conditions as BasicDBObject
    if(!conditions) find()
    else if(fields) find(cond, fields)
    else find(cond)
}
def productCol = collections('product'), commentCol = collections('comment'), salesCol = collections('productSales')
def dateFormatter = config.date.formatter, today = Date.parse(dateFormatter, new Date().format(dateFormatter))
def startDate = today - 1, endDate = today
def dailyCond = [crawlTime: [$gte: startDate, $lt: endDate]]
def dailyUpdatedProducts = productCol.find(dailyCond)

def reporter = [:]

//get product-updated info
def newProducts = [], updatedProducts = []
dailyUpdatedProducts.each {
    if(it._id.getTime() > (today - 1).getTime()) {
        newProducts << it
    } else {
        updatedProducts << it
    }
}

def newComments = commentCol.find(dailyCond)

def newSales = salesCol.find(dailyCond)

def dbData = [
            newProductCount: newProducts,
            updatedProductCount: updatedProducts,
            newCommentCount: newComments,
            newSaleCount: newSales
        ]
dbData.each {key, resultSet ->
    resultSet.each {cmtORprd ->
        if(!reporter[cmtORprd.shop]) reporter[cmtORprd.shop] = [:]
        def sbMap = reporter[cmtORprd.shop][cmtORprd.brand]
        if(!sbMap) sbMap = [:]
        if(sbMap[key]) {
            sbMap[key] += 1
        } else {
            sbMap[key] = 1
        }
        if(!sbMap['shop'])
            sbMap['shop'] = cmtORprd.shop
        if(!sbMap['brand'])
            sbMap['brand'] = cmtORprd.brand
    }
}

def dataList = [], shopSet = [] as Set, brandSet = [] as Set
reporter.each { shop, brandMap ->
    shopSet << shop
    brandMap.each {brand, map ->
        brandSet << brand
        dataList << reporter[shop][brand]
    }
}

def columns = ['电商', '品牌', '新增商品数', '新增评论数', '更新商品数', '销量信息抓取数', '备注']
def fields = ['shop', 'brand', 'newProductCount', 'newCommentCount', 'updatedProductCount', 'newSaleCount', 'remark']

def tb = new DrawTable(columns, fields)
def subject = "数据更新报告"

def meta = """
Dear all,

以下是${startDate.format('yyyy年MM月dd日0时')}~${endDate.format('yyyy年MM月dd日0时')}的数据抓取情况汇总。
共有${shopSet.size()}个电商，${brandSet.size()}个品牌被更新。
"""

def tableData = tb.getTableHtml(dataList)
def content = meta + tableData
sendMail(subject, content)
