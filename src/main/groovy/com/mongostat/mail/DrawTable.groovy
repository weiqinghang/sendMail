package com.mongostat.mail

/**
 * Created with IntelliJ IDEA.
 * User: weicc
 * Date: 13-7-23
 * Time: AM12:56
 * To change this template use File | Settings | File Templates.
 */
class DrawTable {

    def content = [
            preHeader: '''
<table style="border:1px solid #cdcdcd;width:90%;">
    <tbody>''',
            head: '',
            body: '',
            foot: '',
            surFooter: '''</tbody></table>'''
    ]
    def columns = [], fields = []
    
    def thStyle = '"background:#ccc;font-weight:bold;padding:5px;text-align:center;"'
    def tdNormalStyle = '"background:#eee;padding:5px;text-align:center;"'
    def tdWarnStyle = '"background:yellow;color:red;text-align:center;"'

    public DrawTable(cols, fils) {
        if(cols.size() != fils.size()) {
            throw new Exception('columns.length != fields.length')
        }
        columns = cols
        fields = fils
    }


    def drawHead() {
        def lineTemplate = '<tr style="border-bottom:1px dashed #cdcdcd;">'
        columns.each {column ->
            lineTemplate += "<th style=${thStyle})>${column}</th>"
        }
        lineTemplate += '</tr>'
        content.head += lineTemplate
    }

    def drawBody(list) {
        list.each {line ->
            def lineTemplate = '<tr style="border-bottom:1px dashed #cdcdcd;">'
            fields.each {field ->
                def f = line[field]
                if(!f) {
                    if(field == 'remark') {
                        f = ''
                    } else {
                        f = 0
                    }
                }
                lineTemplate += "<td style=${tdNormalStyle})>" + f + '</td>'
            }
            lineTemplate += '</tr>'
            content.body += lineTemplate
        }
    }

    def getTableHtml(list) {
        drawHead()
        drawBody(list)
        content.preHeader + content.head + content.body + content.foot + content.surFooter
    }
}
