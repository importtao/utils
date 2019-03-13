package com.sld.zt.utils.databasedoc;

import com.sld.zt.utils.model.Colume;
import com.sld.zt.utils.model.Table;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import javax.lang.model.element.Element;
import javax.transaction.xa.XAException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

public class WordDocUtils {
    public static boolean generatorDataBaseDoc(List<Table> tables) {
        try {
            //Blank Document
            XWPFDocument document = new XWPFDocument();

            //Write the Document in file system
            FileOutputStream out = new FileOutputStream(new File("smart_planet数据库说明文档.docx"));
            tables.stream().forEach(table -> {



                //换行
                document.createParagraph().createRun().setText("\r");

                //添加标题
                XWPFParagraph titleParagraph = document.createParagraph();
                //设置段落居中
                titleParagraph.setAlignment(ParagraphAlignment.CENTER);

                XWPFRun titleParagraphRun = titleParagraph.createRun();
                titleParagraphRun.setText(table.getTableName() + (table.getTableCommon().equals("") ? "" : "-" + table.getTableCommon()));
                titleParagraphRun.setColor("000000");
                titleParagraphRun.setFontSize(20);

                //表格
                XWPFTable ComTable = document.createTable();
                ComTable.setColBandSize(30);
                //列宽自动分割
                CTTblWidth comTableWidth = ComTable.getCTTbl().addNewTblPr().addNewTblW();
                comTableWidth.setType(STTblWidth.DXA);
                comTableWidth.setW(BigInteger.valueOf(9072));
                //表头
                XWPFTableRow comTableRowOne = ComTable.getRow(0);
                comTableRowOne.setHeight(20);
                comTableRowOne.getCell(0).setText("列名");
                comTableRowOne.addNewTableCell().setText("序号");
                comTableRowOne.addNewTableCell().setText("默认值");
                comTableRowOne.addNewTableCell().setText("是否为空");
                comTableRowOne.addNewTableCell().setText("数据类型");
                comTableRowOne.addNewTableCell().setText("字段类型");
                comTableRowOne.addNewTableCell().setText("主键");
                comTableRowOne.addNewTableCell().setText("注释");
                for(int i=0;i<8;i++){
                    XWPFTableCell cell= comTableRowOne.getCell(i);
                    cell.setColor("b1b1b1");
                    CTTc cttc = cell.getCTTc();
                    CTTcPr ctPr = cttc.addNewTcPr();
                    ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                    cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                }
                for(Colume colume:table.getColume()){
                    XWPFTableRow comTableRow = ComTable.createRow();
                    comTableRow.getCell(0).setText(colume.getName());
                    comTableRow.getCell(1).setText(colume.getPosition());
                    comTableRow.getCell(2).setText(colume.getDefaultValue());
                    comTableRow.getCell(3).setText(colume.getNullAble());
                    comTableRow.getCell(4).setText(colume.getDataType());
                    comTableRow.getCell(5).setText(colume.getColumnType());
                    comTableRow.getCell(6).setText(colume.getKey());
                    comTableRow.getCell(7).setText(colume.getComment());
                    for(int i=0;i<8;i++){
                        XWPFTableCell cell= comTableRow.getCell(i);
                        CTTc cttc = cell.getCTTc();
                        CTTcPr ctPr = cttc.addNewTcPr();
                        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
                        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
                    }
                }

                //换行
                document.createParagraph().createRun().setText("\r");

            });
            CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
            XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

            //添加页眉
            CTP ctpHeader = CTP.Factory.newInstance();
            CTR ctrHeader = ctpHeader.addNewR();
            CTText ctHeader = ctrHeader.addNewT();
            String headerText = "Java POI create MS word file.";
            ctHeader.setStringValue(headerText);
            XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
            //设置为右对齐
            headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
            XWPFParagraph[] parsHeader = new XWPFParagraph[1];
            parsHeader[0] = headerParagraph;
            policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);


            //添加页脚
            CTP ctpFooter = CTP.Factory.newInstance();
            CTR ctrFooter = ctpFooter.addNewR();
            CTText ctFooter = ctrFooter.addNewT();
            String footerText = "http://blog.csdn.net/zhouseawater";
            ctFooter.setStringValue(footerText);
            XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
            headerParagraph.setAlignment(ParagraphAlignment.CENTER);
            XWPFParagraph[] parsFooter = new XWPFParagraph[1];
            parsFooter[0] = footerParagraph;
            policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);


            document.write(out);
            out.close();
            System.out.println("create_table document written success.");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlException e) {
            e.printStackTrace();
        }
        return false;
    }
}
