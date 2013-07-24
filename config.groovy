
/**
 * Created with IntelliJ IDEA.
 * User: weicc
 * Date: 13-7-22
 * Time: PM9:05
 * To change this template use File | Settings | File Templates.
 */

mongodb {
    host = '172.16.141.167'
    port = 27001
    dbOrigin = 'eccrawler300'
    dbFactory = 'eccrawlerFactory'
}

date {
    formatter = 'yyyy-MM-dd'
}

mail {
    host = 'smtp.exmail.qq.com'
    port = 25
    senderMail = 'weiqinghang@admaster.com.cn'
    senderPass = '123qwe!!'
    receiverList = ['weiqinghang@admaster.com.cn']
//    receiverList2 = [
//            'weiqinghang@admaster.com.cn',
//            'jiangmengjun@admaster.com.cn',
//            'zhouling@admaster.com.cn',
//            'shaoli@admaster.com.cn',
//            'shangrenzhen@admaster.com.cn',
//            'zhangli@admaster.com.cn',
//            'huangsiwei@admaster.com.cn',
//            'liujun@admaster.com.cn',
//            'chenjian@admaster.com.cn',
//            'maolei@admaster.com.cn',
//            'sunjia@admaster.com.cn'
//    ]
    ccList = []
    bccList = []
    charset = 'GBK'
}