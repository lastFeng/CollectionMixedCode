/*
 * Copyright 2001-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.learn.poiexceldemo.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p> Title: </p>
 *
 * <p> Description: </p>
 *
 * @author: Guo Weifeng
 * @version: 1.0
 * @create: 2019/11/1 10:31
 */
public class ExcelTest {

    @Test
    public void generate() {

        String filePath = "C:\\Users\\wst\\Desktop\\first.xlsx";

        try (SXSSFWorkbook workbook = new SXSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(filePath)){
            SXSSFSheet sheet = workbook.createSheet("Test");

            // 创建10行5列的表格
            for (int i = 0; i < 10; i++) {
                SXSSFRow row = sheet.createRow(i);
                for (int j = 0; j < 5; j++) {
                    SXSSFCell cell = row.createCell(j);
                    cell.setCellValue(String.format("第%s行，第%s列", i+1, j+1));
                }
            }

            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void generateStyleTest() {
        String filePath = "C:\\Users\\wst\\Desktop\\second.xlsx";

        try (SXSSFWorkbook workbook = new SXSSFWorkbook();
             FileOutputStream outputStream = new FileOutputStream(filePath, false)){

            SXSSFSheet sheet = workbook.createSheet("styleSheet");

            // 设置自定义样式：居中、蓝色、加粗
            // 设置字体
            final Font font = workbook.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.BLUE.index);
            // 设置单元格样式
            final CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);

            // 设置首行
            String[] titles = new String[] {"行首1", "行首2", "行首3", "行首4", "行首5"};
            SXSSFRow headers = sheet.createRow(0);
            for (int i = 0; i < titles.length; i++) {
                SXSSFCell cell = headers.createCell(i);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(titles[i]);
            }

            // 填充数据
            for (int i = 1; i < 11; i++) {
                SXSSFRow row = sheet.createRow(i);

                for (int j = 0; j < titles.length; j++) {
                    SXSSFCell cell = row.createCell(j);
                    cell.setCellValue(String.format("第%s行，第%s列", i+1, j+1));
                }
            }

            // 设置宽度自适应
            sheet.trackAllColumnsForAutoSizing();
            for (int i = 0; i < titles.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) * 17 / 10);
            }

            workbook.write(outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}