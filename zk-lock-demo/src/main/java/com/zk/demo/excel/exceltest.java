package com.zk.demo.excel;

import com.zk.demo.pojo.his;
import com.zk.demo.pojo.yb;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class exceltest {
    public static void main(String[] args) {
        // 设定Excel文件所在路径
        String YBFileName = "C:\\Users\\vic\\Desktop\\医保疾病编码.xls";
        String HISFileName = "C:\\Users\\vic\\Desktop\\疾病编码_医保his数据差异.xls";
        // 读取Excel文件内容
        List<yb> ybResult = ExcelReader.readExcel(YBFileName);
        List<his> hisResult = ExcelHISReader.readExcel(HISFileName);
        for(his h:hisResult ){
            for(yb y:ybResult ){
                System.out.println(h.getHISCODE());
                if(h.getHISCODE().equals(y.getYKE120())||(h.getHISCODE()+"+").equals(y.getYKE120())){
                    h.setYBCODE(y.getYKE120());
                    break;
                }else{
                    h.setYBCODE(h.getHISCODE().substring(0,h.getHISCODE().length()-4));
                }
            }
        }

        Workbook workbook = ExcelWriter.exportData(hisResult);
        FileOutputStream fileOut = null;
        try {
            String exportFilePath = "C:\\Users\\vic\\Desktop\\疾病编码_医保his数据差异ex.xls";
            File exportFile = new File(exportFilePath);
            if (!exportFile.exists()) {
                exportFile.createNewFile();
            }

            fileOut = new FileOutputStream(exportFilePath);
            workbook.write(fileOut);
            fileOut.flush();
        } catch (Exception e) {
        } finally {
            try {
                if (null != fileOut) {
                    fileOut.close();
                }
                if (null != workbook) {
                    workbook.close();
                }
            } catch (IOException e) {
            }
        }
    }
}
